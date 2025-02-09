package at.carcar.carcarbackend.Report;

import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.Trip.Trip;
import at.carcar.carcarbackend.User.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
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
    @ManyToOne
    @JoinColumn(name = "author_user_id")
    private User author_user;
    private Date date;
    private String description;
    @ManyToOne
    @JoinColumn(name = "trip_id")
    @Nullable
    private Trip trip;
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Report(User author_user, Date date, String description, Trip trip, Car car) {
        this.author_user = author_user;
        this.date = date;
        this.description = description;
        this.trip = trip;
        this.car = car;
    }

    public Report(User author_user, Date date, String description, Car car) {
        this.author_user = author_user;
        this.date = date;
        this.description = description;
        this.car = car;
        this.trip = null;
    }
    public Report(){

    }
    public void addChanges(String change){
        //
    }

    @JsonProperty("reportID")
    public long getId() {
        return id;
    }

    public User getAuthor_user() {
        return author_user;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
