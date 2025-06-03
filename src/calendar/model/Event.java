package calendar.model;

import java.time.DayOfWeek;
import java.util.ArrayList;

public class Event implements IEvent {
  private String subject;
  private String location;
  private DateTime start;
  private DateTime end;
  private boolean status;
  private String description;
  private boolean isSeries;
  private ArrayList<DayOfWeek> daysOfWeek;
  private static EventBuilder builder;


  public Event(String subject, String location, DateTime start, DateTime end, boolean status, String description){
    this.subject = subject;
    this.location = location;
    this.start = start;
    this.end = end;
    this.status = status;
    this.description = description;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String newSubject) {
    this.subject = newSubject;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String newLocation) {
    this.location = newLocation;
  }

  public DateTime getStart() {
    return start;
  }

  public void setStart(DateTime newStartDate) {
    this.start = newStartDate;
  }

  public DateTime getEnd() {
    return end;
  }

  public void setEnd(DateTime newEndDate) {
    this.end = newEndDate;
  }


  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean newStatus) {
    this.status = newStatus;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String newDescription) {
    this.description = newDescription;
  }

  public boolean match(String subject, Date date, Time time) {
    return this.subject.equals(subject)
            && this.startDate.toString().equals(date.toString())
            && this.startTime == time;
  }

  public void edit(String property, String subject,
                   Date date, Time time, String newValue) {
    if (this.match(subject, date, time)) {
      applyEdit(property, newValue);
    }
  }

  public void applyEdit(String property, String value) {
    switch (property.toLowerCase()) {
      case "subject":
        setSubject(value);
        break;
      case "start":
        int tIndex = value.indexOf('T');
        String startDateStr = value.substring(0, tIndex);
        String startTimeStr = value.substring(tIndex + 1);
        setStartDate(parseDate(startDateStr));
        setStartTime(parseTime(startTimeStr));
        break;
      case "end":
        int tEndIndex = value.indexOf('T');
        String endDateStr = value.substring(0, tEndIndex);
        String endTimeStr = value.substring(tEndIndex + 1);
        setEndDate(parseDate(endDateStr));
        setEndTime(parseTime(endTimeStr));
        break;
      case "location":
        setLocation(value);
        break;
      case "description":
        setDescription(value);
        break;
      case "status":
        setStatus(value);
        break;
      default:
        throw new IllegalArgumentException("Unknown property: " + property);
    }
  }

  private Date parseDate(String dateStr) {
    int firstDash = dateStr.indexOf('-');
    int secondDash = dateStr.indexOf('-', firstDash + 1);

    String yearStr = dateStr.substring(0, firstDash);
    String monthStr = dateStr.substring(firstDash + 1, secondDash);
    String dayStr = dateStr.substring(secondDash + 1);

    int year = Integer.parseInt(yearStr);
    int month = Integer.parseInt(monthStr);
    int day = Integer.parseInt(dayStr);

    return new Date(day, month, year);
  }

  private Time parseTime(String timeStr) {
    int colonIndex = timeStr.indexOf(':');
    String hourStr = timeStr.substring(0, colonIndex);
    String minuteStr = timeStr.substring(colonIndex + 1);

    int hour = Integer.parseInt(hourStr);
    int minute = Integer.parseInt(minuteStr);

    return new Time(hour, minute);
  }
}
