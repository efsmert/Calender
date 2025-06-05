package calendar;


/**
 * this is the main class for the calendar application.
 * it handles command-line arguments to determine the application mode (interactive or headless).
 */
public class CalendarApp {
/**
     * the main method that starts the calendar application.
     * it parses command-line arguments to determine the mode and initializes the mvc components.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Error: Mode not specified.");
            printUsage();
            return;
        }

        String modeArg = null;
        String commandFilePath = null;

        if (args[0].equalsIgnoreCase("--mode")) {
            if (args.length > 1) {
                modeArg = args[1].toLowerCase();
                if (modeArg.equals("headless")) {
                    if (args.length > 2) {
                        commandFilePath = args[2];
                    } else {
                        System.err.println("Error: Command file path not specified for headless mode.");
                        printUsage();
                        return;
                    }
                } else if (!modeArg.equals("interactive")) {
                    System.err.println("Error: Invalid mode specified. Use 'interactive' or 'headless'.");
                    printUsage();
                    return;
                }
            } else {
                System.err.println("Error: Mode value not provided after --mode flag.");
                printUsage();
                return;
            }
        } else {
            System.err.println("Error: Invalid arguments. First argument must be --mode.");
            printUsage();
            return;
        }
        
        System.out.println("Calendar Application starting...");

        ICalendarModel model = new CalendarModelImpl();
        ICalendarView view = new CalendarViewImpl();
        IController controller = new CalendarControllerImpl(model, view);

        // Run the application
        controller.run(modeArg, commandFilePath);

        System.out.println("calendar application finished.");
    }

/**
      * prints the usage instructions for the calendar application.
      */
    private static void printUsage() {
        System.err.println("Usage: java calendar.CalendarApp --mode interactive");
        System.err.println("   or: java calendar.CalendarApp --mode headless <path_to_command_file>");
    }
}