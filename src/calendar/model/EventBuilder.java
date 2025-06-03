package calendar.model;

public class EventBuilder {
    private String subject;
    private Date startDate;
    private Time startTime;
    private String description;
    private Date endDate;
    private Time endTime;
    private String location;
    private boolean isPrivate;

    public EventBuilder(String subject, Date startDate, Time startTime) {
        this.subject = subject;
        this.startDate = startDate;
        this.startTime = startTime;
        this.description = "No Description Provided";
        this.endDate = startDate; // Default end date is the same as start date
        this.endTime = startTime; // Default end time is the same as start time
        this.location = "No Location Provided";
        this.isPrivate = false; // Default status is public
    }

    public EventBuilder description(String description) {
        this.description = description;
        return this;
    }

    public EventBuilder endDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public EventBuilder endTime(Time endTime) {
        this.endTime = endTime;
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
        return new Event(subject, location, startDate, endDate, startTime, endTime, isPrivate, description);
    }
}
