package com.pashentsev.philarmoniafront.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.pashentsev.philarmoniafront.dto.ArtistDTO;
import com.pashentsev.philarmoniafront.dto.ArtistResponseDTO;
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

public class CrudImpresarioController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private ChoiceBox<Integer> numOfPages;

    @FXML
    private Pagination pagination;

    @FXML
    private Button updateButton;

    @FXML
    private TextField surnameText;

    @FXML
    private TextField birthdayText;

    @FXML
    private Button createButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private TextField idText;

    @FXML
    private TextField nameText;

    private final TableView<Impresario> table = createTable();
    private List<Impresario> data;
    private int rowsPerPage = 10;
    private List<Integer> variable = new ArrayList<>();


    private TableView<Impresario> createTable() {
        TableView<Impresario> table = new TableView<>();

        TableColumn<Impresario, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(param -> param.getValue().id);
        idColumn.setPrefWidth(30);

        TableColumn<Impresario, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(param -> param.getValue().name);
        nameColumn.setPrefWidth(80);

        TableColumn<Impresario, String> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setCellValueFactory(param -> param.getValue().surname);
        surnameColumn.setPrefWidth(80);

        TableColumn<Impresario, String> formattedDateColumn = new TableColumn<>("birthday");
        formattedDateColumn.setCellValueFactory(param -> param.getValue().formattedDate);
        formattedDateColumn.setPrefWidth(80);

        table.getColumns().addAll(idColumn, nameColumn, surnameColumn, formattedDateColumn);
        return table;
    }

    private List<Impresario> createData() throws UnirestException, IOException {
        List<ArtistResponseDTO> list = Arrays.asList(new GsonBuilder().create().fromJson(
                Unirest.post("http://localhost:8080/impresario/fetch/list")
                        .header("content-type", "application/json")
                        .body("{ }")
                        .asJson().getBody().toString(), ArtistResponseDTO[].class)
        );

        return list.stream().map(artist -> new Impresario(artist.getId(), artist.getName(), artist.getSurname(), artist.getFormattedDate()))
                .collect(Collectors.toList());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Stream.iterate(1, i -> i < 20, i -> i + 1).forEachOrdered(i -> variable.add(i));
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

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, data.size());
        table.setItems(FXCollections.observableArrayList(data.subList(fromIndex, toIndex)));
        return table;
    }

    @FXML
    void go(ActionEvent event) {
        this.rowsPerPage = numOfPages.getValue();
        pagination.setPageCount(data.size() / rowsPerPage + 1);
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
    void delete(ActionEvent event) throws UnirestException, IOException {
        HttpResponse<String> deleteResponse = Unirest.post("http://localhost:8080/impresario/delete/{id}")
                .routeParam("id", idText.getText())
                .body("{ }")
                .asString();
        System.out.println("DELETE status = " + deleteResponse.getStatus());

        idText.setText("");
        nameText.setText("");
        surnameText.setText("");
        birthdayText.setText("");

        this.data = createData();
        pagination.setPageFactory(this::createPage);
    }

    private ArtistDTO createDto() {
        String[] partsOfDate = birthdayText.getText().split("/");
        Integer id;
        if(idText.getText() == "") {
            id = null;
        } else {
            id = Integer.valueOf(idText.getText());
        }
        ArtistDTO artistDTO = new ArtistDTO(
                id,
                nameText.getText(),
                surnameText.getText(),
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
        surnameText.setText("");
        birthdayText.setText("");

        return artistDTO;
    }

    @FXML
    void create(ActionEvent event) throws UnirestException, IOException {
        ArtistDTO artistDTO = createDto();

        HttpResponse<JsonNode> addResponse = Unirest.post("http://localhost:8080/impresario/add")
                .header("content-type", "application/json")
                .body(artistDTO)
                .asJson();
        System.out.println("ADD status = " + addResponse.getStatus());

        this.data = createData();
        pagination.setPageFactory(this::createPage);
    }

    @FXML
    void update(ActionEvent event) throws UnirestException, IOException {
        ArtistDTO artistDTO = createDto();

        HttpResponse<JsonNode> addResponse = Unirest.post("http://localhost:8080/impresario/update")
                .header("content-type", "application/json")
                .body(artistDTO)
                .asJson();
        System.out.println("ADD status = " + addResponse.getStatus());

        this.data = createData();
        pagination.setPageFactory(this::createPage);
    }

    @FXML
    void edit(ActionEvent event) {
        Impresario selected = table.getSelectionModel().getSelectedItem();
        idText.setText(selected.getId().getValue().toString());
        nameText.setText(selected.getName().getValue());
        surnameText.setText(selected.getSurname().getValue());
        birthdayText.setText(selected.getFormattedDate().getValue());
    }


    public static class Impresario {
        private final ObservableValue<Integer> id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty surname;
        private final SimpleStringProperty formattedDate;

        public ObservableValue<Integer> getId() {
            return id;
        }
        public SimpleStringProperty getSurname() {
            return surname;
        }
        public SimpleStringProperty getName() {
            return name;
        }
        public SimpleStringProperty getFormattedDate() {
            return formattedDate;
        }

        private Impresario(Integer id, String name, String surname, String formattedDate) {
            this.id = new SimpleObjectProperty<>(id);
            this.name = new SimpleStringProperty(name);
            this.surname = new SimpleStringProperty(surname);
            this.formattedDate = new SimpleStringProperty(formattedDate);
        }
    }
}
