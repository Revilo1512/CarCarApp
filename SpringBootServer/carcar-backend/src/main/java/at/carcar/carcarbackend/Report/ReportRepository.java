package at.carcar.carcarbackend.Report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
/*    List<DamageReport> findAllDamageReports();

    @Query("SELECT r FROM Report r WHERE TYPE(r) = MaintenanceReport")
    List<MaintenanceReport> findAllMaintenanceReports();*/
}
