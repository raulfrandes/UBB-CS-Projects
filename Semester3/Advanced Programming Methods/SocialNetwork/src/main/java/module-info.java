module com.example.gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.lab_8 to javafx.fxml;
    opens com.example.lab_8.controller to javafx.fxml;
    exports com.example.lab_8;
    exports com.example.lab_8.controller;
    exports com.example.lab_8.domain;
    exports com.example.lab_8.service;
    exports com.example.lab_8.utils.events;
    exports com.example.lab_8.utils.observer;
    exports com.example.lab_8.repository;
    exports com.example.lab_8.repository.paging;
    exports com.example.lab_8.domain.validators;
    opens com.example.lab_8.domain to javafx.fxml;
}