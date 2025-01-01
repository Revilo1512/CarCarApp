package at.carcar.carcarbackend.Report;

import at.carcar.carcarbackend.Trip.Trip;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "reports")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Report {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;
    private long author_user_id;
    private Date date;
    private String description;
    //private List<String> changeLog;
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Report(int reportId, int userId, Date date, String description, Trip trip) {
        this.id = reportId;
        this.author_user_id = userId;
        this.date = date;
        this.description = description;
        this.trip = trip;
        //this.changeLog = new ArrayList<String>();
    }
    public Report(){

    }
    public void addChanges(String change){
        //
    }

}
