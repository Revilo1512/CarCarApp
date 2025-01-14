package at.carcar.carcarbackend.Reservation;

import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> getReservationsByUser(User user);

    List<Reservation> getReservationsByCar(Car car);
}
