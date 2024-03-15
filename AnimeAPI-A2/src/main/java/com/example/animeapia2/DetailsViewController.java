package com.example.animeapia2;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class DetailsViewController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private Label totalEpisodesLabel;

    @FXML
    private Label startDateLabel;

    @FXML
    private Label endDateLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private TextArea synopsisArea;

    @FXML
    private ImageView imageView;

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        backButton.setOnAction(this::backToSearch);
    }
    private void backToSearch(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("search-view.fxml"));
            Parent searchView = loader.load();
            Scene searchScene = new Scene(searchView);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(searchScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateDetails(JsonObject animeData) {
        JsonObject attributes = animeData.getAsJsonObject("data").getAsJsonObject("attributes");

        JsonElement titlesElement = attributes.get("titles");
        String title = (titlesElement != null && !titlesElement.isJsonNull() && titlesElement.isJsonObject()) ? titlesElement.getAsJsonObject().get("en").getAsString() : "N/A";

        String rating = getAsStringOrNull(attributes, "averageRating");
        String totalEpisodes = getAsStringOrNull(attributes, "episodeCount");
        String startDate = getAsStringOrNull(attributes, "startDate");
        String endDate = getAsStringOrNull(attributes, "endDate");
        String status = getAsStringOrNull(attributes, "status");
        String synopsis = getAsStringOrNull(attributes, "synopsis");
        String imageUrl = getAsStringOrNull(attributes.getAsJsonObject("posterImage"), "original");

        titleLabel.setText(title);
        ratingLabel.setText(rating);
        totalEpisodesLabel.setText(totalEpisodes);
        startDateLabel.setText(startDate);
        endDateLabel.setText(endDate);
        statusLabel.setText(status);
        synopsisArea.setText(synopsis);

        Image image = new Image(imageUrl);
        imageView.setImage(image);
    }

    private String getAsStringOrNull(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        return (element != null && !element.isJsonNull()) ? element.getAsString() : "N/A";
    }


}
