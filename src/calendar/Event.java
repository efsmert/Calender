package calendar;

import java.util.ArrayList;
import java.time.DayOfWeek;

/**
 * Implementation of the IEvent interface representing a calendar event.
 * This class handles both single events and events that are part of a series.
 */
public class Event implements IEvent {
  private String subject;
  private String location;
  private DateTime start;
  private DateTime end;
  private String statusValue;
  private String description;
  
  private String seriesId;
  private boolean isSeriesException;
  private String originalSeriesId;
  
  private boolean isSeriesFlag;
  private ArrayList<DayOfWeek> daysOfWeekList;

  /**
   * Constructs a new Event with the specified parameters.
   * @param subject the event subject
   * @param location the event location
   * @param start the start date and time (required)
   * @param end the end date and time (can be null for all-day events)
   * @param status the event status ("public" or "private")
   * @param description the event description
   * @throws IllegalArgumentException if start is null
   */
  public Event(String subject, String location, DateTime start, DateTime end, String status, String description) {
    if (start == null) {
        throw new IllegalArgumentException("Start DateTime cannot be null.");
    }
    this.subject = subject;
    this.location = location;
    this.start = start;
    this.end = end;
    this.statusValue = status != null ? status : "public";
    this.description = description;
    this.isSeriesException = false;
    this.originalSeriesId = null;
    this.isSeriesFlag = false;
    this.daysOfWeekList = new ArrayList<>();
  }

  /**
   * Constructs a new all-day Event with default values.
   * @param subject the event subject
   * @param start the start date and time
   */
  public Event(String subject, DateTime start) {
      this(subject, "No Location Provided", start, null, "public", "No Description Provided");
  }

  /**
   * Gets the event subject.
   * @return the event subject
   */
  @Override
  public String getSubject() {
    return subject;
  }

  /**
   * Sets the event subject.
   * @param newSubject the new subject
   */
  @Override
  public void setSubject(String newSubject) {
    this.subject = newSubject;
  }

  /**
   * Gets the event location.
   * @return the event location
   */
  @Override
  public String getLocation() {
    return location;
  }

  /**
   * Sets the event location.
   * @param newLocation the new location
   */
  @Override
  public void setLocation(String newLocation) {
    this.location = newLocation;
  }

  /**
   * Gets the event start date and time.
   * @return the start DateTime
   */
  @Override
  public DateTime getStart() {
    return start;
  }

  /**
   * Sets the event start date and time.
   * @param newStart the new start DateTime
   * @throws IllegalArgumentException if newStart is null or after the end time
   */
  @Override
  public void setStart(DateTime newStart) {
    if (newStart == null) {
        throw new IllegalArgumentException("Start DateTime cannot be null.");
    }
    if (this.end != null && newStart.isAfter(this.end)) {
        throw new IllegalArgumentException("Start DateTime cannot be after End DateTime.");
    }
    this.start = newStart;
  }

  /**
   * Gets the event end date and time.
   * @return the end DateTime, or null for all-day events
   */
  @Override
  public DateTime getEnd() {
    return end;
  }

  /**
   * Sets the event end date and time.
   * @param newEnd the new end DateTime
   * @throws IllegalArgumentException if newEnd is before the start time
   */
  @Override
  public void setEnd(DateTime newEnd) {
    if (newEnd != null && newEnd.isBefore(this.start)) {
        throw new IllegalArgumentException("End DateTime cannot be before Start DateTime.");
    }
    this.end = newEnd;
  }

  /**
   * Gets the event status.
   * @return the event status ("public" or "private")
   */
  @Override
  public String getStatus() {
    return statusValue;
  }

  /**
   * Sets the event status.
   * @param newStatus the new status ("public" or "private")
   * @throws IllegalArgumentException if status is not "public" or "private"
   */
  @Override
  public void setStatus(String newStatus) {
    if (newStatus != null && (newStatus.equalsIgnoreCase("public") || newStatus.equalsIgnoreCase("private"))) {
        this.statusValue = newStatus.toLowerCase();
    } else {
        throw new IllegalArgumentException("Status must be 'public' or 'private'. Received: " + newStatus);
    }
  }

  /**
   * Gets the event description.
   * @return the event description
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Sets the event description.
   * @param newDescription the new description
   */
  @Override
  public void setDescription(String newDescription) {
    this.description = newDescription;
  }

  /**
   * Gets the series ID if this event is part of a series.
   * @return the series ID, or null if not part of a series
   */
  @Override
  public String getSeriesId() {
    return seriesId;
  }

  /**
   * Sets the series ID for this event.
   * @param seriesId the series ID
   */
  @Override
  public void setSeriesId(String seriesId) {
    this.seriesId = seriesId;
  }

