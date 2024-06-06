package model.ruins.objects;

import model.Model;
import model.items.special.FatueKeyItem;
import model.items.special.PieceOfStaffItem;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class FatueKeyObject extends CenterDungeonObject {

    public static final double PREVALENCE = 0.33;
    private final Sprite32x32 sprite;
    private final boolean completesDungeon;
    private final MyColors color;

    public FatueKeyObject(MyColors color, boolean completesDungeon) {
        this.sprite = new Sprite32x32("fatuekeygold", "fatue_plan.png", 0x38,
                MyColors.BLACK, color, getHighlightColor(color));
        this.color = color;
        this.completesDungeon = completesDungeon;
    }

    public FatueKeyObject(MyColors color) {
        this(color, false);
    }

    public static MyColors getHighlightColor(MyColors color) {
        if (color == MyColors.DARK_RED) {
            return MyColors.RED;
        } else if (color == MyColors.DARK_GREEN) {
            return MyColors.GREEN;
        } else if (color == MyColors.GRAY) {
            return MyColors.WHITE;
        } else if (color == MyColors.BROWN) {
            return MyColors.ORANGE;
        }
        return MyColors.YELLOW;
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return sprite;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos, DungeonTheme theme) {
        Sprite spr = getSprite(theme);
        model.getScreenHandler().register(spr.getName(), new Point(xPos, yPos), spr);
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        state.getCurrentRoom().removeObject(this);
        model.getParty().getInventory().add(new FatueKeyItem(color));
        state.println("You got a key. Now to find the lock...");
        if (completesDungeon) {
            state.setDungeonExited(true);
            state.getDungeon().setCompleted(true);
            state.println("Dungeon completed. Press enter to continue.");
            state.waitForReturnSilently();
        }
    }

    @Override
    public String getDescription() {
        return "A key";
    }
}
