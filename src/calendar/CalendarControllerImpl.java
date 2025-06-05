package calendar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements the {@link IController} interface.
 * It handles user input and interacts with the model and view components.
 */
public class CalendarControllerImpl implements IController {
    
    private ICalendarModel model;
    private ICalendarView view;

    /**
     * Constructs a new CalendarControllerImpl.
     * @param model the calendar model
     * @param view the calendar view
     */
    public CalendarControllerImpl(ICalendarModel model, ICalendarView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Runs the calendar application in the specified mode.
     * @param mode the application mode ("interactive" or "headless")
     * @param commandFilePath the path to the command file (for headless mode)
     */
    @Override
    public void run(String mode, String commandFilePath) {
        if ("interactive".equalsIgnoreCase(mode)) {
            runInteractiveMode();
        } else if ("headless".equalsIgnoreCase(mode)) {
            if (commandFilePath == null || commandFilePath.trim().isEmpty()) {
                view.displayError("Command file path not specified for headless mode.");
                return;
            }
            runHeadlessMode(commandFilePath);
        } else {
            view.displayError("Invalid application mode specified. Use 'interactive' or 'headless <filepath>'.");
        }
    }

    /**
     * Runs the application in interactive mode, prompting the user for commands.
     */
    private void runInteractiveMode() {
        view.displayMessage("Interactive mode started. Type 'exit' to quit.");
        String command;
        while (true) {
            command = view.getCommand();
            if (command == null) {
                view.displayError("Failed to read command. Exiting.");
                break;
            }
            if ("exit".equalsIgnoreCase(command.trim())) {
                break;
            }
            processCommand(command);
        }
        view.displayMessage("Exiting application.");
        view.close();
    }

    /**
     * Runs the application in headless mode, processing commands from a file.
     * @param filePath the path to the command file
     */
    private void runHeadlessMode(String filePath) {
        view.displayMessage("Headless mode started. Processing commands from: " + filePath);
        boolean exitCommandFound = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String command;
            while ((command = reader.readLine()) != null) {
                command = command.trim();
                if (command.isEmpty()) continue;

                if (command.startsWith("#")) {
                    view.displayMessage(command);
                    continue;
                }
                
                view.displayMessage("> " + command);
                if ("exit".equalsIgnoreCase(command)) {
                    exitCommandFound = true;
                    break;
                }
                processCommand(command);
            }
        } catch (IOException e) {
            view.displayError("Could not read command file: " + e.getMessage());
            return;
        }

        if (!exitCommandFound) {
            view.displayError("The command file must end with an 'exit' command.");
        }
        view.displayMessage("Finished processing commands from file.");
        view.close();
    }

    /**
     * Parses a date string in YYYY-MM-DD format and returns a Date object.
     * @param dateStr the date string to parse
     * @return a Date object representing the parsed date
     * @throws IllegalArgumentException if the date string format is invalid
     */
    private Date parseDateString(String dateStr) throws IllegalArgumentException {
        if (dateStr == null || !dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Invalid date string format. Expected YYYY-MM-DD. Received: " + dateStr);
        }
        String[] parts = dateStr.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        return new Date(day, month, year);
    }

    /**
     * Parses a time string in HH:mm format and returns a Time object.
     * @param timeStr the time string to parse
     * @return a Time object representing the parsed time
     * @throws IllegalArgumentException if the time string format is invalid
     */
    private Time parseTimeString(String timeStr) throws IllegalArgumentException {
        if (timeStr == null || !timeStr.matches("\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Invalid time string format. Expected HH:mm. Received: " + timeStr);
        }
        String[] parts = timeStr.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        return new Time(hour, minute);
    }

    /**
     * Parses a date-time string in YYYY-MM-DDTHH:mm format and returns a DateTime object.
     * @param dateTimeStr the date-time string to parse
     * @return a DateTime object representing the parsed date and time
     * @throws IllegalArgumentException if the date-time string format is invalid
     */
    private DateTime parseDateTimeString(String dateTimeStr) throws IllegalArgumentException {
        if (dateTimeStr == null || !dateTimeStr.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Invalid date/time string format. Expected YYYY-MM-DDTHH:mm. Received: " + dateTimeStr);
        }
        String[] parts = dateTimeStr.split("T");
        Date date = parseDateString(parts[0]);
        Time time = parseTimeString(parts[1]);
        return new DateTime(date, time);
    }
    
