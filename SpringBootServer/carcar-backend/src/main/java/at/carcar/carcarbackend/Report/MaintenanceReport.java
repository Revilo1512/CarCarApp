package at.carcar.carcarbackend.Report;
import at.carcar.carcarbackend.Trip.Trip;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.List;
@Entity
public class MaintenanceReport extends Report {
    private String maintenanceType;
    private double cost;
    @ElementCollection
    private List<String> receiptPhoto;
    ;
    public MaintenanceReport(int reportId, int userId, Date date, String description, Trip trip, String maintenanceType, double cost, List<String> receiptPhoto) {
        super(reportId, userId, date, description, trip);
        this.maintenanceType = maintenanceType;
        this.cost = cost;
        this.receiptPhoto = receiptPhoto;
    }

    public MaintenanceReport() {
    }

    // Getters and Setters
    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<String> getReceiptPhoto() {
        return receiptPhoto;
    }

    public void setReceiptPhoto(List<String> receiptPhoto) {
        this.receiptPhoto = receiptPhoto;
    }
}

