package calendar.view;

import calendar.model.Event;
import java.util.List;

/**
 * Concrete implementation of the CalendarView interface for text-based output.
 */
public class CalendarViewImpl implements CalendarView {

  @Override
  public void displayEvents(List<Event> events) {
    if (events.isEmpty()) {
      System.out.println("No events found.");
    } else {
      System.out.println("Events:");
      for (Event event : events) {
        System.out.println("- " + event.getSubject() + " from " + event.getStartTime() + " to " + event.getEndTime()
                           + (event.getLocation() != null ? " at " + event.getLocation() : ""));
      }
    }
  }

  @Override
  public void displayBusyStatus(boolean isBusy) {
    if (isBusy) {
      System.out.println("Status: Busy");
    } else {
      System.out.println("Status: Available");
    }
  }

  @Override
  public void displayError(String errorMessage) {
    System.err.println("Error: " + errorMessage);
  }

  @Override
  public void displayMessage(String message) {
    System.out.println(message);
  }
}