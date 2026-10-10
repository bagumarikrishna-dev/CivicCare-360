package com.civic.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name="complaints")
public class Complaint {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=140) private String title;
 @Column(nullable=false,length=30) private String category;
 @Column(nullable=false,length=2000) private String description;
 @Column(nullable=false,length=240) private String location;
 private Double latitude;
 private Double longitude;
 @Column(nullable=false,length=20) private String priority="MEDIUM";
 @Column(nullable=false,length=30) private String status="SUBMITTED";
 private String imageName;
 @Column(length=1000) private String resolutionNotes;
 private LocalDateTime createdAt=LocalDateTime.now();
 private LocalDateTime updatedAt=LocalDateTime.now();
 @ManyToOne(fetch=FetchType.EAGER,optional=false) @JoinColumn(name="user_id") private User reporter;
 public Complaint() {}
 @PreUpdate public void onUpdate(){updatedAt=LocalDateTime.now();}
 public Long getId(){return id;} public String getTitle(){return title;} public void setTitle(String v){title=v;}
 public String getCategory(){return category;} public void setCategory(String v){category=v;}
 public String getDescription(){return description;} public void setDescription(String v){description=v;}
 public String getLocation(){return location;} public void setLocation(String v){location=v;}
 public Double getLatitude(){return latitude;} public void setLatitude(Double v){latitude=v;}
 public Double getLongitude(){return longitude;} public void setLongitude(Double v){longitude=v;}
 public String getPriority(){return priority;} public void setPriority(String v){priority=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public String getImageName(){return imageName;} public void setImageName(String v){imageName=v;}
 public String getResolutionNotes(){return resolutionNotes;} public void setResolutionNotes(String v){resolutionNotes=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
 public User getReporter(){return reporter;} public void setReporter(User v){reporter=v;}
}
