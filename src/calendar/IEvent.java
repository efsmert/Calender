package calendar;

/**
 * Interface representing a calendar event.
 * Defines the contract for event properties and series management functionality.
 */
public interface IEvent {
    
    /**
     * Gets the event subject.
     * @return the event subject
     */
    String getSubject();
    
    /**
     * Sets the event subject.
     * @param subject the event subject
     */
    void setSubject(String subject);

    /**
     * Gets the event start date and time.
     * @return the start DateTime
     */
    DateTime getStart();
    
    /**
     * Sets the event start date and time.
     * @param start the start DateTime
     */
    void setStart(DateTime start);

    /**
     * Gets the event end date and time.
     * @return the end DateTime, or null for all-day events
     */
    DateTime getEnd();
    
    /**
     * Sets the event end date and time.
     * @param end the end DateTime
     */
    void setEnd(DateTime end);

    /**
     * Gets the event location.
     * @return the event location
     */
    String getLocation();
    
    /**
     * Sets the event location.
     * @param location the event location
     */
    void setLocation(String location);

    /**
     * Gets the event description.
     * @return the event description
     */
    String getDescription();
    
    /**
     * Sets the event description.
     * @param description the event description
     */
    void setDescription(String description);

    /**
     * Gets the event status.
     * @return the event status ("public" or "private")
     */
    String getStatus();
    
    /**
     * Sets the event status.
     * @param status the event status ("public" or "private")
     */
    void setStatus(String status);

    /**
     * Gets the series ID if this event is part of a series.
     * @return the series ID, or null if not part of a series
     */
    String getSeriesId();
    
    /**
     * Sets the series ID for this event.
     * @param seriesId the series ID
     */
    void setSeriesId(String seriesId);

    /**
     * Checks if this event is an exception within a series.
     * @return true if this is a series exception, false otherwise
     */
    boolean isSeriesException();
    
    /**
     * Sets whether this event is an exception within a series.
     * @param isSeriesException true if this is a series exception
     */
    void setSeriesException(boolean isSeriesException);

    /**
     * Gets the original series ID before any modifications.
     * @return the original series ID
     */
    String getOriginalSeriesId();
    
    /**
     * Sets the original series ID before any modifications.
     * @param originalSeriesId the original series ID
     */
    void setOriginalSeriesId(String originalSeriesId);
    
    /**
     * Creates a deep copy of this event.
     * Used for creating modifiable copies, especially for series edits.
     * @return a new IEvent instance that is a copy of this event
     */
    IEvent copy();
}