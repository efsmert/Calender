package calendar.model;
import java.util.*;

public class EventSeries implements IEvent {
  private ArrayList<Event> events;
  private EventBuilder builder;
  

  public EventSeries(List<Event> events) {
    this.events = new ArrayList<>(events);

  }


  public void addEvent(Event event) {
    for (Event e : events) {
      if (!e.getStart().equals(event.getStart())) {
        throw new IllegalArgumentException("Event already exists in the series.");
      }
    }
    events.add(event);
  }
  public void removeEvent(Event event) {
    events.remove(event);
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