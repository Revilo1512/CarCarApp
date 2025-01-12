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
public class DamageReport extends Report {
    private String damageDetails;
    @ElementCollection
    private List<String> damagePhotos;

    public DamageReport(int reportId, User author_user, Date date, String description, Trip trip, String damageDetails, List<String> damagePhotos, Car car) {
        super(reportId, author_user, date, description, trip, car);
        this.damageDetails = damageDetails;
        this.damagePhotos = damagePhotos;
    }

    public DamageReport(User author_user, Date date, String description, Trip trip, String damageDetails, List<String> damagePhotos, Car car) {
        super(author_user, date, description, trip, car);
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

