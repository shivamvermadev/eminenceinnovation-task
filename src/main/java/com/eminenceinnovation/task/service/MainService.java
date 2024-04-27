package com.eminenceinnovation.task.service;

import com.eminenceinnovation.task.model.FootballDTO;
import com.eminenceinnovation.task.model.MatchInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class MainService {
    public List<MatchInfo> getDrawMatches(Integer year) {
        CompletableFuture<FootballDTO> completableFuture = this.executeTask(year);
        List<MatchInfo> matches = new ArrayList<>();
        CompletableFuture.allOf(completableFuture).join();

        try {
            if(completableFuture.isDone()) {
                FootballDTO footballDTO = completableFuture.get();

                footballDTO.getMatchInfos().forEach(item -> {
                    if(Objects.equals(item.getTeam1Goals(), item.getTeam2Goals())) {
                        matches.add(item);
                    }
                });

                return matches;
            }
        } catch (Exception e) {
        }
        return null;
    }

    CompletableFuture<FootballDTO> executeTask(int year) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                String urlString = "https://jsonmock.hackerrank.com/api/football_matches?year=" + year + "&page=1";

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestMethod("GET");
                InputStream responseStream = connection.getInputStream();
                ObjectMapper mapper = new ObjectMapper();
                FootballDTO footballDTO = mapper.readValue(responseStream, FootballDTO.class);
                return footballDTO;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
