package at.carcar.carcarbackend.Report;

import at.carcar.carcarbackend.Trip.Trip;

import java.util.Date;
import java.util.List;

public abstract class Report {
    private int reportId;
    private int userId;
    private Date date;
    private String description;
    private List<String> changeLog;
    private Trip trip;

    public void addChanges(String change){
        //
    }
}
