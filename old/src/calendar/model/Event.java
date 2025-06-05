package calendar.model;

import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.Set;
import java.util.UUID;

public class Event {
  private String subject;
  private String description;
  private String location;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private boolean isPublic; // true for public, false for private

  // Fields for recurring events
  private UUID seriesId; // Unique ID for the event series
  private Set<DayOfWeek> recurrenceDays;
  private Integer recurrenceOccurrences; // Number of times it repeats
  private LocalDateTime recurrenceEndDate; // Date until it repeats

  // Constructor for single events
  public Event(String subject, String description, String location, LocalDateTime startTime,
               LocalDateTime endTime, boolean isPublic) {
    this.subject = subject;
    this.description = description;
    this.location = location;
    this.startTime = startTime;
    this.endTime = endTime;
    this.isPublic = isPublic;
    this.seriesId = null; // Single event
    this.recurrenceDays = null;
    this.recurrenceOccurrences = null;
    this.recurrenceEndDate = null;
  }

  // Constructor for recurring events
  public Event(String subject, String description, String location, LocalDateTime startTime,
               LocalDateTime endTime, boolean isPublic, UUID seriesId, Set<DayOfWeek> recurrenceDays,
               Integer recurrenceOccurrences, LocalDateTime recurrenceEndDate) {
    this.subject = subject;
    this.description = description;
    this.location = location;
    this.startTime = startTime;
    this.endTime = endTime;
    this.isPublic = isPublic;
    this.seriesId = seriesId;
    this.recurrenceDays = recurrenceDays;
    this.recurrenceOccurrences = recurrenceOccurrences;
    this.recurrenceEndDate = recurrenceEndDate;
  }

  // Getters
  public String getSubject() {
    return subject;
  }

  public String getDescription() {
    return description;
  }

  public String getLocation() {
    return location;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public boolean isPublic() {
    return isPublic;
  }

  public UUID getSeriesId() {
    return seriesId;
  }

  public Set<DayOfWeek> getRecurrenceDays() {
    return recurrenceDays;
  }

  public Integer getRecurrenceOccurrences() {
    return recurrenceOccurrences;
  }

  public LocalDateTime getRecurrenceEndDate() {
    return recurrenceEndDate;
  }

  // Setters (for editing)
  public void setSubject(String subject) {
    this.subject = subject;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public void setPublic(boolean aPublic) {
    isPublic = aPublic;
  }

  // Method to check if it's a recurring event
  public boolean isRecurring() {
    return seriesId != null;
  }

  // Method to check if it's an all-day event (8am to 5pm)
  public boolean isAllDayEvent() {
    return startTime.getHour() == 8 && startTime.getMinute() == 0 &&
           endTime.getHour() == 17 && endTime.getMinute() == 0 &&
           startTime.toLocalDate().isEqual(endTime.toLocalDate());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Event event = (Event) o;
    return isPublic == event.isPublic &&
           subject.equals(event.subject) &&
           startTime.equals(event.startTime) &&
           endTime.equals(event.endTime);
  }

  @Override
  public int hashCode() {
    // Simple hash code based on unique properties
    int result = subject.hashCode();
    result = 31 * result + startTime.hashCode();
    result = 31 * result + endTime.hashCode();
    result = 31 * result + (isPublic ? 1 : 0);
    return result;
  }
}