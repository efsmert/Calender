package calendar;

import calendar.controller.CalendarController;
import calendar.model.CalendarModel;
import calendar.model.CalendarModelImpl;
import calendar.view.CalendarView;
import calendar.view.CalendarViewImpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Calendar {

  public static void main(String... args) {
    if (args.length == 0) {
      System.err.println("Usage: java Calendar --mode <interactive|headless> [commands_file]");
      System.exit(1);
    }

    String mode = null;
    String commandsFilePath = null;

    for (int i = 0; i < args.length; i++) {
      if (args[i].equalsIgnoreCase("--mode")) {
        if (i + 1 < args.length) {
          mode = args[i + 1].toLowerCase();
          i++; // Consume the mode argument
        } else {
          System.err.println("Error: --mode requires an argument (interactive or headless).");
          System.exit(1);
        }
      } else if (mode != null && mode.equals("headless") && commandsFilePath == null) {
        commandsFilePath = args[i];
      } else {
        System.err.println("Error: Invalid argument: " + args[i]);
        System.err.println("Usage: java Calendar --mode <interactive|headless> [commands_file]");
        System.exit(1);
      }
    }

    if (mode == null) {
      System.err.println("Error: --mode argument is required.");
      System.err.println("Usage: java Calendar --mode <interactive|headless> [commands_file]");
      System.exit(1);
    }

    CalendarModel model = new CalendarModelImpl();
    CalendarView view = new CalendarViewImpl();
    CalendarController controller;

    if (mode.equals("interactive")) {
      controller = new CalendarController(model, view, System.in);
      controller.runInteractiveMode();
    } else if (mode.equals("headless")) {
      if (commandsFilePath == null) {
        System.err.println("Error: headless mode requires a commands file.");
        System.err.println("Usage: java Calendar --mode headless <commands_file>");
        System.exit(1);
      }
      try {
        InputStream commandsStream = new FileInputStream(commandsFilePath);
        controller = new CalendarController(model, view, commandsStream);
        controller.runHeadlessMode(commandsFilePath); // Pass the file path for potential use within headless mode
      } catch (FileNotFoundException e) {
        System.err.println("Error: Commands file not found: " + commandsFilePath);
        System.exit(1);
      }
    } else {
      System.err.println("Error: Invalid mode. Use 'interactive' or 'headless'.");
      System.err.println("Usage: java Calendar --mode <interactive|headless> [commands_file]");
      System.exit(1);
    }
  }
}
