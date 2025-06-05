package calendar;

import java.time.DayOfWeek; // Retaining as it was in your original, though not used in current build()
import java.util.ArrayList; // Retaining as it was in your original

public class EventBuilder {
    private String subject;
    private DateTime start;
    // private DateTime startCopy; // This was causing issues with all-day logic, removing
    private String description;
    private DateTime end;
    private String location;
    private String statusValue; // Changed from isPrivate to match Event's status (String)
    // private ArrayList<DayOfWeek> daysOfWeek; // This seems to belong to series creation, not single event build

    // Constructor for an event that might be all-day by default
    public EventBuilder(String subject, DateTime startDateTime) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Event subject cannot be null or empty.");
        }
        if (startDateTime == null) {
            throw new IllegalArgumentException("Start DateTime cannot be null.");
        }
        this.subject = subject;
        this.start = startDateTime; // Initial start time
        
        // Default to all-day (8 AM - 5 PM) if end is not specified later
        // The actual setting of 8-5 for all-day will be handled if 'end' is not called.
        // Or, the model can enforce this if an event is created with null end.
        this.end = null; // Explicitly null, model/controller will interpret as all-day if not set
        
        // Default values as per your original builder
        this.description = "No Description Provided";
        this.location = "No Location Provided";
        this.statusValue = "public"; // Default status
    }

    // Private constructor for static factory method, if you prefer that style
    private EventBuilder() {}

    // Static factory method as in your original
    public static EventBuilder createBuilder(String subject, DateTime startDateTime){
        return new EventBuilder(subject, startDateTime);
    }
    
    // Static factory for a completely empty builder, if needed, though less safe.
    // public static EventBuilder createEmptyBuilder(){
    //     return new EventBuilder();
    // }


    public EventBuilder subject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Event subject cannot be null or empty.");
        }
        this.subject = subject;
        return this;
    }

    public EventBuilder start(DateTime startDateTime) {
        if (startDateTime == null) {
            throw new IllegalArgumentException("Start DateTime cannot be null.");
        }
        this.start = startDateTime;
        return this;
    }

    public EventBuilder description(String description) {
        this.description = description;
        return this;
    }

    public EventBuilder end(DateTime endDateTime) {
        if (endDateTime == null) { // Allowing end to be explicitly set to null (e.g. to revert to all-day)
            this.end = null;
            return this;
        }
        if (this.start == null) {
            throw new IllegalStateException("Start time must be set before setting end time.");
        }
        if (endDateTime.isBefore(this.start)) {
            throw new IllegalArgumentException("End DateTime cannot be before start DateTime.");
        }
        this.end = endDateTime;
        return this;
    }

    public EventBuilder location(String location) {
        this.location = location;
        return this;
    }

    public EventBuilder status(String status) {
        if (status != null && (status.equalsIgnoreCase("public") || status.equalsIgnoreCase("private"))) {
            this.statusValue = status.toLowerCase();
        } else {
            throw new IllegalArgumentException("Status must be 'public' or 'private'. Received: " + status);
        }
        return this;
    }

    public IEvent build() {
        if (subject == null || start == null) {
            throw new IllegalStateException("Subject and Start DateTime are mandatory to build an event.");
        }
        
        DateTime effectiveEnd = this.end;
        DateTime effectiveStart = this.start;

        if (this.end == null) { // If end was not set, it's an all-day event
            // As per requirements: 8 AM to 5 PM on the start date.
            // Your original builder set start to 8 AM and end to 5 PM if only start date was given.
            // We'll ensure this logic is applied here or in the model.
            // For now, the Event constructor will take start as is, and null end.
            // The model's createEvent method will then adjust start/end for all-day.
        }

        return new Event(subject, location, effectiveStart, effectiveEnd, statusValue, description);
    }
}