module com.example.eerstejavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.example.eerstejavafx to javafx.fxml;
    exports com.example.eerstejavafx;
}
