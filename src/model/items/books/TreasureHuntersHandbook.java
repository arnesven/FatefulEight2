package model.items.books;

import model.items.Item;
import view.MyColors;
import view.TreasureMapView;
import view.sprites.Sprite;

import java.util.HashMap;
import java.util.Map;

public class TreasureHuntersHandbook extends BookItem {
    private static final Map<String, Sprite> FIGURES = makeFigures();

    public TreasureHuntersHandbook() {
        super("Green Book", 20, MyColors.DARK_GREEN, "Treasure Hunter's Handbook", "Wilbur Philemon", makeTextContent());
    }

    private static String makeTextContent() {
        return "For the up-and-coming treasure hunter, here are some useful tips on how to " +
                "read a genuine treasure map.\n\n" +
                "The start point is almost always a town or castle and will be marked on the map " +
                "with the following symbol:\n" +
                "<fig town>\n\n\n\n\n\n\n" +
                "The path to the treasure will then be marked as a dashed path. It is important to " +
                "note that the path does not always take the shortest route to the treasure, but instead " +
                "passes a number of landmarks which will also be marked on the map.\n\n" +
                "For instance mountains will be marked with the following symbol:\n" +
                "<fig mountain>\n\n\n\n\n\n\n\n" +
                "Whereas swamps are usually drawn as:\n" +
                "<fig swamp>\n\n\n\n\n\n\n\n" +
                "Be careful not to confuse them with wasteland symbols!\n" +
                "<fig wasteland>\n\n\n\n\n\n\n\n\n" +
                "Woods and jungles are usually drawn as such:\n" +
                "<fig woods>\n\n\n\n\n\n\n\n" +
                "Hills are typically drawn simply like this:\n" +
                "<fig hills>\n\n\n\n\n\n\n\n"+
                "And, where the path crosses a river:\n" +
                "<fig river>\n\n\n\n\n\n\n\n\n" +
                "Finally the location of the treasure is marked with a red X, like this:\n" +
                "<fig xmark>\n\n\n\n\n\n\n" +
                "With this, finding that booty should be less of a challenge.\n\nGood Luck!";
    }


    private static Map<String, Sprite> makeFigures() {
        Map<String, Sprite> result = new HashMap<>();
        result.put("town", TreasureMapView.TOWN_SPRITE);
        result.put("woods", TreasureMapView.WOODS_SPRITE);
        result.put("hills", TreasureMapView.HILLS_SPRITE);
        result.put("mountain", TreasureMapView.MOUNTAIN_SPRITES);
        result.put("swamp", TreasureMapView.SWAMP_SPRITE);
        result.put("wasteland", TreasureMapView.WASTELAND_SPRITE);
        result.put("river", TreasureMapView.RIVER_SPRITES[0]);
        result.put("xmark", TreasureMapView.FINAL_SPOT_SPRITE);
        return result;
    }

    @Override
    public Map<String, Sprite> getFigures() {
        return FIGURES;
    }

    @Override
    public Item copy() {
        return new TreasureHuntersHandbook();
    }
}
