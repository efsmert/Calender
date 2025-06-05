package calendar;

import java.util.List;
import java.time.DayOfWeek; // For series creation

/**
 * Represents the model interface for the calendar application.
 * The model is responsible for managing the calendar data, such as events.
 */
public interface ICalendarModel {

    /**
     * Creates a single, non-recurring event.
     * @param subject The subject of the event.
     * @param startDateTime The start date and time.
     * @param endDateTime The end date and time (can be null for all-day).
     * @param description Optional description.
     * @param location Optional location.
     * @param status Optional status ("public" or "private").
     * @return true if event creation was successful, false otherwise (e.g., duplicate).
     */
    boolean createEvent(String subject, DateTime startDateTime, DateTime endDateTime, String description, String location, String status);

    /**
     * Creates a series of recurring events.
     * @param subject The subject of the event series.
     * @param seriesStartDateTime The start date and time of the first event in the series.
     * @param seriesEndDateTime The end date and time of the first event (defines duration, can be null for all-day series).
     * @param description Optional description.
     * @param location Optional location.
     * @param status Optional status.
     * @param repeatDays List of DayOfWeek for recurrence.
     * @param occurrences Number of times the event repeats (use null if using seriesEndDate).
     * @param seriesEndDate Date until which the series repeats (use null if using occurrences).
     * @return true if series creation was successful, false otherwise.
     */
    boolean createEventSeries(String subject, DateTime seriesStartDateTime, DateTime seriesEndDateTime,
                              String description, String location, String status,
                              List<DayOfWeek> repeatDays, Integer occurrences, Date seriesEndDate); // Using your Date for seriesEndDate

    /**
     * Edits an existing event or series of events.
     * @param findSubject Subject of the event to find.
     * @param findStartDateTime Start DateTime of the event to find.
     * @param findEndDateTime End DateTime of the event to find (used for "this" scope, can be null if original was all-day and controller derives it).
     * @param propertyToChange Name of the property to change (e.g., "subject", "start", "end").
     * @param newValue The new value for the property (type depends on property, e.g., String or DateTime).
     * @param scope The scope of the edit ("this", "future", "all").
     * @return true if edit was successful, false otherwise.
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
     * @param startRange The start of the range.
     * @param endRange The end of the range.
     * @return A list of events (as IEvent) within the specified range.
     */
    List<IEvent> getEventsInRange(DateTime startRange, DateTime endRange);

    /**
     * Checks if the user is busy at a specific date and time.
     * @param dateTime The specific date and time to check.
     * @return true if busy, false if available.
     */
    boolean isBusyAt(DateTime dateTime);

    // Your original methods from ICalendarModel, adapted if necessary:
    // void addEvent(IEvent event); // Covered by createEvent/createEventSeries
    // void removeEvent(String subject, Date date, Time time); // Deferring direct removal, edit can effectively "remove" by changing significantly or model can implement
}