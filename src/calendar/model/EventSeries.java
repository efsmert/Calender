package calendar.model;
import java.util.*;

public class EventSeries implements IEvent {
  private final List<Event> events;

  public EventSeries(List<Event> events) {
    this.events = new ArrayList<>(events);
  }

  public void edit(String property, String subject,
                   Date date, Time time, String newValue) {
    boolean found = false;
    for (Event e : events) {
      if (!found) {
        if (e.match(subject, date, time)) {
          found = true;
          e.applyEdit(property, newValue);
        }
      } else {
        e.applyEdit(property, newValue);
      }
    }
  }

}