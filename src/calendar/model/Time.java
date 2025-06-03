package calendar.model;

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
      throw new IllegalArgumentException("Hour must be between 0 and 23");
    }
    if (minute < 0 || minute > 59) {
      throw new IllegalArgumentException("Minute must be between 0 and 59");
    }
    this.hour = hour;
    this.minute = minute;
  }

  public int getHour() { return hour; }
  public int getMinute() { return minute; }

  /**
   * Returns the time formatted as "hh:mm" with zero padding.
   */
  @Override
  public String toString() {
    return String.format("%02d:%02d", hour, minute);
  }
}
