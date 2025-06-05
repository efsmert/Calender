package calendar;

import java.util.ArrayList; // Keep if daysOfWeek is used
import java.time.DayOfWeek; // Keep if daysOfWeek is used, though not in IEvent yet

// Your Event class, adapted to implement IEvent
public class Event implements IEvent {
  private String subject;
  private String location;
  private DateTime start;
  private DateTime end;
  private String statusValue; // Using String for status ("public", "private") as per IEvent and main reqs
  private String description;
  
  // For series handling, from my previous Event.java and IEvent
  private String seriesId;
  private boolean isSeriesException;
  private String originalSeriesId; // Added for Phase 3
  
  // Your original fields - isSeries and daysOfWeek might be used by EventSeries logic later
  private boolean isSeriesFlag; // Renamed to avoid conflict if 'isSeries' method is added
  private ArrayList<DayOfWeek> daysOfWeekList; // Renamed

  // Constructor matching your original Event.java structure
  public Event(String subject, String location, DateTime start, DateTime end, String status, String description) {
    if (start == null) {
        throw new IllegalArgumentException("Start DateTime cannot be null.");
    }
    this.subject = subject;
    this.location = location;
    this.start = start;
    this.end = end; // Can be null for all-day events initially
    this.statusValue = status != null ? status : "public"; // Default to public
    this.description = description;
    this.isSeriesException = false; // Default
    this.originalSeriesId = null; // Default
    this.isSeriesFlag = false; // Default
    this.daysOfWeekList = new ArrayList<>(); // Initialize
  }

  // Constructor for all-day events (simplified, assuming EventBuilder handles defaults)
  public Event(String subject, DateTime start) {
      this(subject, "No Location Provided", start, null, "public", "No Description Provided");
      // For all-day, end might be set to start date + 8-5pm by model/builder logic
  }


  @Override
  public String getSubject() {
    return subject;
  }

  @Override
  public void setSubject(String newSubject) {
    this.subject = newSubject;
  }

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public void setLocation(String newLocation) {
    this.location = newLocation;
  }

  @Override
  public DateTime getStart() {
    return start;
  }

  @Override
  public void setStart(DateTime newStart) {
    if (newStart == null) {
        throw new IllegalArgumentException("Start DateTime cannot be null.");
    }
    // Add validation: start must not be after end if end is not null
    if (this.end != null && newStart.isAfter(this.end)) {
        throw new IllegalArgumentException("Start DateTime cannot be after End DateTime.");
    }
    this.start = newStart;
  }

  @Override
  public DateTime getEnd() {
    return end;
  }

  @Override
  public void setEnd(DateTime newEnd) {
    // Add validation: end must not be before start if newEnd is not null
    if (newEnd != null && newEnd.isBefore(this.start)) {
        throw new IllegalArgumentException("End DateTime cannot be before Start DateTime.");
    }
    this.end = newEnd;
  }

  @Override
  public String getStatus() {
    return statusValue;
  }

  @Override
  public void setStatus(String newStatus) {
    if (newStatus != null && (newStatus.equalsIgnoreCase("public") || newStatus.equalsIgnoreCase("private"))) {
        this.statusValue = newStatus.toLowerCase();
    } else {
        throw new IllegalArgumentException("Status must be 'public' or 'private'. Received: " + newStatus);
    }
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String newDescription) {
    this.description = newDescription;
  }

  // Series-related methods from IEvent
  @Override
  public String getSeriesId() {
    return seriesId;
  }

  @Override
  public void setSeriesId(String seriesId) {
    this.seriesId = seriesId;
  }

  @Override
  public boolean isSeriesException() {
    return isSeriesException;
  }

  @Override
  public void setSeriesException(boolean seriesException) {
    isSeriesException = seriesException;
  }

  @Override
  public String getOriginalSeriesId() {
    return originalSeriesId;
  }

  @Override
  public void setOriginalSeriesId(String originalSeriesId) {
    this.originalSeriesId = originalSeriesId;
  }

