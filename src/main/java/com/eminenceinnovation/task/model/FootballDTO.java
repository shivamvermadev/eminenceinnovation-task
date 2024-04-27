package com.eminenceinnovation.task.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FootballDTO {

    @JsonProperty("data")
    private List<MatchInfo> matchInfos;

    public List<MatchInfo> getMatchInfos() {
        return matchInfos;
    }

    public void setMatchInfos(List<MatchInfo> matchInfos) {
        this.matchInfos = matchInfos;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
