package view.subviews;

import model.Model;
import model.ruins.DungeonLevel;
import model.ruins.DungeonRoom;
import model.ruins.RuinsDungeon;
import model.ruins.objects.DungeonChest;
import model.ruins.objects.DungeonMonster;
import model.ruins.objects.DungeonObject;
import view.BorderFrame;
import view.MyColors;

public class DungeonStatsSubView extends SubView {
    private final RuinsDungeon dungeon;
    private final String dungeonType;
    private final int visitedRooms;
    private final int visitedLevels;
    private final int mapsFound;
    private int totalRooms = 0;
    private int undefeatedMonsters = 0;
    private final int defeatedMonsters;
    private int chestsOpened = 0;
    private int chests = 0;

    public DungeonStatsSubView(RuinsDungeon dungeon, String dungeonType,
                               int visitedRooms, int visitedLevels, int defeatedMonsters,
                               int mapsFound) {
        this.dungeon = dungeon;
        this.dungeonType = dungeonType;
        this.visitedRooms = visitedRooms;
        this.visitedLevels = visitedLevels;
        this.defeatedMonsters = defeatedMonsters;
        this.mapsFound = mapsFound;
        for (int i = 0; i < dungeon.getNumberOfLevels(); ++i) {
            DungeonLevel level = dungeon.getLevel(i);
            for (DungeonRoom r : level.getRoomList()) {
                totalRooms++;
                for (DungeonObject dobj : r.getObjects()) {
                    if (dobj instanceof DungeonChest) {
                        if (((DungeonChest) dobj).isOpen()) {
                            chestsOpened++;
                        }
                        chests++;
                    } else if (dobj instanceof DungeonMonster) {
                        undefeatedMonsters++;
                    }
                }
            }
        }
    }

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX,
                blueBlock);
        int row = Y_OFFSET;
        BorderFrame.drawString(model.getScreenHandler(), "Dungeon Completed " + String.format("%10s", dungeon.isCompleted()?"Yes":"No"),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Levels Visited    " + String.format("%10s", visitedLevels + "/" + showIfCompleted(dungeon.getNumberOfLevels())),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Rooms Visited     " + String.format("%10s", visitedRooms + "/" + showIfCompleted(totalRooms)),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Monsters Defeated " + String.format("%10s", defeatedMonsters + "/" + showIfCompleted(undefeatedMonsters+defeatedMonsters)),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Chests Looted     " + String.format("%10s", chestsOpened + "/" + showIfCompleted(chests)),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Maps Found        " + String.format("%10s", mapsFound),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);
    }

    private String showIfCompleted(int totalRooms) {
        if (dungeon.isCompleted()) {
            return totalRooms + "";
        }
        return "???";
    }

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "DUNGEON - SUMMARY";
    }
}
