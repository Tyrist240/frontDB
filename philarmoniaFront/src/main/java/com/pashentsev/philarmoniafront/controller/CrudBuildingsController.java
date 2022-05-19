package com.pashentsev.philarmoniafront.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.pashentsev.philarmoniafront.dto.BuildingResponseDTO;
import com.pashentsev.philarmoniafront.dto.EventDTO;
import com.pashentsev.philarmoniafront.dto.EventResponseDTO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CrudBuildingsController implements Initializable {

    @FXML
    private TextField addressText;

    @FXML
    private Button backButton;

    @FXML
    private ChoiceBox<String> buildingType;

    @FXML
    private TextField capacityText;

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField diagonalText;

    @FXML
    private Button editButton;

    @FXML
    private TextField idText;

    @FXML
    private TextField nameText;

    @FXML
    private ChoiceBox<Integer> numOfPages;

    @FXML
    private Pagination pagination;

    @FXML
    private Button updateButton;

    private final TableView<Building> table = createTable();
    private List<Building> data;
    private int rowsPerPage = 10;
    private List<Integer> variable = new ArrayList<>();
    private List<String> type = new ArrayList<>();

    private TableView<Building> createTable() {
        TableView<Building> table = new TableView<>();

        TableColumn<Building, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(param -> param.getValue().id);
        idColumn.setPrefWidth(30);

        TableColumn<Building, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(param -> param.getValue().name);
        nameColumn.setPrefWidth(80);

        TableColumn<Building, Integer> capacityColumn = new TableColumn<>("Capacity");
        capacityColumn.setCellValueFactory(param -> param.getValue().capacity);
        capacityColumn.setPrefWidth(80);

        TableColumn<Building, Integer> diagonalColumn = new TableColumn<>("Diagonal");
        diagonalColumn.setCellValueFactory(param -> param.getValue().diagonal);
        diagonalColumn.setPrefWidth(80);

        TableColumn<Building, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(param -> param.getValue().address);
        addressColumn.setPrefWidth(80);

        TableColumn<Building, String> buildingTypeColumn = new TableColumn<>("BuildingType");
        buildingTypeColumn.setCellValueFactory(param -> param.getValue().buildingType);
        buildingTypeColumn.setPrefWidth(80);

        table.getColumns().addAll(idColumn, nameColumn, capacityColumn, diagonalColumn, addressColumn, buildingTypeColumn);
        return table;
    }

    private List<Building> createData() throws UnirestException, IOException {
        List<BuildingResponseDTO> list = Arrays.asList(new GsonBuilder().create().fromJson(
                Unirest.post("http://localhost:8080/building/fetch/list")
                        .header("content-type", "application/json")
                        .body("{ }")
                        .asJson().getBody().toString(), BuildingResponseDTO[].class)
        );

        return list.stream().map(event -> new Building(
                event.getId(),
                event.getName(),
                event.getCapacity(),
                event.getDiagonal(),
                event.getAddress(),
                event.getBuildingType()
        )).collect(Collectors.toList()
        );
    }

    @FXML
    void back(ActionEvent event) {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();

            URL fxmlLocation = getClass().getResource("crudAdminView.fxml");
            Parent root = FXMLLoader.load(fxmlLocation);

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("Menu");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void go(ActionEvent event) {
        this.rowsPerPage = numOfPages.getValue();
        pagination.setPageCount(data.size() / rowsPerPage + 1);
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, data.size());
        table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
        return table;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Stream.iterate(1, i -> i < 20, i -> i + 1).forEachOrdered(i -> variable.add(i));
        type.add("theatre");
        type.add("stage");
        type.add("culturePalace");
        type.add("concertSquare");
        type.add("cinema");
        buildingType.getItems().addAll(type);
        numOfPages.getItems().addAll(variable);
        numOfPages.setOnAction(this::go);
        try {
            this.data = createData();
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pagination.setPageCount(this.data.size()/rowsPerPage + 1);
        pagination.setPageFactory(this::createPage);

    }

    private BuildingResponseDTO createDto() throws UnirestException, IOException {
        Integer id;
        if(idText.getText() == "") {
            id = null;
        } else {
            id = Integer.valueOf(idText.getText());
        }
        BuildingResponseDTO buildingResponseDTO = new BuildingResponseDTO(
                id,
                nameText.getText(),
                Integer.valueOf(capacityText.getText()),
                Integer.valueOf(diagonalText.getText()),
                addressText.getText(),
                buildingType.getValue()
        );

        Unirest.setObjectMapper(new ObjectMapper() {
            private Gson gson = new Gson();

            public <T> T readValue(String s, Class<T> aClass) {
                try{
                    return gson.fromJson(s, aClass);
                }catch(Exception e){
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object o) {
                try{
                    return gson.toJson(o);
                }catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        });
        Unirest.setTimeouts(10000, 30000);

        idText.setText("");
        nameText.setText("");
        capacityText.setText("");
        diagonalText.setText("");
        addressText.setText("");
        buildingType.setValue("");

        return buildingResponseDTO;
    }

    @FXML
    void create(ActionEvent event) throws UnirestException, IOException {
        BuildingResponseDTO buildingResponseDTO = createDto();

        HttpResponse<JsonNode> addResponse = Unirest.post("http://localhost:8080/building/add")
                .header("content-type", "application/json")
                .body(buildingResponseDTO)
                .asJson();
        System.out.println("ADD status = " + addResponse.getStatus());

        this.data = createData();
        pagination.setPageFactory(this::createPage);
    }

    @FXML
    void delete(ActionEvent event) throws UnirestException, IOException {
        HttpResponse<String> deleteResponse = Unirest.post("http://localhost:8080/building/delete/{id}")
                .routeParam("id", idText.getText())
                .body("{ }")
                .asString();
        System.out.println("DELETE status = " + deleteResponse.getStatus());

        idText.setText("");
        nameText.setText("");
        capacityText.setText("");
        diagonalText.setText("");
        addressText.setText("");
        buildingType.setValue("");

        this.data = createData();
        pagination.setPageFactory(this::createPage);
    }

    @FXML
    void edit(ActionEvent event) {
        Building selected = table.getSelectionModel().getSelectedItem();
        idText.setText(selected.getId().getValue().toString());
        nameText.setText(selected.getName().getValue());
        capacityText.setText(selected.getCapacity().getValue().toString());
        diagonalText.setText(selected.getCapacity().getValue().toString());
        addressText.setText(selected.getName().getValue());
    }

    @FXML
    void update(ActionEvent event) throws UnirestException, IOException {
        BuildingResponseDTO buildingResponseDTO = createDto();

        HttpResponse<JsonNode> addResponse = Unirest.post("http://localhost:8080/building/update")
                .header("content-type", "application/json")
                .body(buildingResponseDTO)
                .asJson();
        System.out.println("ADD status = " + addResponse.getStatus());

        this.data = createData();
        pagination.setPageFactory(this::createPage);
    }

    public static class Building {
        private final ObservableValue<Integer> id;
        private final SimpleStringProperty name;
        private final ObservableValue<Integer> capacity;
        private final ObservableValue<Integer> diagonal;
        private final SimpleStringProperty address;
        private final SimpleStringProperty buildingType;

        public ObservableValue<Integer> getId() {
            return id;
        }
        public ObservableValue<Integer> getDiagonal() {
            return diagonal;
        }
        public SimpleStringProperty getName() {
            return name;
        }
        public ObservableValue<Integer> getCapacity() {
            return capacity;
        }
        public SimpleStringProperty getAddress() {
            return address;
        }
        public SimpleStringProperty getBuildingType() {
            return buildingType;
        }

        private Building(Integer id, String name, Integer capacity, Integer diagonal, String address, String buildingType) {
            this.id = new SimpleObjectProperty<>(id);
            this.name = new SimpleStringProperty(name);
            this.capacity = new SimpleObjectProperty<>(capacity);
            this.diagonal = new SimpleObjectProperty<>(diagonal);
            this.address = new SimpleStringProperty(address);
            this.buildingType = new SimpleStringProperty(buildingType);
        }
    }

}

