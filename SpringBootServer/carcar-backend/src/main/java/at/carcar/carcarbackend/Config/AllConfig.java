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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.List;

@Configuration
public class AllConfig {

    private final BCryptPasswordEncoder passwordEncoder;
    private final List<User> users;
    private final List<Car> cars;
    private final List<Group> groups;
    private final List<Trip> trips;
    private final List<Report> reports;


    public AllConfig(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.users = listOfUsers();
        this.cars = listOfCars();
        this.groups = listOfGroups();
        this.trips = listOfTrips();
        this.reports = listOfReports();
    }

    private List<User> listOfUsers() {
        return List.of(
                new User("Peter Nis",
                        "p.nis@gmail.com",
                        passwordEncoder.encode("pnis")
                ),

                new User("Anton Gressiv",
                        "a.gressiv@gmail.com",
                        passwordEncoder.encode("agressiv")
                ),

                new User("Bill Yard",
                        "b.yard@gmail.com",
                        passwordEncoder.encode("byard")
                ),

                new User("Lorenz Auch",
                        "l.auch@gmail.com",
                        passwordEncoder.encode("lauch")
                ),

                new User("Herbert Kedicht",
                        "h.kedicht@gmail.com",
                        passwordEncoder.encode("hkedicht")
                ),

                new User("Knut Schfleck",
                        "k.schfleck@gmail.com",
                        passwordEncoder.encode("kschfleck"))
        );
    }

    private List<Car> listOfCars() {
        return List.of(
                new Car("Cityflitzer",
                        "VW",
                        "Up",
                        true),

                new Car("Der Massive",
                        "Dodge",
                        "RAM",
                        true),
                new Car("Old but Gold",
                        "VW",
                        "Käfer",
                        true)
        );
    };

    private List<Group> listOfGroups() {
        return List.of(
                new Group("Heiselpartie",
                        users.get(0),
                        List.of(users.get(1), users.get(2)),
                        List.of(cars.get(0), cars.get(1))),

                new Group("Die App ist mega - Gruppe!",
                        users.get(3),
                        List.of(users.get(4), users.get(5)),
                        List.of(cars.get(2)))
        );
    };

    private List<Trip> listOfTrips() {
        Trip trip1 = new Trip(new Date(2024, 12, 1),
                new Date(2024, 12, 2),
                5,
                1,
                cars.get(0),
                users.getFirst());  // This is where the car is associated with the trip

        Trip trip2 = new Trip(new Date(2025, 1, 1),
                new Date(2025, 1, 2),
                100.5,
                5.4,
                cars.get(2),
                users.get(3));


        return List.of(trip1, trip2);
    }

    private List<Report> listOfReports() {
        return List.of(
                new DamageReport(users.get(0),
                        new Date(2024, 12, 2),
                        "I bin dem Heisl hint'n eini'gfoan!!",
                        trips.get(0), "Mei Auto is fui hinnig, der Schass!",
                        cars.getFirst()),

                new MaintenanceReport(users.get(3),
                        new Date(2025, 1, 2),
                        "Irgendsoana hot den Tank leer g'mocht, geh schleich di!",
                        trips.get(1),
                        "Der Tank woa fui la",
                        1000.7,
                         cars.getLast())

        );
    };

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRep, GroupRepository groupRep, CarRepository carRep,
                                        ReportRepository reportRep, TripRepository tripRep) {
        return args -> {
            if(!userRep.existsById(1L)){
                userRep.saveAll(users);
                carRep.saveAll(cars);
                groupRep.saveAll(groups);
                tripRep.saveAll(trips);
                reportRep.saveAll(reports);
            }

        };
    }
}
