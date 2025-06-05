package calendar;

// Assuming your DateTime, Date, Time classes are in the same 'calendar' package
// import java.time.DayOfWeek; // If DayOfWeek from java.time is used
// import java.util.ArrayList; // If needed for daysOfWeek

public interface IEvent {
    String getSubject();
    void setSubject(String subject);

    DateTime getStart();
    void setStart(DateTime start);

    DateTime getEnd();
    void setEnd(DateTime end);

    String getLocation();
    void setLocation(String location);

    String getDescription();
    void setDescription(String description);

    String getStatus(); // Changed from boolean to String as per main requirements
    void setStatus(String status); // Changed from boolean to String

    // Methods for series handling, to be added from my Event.java logic
    String getSeriesId();
    void setSeriesId(String seriesId);

    boolean isSeriesException();
    void setSeriesException(boolean isSeriesException);

    String getOriginalSeriesId();
    void setOriginalSeriesId(String originalSeriesId);
    
    IEvent copy(); // For creating modifiable copies, especially for series edits
}