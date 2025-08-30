package org.contest.server.util;

public class Triplet {
    public String countryId;
    public String contestantId;
    public int score;

    public Triplet(String countryId, String contestantId, int score) {
        this.countryId = countryId;
        this.contestantId = contestantId;
        this.score = score;
    }
}
