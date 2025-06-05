package calendar;

import java.util.List;

public interface ICalendarView {
    /**
     * Prompts the user and retrieves the next command.
     * @return The command string entered by the user.
     */
    String getCommand();

    /**
     * Displays a general message to the user.
     * @param message The message to display.
     */
    void displayMessage(String message);

    /**
     * Displays an error message to the user.
     * @param errorMessage The error message to display.
     */
    void displayError(String errorMessage);

    /**
     * Displays a list of events.
     * This is a general display method. Specific formatting might be needed for different queries.
     * @param events The list of IEvent objects to display.
     */
    void displayEvents(List<IEvent> events);

    /**
     * Displays events scheduled on a specific date, with specific formatting.
     * @param events List of events for that date.
     * @param dateString The string representation of the date queried.
     */
    void displayEventsOnDate(List<IEvent> events, String dateString);
    
    /**
     * Displays the user's busy/available status.
     * @param isBusy True if the user is busy, false otherwise.
     * @param dateTimeString The string representation of the date/time queried.
     */
    void displayStatus(boolean isBusy, String dateTimeString);

    /**
     * Closes any resources used by the view (e.g., Scanner).
     */
    void close();
}