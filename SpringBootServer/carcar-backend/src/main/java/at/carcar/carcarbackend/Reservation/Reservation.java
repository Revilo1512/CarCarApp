package at.carcar.carcarbackend.Reservation;

import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.User.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Date reservationStart;
    @Column()
    private Date reservationEnd;
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (reservationStart == null || reservationEnd == null) {
            throw new IllegalStateException("Reservation start and end dates cannot be null.");
        }
        if (!reservationEnd.after(reservationStart)) {
            throw new IllegalStateException("Reservation end date must be after the start date.");
        }
    }

    public void setReservationStart(Date reservationStart) {
        this.reservationStart = reservationStart;
    }

    public void setReservationEnd(Date reservationEnd) {
        this.reservationEnd = reservationEnd;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Reservation() {
    }

    public Reservation(Date reservationStart, Date reservationEnd, Car car, User usr) {
        this.reservationStart = reservationStart;
        this.reservationEnd = reservationEnd;
        this.car = car;
        this.user = usr;
    }

    @JsonProperty("reservationID")
    public long getId() {
        return id;
    }

    public Date getReservationStart() {
        return reservationStart;
    }

    public Date getReservationEnd() {
        return reservationEnd;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
