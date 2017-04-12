package com.ipl.model;


public class LeagueLeaderBoardModel {
    private String memberName;
    private String memberRank;
    private String memberPoint;

    public String getMemberPoint() {
        return memberPoint;
    }

    public void setMemberPoint(String memberPoint) {
        this.memberPoint = memberPoint;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberRank() {
        return memberRank;
    }

    public void setMemberRank(String memberRank) {
        this.memberRank = memberRank;
    }
}