    /**
     * Extracts the subject from a raw subject token by removing surrounding quotes if present.
     * @param rawSubjectToken the raw subject token that may be quoted
     * @return the extracted subject string without quotes
     */
    private String extractSubject(String rawSubjectToken) {
        if (rawSubjectToken.startsWith("\"") && rawSubjectToken.endsWith("\"") && rawSubjectToken.length() >=2) {
            return rawSubjectToken.substring(1, rawSubjectToken.length() - 1);
        }
        return rawSubjectToken;
    }
    
    /**
     * Extracts a quoted value by removing surrounding quotes if present.
     * @param rawValue the raw value that may be quoted
     * @return the extracted value string without quotes
     */
    private String extractQuotedValue(String rawValue) {
        rawValue = rawValue.trim();
        if (rawValue.startsWith("\"") && rawValue.endsWith("\"") && rawValue.length() >= 2) {
            return rawValue.substring(1, rawValue.length() - 1);
        }
        return rawValue;
    }

    /**
     * Processes a single command by parsing it and delegating to appropriate handler methods.
     * @param command the command string to process
     */
    private void processCommand(String command) {
        String trimmedCommand = command.trim();
        String[] commandParts = trimmedCommand.split("\\s+");

        if (commandParts.length == 0 || commandParts[0].isEmpty()) {
            view.displayError("Empty command.");
            return;
        }

        String mainAction = commandParts[0].toLowerCase();

        try {
            switch (mainAction) {
                case "create":
                    handleCreateCommand(trimmedCommand);
                    break;
                case "edit":
                    handleEditCommand(trimmedCommand);
                    break;
                case "print":
                    handlePrintCommand(trimmedCommand);
                    break;
                case "show":
                    handleShowCommand(trimmedCommand);
                    break;
                default:
                    view.displayError("Unrecognized command: " + mainAction);
            }
        } catch (IllegalArgumentException e) {
            view.displayError(e.getMessage());
        } catch (Exception e) {
            view.displayError("An unexpected error occurred while processing command: " + e.getMessage());
        }
    }

    /**
     * Parses a weekdays string and returns a list of DayOfWeek objects.
     * @param weekdaysString the string containing weekday codes (M, T, W, R, F, S, U)
     * @return a list of DayOfWeek objects representing the parsed weekdays
     * @throws IllegalArgumentException if an invalid weekday code is encountered
     */
    private List<DayOfWeek> parseWeekdays(String weekdaysString) {
        List<DayOfWeek> days = new ArrayList<>();
        if (weekdaysString == null) return days;
        for (char c : weekdaysString.toUpperCase().toCharArray()) {
            switch (c) {
                case 'M': days.add(DayOfWeek.MONDAY); break;
                case 'T': days.add(DayOfWeek.TUESDAY); break;
                case 'W': days.add(DayOfWeek.WEDNESDAY); break;
                case 'R': days.add(DayOfWeek.THURSDAY); break;
                case 'F': days.add(DayOfWeek.FRIDAY); break;
                case 'S': days.add(DayOfWeek.SATURDAY); break;
                case 'U': days.add(DayOfWeek.SUNDAY); break;
                default:
                    throw new IllegalArgumentException("Invalid weekday code: " + c + " in " + weekdaysString);
            }
        }
        return days;
    }

