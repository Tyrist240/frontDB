package com.pashentsev.philarmoniafront.dto;

import java.util.Date;

public class ArtistDTO {

    private Integer id;

    private String name;

    private String surname;

    private String birthDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public ArtistDTO(final Integer id, final String name, final String surname, final String birthDate) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        } else if (surname == null) {
            throw new NullPointerException("surname is marked non-null but is null");
        } else if (birthDate == null) {
            throw new NullPointerException("birthDate is marked non-null but is null");
        } else {
            this.id = id;
            this.name = name;
            this.surname = surname;
            this.birthDate = birthDate;
        }
    }
}
