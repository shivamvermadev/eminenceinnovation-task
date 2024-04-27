package com.eminenceinnovation.task.service;

import com.eminenceinnovation.task.model.FootballDTO;
import com.eminenceinnovation.task.model.MatchInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
            if (completableFuture.isDone()) {
                FootballDTO footballDTO = completableFuture.get();

                footballDTO.getMatchInfos().forEach(item -> {
                    if (Objects.equals(item.getTeam1Goals(), item.getTeam2Goals())) {
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
                RestTemplate restTemplate = new RestTemplate();
                FootballDTO footballDTO = restTemplate.getForObject(urlString, FootballDTO.class);
                return footballDTO;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
