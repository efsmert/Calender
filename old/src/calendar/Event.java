package calendar;

public class Event {
  private String subject;
  private String description;
  private String location;
  private Date startDate;
  private Date endDate;
  private int startTime; //unix time
  private int endTime;
  private boolean status;

  public Event(String subject, String description, String location, Date startDate,
               Date endDate, int startTime, int endTime, boolean status){
    this.description = description;
    this.subject = subject;
    this.location = location;
    this.startDate = startDate;
    this.endDate = endDate;
    this.startTime = startTime;
    this.endTime = endTime;
    this.status = status;
  }


}