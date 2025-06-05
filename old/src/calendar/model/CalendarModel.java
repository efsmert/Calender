package calendar.model;

import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Represents the model for the calendar application, handling all data and business logic.
 */
public interface CalendarModel {

  /**
   * Creates a single event in the calendar.
   *
   * @param subject The subject of the event.
   * @param description The description of the event (optional).
   * @param location The location of the event (optional).
   * @param startTime The start date and time of the event.
   * @param endTime The end date and time of the event (optional, defaults to 8am-5pm if null for all-day).
   * @param isPublic The status of the event (true for public, false for private).
   * @throws IllegalArgumentException if an event with the same subject, start, and end time already exists.
   */
  void createSingleEvent(String subject, String description, String location, LocalDateTime startTime,
                         LocalDateTime endTime, boolean isPublic) throws IllegalArgumentException;

  /**
   * Creates a series of recurring events based on occurrences.
   *
   * @param subject The subject of the event series.
   * @param description The description of the event series (optional).
   * @param location The location of the event series (optional).
   * @param startTime The start date and time of the first event in the series.
   * @param endTime The end date and time of each event in the series (must be on the same day as start time).
   * @param isPublic The status of the events in the series (true for public, false for private).
   * @param recurrenceDays The days of the week the event repeats on.
   * @param occurrences The number of times the event should repeat.
   * @throws IllegalArgumentException if any generated event conflicts with an existing event or if end time is not on the same day as start time.
   */
  void createRecurringEventByOccurrences(String subject, String description, String location, LocalDateTime startTime,
                                         LocalDateTime endTime, boolean isPublic, Set<DayOfWeek> recurrenceDays,
                                         int occurrences) throws IllegalArgumentException;

  /**
   * Creates a series of recurring events based on an end date.
   *
   * @param subject The subject of the event series.
   * @param description The description of the event series (optional).
   * @param location The location of the event series (optional).
   * @param startTime The start date and time of the first event in the series.
   * @param endTime The end date and time of each event in the series (must be on the same day as start time).
   * @param isPublic The status of the events in the series (true for public, false for private).
   * @param recurrenceDays The days of the week the event repeats on.
   * @param recurrenceEndDate The date until which the event should repeat (inclusive).
   * @throws IllegalArgumentException if any generated event conflicts with an existing event or if end time is not on the same day as start time.
   */
  void createRecurringEventByEndDate(String subject, String description, String location, LocalDateTime startTime,
                                     LocalDateTime endTime, boolean isPublic, Set<DayOfWeek> recurrenceDays,
                                     LocalDateTime recurrenceEndDate) throws IllegalArgumentException;

  /**
   * Edits a single event.
   *
   * @param subject The subject of the event to edit.
   * @param startTime The start date and time of the event to edit.
   * @param endTime The end date and time of the event to edit.
   * @param newSubject The new subject (optional, null to keep existing).
   * @param newDescription The new description (optional, null to keep existing).
   * @param newLocation The new location (optional, null to keep existing).
   * @param newStartTime The new start date and time (optional, null to keep existing).
   * @param newEndTime The new end date and time (optional, null to keep existing).
   * @param newIsPublic The new status (optional, null to keep existing).
   * @throws IllegalArgumentException if the event is not found or if the changes cause a conflict.
   */
  void editSingleEvent(String subject, LocalDateTime startTime, LocalDateTime endTime,
                       String newSubject, String newDescription, String newLocation,
                       LocalDateTime newStartTime, LocalDateTime newEndTime, Boolean newIsPublic) throws IllegalArgumentException;

  /**
   * Edits an event series from a specific instance onwards.
   *
   * @param subject The subject of the event series.
   * @param startTime The start date and time of the event instance to start editing from.
   * @param newSubject The new subject (optional, null to keep existing).
   * @param newDescription The new description (optional, null to keep existing).
   * @param newLocation The new location (optional, null to keep existing).
   * @param newStartTime The new start date and time (optional, null to keep existing).
   * @param newEndTime The new end date and time (optional, null to keep existing).
   * @param newIsPublic The new status (optional, null to keep existing).
   * @throws IllegalArgumentException if the event instance is not found or if the changes cause a conflict.
   */
  void editEventSeriesFromInstance(String subject, LocalDateTime startTime,
                                   String newSubject, String newDescription, String newLocation,
                                   LocalDateTime newStartTime, LocalDateTime newEndTime, Boolean newIsPublic) throws IllegalArgumentException;

  /**
   * Edits all events in an event series.
   *
   * @param subject The subject of the event series.
   * @param startTime The start date and time of any event instance in the series.
   * @param newSubject The new subject (optional, null to keep existing).
   * @param newDescription The new description (optional, null to keep existing).
   * @param newLocation The new location (optional, null to keep existing).
   * @param newStartTime The new start date and time (optional, null to keep existing).
   * @param newEndTime The new end date and time (optional, null to keep existing).
   * @param newIsPublic The new status (optional, null to keep existing).
   * @throws IllegalArgumentException if the event instance is not found or if the changes cause a conflict.
   */
  void editEntireEventSeries(String subject, LocalDateTime startTime,
                             String newSubject, String newDescription, String newLocation,
                             LocalDateTime newStartTime, LocalDateTime newEndTime, Boolean newIsPublic) throws IllegalArgumentException;


  /**
   * Queries all events on a specific date.
   *
   * @param date The date to query for events.
   * @return A list of events scheduled on the given date.
   */
  List<Event> getEventsOnDate(LocalDateTime date);

  /**
   * Queries all events within a date range.
   *
   * @param startTime The start date and time of the range (inclusive).
   * @param endTime The end date and time of the range (inclusive).
   * @return A list of events scheduled within the given date range.
   */
  List<Event> getEventsInRange(LocalDateTime startTime, LocalDateTime endTime);

  /**
   * Checks if the user is busy at a specific date and time.
   *
   * @param dateTime The date and time to check.
   * @return True if the user has an event scheduled at the given date and time, false otherwise.
   */
  boolean isBusyAt(LocalDateTime dateTime);
}