package at.carcar.carcarbackend.Reservation;

import at.carcar.carcarbackend.Car.Car;
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

    private Date reservationStart;
    private Date reservationEnd;
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Reservation() {
    }

    public Reservation(Date reservationStart, Date reservationEnd) {
        this.reservationStart = reservationStart;
        this.reservationEnd = reservationEnd;
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
}
