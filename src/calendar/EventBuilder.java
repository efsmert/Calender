package calendar;

import java.time.DayOfWeek;
import java.util.ArrayList;

/**
 * Builder class for creating Event instances using the builder pattern.
 * Provides a fluent interface for setting event properties and validates constraints.
 */
public class EventBuilder {
    private String subject;
    private DateTime start;
    private String description;
    private DateTime end;
    private String location;
    private String statusValue;

    /**
     * Constructs an EventBuilder with required subject and start time.
     * Sets default values for optional properties.
     * @param subject the event subject (required)
     * @param startDateTime the start date and time (required)
     * @throws IllegalArgumentException if subject is null/empty or startDateTime is null
     */
    public EventBuilder(String subject, DateTime startDateTime) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Event subject cannot be null or empty.");
        }
        if (startDateTime == null) {
            throw new IllegalArgumentException("Start DateTime cannot be null.");
        }
        this.subject = subject;
        this.start = startDateTime;
        this.end = null;
        this.description = "No Description Provided";
        this.location = "No Location Provided";
        this.statusValue = "public";
    }

    /**
     * Private constructor for static factory methods.
     */
    private EventBuilder() {}

    /**
     * Static factory method to create a new EventBuilder instance.
     * @param subject the event subject
     * @param startDateTime the start date and time
     * @return a new EventBuilder instance
     */
    public static EventBuilder createBuilder(String subject, DateTime startDateTime){
        return new EventBuilder(subject, startDateTime);
    }

    /**
     * Sets the event subject.
     * @param subject the event subject
     * @return this EventBuilder instance for method chaining
     * @throws IllegalArgumentException if subject is null or empty
     */
    public EventBuilder subject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Event subject cannot be null or empty.");
        }
        this.subject = subject;
        return this;
    }

    /**
     * Sets the event start date and time.
     * @param startDateTime the start date and time
     * @return this EventBuilder instance for method chaining
     * @throws IllegalArgumentException if startDateTime is null
     */
    public EventBuilder start(DateTime startDateTime) {
        if (startDateTime == null) {
            throw new IllegalArgumentException("Start DateTime cannot be null.");
        }
        this.start = startDateTime;
        return this;
    }

    /**
     * Sets the event description.
     * @param description the event description
     * @return this EventBuilder instance for method chaining
     */
    public EventBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the event end date and time.
     * @param endDateTime the end date and time (can be null for all-day events)
     * @return this EventBuilder instance for method chaining
     * @throws IllegalStateException if start time has not been set
     * @throws IllegalArgumentException if endDateTime is before start time
     */
    public EventBuilder end(DateTime endDateTime) {
        if (endDateTime == null) {
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

    /**
     * Sets the event location.
     * @param location the event location
     * @return this EventBuilder instance for method chaining
     */
    public EventBuilder location(String location) {
        this.location = location;
        return this;
    }

    /**
     * Sets the event status.
     * @param status the event status ("public" or "private")
     * @return this EventBuilder instance for method chaining
     * @throws IllegalArgumentException if status is not "public" or "private"
     */
    public EventBuilder status(String status) {
        if (status != null && (status.equalsIgnoreCase("public") || status.equalsIgnoreCase("private"))) {
            this.statusValue = status.toLowerCase();
        } else {
            throw new IllegalArgumentException("Status must be 'public' or 'private'. Received: " + status);
        }
        return this;
    }

    /**
     * Builds and returns a new Event instance with the configured properties.
     * @return a new Event instance
     * @throws IllegalStateException if subject or start time is not set
     */
    public IEvent build() {
        if (subject == null || start == null) {
            throw new IllegalStateException("Subject and Start DateTime are mandatory to build an event.");
        }
        
        DateTime effectiveEnd = this.end;
        DateTime effectiveStart = this.start;

        return new Event(subject, location, effectiveStart, effectiveEnd, statusValue, description);
    }
}