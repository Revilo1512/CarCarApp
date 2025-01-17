package at.carcar.carcarbackend.Report;

import at.carcar.carcarbackend.Car.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByCar(Car car);

    Report getReportById(long reportId);
/*    List<DamageReport> findAllDamageReports();

    @Query("SELECT r FROM Report r WHERE TYPE(r) = MaintenanceReport")
    List<MaintenanceReport> findAllMaintenanceReports();*/
}
