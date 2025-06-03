package calendar.model;

public class Date {
  private int day;
  private int month;
  private int year;

  private boolean leap = false;

  private final int[] daysinmonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

  /**
   * constructs a new MyDate instance.
   *
   * @param day   the day of month (1‑31, further constrained by month)
   * @param month the month of year (1‑12)
   * @param year  the year (must be positive)
   * @throws IllegalArgumentException if any argument does not form a valid date
   */
  public Date(int day, int month, int year) {
    if (year < 1) {
      throw new IllegalArgumentException("Year must be positive");
    }
    if (day > 31 || day < 1) {
      throw new IllegalArgumentException("Day is out of range (1‑31)");
    }
    if (month > 12 || month < 1) {
      throw new IllegalArgumentException("Month is out of range (1‑12)");
    }

    boolean leap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));

    switch (month) {
      case 2:
        if (leap && day > 29) {
          throw new IllegalArgumentException("Day is out of range (leap‑year Feb)");
        }
        if (!leap && day > 28) {
          throw new IllegalArgumentException("Day is out of range (non‑leap Feb)");
        }
        break;
      case 4:
      case 6:
      case 9:
      case 11:
        if (day > 30) {
          throw new IllegalArgumentException("Day is out of range (30‑day month)");
        }
        break;
      default:
        break;
    }

    this.day = day;
    this.month = month;
    this.year = year;
    this.leap = leap;
  }

  /**
   * advances this date by the specified number of days (negative allowed).
   *
   * @param days the number of days to advance; may be negative
   */
  public void advance(int days) {
    this.day += days;

    while (true) {
      int dim = daysinmonths[this.month - 1];
      if (this.month == 2 && (this.year % 4 == 0
              && (this.year % 100 != 0 || this.year % 400 == 0))) {
        dim = 29;
      }

      if (this.day >= 1 && this.day <= dim) {
        break;
      }

      if (this.day > dim) {
        this.day -= dim;
        this.month++;
        if (this.month > 12) {
          this.month = 1;
          this.year++;
        }
      } else { // this.day <= 0
        this.month--;
        if (this.month < 1) {
          this.month = 12;
          this.year--;
        }
        int dimPrev = daysinmonths[this.month - 1];
        if (this.month == 2 && (this.year % 4 == 0
                && (this.year % 100 != 0 || this.year % 400 == 0))) {
          dimPrev = 29;
        }
        this.day += dimPrev;
      }
    }

    this.leap = (this.year % 4 == 0 && (this.year % 100 != 0 || this.year % 400 == 0));
  }

  public int getYear() { return year; }
  public int getMonth() { return month; }
  public int getDay() { return day; }

  /**
   * returns the date formatted as "yyyy‑mm‑dd" with zero padding.
   *
   * @return the formatted date string
   */
  @Override
  public String toString() {
    return String.format("%04d-%02d-%02d", year, month, day);
  }
}