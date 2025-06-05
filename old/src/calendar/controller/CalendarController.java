package calendar.controller;

import calendar.model.CalendarModel;
import calendar.view.CalendarView;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.time.DayOfWeek;

/**
 * Represents the controller for the calendar application, handling user input and interacting with the model and view.
 */
public class CalendarController {

  private CalendarModel model;
  private CalendarView view;
  private Scanner scanner;

  public CalendarController(CalendarModel model, CalendarView view, InputStream inputStream) {
    this.model = model;
    this.view = view;
    this.scanner = new Scanner(inputStream);
  }

  /**
   * Starts the interactive mode of the calendar application.
   */
  public void runInteractiveMode() {
    view.displayMessage("Calendar Application - Interactive Mode");
    view.displayMessage("Enter commands or type 'exit' to quit.");

    while (true) {
      view.displayMessage("> ");
      String command = scanner.nextLine();
      if (command.equalsIgnoreCase("exit")) {
        break;
      }
      processCommand(command);
    }
    view.displayMessage("Exiting Calendar Application.");
    scanner.close();
  }

  /**
   * Processes a single command string.
   *
   * @param command The command string to process.
   */
  private void processCommand(String command) {
    // TODO: Implement command parsing and execution logic based on the command format
    // Example: parse "create event <subject> from <dateStringTtimeString> to <dateStringTtimeString>"
    // and call the appropriate model method.
    view.displayMessage("Processing command: " + command);
    // Placeholder for command handling
    if (command.startsWith("create event")) {
      // TODO: Handle create event commands
      view.displayError("Create event command not fully implemented yet.");
    } else if (command.startsWith("edit event")) {
      // TODO: Handle edit event commands
      view.displayError("Edit event command not fully implemented yet.");
    } else if (command.startsWith("edit events")) {
      // TODO: Handle edit events commands
      view.displayError("Edit events command not fully implemented yet.");
    } else if (command.startsWith("edit series")) {
      // TODO: Handle edit series commands
      view.displayError("Edit series command not fully implemented yet.");
    } else if (command.startsWith("print events on")) {
      // TODO: Handle print events on command
      view.displayError("Print events on command not fully implemented yet.");
    } else if (command.startsWith("print events from")) {
      // TODO: Handle print events from command
      view.displayError("Print events from command not fully implemented yet.");
    } else if (command.startsWith("show status on")) {
      // TODO: Handle show status on command
      view.displayError("Show status on command not fully implemented yet.");
    } else {
      view.displayError("Invalid command: " + command);
    }
  }

  // Helper method to parse date and time string "YYYY-MM-DDThh::mm"
  private LocalDateTime parseDateTimeString(String dateTimeString) throws DateTimeParseException {
    // TODO: Implement robust date time parsing
    return LocalDateTime.parse(dateTimeString.replace("::", ":")); // Basic parsing, needs refinement
  }

  // Helper method to parse weekdays string "MRU"
  private Set<DayOfWeek> parseWeekdays(String weekdaysString) {
    Set<DayOfWeek> days = new HashSet<>();
    for (char dayChar : weekdaysString.toUpperCase().toCharArray()) {
      switch (dayChar) {
        case 'M':
          days.add(DayOfWeek.MONDAY);
          break;
        case 'T':
          days.add(DayOfWeek.TUESDAY);
          break;
        case 'W':
          days.add(DayOfWeek.WEDNESDAY);
          break;
        case 'R':
          days.add(DayOfWeek.THURSDAY);
          break;
        case 'F':
          days.add(DayOfWeek.FRIDAY);
          break;
        case 'S':
          days.add(DayOfWeek.SATURDAY);
          break;
        case 'U':
          days.add(DayOfWeek.SUNDAY);
          break;
        default:
          // Ignore invalid characters for now, could add error handling
          break;
      }
    }
    return days;
  }

  // TODO: Implement headless mode logic
  public void runHeadlessMode(String commandsFilePath) {
    view.displayError("Headless mode not yet implemented.");
  }
}