package at.carcar.carcarbackend.Report;
import at.carcar.carcarbackend.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/reports")
public class ReportController {

    private final ReportService service;
    private final AuthorizationService authorizationService;
    @Autowired
    public ReportController(ReportService service, AuthorizationService authorizationService) {
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<?> getReport(@PathVariable long reportId) {
        try {
            Report report = service.getReportById(reportId, authorizationService.getAuthenticatedUserId());
            return ResponseEntity.ok(report);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/createMaintenance")
    public ResponseEntity<?> createMaintenanceReport(
            @RequestParam long carId,
            @RequestParam(required = false) Long tripId,
            @RequestParam String description,
            @RequestParam String type,
            @RequestParam double cost) {
        try {
            long userId = authorizationService.getAuthenticatedUserId();
            MaintenanceReport report = service.createMaintenanceReport(userId, carId, tripId, description, type, cost);
            return ResponseEntity.status(HttpStatus.CREATED).body(report);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/createDamage")
    public ResponseEntity<?> createDamageReport(
            @RequestParam long carId,
            @RequestParam(required = false) Long tripId,
            @RequestParam String description,
            @RequestParam String damageDetails) {
        try {
            long userId = authorizationService.getAuthenticatedUserId();
            DamageReport report = service.createDamageReport(userId, carId, tripId, description, damageDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(report);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{reportId}")
    public ResponseEntity<?> deleteReport(@PathVariable long reportId) {
        try {
            long userId = authorizationService.getAuthenticatedUserId();
            service.deleteReport(userId, reportId);
            return ResponseEntity.ok("Report deleted successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<?> getReportsByCar(@PathVariable long carId) {
        try {
            long userId = authorizationService.getAuthenticatedUserId();
            List<Report> reports = service.getReportsByCar(userId, carId);
            return ResponseEntity.ok(reports);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
