package com.pashentsev.philarmoniafront.dto;

public class EventDTO {
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private Integer eventType;

    public Integer getEventType() {
        return eventType;
    }
    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    private Integer eventPlace;

    public Integer getEventPlace() {
        return eventPlace;
    }
    public void setEventPlace(Integer eventPlace) {
        this.eventPlace = eventPlace;
    }


    private String eventDate;

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }


    public EventDTO(final Integer id, final String name, final Integer eventType, final Integer eventPlace, final String eventDate) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        } else if (eventType == null) {
            throw new NullPointerException("eventType is marked non-null but is null");
        } else if (eventPlace == null) {
            throw new NullPointerException("eventPlace is marked non-null but is null");
        } else if (eventDate == null) {
            throw new NullPointerException("eventDate is marked non-null but is null");
        } else {
            this.id = id;
            this.name = name;
            this.eventType = eventType;
            this.eventPlace = eventPlace;
            this.eventDate = eventDate;
        }
    }
}
