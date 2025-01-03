package at.carcar.carcarbackend.Config;

import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.Car.CarRepository;
import at.carcar.carcarbackend.Group.Group;
import at.carcar.carcarbackend.Group.GroupRepository;
import at.carcar.carcarbackend.Report.DamageReport;
import at.carcar.carcarbackend.Report.MaintenanceReport;
import at.carcar.carcarbackend.Report.Report;
import at.carcar.carcarbackend.Report.ReportRepository;
import at.carcar.carcarbackend.Trip.Trip;
import at.carcar.carcarbackend.Trip.TripRepository;
import at.carcar.carcarbackend.User.User;
import at.carcar.carcarbackend.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.List;

@Configuration
public class AllConfig {

    private List<User> users = List.of(
            new User("Peter Nis",
                    "p.nis@gmail.com",
                    "pnis"
            ),

            new User("Anton Gressiv",
            "a.gressiv@gmail.com",
            "agressiv"
            ),

            new User("Bill Yard",
            "b.yard@gmail.com",
            "byard"
            ),

            new User("Lorenz Auch",
            "l.auch@gmail.com",
            "lauch"
            ),

            new User("Herbert Kedicht",
            "h.kedicht@gmail.com",
            "hkedicht"
            ),

            new User("Knut Schfleck",
            "k.schfleck@gmail.com",
            "kschfleck")
    );

    List<Car> cars = List.of(
            new Car("Cityflitzer",
                    "VW",
                    "Up",
                    true),

            new Car("Der Dicke",
                    "Dodge",
                    "RAM",
                    true),
            new Car("Old but Gold",
                    "VW",
                    "Käfer",
                    true)
    );

    List<Group> groups = List.of(
            new Group("Heiselpartie",
                    users.get(0),
                    List.of(users.get(1), users.get(2)),
                    List.of(cars.get(0), cars.get(1))),

            new Group("Die Geilen",
                    users.get(3),
                    List.of(users.get(4), users.get(5)),
                    List.of(cars.get(2)))
    );

    List<Trip> trips = List.of(
            new Trip(new Date(2024, 12, 1),
                    new Date(2024, 12, 2),
                    5,
                    1,
                    cars.get(0),
                    users.get(0)),

            new Trip (new Date(2025, 1, 1),
                    new Date (2025, 1, 2),
                    100.5,
                    5.4,
                    cars.get(2),
                    users.get(3))
    );

    List<Report> reports = List.of(
            new DamageReport(users.get(0),
                    new Date(2024, 12, 2),
                    "I bin dem Heisl hint'n eini'gfoan!!",
                    trips.get(0),
                    "Mei Auto is fui hinnig, der Schass!",
                    List.of("Leider nur in der PRO-Version verfügbar", "Kostet nur 200€ pro Tag")),

            new MaintenanceReport(users.get(3),
                new Date(2025, 1, 2),
                "Irgendsoana hot den Tank leer g'mocht, geh schleich di!",
                trips.get(1),
                "Der Tank woa fui la",
                1000.7,
                List.of("Leider nur in der PRO-Version verfügbar", "Kostet nur 200€ pro Tag"))

    );

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRep, GroupRepository groupRep, CarRepository carRep,
                                        ReportRepository reportRep, TripRepository tripRep) {
        return args -> {
            userRep.saveAll(users);
            carRep.saveAll(cars);
            groupRep.saveAll(groups);
            tripRep.saveAll(trips);
            reportRep.saveAll(reports);
        };
    }
}
