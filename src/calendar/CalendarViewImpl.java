package calendar;

import java.util.List;
import java.util.Scanner;

/**
 * Implementation of the ICalendarView interface that handles user interaction through console I/O.
 * This class provides methods to display information to the user and get input from the user.
 */
public class CalendarViewImpl implements ICalendarView {
    private Scanner scanner;

    /**
     * Constructs a new CalendarViewImpl with a Scanner for user input.
     */
    public CalendarViewImpl() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prompts the user for a command and returns their input.
     * @return the command string entered by the user
     */
    @Override
    public String getCommand() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    /**
     * Displays a message to the user.
     * @param message the message to display
     */
    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message to the user.
     * @param errorMessage the error message to display
     */
    @Override
    public void displayError(String errorMessage) {
        System.err.println("Error: " + errorMessage);
    }

    /**
     * Displays a list of events in a formatted bulleted list.
     * Shows subject, start date/time, end date/time, and location (if available).
     * @param events the list of events to display
     */
    @Override
    public void displayEvents(List<IEvent> events) {
        if (events == null || events.isEmpty()) {
            displayMessage("No events found.");
            return;
        }
        for (IEvent event : events) {
            StringBuilder eventDetails = new StringBuilder();
            eventDetails.append("* Subject: ").append(event.getSubject());
            eventDetails.append(", Start: ").append(event.getStart().toString());

            if (event.getEnd() != null) {
                eventDetails.append(", End: ").append(event.getEnd().toString());
            } else {
                eventDetails.append(", End: (All-day on ").append(event.getStart().getDate().toString()).append(")");
            }

            if (event.getLocation() != null && !event.getLocation().isEmpty() && !event.getLocation().equals("No Location Provided")) {
                eventDetails.append(", Location: ").append(event.getLocation());
            }
            displayMessage(eventDetails.toString());
        }
    }

    /**
     * Displays a list of events scheduled for a specific date.
     * Shows subject, start time, end time, and location (if available) for each event.
     * @param events the list of events to display
     * @param dateString the date string for which events are being displayed
     */
    @Override
    public void displayEventsOnDate(List<IEvent> events, String dateString) {
        if (events == null || events.isEmpty()) {
            displayMessage("No events scheduled on " + dateString + ".");
            return;
        }
        displayMessage("Events on " + dateString + ":");
        for (IEvent event : events) {
            StringBuilder eventDetails = new StringBuilder();
            eventDetails.append("* ").append(event.getSubject());
            
            eventDetails.append(" from ").append(event.getStart().getTime().toString());

            if (event.getEnd() != null) {
                if (event.getStart().getDate().equals(event.getEnd().getDate())) {
                    eventDetails.append(" to ").append(event.getEnd().getTime().toString());
                } else {
                    eventDetails.append(" to ").append(event.getEnd().getTime().toString());
                }
            } else {
                 eventDetails.append(" to 17:00 (All-day)");
            }

            if (event.getLocation() != null && !event.getLocation().isEmpty() && !event.getLocation().equals("No Location Provided")) {
                eventDetails.append(" at ").append(event.getLocation());
            }
            displayMessage(eventDetails.toString());
        }
    }

    /**
     * Displays the user's availability status at a specific date and time.
     * Prints "busy" if there are events scheduled, "available" otherwise.
     * @param isBusy true if the user is busy at the specified time, false if available
     * @param dateTimeString the date and time string being queried
     */
    @Override
    public void displayStatus(boolean isBusy, String dateTimeString) {
        if (isBusy) {
            displayMessage("busy");
        } else {
            displayMessage("available");
        }
    }

    /**
     * Closes the scanner and releases resources.
     */
    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}