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

    private final Sprite32x32 sprite;

    public FatueKeyObject(MyColors color) {
        this.sprite = new Sprite32x32("fatuekeygold", "fatue_plan.png", 0x38,
                MyColors.BLACK, color, getHighlightColor(color));
    }

    public static MyColors getHighlightColor(MyColors color) {
        if (color == MyColors.DARK_RED) {
            return MyColors.RED;
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
        model.getParty().getInventory().add(new FatueKeyItem(MyColors.GOLD));
        state.println("You got a key. Now to find the lock...");
    }

    @Override
    public String getDescription() {
        return "A key";
    }
}
