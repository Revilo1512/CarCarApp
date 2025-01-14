package at.carcar.carcarbackend.Trip;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TripService {
    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Optional<Trip> findTripById(long id) {
        return tripRepository.findById(id);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public void createTrip(Trip trip) {

    }
}
