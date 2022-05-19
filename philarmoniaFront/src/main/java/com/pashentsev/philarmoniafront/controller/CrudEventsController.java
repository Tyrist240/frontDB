package com.pashentsev.philarmoniafront.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.RequestBodyEntity;
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

public class CrudEventsController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Button createButton;

    @FXML
    private TextField dateText;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private ChoiceBox<String> eventType;

    @FXML
    private TextField idText;

    @FXML
    private TextField nameText;

    @FXML
    private ChoiceBox<Integer> numOfPages;

    @FXML
    private Pagination pagination;

    @FXML
    private TextField placeText;

    @FXML
    private Button updateButton;

    private final TableView<Event> table = createTable();
    private List<Event> data;
    private int rowsPerPage = 10;
    private List<Integer> variable = new ArrayList<>();
    private List<String> type = new ArrayList<>();


    private TableView<Event> createTable() {
        TableView<Event> table = new TableView<>();

        TableColumn<Event, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(param -> param.getValue().id);
        idColumn.setPrefWidth(30);

        TableColumn<Event, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(param -> param.getValue().name);
        nameColumn.setPrefWidth(80);

        TableColumn<Event, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(param -> param.getValue().eventType);
        typeColumn.setPrefWidth(80);

        TableColumn<Event, String> placeColumn = new TableColumn<>("Place");
        placeColumn.setCellValueFactory(param -> param.getValue().place);
        placeColumn.setPrefWidth(80);

        TableColumn<Event, String> formattedDateColumn = new TableColumn<>("Date");
        formattedDateColumn.setCellValueFactory(param -> param.getValue().formattedDate);
        formattedDateColumn.setPrefWidth(80);

        table.getColumns().addAll(idColumn, nameColumn, typeColumn, placeColumn, formattedDateColumn);
        return table;
    }

    private List<Event> createData() throws UnirestException, IOException {
        List<EventResponseDTO> list = Arrays.asList(new GsonBuilder().create().fromJson(
                Unirest.post("http://localhost:8080/event/fetch/list")
                        .header("content-type", "application/json")
                        .body("{ }")
                        .asJson().getBody().toString(), EventResponseDTO[].class)
        );

        return list.stream().map(event -> new Event(
                    event.getId(),
                    event.getName(),
                    event.getEventType(),
                    event.getEventPlace(),
                    event.getEventDate()
                )).collect(Collectors.toList()
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Stream.iterate(1, i -> i < 20, i -> i + 1).forEachOrdered(i -> variable.add(i));
        type.add("CONCERT");
        type.add("PERFORMANCE");
        type.add("COMPETITION");
        type.add("FILM");
        eventType.getItems().addAll(type);
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

    private Integer eventMapping(String event) throws IOException {
        switch (event) {
            case "CONCERT":
                return 1;
            case "PERFORMANCE":
                return 2;
            case "COMPETITION":
                return 3;
            case "FILM":
                return 4;
            default:
                throw new IOException();

        }
    }

    private EventDTO createDto() throws UnirestException, IOException {
        String[] partsOfDate = dateText.getText().split("/");
        Integer id;
        if(idText.getText() == "") {
            id = null;
        } else {
            id = Integer.valueOf(idText.getText());
        }
        EventDTO eventDTO = new EventDTO(
                id,
                nameText.getText(),
                eventMapping(eventType.getValue()),
                Integer.valueOf(placeText.getText()),
                partsOfDate[2] + "-" + partsOfDate[1] + "-" + partsOfDate[0]
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
        eventType.setValue("");
        placeText.setText("");
        dateText.setText("");

        return eventDTO;
    }

    @FXML
    void create(ActionEvent event) throws UnirestException, IOException {
        EventDTO eventDTO = createDto();

        HttpResponse<JsonNode> addResponse = Unirest.post("http://localhost:8080/event/add")
                .header("content-type", "application/json")
                .body(eventDTO)
                .asJson();
        System.out.println("ADD status = " + addResponse.getStatus());

        this.data = createData();
        pagination.setPageFactory(this::createPage);
    }

    @FXML
    void delete(ActionEvent event) throws UnirestException, IOException {
        HttpResponse<String> deleteResponse = Unirest.post("http://localhost:8080/event/delete/{id}")
                .routeParam("id", idText.getText())
                .body("{ }")
                .asString();
        System.out.println("DELETE status = " + deleteResponse.getStatus());

        idText.setText("");
        nameText.setText("");
        eventType.setValue("");
        placeText.setText("");
        dateText.setText("");

        this.data = createData();
        pagination.setPageFactory(this::createPage);
    }

    @FXML
    void edit(ActionEvent event) throws UnirestException {
        Event selected = table.getSelectionModel().getSelectedItem();
        idText.setText(selected.getId().getValue().toString());
        nameText.setText(selected.getName().getValue());
        HttpResponse<String> indexResponse = Unirest.post("http://localhost:8080/building/fetch/fetchByEventId/{id}")
                .header("content-type", "application/json")
                .routeParam("id", idText.getText())
                .body("{ }")
                .asString();
        placeText.setText(String.valueOf(indexResponse.getBody()));
        dateText.setText(selected.getFormattedDate().getValue());
    }

    @FXML
    void update(ActionEvent event) throws UnirestException, IOException {
        EventDTO eventDTO = createDto();

        HttpResponse<JsonNode> addResponse = Unirest.post("http://localhost:8080/event/update")
                .header("content-type", "application/json")
                .body(eventDTO)
                .asJson();
        System.out.println("ADD status = " + addResponse.getStatus());

        this.data = createData();
        pagination.setPageFactory(this::createPage);
    }

    public static class Event {
        private final ObservableValue<Integer> id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty eventType;
        private final SimpleStringProperty place;
        private final SimpleStringProperty formattedDate;

        public ObservableValue<Integer> getId() {
            return id;
        }
        public SimpleStringProperty getPlace() {
            return place;
        }
        public SimpleStringProperty getName() {
            return name;
        }
        public SimpleStringProperty getEventType() {
            return eventType;
        }
        public SimpleStringProperty getFormattedDate() {
            return formattedDate;
        }

        private Event(Integer id, String name, String eventType, String place, String formattedDate) {
            this.id = new SimpleObjectProperty<>(id);
            this.name = new SimpleStringProperty(name);
            this.eventType = new SimpleStringProperty(eventType);
            this.place = new SimpleStringProperty(place);
            this.formattedDate = new SimpleStringProperty(formattedDate);
        }
    }

}
