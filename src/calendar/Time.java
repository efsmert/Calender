package calendar;

/**
 * Represents a time of day with hour and minute components.
 * Uses 24-hour format with validation for proper time ranges.
 */
public class Time {
  private int hour;
  private int minute;

  /**
   * Constructs a new Time instance.
   * @param hour   the hour of day (0-23)
   * @param minute the minute of hour (0-59)
   * @throws IllegalArgumentException if hour or minute are out of range
   */
  public Time(int hour, int minute) {
    if (hour < 0 || hour > 23) {
      throw new IllegalArgumentException("Hour must be between 0 and 23. Received: " + hour);
    }
    if (minute < 0 || minute > 59) {
      throw new IllegalArgumentException("Minute must be between 0 and 59. Received: " + minute);
    }
    this.hour = hour;
    this.minute = minute;
  }

  /**
   * Gets the hour component of this time.
   * @return the hour (0-23)
   */
  public int getHour() { return hour; }
  
  /**
   * Gets the minute component of this time.
   * @return the minute (0-59)
   */
  public int getMinute() { return minute; }

  /**
   * Returns the time formatted as "hh:mm" with zero padding.
   * @return a string representation of this time in HH:mm format
   */
  @Override
  public String toString() {
    return String.format("%02d:%02d", hour, minute);
  }

  /**
   * Checks if this Time is equal to another object.
   * @param o the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Time time = (Time) o;
      return hour == time.hour && minute == time.minute;
  }

  /**
   * Returns a hash code for this Time.
   * @return the hash code value for this Time
   */
  @Override
  public int hashCode() {
      int result = hour;
      result = 31 * result + minute;
      return result;
  }
}