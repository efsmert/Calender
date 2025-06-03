package calendar.model;

public class DateTime {
  private Date date;
  private Time time;

  /**
   * Constructs a new DateTime instance from Date and Time.
   * @param date the Date object
   * @param time the Time object
   * @throws IllegalArgumentException if either date or time is null
   */
  public DateTime(Date date, Time time) {
    if (date == null || time == null) {
      throw new IllegalArgumentException("Date and Time must not be null");
    }
    this.date = date;
    this.time = time;
  }

  public Date getDate() {
    return date;
  }
  public Time getTime() {
    return time;
  }

  public boolean isBefore(DateTime other) {
    // Check for null parameter
    if (other == null) {
        throw new IllegalArgumentException("Other DateTime must not be null");
    }
    
    // We need getter methods in Date class to access year, month, day
    // Compare years first
    if (this.date.getYear() < other.date.getYear()) {
        return true;
    }
    if (this.date.getYear() > other.date.getYear()) {
        return false;
    }
    
    // Years are equal, compare months
    if (this.date.getMonth() < other.date.getMonth()) {
        return true;
    }
    if (this.date.getMonth() > other.date.getMonth()) {
        return false;
    }
    
    // Months are equal, compare days
    if (this.date.getDay() < other.date.getDay()) {
        return true;
    }
    if (this.date.getDay() > other.date.getDay()) {
        return false;
    }
    
    // Dates are equal, compare times
    // We need getter methods in Time class to access hour and minute
    if (this.time.getHour() < other.time.getHour()) {
        return true;
    }
    if (this.time.getHour() > other.time.getHour()) {
        return false;
    }
    
    // Hours are equal, compare minutes
    return this.time.getMinute() < other.time.getMinute();
}

  /**
   * Returns the date-time formatted as "YYYY-MM-DDThh:mm".
   */
  @Override
  public String toString() {
    return date.toString() + "T" + time.toString();
  }
}
