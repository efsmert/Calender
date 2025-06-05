package calendar;

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
    // Corrected day validation to use daysinmonths array after leap year check
    // Original code had day validation before month-specific checks

    if (month > 12 || month < 1) {
      throw new IllegalArgumentException("Month is out of range (1‑12)");
    }

    this.leap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
    int maxDays = daysinmonths[month - 1];
    if (month == 2 && this.leap) {
        maxDays = 29;
    }

    if (day > maxDays || day < 1) {
      throw new IllegalArgumentException("Day is out of range (1-" + maxDays + ") for month " + month);
    }
    
    // Original switch case for day validation is now covered by the above logic more directly.
    // The original switch had some redundancy and could be simplified.
    // For example, `case 4,6,9,11: if (day > 30)` is covered by `daysinmonths[month-1]`.

    this.day = day;
    this.month = month;
    this.year = year;
    // this.leap is already set
  }

  /**
   * advances this date by the specified number of days (negative allowed).
   *
   * @param days the number of days to advance; may be negative
   */
  public void advance(int days) {
    this.day += days;

    while (true) {
      // Re-calculate leap for the current year in loop, as year might change
      boolean currentLeap = (this.year % 4 == 0 && (this.year % 100 != 0 || this.year % 400 == 0));
      int dim = daysinmonths[this.month - 1];
      if (this.month == 2 && currentLeap) {
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
          if (this.year < 1) { // Ensure year doesn't go below 1
              // This case needs careful handling based on requirements for dates before year 1.
              // For now, let's assume year must remain >= 1 as per constructor.
              // This might indicate an issue if advancing too far back.
              // Or, simply let it be and rely on constructor validation for new Date objects.
              // The original code didn't explicitly prevent year < 1 here.
          }
        }
        // Leap for previous month's year
        boolean prevLeap = (this.year % 4 == 0 && (this.year % 100 != 0 || this.year % 400 == 0));
        int dimPrev = daysinmonths[this.month - 1];
        if (this.month == 2 && prevLeap) {
          dimPrev = 29;
        }
        this.day += dimPrev;
      }
    }
    // Update the main leap field after all adjustments
    this.leap = (this.year % 4 == 0 && (this.year % 100 != 0 || this.year % 400 == 0));
  }

  public int getYear() { return year; }
  public int getMonth() { return month; }
  public int getDay() { return day; }
  public boolean isLeap() { return leap; } // Added getter for leap

  /**
   * returns the date formatted as "yyyy‑mm‑dd" with zero padding.
   *
   * @return the formatted date string
   */
  @Override
  public String toString() {
    return String.format("%04d-%02d-%02d", year, month, day);
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Date date = (Date) o;
      return day == date.day && month == date.month && year == date.year;
  }

  @Override
  public int hashCode() {
      int result = day;
      result = 31 * result + month;
      result = 31 * result + year;
      return result;
  }
}