  /**
   * Checks if this event is an exception within a series.
   * @return true if this is a series exception, false otherwise
   */
  @Override
  public boolean isSeriesException() {
    return isSeriesException;
  }

  /**
   * Sets whether this event is an exception within a series.
   * @param seriesException true if this is a series exception
   */
  @Override
  public void setSeriesException(boolean seriesException) {
    isSeriesException = seriesException;
  }

  /**
   * Gets the original series ID before any modifications.
   * @return the original series ID
   */
  @Override
  public String getOriginalSeriesId() {
    return originalSeriesId;
  }

  /**
   * Sets the original series ID before any modifications.
   * @param originalSeriesId the original series ID
   */
  @Override
  public void setOriginalSeriesId(String originalSeriesId) {
    this.originalSeriesId = originalSeriesId;
  }

  /**
   * Gets whether this event is flagged as part of a series.
   * @return true if this event is part of a series
   */
  public boolean getIsSeriesFlag() {
      return isSeriesFlag;
  }

  /**
   * Sets whether this event is flagged as part of a series.
   * @param isSeriesFlag true if this event is part of a series
   */
  public void setIsSeriesFlag(boolean isSeriesFlag) {
      this.isSeriesFlag = isSeriesFlag;
  }

  /**
   * Gets the list of days of the week for recurring events.
   * @return the list of days of the week
   */
  public ArrayList<DayOfWeek> getDaysOfWeekList() {
      return daysOfWeekList;
  }

  /**
   * Sets the list of days of the week for recurring events.
   * @param daysOfWeekList the list of days of the week
   */
  public void setDaysOfWeekList(ArrayList<DayOfWeek> daysOfWeekList) {
      this.daysOfWeekList = daysOfWeekList;
  }
  
  /**
   * Creates a deep copy of this event.
   * @return a new Event instance that is a copy of this event
   */
  @Override
  public IEvent copy() {
      Event newEvent = new Event(this.subject, this.location, this.start, this.end, this.statusValue, this.description);
      newEvent.setSeriesId(this.seriesId);
      newEvent.setSeriesException(this.isSeriesException);
      newEvent.setOriginalSeriesId(this.originalSeriesId);
      newEvent.setIsSeriesFlag(this.isSeriesFlag);
      if (this.daysOfWeekList != null) {
        newEvent.setDaysOfWeekList(new ArrayList<>(this.daysOfWeekList));
      }
      return newEvent;
  }

  /**
   * Checks if this event matches the specified subject, date, and time.
   * @param subject the subject to match
   * @param date the date to match
   * @param time the time to match
   * @return true if this event matches the criteria
   */
  public boolean match(String subject, Date date, Time time) {
    if (this.start == null) return false;
    return this.subject.equals(subject)
            && this.start.getDate().equals(date)
            && this.start.getTime().equals(time);
  }

  /**
   * Checks if this event is equal to another object.
   * Events are considered equal if they have the same subject, start time, and end time.
   * @param o the object to compare with
   * @return true if the events are equal
   */
  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Event event = (Event) o;
      return subject.equals(event.subject) &&
             start.equals(event.start) &&
             ( (end == null && event.end == null) || (end != null && end.equals(event.end)) );
  }

  /**
   * Returns a hash code for this event.
   * @return the hash code based on subject, start time, and end time
   */
  @Override
  public int hashCode() {
      int result = subject.hashCode();
      result = 31 * result + start.hashCode();
      result = 31 * result + (end != null ? end.hashCode() : 0);
      return result;
  }

  /**
   * Returns a string representation of this event.
   * @return a detailed string representation including all event properties
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Event{subject='").append(subject).append('\'');
    sb.append(", start=").append(start);
    if (end != null) {
        sb.append(", end=").append(end);
    } else {
        sb.append(", end=(All-day or not specified)");
    }
    if (location != null && !location.equals("No Location Provided")) sb.append(", location='").append(location).append('\'');
    if (description != null && !description.equals("No Description Provided")) sb.append(", description='").append(description).append('\'');
    sb.append(", status='").append(statusValue).append('\'');
    if (seriesId != null) sb.append(", seriesId='").append(seriesId).append('\'');
    if (originalSeriesId != null) sb.append(", originalSeriesId='").append(originalSeriesId).append('\'');
    if (isSeriesException) sb.append(", isSeriesException=true");
    if (isSeriesFlag) {
        sb.append(", isPartOfSeries=true");
        if (daysOfWeekList != null && !daysOfWeekList.isEmpty()) {
            sb.append(", repeatsOn=").append(daysOfWeekList);
        }
    }
    sb.append('}');
    return sb.toString();
  }
}