    /**
     * Handles the 'create' command for creating events and event series.
     * @param command the full create command string
     */
    private void handleCreateCommand(String command) {
        Pattern createEventPattern = Pattern.compile(
            "create event (\"[^\"]+\"|[^\\s]+) from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) to (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})(?: repeats ([MTWRFSU]+) (?:for (\\d+) times|until (\\d{4}-\\d{2}-\\d{2})))?(.*)", Pattern.CASE_INSENSITIVE);
        Pattern createAllDayEventPattern = Pattern.compile(
            "create event (\"[^\"]+\"|[^\\s]+) on (\\d{4}-\\d{2}-\\d{2})(?: repeats ([MTWRFSU]+) (?:for (\\d+) times|until (\\d{4}-\\d{2}-\\d{2})))?(.*)", Pattern.CASE_INSENSITIVE);

        Matcher matcher = createEventPattern.matcher(command);
        boolean isAllDay = false;

        if (!matcher.matches()) {
            matcher = createAllDayEventPattern.matcher(command);
            if (matcher.matches()) {
                isAllDay = true;
            } else {
                view.displayError("Invalid 'create event' command syntax. Problem with general structure, subject, or from/to/on clauses.");
                return;
            }
        }

        String subject = extractSubject(matcher.group(1));
        DateTime startDateTime;
        DateTime endDateTime = null;
        String description = null;
        String location = null;
        String status = "public";

        if (isAllDay) {
            Date date = parseDateString(matcher.group(2));
            startDateTime = new DateTime(date, new Time(8,0));
        } else {
            startDateTime = parseDateTimeString(matcher.group(2));
            endDateTime = parseDateTimeString(matcher.group(3));
        }

        String weekdaysString = null;
        Integer occurrences = null;
        Date seriesEndDate = null;
        boolean isSeries = false;

        int baseRepeatGroupIndex = isAllDay ? 3 : 4;
        if (matcher.group(baseRepeatGroupIndex) != null) {
            isSeries = true;
            weekdaysString = matcher.group(baseRepeatGroupIndex);
            if (matcher.group(baseRepeatGroupIndex + 1) != null) {
                occurrences = Integer.parseInt(matcher.group(baseRepeatGroupIndex + 1));
            } else if (matcher.group(baseRepeatGroupIndex + 2) != null) {
                seriesEndDate = parseDateString(matcher.group(baseRepeatGroupIndex + 2));
            } else {
                 view.displayError("Invalid repeat arguments for series. Must specify 'for N times' or 'until date'.");
                 return;
            }
        }

        int optionalArgsGroupIndex = isAllDay ? 6 : 7;
        String optionalArgsStr = matcher.group(optionalArgsGroupIndex);
        if (optionalArgsStr != null && !optionalArgsStr.trim().isEmpty()) {
            Pattern optionalArgPattern = Pattern.compile("(?:with\\s+)?(description|location|status)\\s+\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
            Matcher optionalArgMatcher = optionalArgPattern.matcher(optionalArgsStr.trim());
            while (optionalArgMatcher.find()) {
                String key = optionalArgMatcher.group(1).toLowerCase();
                String value = optionalArgMatcher.group(2);
                switch (key) {
                    case "description":
                        description = value;
                        break;
                    case "location":
                        location = value;
                        break;
                    case "status":
                        if ("public".equalsIgnoreCase(value) || "private".equalsIgnoreCase(value)) {
                            status = value.toLowerCase();
                        } else {
                            view.displayError("Invalid status value for create: '" + value + "'. Must be 'public' or 'private'.");
                            return;
                        }
                        break;
                }
            }
        }
        
        boolean success;
        if (isSeries) {
            List<DayOfWeek> repeatDays = parseWeekdays(weekdaysString);
            if (repeatDays.isEmpty() && weekdaysString != null && !weekdaysString.isEmpty()) {
                view.displayError("Repeat days cannot be empty for a series.");
                return;
            }
            success = model.createEventSeries(subject, startDateTime, endDateTime, description, location, status, repeatDays, occurrences, seriesEndDate);
        } else {
            success = model.createEvent(subject, startDateTime, endDateTime, description, location, status);
        }

        if (success) {
            view.displayMessage("Event(s) created successfully.");
        }
    }

    /**
     * Handles the 'edit' command for modifying existing events.
     * @param command the full edit command string
     */
    private void handleEditCommand(String command) {
        Pattern editPattern = Pattern.compile(
            "edit (event|events|series) (subject|start|end|description|location|status) (\"[^\"]+\"|[^\\s]+) from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})(?: to (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}))? with (.*)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = editPattern.matcher(command);

        if (!matcher.matches()) {
            view.displayError("Invalid 'edit' command syntax.");
            return;
        }

        String scopeKey = matcher.group(1).toLowerCase();
        String property = matcher.group(2).toLowerCase();
        String findSubject = extractSubject(matcher.group(3));
        DateTime findStartDateTime = parseDateTimeString(matcher.group(4));
        DateTime findEndDateTime = null;
        String capturedNewValuePart = matcher.group(6).trim();
        String newValueRaw = capturedNewValuePart.split("#", 2)[0].trim();

        String scope;
        if (scopeKey.equals("event")) {
            scope = "this";
            if (matcher.group(5) == null) {
                view.displayError("For 'edit event' (this instance), the 'to <endDateTtimeString>' part is required to uniquely identify the event.");
                return;
            }
            findEndDateTime = parseDateTimeString(matcher.group(5));
        } else if (scopeKey.equals("events")) {
            scope = "future";
            if (matcher.group(5) != null) {
                view.displayError("For 'edit events' (this and future), 'to <endDateTtimeString>' should not be specified for identification. Use only subject and start time.");
                return;
            }
        } else if (scopeKey.equals("series")) {
            scope = "all";
            if (matcher.group(5) != null) {
                view.displayError("For 'edit series' (all instances), 'to <endDateTtimeString>' should not be specified for identification. Use only subject and start time.");
                return;
            }
        } else {
            view.displayError("Internal error: Unrecognized edit scope key: " + scopeKey);
            return;
        }

        Object parsedNewValue;
        switch (property) {
            case "subject":
            case "description":
            case "location":
                parsedNewValue = extractQuotedValue(newValueRaw);
                break;
            case "status":
                String statusVal = extractQuotedValue(newValueRaw).toLowerCase();
                if (!statusVal.equals("public") && !statusVal.equals("private")) {
                    view.displayError("Invalid status value. Must be 'public' or 'private'. Received: " + newValueRaw);
                    return;
                }
                parsedNewValue = statusVal;
                break;
            case "start":
            case "end":
                parsedNewValue = parseDateTimeString(extractQuotedValue(newValueRaw));
                break;
            default:
                view.displayError("Internal error: Unrecognized property to edit: " + property);
                return;
        }
        
        boolean success = model.editEvent(findSubject, findStartDateTime, findEndDateTime, property, parsedNewValue, scope);
        if (success) {
            view.displayMessage("Event(s) edited successfully.");
        }
    }

    /**
     * Handles the 'print' command for displaying events on a date or within a range.
     * @param command the full print command string
     */
    private void handlePrintCommand(String command) {
        Pattern printOnDatePattern = Pattern.compile("print events on (\\d{4}-\\d{2}-\\d{2})", Pattern.CASE_INSENSITIVE);
        Pattern printRangePattern = Pattern.compile("print events from (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}) to (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})", Pattern.CASE_INSENSITIVE);

        Matcher matcher = printOnDatePattern.matcher(command);
        if (matcher.matches()) {
            Date date = parseDateString(matcher.group(1));
            List<IEvent> eventsFound = model.getEventsOnDate(date);
            view.displayEventsOnDate(eventsFound, matcher.group(1));
        } else {
            matcher = printRangePattern.matcher(command);
            if (matcher.matches()) {
                DateTime startRange = parseDateTimeString(matcher.group(1));
                DateTime endRange = parseDateTimeString(matcher.group(2));
                if (endRange.isBefore(startRange)) {
                    view.displayError("End of range cannot be before start of range for 'print events'.");
                    return;
                }
                List<IEvent> eventsFound = model.getEventsInRange(startRange, endRange);
                view.displayEvents(eventsFound);
            } else {
                view.displayError("Invalid 'print events' command syntax.");
            }
        }
    }

    /**
     * Handles the 'show' command for displaying status information.
     * @param command the full show command string
     */
    private void handleShowCommand(String command) {
        Pattern showStatusPattern = Pattern.compile("show status on (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})", Pattern.CASE_INSENSITIVE);
        Matcher matcher = showStatusPattern.matcher(command);
        if (matcher.matches()) {
            DateTime dateTime = parseDateTimeString(matcher.group(1));
            boolean isBusy = model.isBusyAt(dateTime);
            view.displayStatus(isBusy, matcher.group(1));
        } else {
            view.displayError("Invalid 'show status' command syntax.");
        }
    }
}