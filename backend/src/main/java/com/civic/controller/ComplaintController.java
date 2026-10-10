package com.civic.controller;
import com.civic.model.*;
import com.civic.repo.*;
import com.civic.service.UploadService;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
@RestController @RequestMapping("/api/complaints")
public class ComplaintController {
 private final ComplaintRepository complaints; private final UserRepository users; private final UploadService uploads;
 public ComplaintController(ComplaintRepository c,UserRepository u,UploadService s){complaints=c;users=u;uploads=s;}
 public record StatusRequest(@NotBlank String status,String resolutionNotes){}
 private User current(Authentication a){return users.findByEmail(a.getName()).orElseThrow();}
 private Map<String,Object> dto(Complaint c){
  Map<String,Object> m=new LinkedHashMap<>(); m.put("id",c.getId());m.put("title",c.getTitle());m.put("category",c.getCategory());
  m.put("description",c.getDescription());m.put("location",c.getLocation());m.put("latitude",c.getLatitude());m.put("longitude",c.getLongitude());m.put("priority",c.getPriority());m.put("status",c.getStatus());
  m.put("imageUrl",c.getImageName()==null?null:"http://localhost:8080/uploads/"+c.getImageName());
  m.put("resolutionNotes",c.getResolutionNotes());m.put("createdAt",c.getCreatedAt());m.put("updatedAt",c.getUpdatedAt());
  m.put("reporterName",c.getReporter().getName());m.put("reporterEmail",c.getReporter().getEmail());return m;
 }
 @PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
 public ResponseEntity<?> create(@RequestParam String title,@RequestParam String category,@RequestParam String description,
   @RequestParam String location,@RequestParam(defaultValue="MEDIUM") String priority,
   @RequestParam(required=false) String latitude,@RequestParam(required=false) String longitude,
   @RequestParam(required=false) MultipartFile image,Authentication auth){
  try{
   if(title.isBlank()||description.isBlank()||location.isBlank())return ResponseEntity.badRequest().body(Map.of("message","Title, description, and location are required."));
   if(!Set.of("GARBAGE","POTHOLE","STREETLIGHT","WATER_LEAKAGE","OTHER").contains(category))return ResponseEntity.badRequest().body(Map.of("message","Invalid category."));
   if(!Set.of("LOW","MEDIUM","HIGH").contains(priority))return ResponseEntity.badRequest().body(Map.of("message","Invalid priority."));
   Complaint c=new Complaint();c.setTitle(title.trim());c.setCategory(category);c.setDescription(description.trim());c.setLocation(location.trim());c.setPriority(priority);
   c.setReporter(current(auth));
   if(latitude!=null&&!latitude.isBlank()&&longitude!=null&&!longitude.isBlank()){
    try{c.setLatitude(Double.valueOf(latitude));c.setLongitude(Double.valueOf(longitude));}
    catch(NumberFormatException ex){return ResponseEntity.badRequest().body(Map.of("message","Invalid GPS coordinates."));}
   }
   c.setImageName(uploads.save(image));return ResponseEntity.status(201).body(dto(complaints.save(c)));
  }catch(Exception e){return ResponseEntity.badRequest().body(Map.of("message",e.getMessage()==null?"Could not save complaint.":e.getMessage()));}
 }
 @GetMapping("/mine") public List<Map<String,Object>> mine(Authentication auth){return complaints.findByReporterEmailOrderByCreatedAtDesc(auth.getName()).stream().map(this::dto).toList();}
 @GetMapping("/all") public ResponseEntity<?> all(Authentication auth){
  User u=current(auth);if(!"ADMIN".equals(u.getRole()))return ResponseEntity.status(403).body(Map.of("message","Admin access required."));
  return ResponseEntity.ok(complaints.findAllByOrderByCreatedAtDesc().stream().map(this::dto).toList());
 }
 @PutMapping("/{id}/status") public ResponseEntity<?> update(@PathVariable Long id,@RequestBody StatusRequest req,Authentication auth){
  User u=current(auth);if(!"ADMIN".equals(u.getRole()))return ResponseEntity.status(403).body(Map.of("message","Admin access required."));
  if(!Set.of("SUBMITTED","IN_PROGRESS","RESOLVED","REJECTED").contains(req.status()))return ResponseEntity.badRequest().body(Map.of("message","Invalid status."));
  return complaints.findById(id).map(c->{c.setStatus(req.status());c.setResolutionNotes(req.resolutionNotes());return ResponseEntity.ok(dto(complaints.save(c)));})
   .orElseGet(()->ResponseEntity.notFound().build());
 }
 @GetMapping("/stats") public ResponseEntity<?> stats(Authentication auth){
  User u=current(auth);if(!"ADMIN".equals(u.getRole()))return ResponseEntity.status(403).body(Map.of("message","Admin access required."));
  List<Complaint> all=complaints.findAll();
  Map<String,Long> counts=new LinkedHashMap<>();for(String s:List.of("SUBMITTED","IN_PROGRESS","RESOLVED","REJECTED"))counts.put(s,all.stream().filter(c->s.equals(c.getStatus())).count());
  return ResponseEntity.ok(Map.of("total",all.size(),"statuses",counts));
 }
}
