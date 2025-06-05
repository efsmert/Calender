package calendar;

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
    if (other == null) {
        throw new IllegalArgumentException("Other DateTime must not be null for comparison.");
    }
    
    if (this.date.getYear() < other.date.getYear()) return true;
    if (this.date.getYear() > other.date.getYear()) return false;
    
    if (this.date.getMonth() < other.date.getMonth()) return true;
    if (this.date.getMonth() > other.date.getMonth()) return false;
    
    if (this.date.getDay() < other.date.getDay()) return true;
    if (this.date.getDay() > other.date.getDay()) return false;
    
    // Dates are equal, compare times
    if (this.time.getHour() < other.time.getHour()) return true;
    if (this.time.getHour() > other.time.getHour()) return false;
    
    return this.time.getMinute() < other.time.getMinute();
  }

  public boolean isAfter(DateTime other) {
    if (other == null) {
        throw new IllegalArgumentException("Other DateTime must not be null for comparison.");
    }
    return !this.isBefore(other) && !this.equals(other);
  }


  /**
   * Returns the date-time formatted as "YYYY-MM-DDThh:mm".
   */
  @Override
  public String toString() {
    return date.toString() + "T" + time.toString();
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      DateTime dateTime = (DateTime) o;
      return date.equals(dateTime.date) && time.equals(dateTime.time);
  }

  @Override
  public int hashCode() {
      int result = date.hashCode();
      result = 31 * result + time.hashCode();
      return result;
  }
}