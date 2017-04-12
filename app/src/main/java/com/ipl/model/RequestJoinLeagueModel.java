package com.ipl.model;


public class RequestJoinLeagueModel {
    private String leagueName;
    private String requestFromId;


    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getRequestFromId() {
        return requestFromId;
    }

    public void setRequestFromId(String requestFromId) {
        this.requestFromId = requestFromId;
    }
}
