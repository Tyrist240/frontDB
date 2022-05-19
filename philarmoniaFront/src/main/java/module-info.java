module com.pashentsev.philarmoniafront {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    //needed for HTTP
    requires unirest.java;

    //needed for JSON
    requires gson;
    requires java.sql;

    requires com.fasterxml.jackson.databind;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.pashentsev.philarmoniafront.dto to gson;
    //opens com.pashentsev.philarmoniafront.dto to com.fasterxml.jackson.databind;


    opens com.pashentsev.philarmoniafront.controller to javafx.fxml;
    exports com.pashentsev.philarmoniafront.controller;
}