package at.carcar.carcarbackend.Report;
import at.carcar.carcarbackend.Trip.Trip;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.List;
@Entity
public class DamageReport extends Report {
    private String damageDetails;
    @ElementCollection
    private List<String> damagePhotos;

    public DamageReport(int reportId, int userId, Date date, String description, Trip trip, String damageDetails, List<String> damagePhotos) {
        super(reportId, userId, date, description, trip);
        this.damageDetails = damageDetails;
        this.damagePhotos = damagePhotos;
    }

    public DamageReport() {
    }

    public String getDamageDetails() {
        return damageDetails;
    }

    public void setDamageDetails(String damageDetails) {
        this.damageDetails = damageDetails;
    }

    public List<String> getDamagePhotos() {
        return damagePhotos;
    }

    public void setDamagePhotos(List<String> damagePhotos) {
        this.damagePhotos = damagePhotos;
    }

}

