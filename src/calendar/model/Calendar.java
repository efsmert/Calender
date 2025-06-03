package calendar.model;

import java.util.ArrayList;

public class Calendar {

    private ArrayList<IEvent> events;
    private String name;
    private String description;
    private int year;

    /**
     * Constructs a new Calendar instance.
     * @param name        the name of the calendar
     * @param description a brief description of the calendar
     * @param year        the year for which this calendar is created
     */
    public Calendar(String name, String description, int year) {
        this.name = name;
        this.description = description;
        this.year = year;
        this.events = new ArrayList<>();

        
}
