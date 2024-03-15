package com.example.animeapia2;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.scene.control.ListView;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class APIUtility {
    private static final String KITSU_API_URL = "https://kitsu.io/api/edge/anime";
    public static String searchAnime(String searchKeyword, int limit, int offset) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String apiUrl = KITSU_API_URL + "?filter[text]=" + URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8)
                    + "&page[limit]=" + limit + "&page[offset]=" + offset;

            URI uri = URI.create(apiUrl);

            HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    public static String searchAnime(String searchKeyword) {
        return searchAnime(searchKeyword, 10, 0);
    }
    public static JsonObject parseJsonResponse(String jsonResponse) {
        Gson gson = new Gson();
        return gson.fromJson(jsonResponse, JsonObject.class);
    }

    public static void getEnglishTitles(JsonObject jsonResponse, ListView<String> listView) {
        JsonArray dataArray = jsonResponse.getAsJsonArray("data");
        listView.getItems().clear();

        for (int i = 0; i < dataArray.size(); i++) {
            JsonObject attributes = dataArray.get(i).getAsJsonObject().getAsJsonObject("attributes");
            JsonObject titles = attributes.getAsJsonObject("titles");

            if (titles.has("en") && !titles.get("en").isJsonNull() && !titles.get("en").getAsString().equalsIgnoreCase("N/A")) {
                String englishTitle = titles.get("en").getAsString();

                listView.getItems().add(englishTitle);
            }
        }
    }
    public static String getAnimeDetailsById(String englishName) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String searchApiResponse = searchAnime(englishName);

            JsonObject searchJsonResponse = parseJsonResponse(searchApiResponse);
            JsonArray dataArray = searchJsonResponse.getAsJsonArray("data");

            if (!dataArray.isEmpty()) {
                String animeId = dataArray.get(0).getAsJsonObject().get("id").getAsString();

                String apiUrl = KITSU_API_URL + "/" + animeId;
                URI uri = URI.create(apiUrl);

                HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                return response.body();
            } else {
                return "Error: Anime not found";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }


}
