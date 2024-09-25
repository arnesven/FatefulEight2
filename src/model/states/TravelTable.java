package model.states;

import model.map.TownLocation;
import util.MyPair;

public class TravelTable {
    private final TownLocation[] destinations;
    private final int[] costs;

    public TravelTable(TownLocation d1, int c1, TownLocation d2, int c2, TownLocation d3, int c3,
                       TownLocation d4, int c4, TownLocation d5, int c5, TownLocation d6, int c6,
                       TownLocation d7, int c7, TownLocation d8, int c8, TownLocation d9, int c9,
                       TownLocation d10, int c10) {
        this.destinations = new TownLocation[]{d1, d2, d3, d4, d5, d6, d7, d8, d9, d10};
        this.costs = new int[]{c1, c2, c3, c4, c5, c6, c7, c8, c9, c10};
    }

    public MyPair<TownLocation, Integer> get(int roll) {
        return new MyPair<>(destinations[roll - 1], costs[roll - 1]);
    }
}
