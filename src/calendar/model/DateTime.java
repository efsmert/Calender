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

  /**
   * Returns the date-time formatted as "YYYY-MM-DDThh:mm".
   */
  @Override
  public String toString() {
    return date.toString() + "T" + time.toString();
  }
}
