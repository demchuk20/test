package com.relabs.test.services;

import com.relabs.test.entity.Game;
import com.relabs.test.logger.Print;
import com.relabs.test.repository.GameListRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SyncService {
    GameListRepository repository;

    @Autowired
    public SyncService(GameListRepository repository) {
        this.repository = repository;
    }

    public String getJson(URL url) throws IOException {
        StringBuilder sb = new StringBuilder();
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String inputLine = bufferedReader.readLine();
            while (inputLine != null) {
                sb.append(inputLine);
                inputLine = bufferedReader.readLine();
            }
        } finally {
            urlConnection.disconnect();
        }
        return sb.toString();
    }

    public List<Game> jsonParsing(String rawJson) throws Exception {
        List<Game> gameList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(rawJson).getJSONObject("feed");
        JSONArray games = jsonObject.getJSONArray("results");
        for (int i = 0; i < games.length(); i++) {
            JSONObject jsonGame = games.getJSONObject(i);
            Game game = new Game();
            game.setArtistName(jsonGame.getString("artistName"));
            game.setId(jsonGame.getLong("id"));
            game.setName(jsonGame.getString("name"));
            game.setReleaseDate(LocalDate.parse(jsonGame.getString("releaseDate")));
            game.setUrl(new URL(jsonGame.getString("url")));
            game.setArtworkUrl100(new URL(jsonGame.getString("artworkUrl100")));
            gameList.add(game);
        }
        return gameList;
    }

    //3 600 000
    @Print
    @Scheduled(fixedRate = 60000)
    public void syncing() throws Exception {
        List<Game> topFreeUS = jsonParsing(getJson(new URL("https://rss.itunes.apple.com/" +
                "api/v1/us/ios-apps/top-free/games/100/explicit.json")));
        repository.save(topFreeUS, "topFreeUS");
        List<Game> topPaidUS = jsonParsing(getJson(new URL("https://rss.itunes.apple.com/" +
                "api/v1/us/ios-apps/top-paid/games/100/explicit.json")));
        repository.save(topPaidUS, "topPaidUS");
        List<Game> topGrossingUS = jsonParsing(getJson(new URL("https://rss.itunes.apple.com/api/" +
                "v1/us/ios-apps/top-grossing/all/100/explicit.json")));
        repository.save(topGrossingUS, "topGrossingUS");
        List<Game> topFreeUA = jsonParsing(getJson(new URL("https://rss.itunes.apple.com/api/" +
                "v1/ua/ios-apps/top-free/all/100/explicit.json")));
        repository.save(topFreeUA, "topFreeUA");
        List<Game> topPaidUA = jsonParsing(getJson(new URL("https://rss.itunes.apple.com/api/" +
                "v1/ua/ios-apps/top-paid/games/100/explicit.json")));
        repository.save(topPaidUA, "topPaidUA");
        List<Game> topGrossingUA = jsonParsing(getJson(new URL("https://rss.itunes.apple.com/api/" +
                "v1/ua/ios-apps/top-grossing/all/100/explicit.json")));
        repository.save(topGrossingUA, "topGrossingUA");
    }
}

