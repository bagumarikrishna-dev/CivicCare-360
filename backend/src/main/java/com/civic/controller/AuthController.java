package com.civic.controller;
import com.civic.model.User;
import com.civic.repo.UserRepository;
import com.civic.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthController(UserRepository u,PasswordEncoder e,JwtService j){users=u;encoder=e;jwt=j;}
 public record RegisterRequest(@NotBlank @Size(max=80) String name,@Email @NotBlank String email,@Size(min=8,max=72) String password){}
 public record LoginRequest(@Email @NotBlank String email,@NotBlank String password){}
 @PostMapping("/register") public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest r){
  String email=r.email().trim().toLowerCase();
  if(users.existsByEmail(email)) return ResponseEntity.status(409).body(Map.of("message","Email is already registered."));
  User u=new User(); u.setName(r.name().trim()); u.setEmail(email); u.setPassword(encoder.encode(r.password())); u.setRole("CITIZEN"); users.save(u);
  return ResponseEntity.status(201).body(Map.of("message","Registration successful. Please log in."));
 }
 @PostMapping("/login") public ResponseEntity<?> login(@Valid @RequestBody LoginRequest r){
  User u=users.findByEmail(r.email().trim().toLowerCase()).orElse(null);
  if(u==null||!encoder.matches(r.password(),u.getPassword())) return ResponseEntity.status(401).body(Map.of("message","Invalid email or password."));
  return ResponseEntity.ok(Map.of("token",jwt.generate(u.getEmail(),u.getRole()),"name",u.getName(),"email",u.getEmail(),"role",u.getRole()));
 }
}
