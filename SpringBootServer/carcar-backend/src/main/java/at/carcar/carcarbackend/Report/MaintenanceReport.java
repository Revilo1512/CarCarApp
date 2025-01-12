package at.carcar.carcarbackend.Report;
import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.Trip.Trip;
import at.carcar.carcarbackend.User.User;
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
    public MaintenanceReport(int reportId, User author_user, Date date, String description, Trip trip, String maintenanceType, double cost, List<String> receiptPhoto, Car car) {
        super(reportId, author_user, date, description, trip, car);
        this.maintenanceType = maintenanceType;
        this.cost = cost;
        this.receiptPhoto = receiptPhoto;
    }
    public MaintenanceReport(User author_user, Date date, String description, Trip trip, String maintenanceType, double cost, List<String> receiptPhoto, Car car) {
        super(author_user, date, description, trip, car);
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

