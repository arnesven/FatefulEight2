package model.states.events;

public class TournamentOdds {
    private final int strength;
    private final int total;

    public TournamentOdds(int strength, int total) {
        this.strength = strength;
        this.total = total;
    }

    public String getOddsString() {
        if (total < strength * 2) {
            return strength + ":" + total;
        }
        return ((int) Math.round(getBetMultiplier())) + ":1";
    }

    public double getBetMultiplier() {
        return (double) total / (double) strength;
    }
}
