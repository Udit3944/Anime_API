package com.example.animeapia2;

import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class SearchViewController {

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchBtn;

    @FXML
    private ListView<String> listView;

    @FXML
    private Text totalResults;

    @FXML
    private Text infoText;

    @FXML
    private Button getDetailsBtn;


    private static final String DETAILS_VIEW = "details-View.fxml";
    @FXML
    private void initialize() {
        searchBtn.setOnAction(event -> searchAnime());
        getDetailsBtn.setVisible(false);
        getDetailsBtn.setOnAction(event -> loadDetailsView());
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleItemSelected(newValue));
    }

    private void handleItemSelected(String selectedTitle) {
        getDetailsBtn.setVisible(selectedTitle != null && !selectedTitle.isEmpty());
        searchBtn.setVisible(false);
    }

    private void searchAnime() {
        String searchKeyword = searchBar.getText();
        if (searchKeyword.isEmpty()) {
            infoText.setText("Please enter an Anime name.");
            infoText.setVisible(true);

            listView.getItems().clear();
            totalResults.setText("");
            listView.setVisible(false);
        } else {
            String apiResponse = APIUtility.searchAnime(searchKeyword, 20, 0);

            JsonObject jsonResponse = APIUtility.parseJsonResponse(apiResponse);
            APIUtility.getEnglishTitles(jsonResponse, listView);

            int resultsCount = jsonResponse.getAsJsonArray("data").size();
            totalResults.setText("Number of search results: " + resultsCount);

            infoText.setText("(Select Anime for more details)");
            listView.setVisible(true);
            infoText.setVisible(true);
        }
    }

    private void loadDetailsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(DETAILS_VIEW));
            Parent detailsView = loader.load();

            DetailsViewController detailsController = loader.getController();

            String selectedEnglishName = listView.getSelectionModel().getSelectedItem();
            String animeDetailsResponse = APIUtility.getAnimeDetailsById(selectedEnglishName);
            JsonObject animeDetailsJson = APIUtility.parseJsonResponse(animeDetailsResponse);

            detailsController.updateDetails(animeDetailsJson);

            Stage stage = (Stage) searchBtn.getScene().getWindow();
            Scene scene = new Scene(detailsView);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
