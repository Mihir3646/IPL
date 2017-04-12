package com.ipl.model;


public class MatchPredictionModel {
    private String selectedTeam;
    private String selectedBuId;

    public String getSelectedBuId() {
        return selectedBuId;
    }

    public void setSelectedBuId(String selectedBuId) {
        this.selectedBuId = selectedBuId;
    }

    public String getSelectedTeam() {
        return selectedTeam;
    }

    public void setSelectedTeam(String selectedTeam) {
        this.selectedTeam = selectedTeam;
    }
}
