package at.carcar.carcarbackend.Trip;

import at.carcar.carcarbackend.Car.Car;
import at.carcar.carcarbackend.User.User;

import java.util.Date;

public class Trip {
    private int tripID;
    private Date startTime;
    private Date endTime;
    private double distance;
    private double fuelUsed;
    private Car car;
    private User user;

    public Trip(int tripID, Date startTime, Car car, User user) {
        this.tripID = tripID;
        this.startTime = startTime;
        this.car = car;
        this.user = user;
    }

    public int getTripID() {
        return tripID;
    }

    public Car getCar() {
        return car;
    }

    public User getUser() {
        return user;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getFuelUsed() {
        return fuelUsed;
    }

    public void setFuelUsed(double fuelUsed) {
        this.fuelUsed = fuelUsed;
    }

    public Trip initiateTrip(User user) {
        // Implement functionality to initiate a trip
        //return new Trip();
        return null;
    }
}
