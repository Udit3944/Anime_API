module com.example.animeapia2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;

    opens com.example.animeapia2 to javafx.fxml;
    exports com.example.animeapia2;
}