package calendar;

/**
 * Represents a date and time combination.
 * This class combines a Date and Time object to represent a specific moment in time.
 */
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

  /**
   * Gets the date component of this DateTime.
   * @return the Date object
   */
  public Date getDate() {
    return date;
  }

  /**
   * Gets the time component of this DateTime.
   * @return the Time object
   */
  public Time getTime() {
    return time;
  }

  /**
   * Checks if this DateTime is before another DateTime.
   * @param other the DateTime to compare against
   * @return true if this DateTime is before the other DateTime, false otherwise
   * @throws IllegalArgumentException if other is null
   */
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
    
    if (this.time.getHour() < other.time.getHour()) return true;
    if (this.time.getHour() > other.time.getHour()) return false;
    
    return this.time.getMinute() < other.time.getMinute();
  }

  /**
   * Checks if this DateTime is after another DateTime.
   * @param other the DateTime to compare against
   * @return true if this DateTime is after the other DateTime, false otherwise
   * @throws IllegalArgumentException if other is null
   */
  public boolean isAfter(DateTime other) {
    if (other == null) {
        throw new IllegalArgumentException("Other DateTime must not be null for comparison.");
    }
    return !this.isBefore(other) && !this.equals(other);
  }

  /**
   * Returns the date-time formatted as "YYYY-MM-DDThh:mm".
   * @return a string representation of this DateTime in ISO format
   */
  @Override
  public String toString() {
    return date.toString() + "T" + time.toString();
  }

  /**
   * Checks if this DateTime is equal to another object.
   * @param o the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      DateTime dateTime = (DateTime) o;
      return date.equals(dateTime.date) && time.equals(dateTime.time);
  }

  /**
   * Returns a hash code for this DateTime.
   * @return the hash code value for this DateTime
   */
  @Override
  public int hashCode() {
      int result = date.hashCode();
      result = 31 * result + time.hashCode();
      return result;
  }
}