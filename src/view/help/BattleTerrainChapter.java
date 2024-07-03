package view.help;

import model.Model;
import model.map.HexLocation;
import model.map.WaterPath;
import model.states.battle.*;
import view.GameView;
import view.MyColors;
import view.party.DrawableObject;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BattleTerrainChapter extends SubChapterHelpDialog{
    private static final String TEXT = "Terrain affects many aspects of battle. In particular, a space with a " +
            "specific type of terrain incurs a specific movement costs to units entering it.";
    private static final List<BattleTerrain> TERRAINS = List.of(
            new DummyNoneTerrain(),
            new WoodsBattleTerrain(),
            new DenseWoodsBattleTerrain(),
            new HillsBattleTerrain(MyColors.GREEN),
            new SwampBattleTerrain(),
            new WaterBattleTerrain());
    private static final Sprite GREEN_BLOCK = new FilledBlockSprite(MyColors.GREEN);

    public BattleTerrainChapter(GameView view) {
        super(view, "Terrain", TEXT + "\n\n" + makeTable());
    }

    private static String makeTable() {
        StringBuilder result = new StringBuilder();
        result.append("     TERRAIN     MP  NOTE\n\n");
        for (BattleTerrain bt : TERRAINS) {
            if (bt instanceof WaterBattleTerrain) {
                result.append("     ").append(bt.getName()).append("       Impassible\n\n\n\n");
            } else {
                result.append(String.format("     %-11s%2d   %s\n\n\n\n", bt.getName(), bt.getMoveCost(), bt.getHelpNote()));
            }
        }
        return result.toString();
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> result = super.buildDecorations(model, xStart, yStart);
        result.add(new DrawableObject(xStart, yStart) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                int count = 0;
                for (BattleTerrain bt : TERRAINS) {
                    int finalX = x + 2;
                    int finalY = 10 + y + count;
                    model.getScreenHandler().fillSpace(finalX, finalX+4, finalY, finalY+4, GREEN_BLOCK);
                    if (!(bt instanceof DummyNoneTerrain)) {
                        bt.drawYourself(model.getScreenHandler(), new Point(finalX, finalY));
                    }
                    count += 4;
                }
            }
        });
        return result;
    }

    private static class DummyNoneTerrain extends BattleTerrain {
        public DummyNoneTerrain() {
            super("None", null);
        }

        @Override
        public String getHelpNote() {
            return "";
        }
    }
}
