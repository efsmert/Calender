package calendar;

import java.util.List;
import java.time.DayOfWeek;

/**
 * Represents the model interface for the calendar application.
 * The model is responsible for managing the calendar data, such as events.
 */
public interface ICalendarModel {

    /**
     * Creates a single, non-recurring event.
     * @param subject the subject of the event
     * @param startDateTime the start date and time
     * @param endDateTime the end date and time (can be null for all-day)
     * @param description optional description
     * @param location optional location
     * @param status optional status ("public" or "private")
     * @return true if event creation was successful, false otherwise (e.g., duplicate)
     */
    boolean createEvent(String subject, DateTime startDateTime, DateTime endDateTime, String description, String location, String status);

    /**
     * Creates a series of recurring events.
     * @param subject the subject of the event series
     * @param seriesStartDateTime the start date and time of the first event in the series
     * @param seriesEndDateTime the end date and time of the first event (defines duration, can be null for all-day series)
     * @param description optional description
     * @param location optional location
     * @param status optional status
     * @param repeatDays list of DayOfWeek for recurrence
     * @param occurrences number of times the event repeats (use null if using seriesEndDate)
     * @param seriesEndDate date until which the series repeats (use null if using occurrences)
     * @return true if series creation was successful, false otherwise
     */
    boolean createEventSeries(String subject, DateTime seriesStartDateTime, DateTime seriesEndDateTime,
                              String description, String location, String status,
                              List<DayOfWeek> repeatDays, Integer occurrences, Date seriesEndDate);

    /**
     * Edits an existing event or series of events.
     * @param findSubject subject of the event to find
     * @param findStartDateTime start DateTime of the event to find
     * @param findEndDateTime end DateTime of the event to find (used for "this" scope, can be null if original was all-day and controller derives it)
     * @param propertyToChange name of the property to change (e.g., "subject", "start", "end")
     * @param newValue the new value for the property (type depends on property, e.g., String or DateTime)
     * @param scope the scope of the edit ("this", "future", "all")
     * @return true if edit was successful, false otherwise
     */
    boolean editEvent(String findSubject, DateTime findStartDateTime, DateTime findEndDateTime,
                      String propertyToChange, Object newValue, String scope);
    
    /**
     * Gets all events in the calendar.
     * @return a list of all events (as IEvent)
     */
    List<IEvent> getAllEvents();

    /**
     * Gets events for a specific date.
     * @param date the date to get events for (using your Date class)
     * @return a list of events (as IEvent) on the specified date
     */
    List<IEvent> getEventsOnDate(Date date);

    /**
     * Gets events within a given date and time range.
     * @param startRange the start of the range
     * @param endRange the end of the range
     * @return a list of events (as IEvent) within the specified range
     */
    List<IEvent> getEventsInRange(DateTime startRange, DateTime endRange);

    /**
     * Checks if the user is busy at a specific date and time.
     * @param dateTime the specific date and time to check
     * @return true if busy, false if available
     */
    boolean isBusyAt(DateTime dateTime);
}