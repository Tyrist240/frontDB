package com.pashentsev.philarmoniafront.dto;

public class EventResponseDTO {
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

    private String eventType;

    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    private String eventPlace;

    public String getEventPlace() {
        return eventPlace;
    }
    public void setEventPlace(String eventPlace) {
        this.eventPlace = eventPlace;
    }


    private String eventDate;

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public EventResponseDTO(final Integer id, final String name, final String eventType, final String eventPlace, final String eventDate) {
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
