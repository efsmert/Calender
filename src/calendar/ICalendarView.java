package calendar;

import java.util.List;

/**
 * Interface for the view component of the calendar application.
 * Handles user interaction including displaying information and getting user input.
 */
public interface ICalendarView {
    /**
     * Prompts the user and retrieves the next command.
     * @return the command string entered by the user
     */
    String getCommand();

    /**
     * Displays a general message to the user.
     * @param message the message to display
     */
    void displayMessage(String message);

    /**
     * Displays an error message to the user.
     * @param errorMessage the error message to display
     */
    void displayError(String errorMessage);

    /**
     * Displays a list of events.
     * This is a general display method. Specific formatting might be needed for different queries.
     * @param events the list of IEvent objects to display
     */
    void displayEvents(List<IEvent> events);

    /**
     * Displays events scheduled on a specific date, with specific formatting.
     * @param events list of events for that date
     * @param dateString the string representation of the date queried
     */
    void displayEventsOnDate(List<IEvent> events, String dateString);
    
    /**
     * Displays the user's busy/available status.
     * @param isBusy true if the user is busy, false otherwise
     * @param dateTimeString the string representation of the date/time queried
     */
    void displayStatus(boolean isBusy, String dateTimeString);

    /**
     * Closes any resources used by the view (e.g., Scanner).
     */
    void close();
}