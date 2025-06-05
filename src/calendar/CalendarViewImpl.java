package calendar;

import java.util.List;
import java.util.Scanner;
// No java.time.format.DateTimeFormatter needed if using your DateTime.toString() directly.
// However, for specific formatting like only time (HH:mm), we might need to access Time object's parts.

public class CalendarViewImpl implements ICalendarView {
    private Scanner scanner;

    // Using your DateTime.toString() which is YYYY-MM-DDTHH:mm
    // Your Time.toString() is HH:mm
    // Your Date.toString() is YYYY-MM-DD

    public CalendarViewImpl() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getCommand() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayError(String errorMessage) {
        // The requirements asked for error messages to be printed.
        // My previous controller also called view.displayError.
        // The model also prints some errors to System.err. This is fine.
        System.err.println("Error: " + errorMessage);
    }

    @Override
    public void displayEvents(List<IEvent> events) {
        if (events == null || events.isEmpty()) {
            displayMessage("No events found.");
            return;
        }
        // "Prints a bulleted list of all events within the given interval, 
        // including their subject, start date/time, end date/time, and location (if available)."
        for (IEvent event : events) {
            StringBuilder eventDetails = new StringBuilder();
            eventDetails.append("* Subject: ").append(event.getSubject());
            eventDetails.append(", Start: ").append(event.getStart().toString()); // Uses your DateTime.toString()

            if (event.getEnd() != null) {
                eventDetails.append(", End: ").append(event.getEnd().toString());
            } else {
                // All-day event (8am-5pm on start date)
                // Model ensures all-day events have start 8am, end 5pm if created via 'on <dateString>'
                // If event.getEnd() is null here, it means it was created as all-day and model might not have set explicit end.
                // Or, it's an event where end time is genuinely not set.
                // The requirement for "print events from X to Y" implies full date/time.
                // Let's assume if getEnd() is null, it's an all-day event for that start date.
                // The model should ideally set the 8-5 times for all-day events.
                // If model sets end time for all-day, this 'else' might not be hit often.
                eventDetails.append(", End: (All-day on ").append(event.getStart().getDate().toString()).append(")");
            }

            if (event.getLocation() != null && !event.getLocation().isEmpty() && !event.getLocation().equals("No Location Provided")) {
                eventDetails.append(", Location: ").append(event.getLocation());
            }
            displayMessage(eventDetails.toString());
        }
    }

    @Override
    public void displayEventsOnDate(List<IEvent> events, String dateString) {
        if (events == null || events.isEmpty()) {
            displayMessage("No events scheduled on " + dateString + ".");
            return;
        }
        displayMessage("Events on " + dateString + ":");
        // "Prints a bulleted list of all events on that day, including their subject, 
        // start time, end time, and location (if available)."
        for (IEvent event : events) {
            StringBuilder eventDetails = new StringBuilder();
            eventDetails.append("* ").append(event.getSubject());
            
            // Start time
            eventDetails.append(" from ").append(event.getStart().getTime().toString()); // HH:mm

            // End time
            if (event.getEnd() != null) {
                // If start and end are on the same day, just show end time.
                // Otherwise, show full end date/time (though query is for a single date).
                if (event.getStart().getDate().equals(event.getEnd().getDate())) {
                    eventDetails.append(" to ").append(event.getEnd().getTime().toString());
                } else {
                    // This case should ideally not happen if query is for a single date and event spans it.
                    // But if an event ENDS on this day but started earlier, show full end.
                    // Or if it STARTS on this day and ends later.
                    // For "print events on <date>", it's simpler to assume we only care about times on *that* day.
                    // The model's getEventsOnDate should filter appropriately.
                    // If an event spans midnight, this display might need more context.
                    // For now, if end date is same, show time, else show full.
                    // This is for events *on* a specific date.
                    // If an event is all-day (8-5), it will have start/end on same date.
                    eventDetails.append(" to ").append(event.getEnd().getTime().toString());
                }
            } else {
                 // All-day event, implies 8-5. Model should set this.
                 // If event.getEnd() is null, it means it's an all-day event.
                 // The model should have set start to 8:00 and end to 17:00 for such events.
                 // So, event.getEnd() should not be null if model processed it.
                 // This 'else' is a fallback.
                 eventDetails.append(" to 17:00 (All-day)");
            }

            if (event.getLocation() != null && !event.getLocation().isEmpty() && !event.getLocation().equals("No Location Provided")) {
                eventDetails.append(" at ").append(event.getLocation());
            }
            displayMessage(eventDetails.toString());
        }
    }

    @Override
    public void displayStatus(boolean isBusy, String dateTimeString) {
        // "Prints "busy" if the user has any event scheduled at the specified date and time; otherwise, prints "available"."
        if (isBusy) {
            displayMessage("busy");
        } else {
            displayMessage("available");
        }
    }

    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}