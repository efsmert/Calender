package calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.DayOfWeek;

/**
 * Implementation of the ICalendarModel interface that manages calendar events.
 * This class handles creating, editing, and querying calendar events and event series.
 */
public class CalendarModelImpl implements ICalendarModel {
    private List<IEvent> events;

    /**
     * Constructs a new CalendarModelImpl with an empty list of events.
     */
    public CalendarModelImpl() {
        this.events = new ArrayList<>();
    }

    /**
     * Checks if an event would be a duplicate of an existing event.
     * Events are considered duplicates if they have the same subject, start date/time, and end date/time.
     * @param eventToCheck the event to check for duplication
     * @param eventToExclude an event to exclude from the duplicate check (can be null)
     * @return true if the event is a duplicate, false otherwise
     */
    private boolean isDuplicate(IEvent eventToCheck, IEvent eventToExclude) {
        for (IEvent existingEvent : events) {
            if (existingEvent == eventToExclude) {
                continue;
            }
            if (existingEvent.getSubject().equals(eventToCheck.getSubject()) &&
                existingEvent.getStart().equals(eventToCheck.getStart()) &&
                Objects.equals(existingEvent.getEnd(), eventToCheck.getEnd())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a single calendar event.
     * @param subject the event subject (required)
     * @param startDateTime the start date and time (required)
     * @param endDateTime the end date and time (null for all-day events)
     * @param description the event description (can be null)
     * @param location the event location (can be null)
     * @param status the event status ("public" or "private")
     * @return true if the event was created successfully, false otherwise
     */
    @Override
    public boolean createEvent(String subject, DateTime startDateTime, DateTime endDateTime, String description, String location, String status) {
        if (startDateTime == null || subject == null || subject.trim().isEmpty()) {
            System.err.println("Error: Subject and start date/time are required.");
            return false;
        }

        DateTime effectiveStart = startDateTime;
        DateTime effectiveEnd = endDateTime;

        if (effectiveEnd == null) {
            Date date = startDateTime.getDate();
            effectiveStart = new DateTime(date, new Time(8, 0));
            effectiveEnd = new DateTime(date, new Time(17, 0));
        }

        if (effectiveEnd.isBefore(effectiveStart)) {
            System.err.println("Error: Event end time cannot be before start time.");
            return false;
        }

        IEvent newEvent = new Event(subject, location, effectiveStart, effectiveEnd, status, description);

        if (isDuplicate(newEvent, null)) {
            System.err.println("Error: An event with the same subject, start date/time, and end date/time already exists.");
            return false;
        }

        events.add(newEvent);
        return true;
    }

    /**
     * Creates a series of recurring calendar events.
     * @param subject the event subject (required)
     * @param seriesStartDateTime the start date and time for the series (required)
     * @param seriesEndDateTime the end date and time for each event in the series (null for all-day events)
     * @param description the event description (can be null)
     * @param location the event location (can be null)
     * @param status the event status ("public" or "private")
     * @param repeatDays the days of the week on which to repeat the event (required)
     * @param occurrences the number of occurrences (null if using seriesEndDate)
     * @param seriesEndDate the date after which to stop creating events (null if using occurrences)
     * @return true if the event series was created successfully, false otherwise
     */
    @Override
    public boolean createEventSeries(String subject, DateTime seriesStartDateTime, DateTime seriesEndDateTime,
                                     String description, String location, String status,
                                     List<DayOfWeek> repeatDays, Integer occurrences, Date seriesEndDate) {

        if (seriesStartDateTime == null || subject == null || subject.trim().isEmpty() || repeatDays == null || repeatDays.isEmpty()) {
            System.err.println("Error: Subject, start date/time, and repeat days are required for a series.");
            return false;
        }
        if (occurrences == null && seriesEndDate == null) {
            System.err.println("Error: Either number of occurrences or a series end date must be specified.");
            return false;
        }
        if (occurrences != null && seriesEndDate != null) {
            System.err.println("Error: Specify either number of occurrences or a series end date, not both.");
            return false;
        }
        if (occurrences != null && occurrences <= 0) {
            System.err.println("Error: Number of occurrences must be positive.");
            return false;
        }
        
        DateTime effectiveSeriesStart = seriesStartDateTime;
        DateTime effectiveSeriesEnd = seriesEndDateTime;

        if (effectiveSeriesEnd == null) {
            Date date = seriesStartDateTime.getDate();
            effectiveSeriesStart = new DateTime(date, new Time(8, 0));
            effectiveSeriesEnd = new DateTime(date, new Time(17, 0));
        }
        
        if (seriesEndDate != null) {
            boolean endDateIsBeforeStartDate = seriesEndDate.getYear() < effectiveSeriesStart.getDate().getYear() ||
               (seriesEndDate.getYear() == effectiveSeriesStart.getDate().getYear() && seriesEndDate.getMonth() < effectiveSeriesStart.getDate().getMonth()) ||
               (seriesEndDate.getYear() == effectiveSeriesStart.getDate().getYear() && seriesEndDate.getMonth() == effectiveSeriesStart.getDate().getMonth() && seriesEndDate.getDay() < effectiveSeriesStart.getDate().getDay());
            if (endDateIsBeforeStartDate) {
                System.err.println("Error: Series end date cannot be before the series start date.");
                return false;
            }
        }

        if (effectiveSeriesStart.getDate().getYear() != effectiveSeriesEnd.getDate().getYear() ||
            effectiveSeriesStart.getDate().getMonth() != effectiveSeriesEnd.getDate().getMonth() ||
            effectiveSeriesStart.getDate().getDay() != effectiveSeriesEnd.getDate().getDay()) {
            System.err.println("Error: For recurring events, the start and end time must be on the same day.");
            return false;
        }

        List<IEvent> potentialSeriesEvents = new ArrayList<>();
        Date currentDate = new Date(effectiveSeriesStart.getDate().getDay(), effectiveSeriesStart.getDate().getMonth(), effectiveSeriesStart.getDate().getYear());
        int eventsCreated = 0;
        String generatedSeriesId = UUID.randomUUID().toString();

        Time startTime = effectiveSeriesStart.getTime();
        Time endTime = effectiveSeriesEnd.getTime();

        int safetyBreak = 0;

        while (safetyBreak++ < (366 * 5)) {
            if (occurrences != null && eventsCreated >= occurrences) {
                break;
            }
            if (seriesEndDate != null && 
                (currentDate.getYear() > seriesEndDate.getYear() ||
                 (currentDate.getYear() == seriesEndDate.getYear() && currentDate.getMonth() > seriesEndDate.getMonth()) ||
                 (currentDate.getYear() == seriesEndDate.getYear() && currentDate.getMonth() == seriesEndDate.getMonth() && currentDate.getDay() > seriesEndDate.getDay()))) {
                break;
            }

            java.time.LocalDate tempLocalDate = java.time.LocalDate.of(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay());
            if (repeatDays.contains(tempLocalDate.getDayOfWeek())) {
                DateTime eventStartDt = new DateTime(new Date(currentDate.getDay(), currentDate.getMonth(), currentDate.getYear()), startTime);
                DateTime eventEndDt = new DateTime(new Date(currentDate.getDay(), currentDate.getMonth(), currentDate.getYear()), endTime);
                
                IEvent seriesInstance = new Event(subject, location, eventStartDt, eventEndDt, status, description);
                seriesInstance.setSeriesId(generatedSeriesId);
                seriesInstance.setOriginalSeriesId(generatedSeriesId);
                if (seriesInstance instanceof Event) {
                    ((Event) seriesInstance).setIsSeriesFlag(true);
                    ((Event) seriesInstance).setDaysOfWeekList(new ArrayList<>(repeatDays));
                }

                boolean conflict = false;
                if (isDuplicate(seriesInstance, null)) {
                    conflict = true;
                }
                if (!conflict) {
                    for (IEvent potentialEvent : potentialSeriesEvents) {
                        if (potentialEvent.getSubject().equals(seriesInstance.getSubject()) &&
                            potentialEvent.getStart().equals(seriesInstance.getStart()) &&
                            Objects.equals(potentialEvent.getEnd(), seriesInstance.getEnd())) {
                            conflict = true;
                            break;
                        }
                    }
                }

                if (conflict) {
                    System.err.println("Error: A generated event in the series conflicts with an existing or another potential series event: "
                                       + seriesInstance.getSubject() + " on " + seriesInstance.getStart());
                    return false; 
                }
                potentialSeriesEvents.add(seriesInstance);
                eventsCreated++;
            }
            currentDate.advance(1);
        }
        
        if (safetyBreak >= (366*5) && (occurrences == null || eventsCreated < occurrences) && seriesEndDate == null) {
            System.err.println("Error: Series generation exceeded safety limit. Please specify occurrences or a valid end date.");
            return false;
        }

        if (potentialSeriesEvents.isEmpty() && (occurrences != null && occurrences > 0 || seriesEndDate != null)) {
            System.err.println("Warning: No events were generated for the series based on the criteria.");
        }

        this.events.addAll(potentialSeriesEvents);
        return true;
    }
    
    /**
     * Edits an existing event or series of events.
     * @param findSubject the subject of the event to find
     * @param findStartDateTime the start date/time of the event to find
     * @param findEndDateTime the end date/time of the event to find (required for "this" scope)
     * @param propertyToChange the property to modify (subject, start, end, description, location, status)
     * @param newValue the new value for the property
     * @param scope the scope of the edit ("this", "future", or "all")
     * @return true if the edit was successful, false otherwise
     */
    @Override
    public boolean editEvent(String findSubject, DateTime findStartDateTime, DateTime findEndDateTime,
                             String propertyToChange, Object newValue, String scope) {
        List<IEvent> targetEvents = new ArrayList<>();
        IEvent anchorEvent = null;

        if ("this".equals(scope)) {
            if (findEndDateTime == null && findStartDateTime != null) {
                 Date date = findStartDateTime.getDate();
                 findStartDateTime = new DateTime(date, new Time(8,0));
                 findEndDateTime = new DateTime(date, new Time(17,0));
            }
            for (IEvent event : events) {
                if (event.getSubject().equals(findSubject) &&
                    event.getStart().equals(findStartDateTime) &&
                    Objects.equals(event.getEnd(), findEndDateTime)) {
                    targetEvents.add(event);
                }
            }
            if (targetEvents.isEmpty()) {
                System.err.println("Error: No event found matching subject '" + findSubject + "', start '" + findStartDateTime + "', and end '" + findEndDateTime + "'.");
                return false;
            }
            if (targetEvents.size() > 1) {
                System.err.println("Error: Multiple events found for 'edit event' (this scope). This indicates a data integrity issue or overly broad match.");
                return false;
            }
            anchorEvent = targetEvents.get(0);
        } else {
            List<IEvent> candidates = new ArrayList<>();
            for (IEvent event : events) {
                if (event.getSubject().equals(findSubject) && event.getStart().equals(findStartDateTime)) {
                    candidates.add(event);
                }
            }
            if (candidates.isEmpty()) {
                System.err.println("Error: No event found matching subject '" + findSubject + "' and start time '" + findStartDateTime + "'.");
                return false;
            }
            anchorEvent = candidates.get(0);
            
            if (candidates.size() > 1) {
                String firstSeriesId = anchorEvent.getSeriesId();
                if (firstSeriesId == null) {
                     System.err.println("Error: Ambiguous edit. Multiple non-series events match subject '" + findSubject + "' and start time '" + findStartDateTime + "'.");
                     return false;
                }
                for (IEvent candidate : candidates) {
                    if (!firstSeriesId.equals(candidate.getSeriesId())) {
                        System.err.println("Error: Ambiguous edit. Multiple distinct series match subject '" + findSubject + "' and start time '" + findStartDateTime + "'.");
                        return false;
                    }
                }
            }

            if ("future".equals(scope)) {
                if (anchorEvent.getSeriesId() != null) {
                    if (anchorEvent.isSeriesException()) {
                        targetEvents.add(anchorEvent);
                    } else {
                        for (IEvent event : events) {
                            if (anchorEvent.getSeriesId().equals(event.getSeriesId()) &&
                                !event.getStart().isBefore(anchorEvent.getStart()) &&
                                !event.isSeriesException()) {
                                targetEvents.add(event);
                            }
                        }
                    }
                } else {
                    targetEvents.add(anchorEvent);
                }
            } else if ("all".equals(scope)) {
                String seriesIdToMatch = anchorEvent.getSeriesId();
                if (seriesIdToMatch != null) {
                    for (IEvent event : events) {
                        if (seriesIdToMatch.equals(event.getSeriesId())) {
                            targetEvents.add(event);
                        }
                    }
                } else {
                    targetEvents.add(anchorEvent);
                }
            }
        }

        if (targetEvents.isEmpty()) {
            System.err.println("Error: No events targeted for modification based on scope '" + scope + "'.");
            return false;
        }

        List<IEvent> eventsToRemove = new ArrayList<>();
        List<IEvent> eventsToAdd = new ArrayList<>();
        boolean startPropertyChanged = propertyToChange.equalsIgnoreCase("start");
        String newSeriesIdForSplit = null;
        String newSeriesIdForFutureScope = null;

        if (scope.equals("future") && anchorEvent != null && anchorEvent.getSeriesId() != null && !targetEvents.isEmpty()) {
            newSeriesIdForFutureScope = UUID.randomUUID().toString();
        } else if (startPropertyChanged && anchorEvent != null && anchorEvent.getSeriesId() != null && scope.equals("all") && !targetEvents.isEmpty()) {
            newSeriesIdForSplit = UUID.randomUUID().toString();
        }

        for (IEvent originalEvent : targetEvents) {
            IEvent eventToModify = originalEvent.copy();

            switch (propertyToChange.toLowerCase()) {
                case "subject": eventToModify.setSubject((String) newValue); break;
                case "start": eventToModify.setStart((DateTime) newValue); break;
                case "end": eventToModify.setEnd((DateTime) newValue); break;
                case "description": eventToModify.setDescription((String) newValue); break;
                case "location": eventToModify.setLocation((String) newValue); break;
                case "status": eventToModify.setStatus((String) newValue); break;
                default: System.err.println("Error: Unknown property to change: " + propertyToChange); return false;
            }

            String currentOriginalId = originalEvent.getOriginalSeriesId();
            String currentSeriesId = originalEvent.getSeriesId();

            eventToModify.setOriginalSeriesId(currentOriginalId != null ? currentOriginalId : currentSeriesId);

            if (scope.equals("future") && newSeriesIdForFutureScope != null) {
                eventToModify.setOriginalSeriesId(originalEvent.getSeriesId());
                eventToModify.setSeriesId(newSeriesIdForFutureScope);
                eventToModify.setSeriesException(false);
            } else if (scope.equals("all")) {
                String masterSeriesIdForAnchor = anchorEvent.getSeriesId();
                if (startPropertyChanged && newSeriesIdForSplit != null) {
                    eventToModify.setOriginalSeriesId(masterSeriesIdForAnchor);
                    eventToModify.setSeriesId(newSeriesIdForSplit);
                    eventToModify.setSeriesException(false);
                } else if (!startPropertyChanged && masterSeriesIdForAnchor != null) {
                    eventToModify.setSeriesId(masterSeriesIdForAnchor);
                    eventToModify.setOriginalSeriesId(masterSeriesIdForAnchor);
                    eventToModify.setSeriesException(false);
                }
            } else if (scope.equals("this")) {
                eventToModify.setSeriesId(originalEvent.getSeriesId());
                eventToModify.setSeriesException(true);
            }

            if (isDuplicate(eventToModify, originalEvent)) {
                System.err.println("Error: Modified event (" + eventToModify.getSubject() + " at " + eventToModify.getStart() + ") conflicts with an existing event.");
                return false;
            }
            
            eventsToRemove.add(originalEvent);
            eventsToAdd.add(eventToModify);
        }

        this.events.removeAll(eventsToRemove);
        this.events.addAll(eventsToAdd);
        return true;
    }

    /**
     * Returns all events in the calendar.
     * @return a copy of the list of all events
     */
    @Override
    public List<IEvent> getAllEvents() {
        return new ArrayList<>(events);
    }

    /**
     * Returns all events that occur on a specific date.
     * @param date the date to search for events
     * @return a list of events that occur on the specified date
     */
    @Override
    public List<IEvent> getEventsOnDate(Date date) {
        List<IEvent> result = new ArrayList<>();
        for (IEvent event : events) {
            DateTime start = event.getStart();
            DateTime end = event.getEnd() != null ? event.getEnd() : new DateTime(start.getDate(), new Time(17,0));

            boolean startsBeforeOrOn = start.getDate().getYear() < date.getYear() ||
                                     (start.getDate().getYear() == date.getYear() && start.getDate().getMonth() < date.getMonth()) ||
                                     (start.getDate().getYear() == date.getYear() && start.getDate().getMonth() == date.getMonth() && start.getDate().getDay() <= date.getDay());

            boolean endsAfterOrOn = end.getDate().getYear() > date.getYear() ||
                                   (end.getDate().getYear() == date.getYear() && end.getDate().getMonth() > date.getMonth()) ||
                                   (end.getDate().getYear() == date.getYear() && end.getDate().getMonth() == date.getMonth() && end.getDate().getDay() >= date.getDay());
            
            if (startsBeforeOrOn && endsAfterOrOn) {
                if ( (start.getDate().getYear() < date.getYear() || (start.getDate().getYear() == date.getYear() && (start.getDate().getMonth() < date.getMonth() || (start.getDate().getMonth() == date.getMonth() && start.getDate().getDay() <= date.getDay())))) &&
                     (end.getDate().getYear() > date.getYear() || (end.getDate().getYear() == date.getYear() && (end.getDate().getMonth() > date.getMonth() || (end.getDate().getMonth() == date.getMonth() && end.getDate().getDay() >= date.getDay())))) )
                {
                    boolean dateIsAfterOrSameAsStart = date.getYear() > start.getDate().getYear() ||
                        (date.getYear() == start.getDate().getYear() && (date.getMonth() > start.getDate().getMonth() ||
                        (date.getMonth() == start.getDate().getMonth() && date.getDay() >= start.getDate().getDay())));

                    boolean dateIsBeforeOrSameAsEnd = date.getYear() < end.getDate().getYear() ||
                        (date.getYear() == end.getDate().getYear() && (date.getMonth() < end.getDate().getMonth() ||
                        (date.getMonth() == end.getDate().getMonth() && date.getDay() <= end.getDate().getDay())));
                    
                    if(dateIsAfterOrSameAsStart && dateIsBeforeOrSameAsEnd) {
                        result.add(event);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns all events that occur within a specified date-time range.
     * @param startRange the start of the range (inclusive)
     * @param endRange the end of the range (exclusive)
     * @return a list of events that overlap with the specified range
     */
    @Override
    public List<IEvent> getEventsInRange(DateTime startRange, DateTime endRange) {
        List<IEvent> result = new ArrayList<>();
        for (IEvent event : events) {
            DateTime eventStart = event.getStart();
            DateTime eventEnd = event.getEnd() != null ? event.getEnd() : new DateTime(eventStart.getDate(), new Time(17,0));

            if (eventStart.isBefore(endRange) && eventEnd.isAfter(startRange)) {
                result.add(event);
            }
        }
        return result;
    }

    /**
     * Checks if the calendar has any events at the specified date and time.
     * @param dateTime the date and time to check
     * @return true if there is an event at the specified time, false otherwise
     */
    @Override
    public boolean isBusyAt(DateTime dateTime) {
        for (IEvent event : events) {
            DateTime eventStart = event.getStart();
            DateTime eventEnd = event.getEnd() != null ? event.getEnd() : new DateTime(eventStart.getDate(), new Time(17,0));

            if (!dateTime.isBefore(eventStart) && dateTime.isBefore(eventEnd)) {
                return true;
            }
        }
        return false;
    }
}