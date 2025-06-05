package calendar.view;

import calendar.model.Event;
import java.util.List;

/**
 * Represents the view for the calendar application, handling user output.
 */
public interface CalendarView {

  /**
   * Displays a list of events.
   *
   * @param events The list of events to display.
   */
  void displayEvents(List<Event> events);

  /**
   * Displays a message indicating the user's busy status.
   *
   * @param isBusy True if the user is busy, false otherwise.
   */
  void displayBusyStatus(boolean isBusy);

  /**
   * Displays an error message to the user.
   *
   * @param errorMessage The error message to display.
   */
  void displayError(String errorMessage);

  /**
   * Displays a general message to the user.
   *
   * @param message The message to display.
   */
  void displayMessage(String message);
}