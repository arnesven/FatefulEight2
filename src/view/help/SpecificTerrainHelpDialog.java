package view.help;

import model.Model;
import model.map.*;
import util.MyStrings;
import view.GameView;

import java.util.ArrayList;
import java.util.List;

public class SpecificTerrainHelpDialog extends SubChapterHelpDialog {
    private final WorldHex hex;
    private final boolean features;

    public SpecificTerrainHelpDialog(GameView view, WorldHex currentHex, boolean features) {
        super(view, MyStrings.capitalize(currentHex.getTerrainName()), "\n\n\n\n\n\n\n" +
                currentHex.getTerrainDescription() + makeFeatures(features, currentHex));
        this.hex = currentHex;
        this.features = features;
    }

    private static String makeFeatures(boolean features, WorldHex currentHex) {
        if (!features || !hasFeature(currentHex)) {
            return "";
        }
        return "\n\nFeatures:\n\n\n\n.";
    }

    private static boolean hasFeature(WorldHex currentHex) {
        return currentHex.hasRoad() || currentHex.getRivers() != 0 ||
                (currentHex.getLocation() != null && !currentHex.getLocation().isDecoration());
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> list = super.buildDecorations(model, xStart, yStart);

        list.add(new DrawableObject(xStart+15, yStart+4) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                model.getScreenHandler().clearSpace(x, x+4, y, y+4);
                hex.drawYourself(model.getScreenHandler(), x, y, HexLocation.FLAG_NONE);
            }
        });
        return list;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> content = new ArrayList<>(super.buildContent(model, xStart, yStart));

        if (features) {
            int count = 0;
            if (hex.hasRoad()) {
                if (hasBrokenRoad(hex)) {
                    addFeatureLink(content, count, xStart, yStart, "Broken Road", new BrokenRoadTerrainHelpDialog(model.getView()));
                } else {
                    addFeatureLink(content, count, xStart, yStart, "Roads", new RoadsTerrainHelpDialog(model.getView()));
                }
                count++;
            }
            if (hex.getRivers() != 0) {
                addFeatureLink(content, count, xStart, yStart,  "River/Coast", new RiverTerrainHelpDialog(model.getView()));
                count++;
            }
            if (hex.getLocation() != null && !hex.getLocation().isDecoration()) {
                addFeatureLink(content, count, xStart, yStart, hex.getLocation().getName(), hex.getLocation().getHelpDialog(model.getView()));
                count++;
            }
        }
        return content;
    }

    private boolean hasBrokenRoad(WorldHex hex) {
        switch (hex.getRoads()) {
            case Direction.NORTH:
            case Direction.NORTH_WEST:
            case Direction.NORTH_EAST:
            case Direction.SOUTH:
            case Direction.SOUTH_EAST:
            case Direction.SOUTH_WEST:
                return hex.getLocation() == null || hex.getLocation().isDecoration();
        }
        return false;
    }

    private void addFeatureLink(List<ListContent> content, int count, int xStart, int yStart, String name, HelpDialog helpDialog) {
        content.add(new SelectableListContent(xStart + 3, yStart + getHeight() - 7 + count, name) {
            @Override
            public void performAction(Model model, int x, int y) {
                model.transitionToDialog(helpDialog);
            }

            @Override
            public boolean isEnabled(Model model) {
                return true;
            }
        });
    }
}
