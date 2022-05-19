package com.pashentsev.philarmoniafront.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BuildingResponseDTO {

    private Integer id;

    private String name;

    private Integer capacity;

    private Integer diagonal;


    private String address;

    private String buildingType;

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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getDiagonal() {
        return diagonal;
    }

    public void setDiagonal(Integer diagonal) {
        this.diagonal = diagonal;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    public BuildingResponseDTO(Integer id, String name, Integer capacity, Integer diagonal, String address, String buildingType) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.diagonal = diagonal;
        this.address = address;
        this.buildingType = buildingType;
    }
}