package view.help;

import view.GameView;

public class RoadsTerrainHelpDialog extends SubChapterHelpDialog{
    private static final String TEXT = "Roads are generally safe to travel and adventurers who do often " +
            "meet many kinds of people. Roads are traversed by merchants, priests, storytellers, noblemen, couriers, " +
            "artisans, soldiers, wizards, bandits and many others. You are likely to find some new friends on the road and every now and then, some " +
            "old friends too.\n\nFarmers traveling by wagon sometimes offer to give adventurers a ride on " +
            "the road.\n\nWhen traveling along a road it is possible to encounter a road-related event or an event " +
            "connected to the terrain which that road is located in.";

    public RoadsTerrainHelpDialog(GameView view) {
        super(view, "Roads", TEXT);

    }
}
