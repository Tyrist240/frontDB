package com.pashentsev.philarmoniafront.dto;



public class ArtistResponseDTO {


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

    private String surname;

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }


    private String formattedDate;

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public ArtistResponseDTO(final Integer id, final String name, final String surname, final String formattedDate) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        } else if (surname == null) {
            throw new NullPointerException("surname is marked non-null but is null");
        } else if (formattedDate == null) {
            throw new NullPointerException("formattedDate is marked non-null but is null");
        } else {
            this.id = id;
            this.name = name;
            this.surname = surname;
            this.formattedDate = formattedDate;
        }
    }

}
