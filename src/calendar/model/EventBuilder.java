package calendar.model;

import java.time.DayOfWeek;
import java.util.ArrayList;

public class EventBuilder {
    private String subject;
    private DateTime start;
    private DateTime startCopy;
    private String description;
    private DateTime end;
    private String location;
    private boolean isPrivate;
    private ArrayList<DayOfWeek> daysOfWeek;

    public EventBuilder(String subject, DateTime start) {
        this.subject = subject;
        this.startCopy = start;
        this.start = new DateTime(start.getDate(), new Time(8, 0));
        this.description = "No Description Provided";
        this.end = new DateTime(start.getDate(), new Time(17, 0));
        this.location = "No Location Provided";
        this.isPrivate = false; // Default status is public
    }

    private EventBuilder() {
        
    }

    public static EventBuilder createBuilder(){
        return new EventBuilder();
    }

    public EventBuilder description(String description) {
        this.description = description;
        return this;
    }

    public EventBuilder end(DateTime end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        this.end = end;
        this.start = startCopy; // Reset start to the original start time
        return this;
    }

    public EventBuilder location(String location) {
        this.location = location;
        return this;
    }

    public EventBuilder isPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
        return this;
    }

    public Event build() {
        return new Event(subject, location, start, end, isPrivate, description);
    }
}
