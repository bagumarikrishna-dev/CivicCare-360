package com.civic.repo;
import com.civic.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ComplaintRepository extends JpaRepository<Complaint,Long> {
 List<Complaint> findByReporterEmailOrderByCreatedAtDesc(String email);
 List<Complaint> findAllByOrderByCreatedAtDesc();
}
