module com.example.samplerad {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.commons;
    requires mysql.connector.j;
    requires transitive javafx.base;


    opens com.example.samplerad to javafx.fxml;
    exports com.example.samplerad;

    requires javafx.graphics;
}