package calendar.model;

import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Concrete implementation of the CalendarModel interface.
 */
public class CalendarModelImpl implements CalendarModel {

  private List<Event> events;

  public CalendarModelImpl() {
    this.events = new ArrayList<>();
  }

  @Override
  public void createSingleEvent(String subject, String description, String location, LocalDateTime startTime,
                                 LocalDateTime endTime, boolean isPublic) throws IllegalArgumentException {
    // TODO: Implement single event creation logic, including all-day event handling and conflict checking
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public void createRecurringEventByOccurrences(String subject, String description, String location, LocalDateTime startTime,
                                                LocalDateTime endTime, boolean isPublic, Set<DayOfWeek> recurrenceDays,
                                                int occurrences) throws IllegalArgumentException {
    // TODO: Implement recurring event creation by occurrences logic, including conflict checking
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public void createRecurringEventByEndDate(String subject, String description, String location, LocalDateTime startTime,
                                            LocalDateTime endTime, boolean isPublic, Set<DayOfWeek> recurrenceDays,
                                            LocalDateTime recurrenceEndDate) throws IllegalArgumentException {
    // TODO: Implement recurring event creation by end date logic, including conflict checking
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public void editSingleEvent(String subject, LocalDateTime startTime, LocalDateTime endTime,
                       String newSubject, String newDescription, String newLocation,
                       LocalDateTime newStartTime, LocalDateTime newEndTime, Boolean newIsPublic) throws IllegalArgumentException {
    // TODO: Implement single event editing logic, including conflict checking
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public void editEventSeriesFromInstance(String subject, LocalDateTime startTime,
                                   String newSubject, String newDescription, String newLocation,
                                   LocalDateTime newStartTime, LocalDateTime newEndTime, Boolean newIsPublic) throws IllegalArgumentException {
    // TODO: Implement event series editing from instance logic, including conflict checking
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public void editEntireEventSeries(String subject, LocalDateTime startTime,
                             String newSubject, String newDescription, String newLocation,
                             LocalDateTime newStartTime, LocalDateTime newEndTime, Boolean newIsPublic) throws IllegalArgumentException {
    // TODO: Implement entire event series editing logic, including conflict checking
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public List<Event> getEventsOnDate(LocalDateTime date) {
    // TODO: Implement querying events on a specific date
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public List<Event> getEventsInRange(LocalDateTime startTime, LocalDateTime endTime) {
    // TODO: Implement querying events in a date range
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public boolean isBusyAt(LocalDateTime dateTime) {
    // TODO: Implement checking if busy at a specific date and time
    throw new UnsupportedOperationException("Not yet implemented");
  }

  // Helper method to check for event conflicts
  private boolean hasConflict(Event newEvent) {
    // TODO: Implement conflict checking logic
    return false; // Placeholder
  }
}