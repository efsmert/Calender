package calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.DayOfWeek; // Standard DayOfWeek

// Assuming your Date, Time, DateTime, Event (as IEvent) are in this package.

public class CalendarModelImpl implements ICalendarModel {
    private List<IEvent> events;
    // EST timezone is an application-level requirement, not strictly a model one,
    // but date/time comparisons need to be consistent. Your custom Date/Time
    // don't have timezone info, so comparisons are direct. My previous ZoneId logic
    // is not directly applicable here unless we convert your DateTime to java.time.ZonedDateTime.
    // For now, all operations will assume inputs are already in EST as per problem statement.

    public CalendarModelImpl() {
        this.events = new ArrayList<>();
    }

    private boolean isDuplicate(IEvent eventToCheck, IEvent eventToExclude) {
        for (IEvent existingEvent : events) {
            if (existingEvent == eventToExclude) {
                continue;
            }
            // Uniqueness: subject, start date/time, and end date/time.
            // IEvent.equals() should implement this.
            if (existingEvent.getSubject().equals(eventToCheck.getSubject()) &&
                existingEvent.getStart().equals(eventToCheck.getStart()) &&
                Objects.equals(existingEvent.getEnd(), eventToCheck.getEnd())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean createEvent(String subject, DateTime startDateTime, DateTime endDateTime, String description, String location, String status) {
        if (startDateTime == null || subject == null || subject.trim().isEmpty()) {
            System.err.println("Error: Subject and start date/time are required.");
            return false;
        }

        DateTime effectiveStart = startDateTime;
        DateTime effectiveEnd = endDateTime;

        if (effectiveEnd == null) { // All-day event
            // As per requirements: 8:00 AM to 5:00 PM on the specified start date.
            // Your Date and Time classes are separate.
            Date date = startDateTime.getDate();
            effectiveStart = new DateTime(date, new Time(8, 0));
            effectiveEnd = new DateTime(date, new Time(17, 0));
        }

        // Validate that end is not before start
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

        if (effectiveSeriesEnd == null) { // All-day event series
            Date date = seriesStartDateTime.getDate();
            effectiveSeriesStart = new DateTime(date, new Time(8, 0));
            effectiveSeriesEnd = new DateTime(date, new Time(17, 0));
        }
        
        if (seriesEndDate != null) { // Only check if seriesEndDate is provided
            boolean endDateIsBeforeStartDate = seriesEndDate.getYear() < effectiveSeriesStart.getDate().getYear() ||
               (seriesEndDate.getYear() == effectiveSeriesStart.getDate().getYear() && seriesEndDate.getMonth() < effectiveSeriesStart.getDate().getMonth()) ||
               (seriesEndDate.getYear() == effectiveSeriesStart.getDate().getYear() && seriesEndDate.getMonth() == effectiveSeriesStart.getDate().getMonth() && seriesEndDate.getDay() < effectiveSeriesStart.getDate().getDay());
            if (endDateIsBeforeStartDate) {
                System.err.println("Error: Series end date cannot be before the series start date.");
                return false;
            }
        }


        // Constraint: Each individual event occurrence within a series must start and finish on the same calendar day.
        if (effectiveSeriesStart.getDate().getYear() != effectiveSeriesEnd.getDate().getYear() ||
            effectiveSeriesStart.getDate().getMonth() != effectiveSeriesEnd.getDate().getMonth() ||
            effectiveSeriesStart.getDate().getDay() != effectiveSeriesEnd.getDate().getDay()) {
            System.err.println("Error: For recurring events, the start and end time must be on the same day.");
            return false;
        }
        
        // Constraint: All event occurrences within the same series must have the same start time and the same duration.
        // This is implicitly handled by using the start/end times of the first event for all instances.

        List<IEvent> potentialSeriesEvents = new ArrayList<>();
        Date currentDate = new Date(effectiveSeriesStart.getDate().getDay(), effectiveSeriesStart.getDate().getMonth(), effectiveSeriesStart.getDate().getYear()); // Use a mutable copy
        int eventsCreated = 0;
        String generatedSeriesId = UUID.randomUUID().toString();

        Time startTime = effectiveSeriesStart.getTime();
        Time endTime = effectiveSeriesEnd.getTime();

        int safetyBreak = 0; // To prevent infinite loops with bad date logic

        while (safetyBreak++ < (366 * 5)) { // Max 5 years of daily events
            if (occurrences != null && eventsCreated >= occurrences) {
                break;
            }
            if (seriesEndDate != null && 
                (currentDate.getYear() > seriesEndDate.getYear() ||
                 (currentDate.getYear() == seriesEndDate.getYear() && currentDate.getMonth() > seriesEndDate.getMonth()) ||
                 (currentDate.getYear() == seriesEndDate.getYear() && currentDate.getMonth() == seriesEndDate.getMonth() && currentDate.getDay() > seriesEndDate.getDay()))) {
                break;
            }

            // Convert your Date's day of week to java.time.DayOfWeek
            // This requires a mapping or using java.time.LocalDate internally for this check.
            // For simplicity, let's assume a helper method or direct java.time.LocalDate conversion here.
            java.time.LocalDate tempLocalDate = java.time.LocalDate.of(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay());
            if (repeatDays.contains(tempLocalDate.getDayOfWeek())) {
                DateTime eventStartDt = new DateTime(new Date(currentDate.getDay(), currentDate.getMonth(), currentDate.getYear()), startTime);
                DateTime eventEndDt = new DateTime(new Date(currentDate.getDay(), currentDate.getMonth(), currentDate.getYear()), endTime);
                
                IEvent seriesInstance = new Event(subject, location, eventStartDt, eventEndDt, status, description);
                seriesInstance.setSeriesId(generatedSeriesId);
                seriesInstance.setOriginalSeriesId(generatedSeriesId); // Initialize originalSeriesId
                // Set series flag and days for the instance if your Event class supports it
                if (seriesInstance instanceof Event) { // Check if it's your concrete Event
                    ((Event) seriesInstance).setIsSeriesFlag(true);
                    ((Event) seriesInstance).setDaysOfWeekList(new ArrayList<>(repeatDays)); // Store repeating days
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
            currentDate.advance(1); // Use your Date's advance method
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
    
    @Override
    public boolean editEvent(String findSubject, DateTime findStartDateTime, DateTime findEndDateTime,
                             String propertyToChange, Object newValue, String scope) {
        List<IEvent> targetEvents = new ArrayList<>();
        IEvent anchorEvent = null;

        // 1. Find the event(s) to edit
        if ("this".equals(scope)) {
            if (findEndDateTime == null && findStartDateTime != null) { // All-day event identification
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
            if (targetEvents.size() > 1) { // Should be unique
                System.err.println("Error: Multiple events found for 'edit event' (this scope). This indicates a data integrity issue or overly broad match.");
                return false;
            }
            anchorEvent = targetEvents.get(0);
        } else { // "future" (events) or "all" (series)
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
            // Simplified: pick first candidate as anchor. Robust solution might need user input for ambiguity.
            anchorEvent = candidates.get(0);
            
            if (candidates.size() > 1) {
                // Check if all candidates share the same non-null seriesId. If not, it's ambiguous.
                String firstSeriesId = anchorEvent.getSeriesId();
                if (firstSeriesId == null) { // Anchor is not a series event, but multiple matches found
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

            if ("future".equals(scope)) { // This and future
                if (anchorEvent.getSeriesId() != null) {
                    if (anchorEvent.isSeriesException()) {
                        // If the anchor event itself is an exception, "this and future" only applies to this one instance.
                        targetEvents.add(anchorEvent);
                    } else {
                        // If anchor is not an exception, target it and all future non-exception instances of the same series.
                        for (IEvent event : events) {
                            if (anchorEvent.getSeriesId().equals(event.getSeriesId()) &&
                                !event.getStart().isBefore(anchorEvent.getStart()) &&
                                !event.isSeriesException()) { // Only target non-exceptions for propagation
                                targetEvents.add(event);
                            }
                        }
                    }
                } else { // Anchor is a single, non-series event
                    targetEvents.add(anchorEvent);
                }
            } else if ("all".equals(scope)) { // All in series
                String seriesIdToMatch = anchorEvent.getSeriesId(); // Target based on the anchor's CURRENT series ID.
                if (seriesIdToMatch != null) {
                    for (IEvent event : events) {
                        // Only add events that are currently part of this exact series.
                        // Do not include events that may have originated from it but now have a different seriesId.
                        if (seriesIdToMatch.equals(event.getSeriesId())) {
                            // For "all" scope, we typically want to modify all instances, including those
                            // that might have been marked as exceptions if the intent is to override those exceptions.
                            // However, the prompt implies "all in series" should respect prior splits.
                            // The critical part is that "Second" events have a *different* seriesId than "First".
                            // So, if anchor is "First" (seriesId S1), this will only get S1 events.
                            targetEvents.add(event);
                        }
                    }
                } else { // Anchor is a single, non-series event
                    targetEvents.add(anchorEvent); // Only target the anchor itself
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
        String newSeriesIdForSplit = null; // For "all" scope start changes, or "this" scope start changes on series
        String newSeriesIdForFutureScope = null; // For "future" scope edits on a series

        if (scope.equals("future") && anchorEvent != null && anchorEvent.getSeriesId() != null && !targetEvents.isEmpty()) {
            newSeriesIdForFutureScope = UUID.randomUUID().toString();
        } else if (startPropertyChanged && anchorEvent != null && anchorEvent.getSeriesId() != null && scope.equals("all") && !targetEvents.isEmpty()) {
            // If start time of an entire series ("all" scope) is changed, these events form a new series.
            newSeriesIdForSplit = UUID.randomUUID().toString();
        }
        // Note: "this" scope changing start time on a series event makes it an exception, but doesn't usually create a new series ID for just one event.
        // The existing logic for "this" scope setting isSeriesException(true) handles this.

        for (IEvent originalEvent : targetEvents) {
            IEvent eventToModify = originalEvent.copy(); // Always work on a copy for modifications

            // Apply the actual property change
            switch (propertyToChange.toLowerCase()) {
                case "subject": eventToModify.setSubject((String) newValue); break;
                case "start": eventToModify.setStart((DateTime) newValue); break;
                case "end": eventToModify.setEnd((DateTime) newValue); break;
                case "description": eventToModify.setDescription((String) newValue); break;
                case "location": eventToModify.setLocation((String) newValue); break;
                case "status": eventToModify.setStatus((String) newValue); break;
                default: System.err.println("Error: Unknown property to change: " + propertyToChange); return false;
            }

            // Handle series logic for the modified copy
            String currentOriginalId = originalEvent.getOriginalSeriesId();
            String currentSeriesId = originalEvent.getSeriesId();

            // Preserve original series ID by default if it exists, otherwise use current series ID as original
            eventToModify.setOriginalSeriesId(currentOriginalId != null ? currentOriginalId : currentSeriesId);

            if (scope.equals("future") && newSeriesIdForFutureScope != null) {
                // "future" scope edit on a series: these events form a new current series.
                // Their originalSeriesId should point to the seriesId they are branching from.
                eventToModify.setOriginalSeriesId(originalEvent.getSeriesId()); // The series they are leaving
                eventToModify.setSeriesId(newSeriesIdForFutureScope);
                eventToModify.setSeriesException(false);
            } else if (scope.equals("all")) {
                String masterSeriesIdForAnchor = anchorEvent.getSeriesId(); // The series we are intending to edit "all" of
                if (startPropertyChanged && newSeriesIdForSplit != null) {
                    // "all" scope AND start time changed: all targeted events form a new current series.
                    // Their originalSeriesId should be the ID of the series they are splitting from.
                    eventToModify.setOriginalSeriesId(masterSeriesIdForAnchor);
                    eventToModify.setSeriesId(newSeriesIdForSplit);
                    eventToModify.setSeriesException(false);
                } else if (!startPropertyChanged && masterSeriesIdForAnchor != null) {
                    // "all" scope, and NOT changing start time: re-align event with the master series.
                    eventToModify.setSeriesId(masterSeriesIdForAnchor);
                    // If the event had a different originalSeriesId, it means it was an exception from an even older series.
                    // For an "all" edit, we are re-aligning it fully to the masterSeriesIdForAnchor.
                    eventToModify.setOriginalSeriesId(masterSeriesIdForAnchor);
                    eventToModify.setSeriesException(false);
                }
                // If masterSeriesIdForAnchor is null (anchor was not a series event), no series changes for "all".
            } else if (scope.equals("this")) {
                // "this" scope: this instance becomes an exception.
                eventToModify.setSeriesId(originalEvent.getSeriesId()); // Keep current series ID
                // OriginalSeriesId is already set from originalEvent at the start of this block.
                eventToModify.setSeriesException(true);
            }
            // If originalEvent had no series affiliation (seriesId was null), eventToModify also won't,
            // unless a new one is assigned by "future" or "all" (start changed) scopes.


            if (isDuplicate(eventToModify, originalEvent)) {
                System.err.println("Error: Modified event (" + eventToModify.getSubject() + " at " + eventToModify.getStart() + ") conflicts with an existing event.");
                return false; // Fail the whole operation
            }
            
            eventsToRemove.add(originalEvent);
            eventsToAdd.add(eventToModify);
        }

        // Commit changes
        this.events.removeAll(eventsToRemove);
        this.events.addAll(eventsToAdd);
        return true;
    }


    @Override
    public List<IEvent> getAllEvents() {
        return new ArrayList<>(events); // Return a copy
    }

    @Override
    public List<IEvent> getEventsOnDate(Date date) {
        List<IEvent> result = new ArrayList<>();
        for (IEvent event : events) {
            DateTime start = event.getStart();
            DateTime end = event.getEnd() != null ? event.getEnd() : new DateTime(start.getDate(), new Time(17,0)); // Assume 5PM end for all-day if end is null

            // Event is on 'date' if:
            // event.start.date <= date <= event.end.date
            boolean startsBeforeOrOn = start.getDate().getYear() < date.getYear() ||
                                     (start.getDate().getYear() == date.getYear() && start.getDate().getMonth() < date.getMonth()) ||
                                     (start.getDate().getYear() == date.getYear() && start.getDate().getMonth() == date.getMonth() && start.getDate().getDay() <= date.getDay());

            boolean endsAfterOrOn = end.getDate().getYear() > date.getYear() ||
                                   (end.getDate().getYear() == date.getYear() && end.getDate().getMonth() > date.getMonth()) ||
                                   (end.getDate().getYear() == date.getYear() && end.getDate().getMonth() == date.getMonth() && end.getDate().getDay() >= date.getDay());
            
            if (startsBeforeOrOn && endsAfterOrOn) {
                 // More precise check: if event spans multiple days, it's on 'date' if 'date' is between event.start.date and event.end.date (inclusive)
                if ( (start.getDate().getYear() < date.getYear() || (start.getDate().getYear() == date.getYear() && (start.getDate().getMonth() < date.getMonth() || (start.getDate().getMonth() == date.getMonth() && start.getDate().getDay() <= date.getDay())))) &&
                     (end.getDate().getYear() > date.getYear() || (end.getDate().getYear() == date.getYear() && (end.getDate().getMonth() > date.getMonth() || (end.getDate().getMonth() == date.getMonth() && end.getDate().getDay() >= date.getDay())))) )
                {
                     // Check if the specific date 'date' falls within the event's date span
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


    @Override
    public List<IEvent> getEventsInRange(DateTime startRange, DateTime endRange) {
        List<IEvent> result = new ArrayList<>();
        for (IEvent event : events) {
            DateTime eventStart = event.getStart();
            DateTime eventEnd = event.getEnd() != null ? event.getEnd() : new DateTime(eventStart.getDate(), new Time(17,0)); // Handle all-day

            // Event overlaps with range if:
            // eventStart is before endRange AND eventEnd is after startRange
            if (eventStart.isBefore(endRange) && eventEnd.isAfter(startRange)) {
                result.add(event);
            }
        }
        return result;
    }

    @Override
    public boolean isBusyAt(DateTime dateTime) {
        for (IEvent event : events) {
            DateTime eventStart = event.getStart();
            DateTime eventEnd = event.getEnd() != null ? event.getEnd() : new DateTime(eventStart.getDate(), new Time(17,0)); // Handle all-day

            // dateTime is on or after eventStart AND dateTime is before eventEnd
            if (!dateTime.isBefore(eventStart) && dateTime.isBefore(eventEnd)) {
                return true;
            }
        }
        return false;
    }
}