  // Your original isSeries/daysOfWeek related fields/methods (getters/setters if needed)
  public boolean getIsSeriesFlag() { // Using your naming convention
      return isSeriesFlag;
  }

  public void setIsSeriesFlag(boolean isSeriesFlag) {
      this.isSeriesFlag = isSeriesFlag;
  }

  public ArrayList<DayOfWeek> getDaysOfWeekList() { // Using your naming convention
      return daysOfWeekList;
  }

  public void setDaysOfWeekList(ArrayList<DayOfWeek> daysOfWeekList) {
      this.daysOfWeekList = daysOfWeekList;
  }
  
  @Override
  public IEvent copy() {
      Event newEvent = new Event(this.subject, this.location, this.start, this.end, this.statusValue, this.description);
      newEvent.setSeriesId(this.seriesId);
      newEvent.setSeriesException(this.isSeriesException);
      newEvent.setOriginalSeriesId(this.originalSeriesId); // Added for Phase 3
      newEvent.setIsSeriesFlag(this.isSeriesFlag);
      if (this.daysOfWeekList != null) {
        newEvent.setDaysOfWeekList(new ArrayList<>(this.daysOfWeekList));
      }
      return newEvent;
  }

  // Your match method - needs adjustment as startDate and startTime are now in DateTime 'start'
  // This method is crucial for your edit logic.
  public boolean match(String subject, Date date, Time time) {
    if (this.start == null) return false;
    return this.subject.equals(subject)
            && this.start.getDate().equals(date) // Assuming Date has a proper equals method
            && this.start.getTime().equals(time); // Assuming Time has a proper equals method
  }

  // Your original applyEdit method is problematic because it takes String... value
  // and tries to parse them. This is not type-safe and error-prone.
  // The IEvent interface promotes type-safe setters, which should be used instead.
  // I will comment out your applyEdit and parseDate/parseTime as they are superseded by direct setters
  // and the controller should handle parsing strings to Date/Time objects.

  /*
  public void applyEdit(String property, String... value) {
    switch (property.toLowerCase()) {
      case "subject":
        setSubject(value[0]);
        break;
      case "start":
        setStart(new DateTime(new Date(Integer.parseInt(value[0]), Integer.parseInt(value[1]), Integer.parseInt(value[2])), new Time(Integer.parseInt(value[3]), Integer.parseInt(value[4]))));
        break;
      case "end":
        setEnd(new DateTime(new Date(Integer.parseInt(value[0]), Integer.parseInt(value[1]), Integer.parseInt(value[2])), new Time(Integer.parseInt(value[3]), Integer.parseInt(value[4]))));
        break;
      case "location":
        // setLocation(value); // This was an error, setLocation expects a single String
        setLocation(value[0]);
        break;
      case "description":
        // setDescription(value); // This was an error, setDescription expects a single String
        setDescription(value[0]);
        break;
      case "status":
        // setStatus(value); // This was an error, setStatus expects a single String (or boolean in your original)
        setStatus(value[0]); // Assuming status is now a string like "public" or "private"
        break;
      default:
        throw new IllegalArgumentException("Unknown property: " + property);
    }
  }

  private Date parseDate(String dateStr) {
    // ... your parsing logic ...
  }

  private Time parseTime(String timeStr) {
    // ... your parsing logic ...
  }
  */

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Event event = (Event) o;
      // Uniqueness based on subject, start, and end as per requirements
      return subject.equals(event.subject) &&
             start.equals(event.start) &&
             // End can be null for all-day events, handle this.
             ( (end == null && event.end == null) || (end != null && end.equals(event.end)) );
  }

  @Override
  public int hashCode() {
      int result = subject.hashCode();
      result = 31 * result + start.hashCode();
      result = 31 * result + (end != null ? end.hashCode() : 0);
      return result;
  }

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
    if (originalSeriesId != null) sb.append(", originalSeriesId='").append(originalSeriesId).append('\''); // Added for Phase 3
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