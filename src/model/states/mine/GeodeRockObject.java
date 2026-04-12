package model.states.mine;

import model.Model;
import model.items.treasures.TreasureItem;
import sound.SoundEffects;
import util.MyStrings;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.MineRockSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public abstract class GeodeRockObject extends MineableObject {
    private final Sprite32x32 floatingSprite;

    public GeodeRockObject(String name, int difficulty, Sprite32x32 floatingSprite) {
        super(name + " Geode", difficulty);
        this.floatingSprite = floatingSprite;
    }

    protected abstract Sprite getSprite();

    protected abstract TreasureItem makeGemItem();

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        screenHandler.put(screenPosition.x, screenPosition.y, getSprite());
    }

    @Override
    public boolean gotBumpedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
        state.askToMineObject(model, this);
        return false;
    }

    @Override
    public void giveReward(Model model, AdvancedMineEvent advancedMineEvent) {
        advancedMineEvent.addFloatingAnimation(this, floatingSprite);
        TreasureItem gemstone = makeGemItem();
        advancedMineEvent.println("You got " + MyStrings.aOrAn(gemstone.getName()) + " " + gemstone.getName() + "!");
        gemstone.addYourself(model.getParty().getInventory());
        SoundEffects.playSound(gemstone.getSound());
    }

    @Override
    public MyColors getAnimationColor() {
        return MyColors.GRAY;
    }

    protected static Sprite[] makeGeodeSpriteSet(MyColors lineColor, MyColors fillColor) {
        return new Sprite[]{
                new MineRockSprite(0x14, MyColors.DARK_GRAY, MyColors.GRAY, lineColor, fillColor),
                new MineRockSprite(0x15, MyColors.DARK_GRAY, MyColors.GRAY, lineColor, fillColor),
                new MineRockSprite(0x16, MyColors.DARK_GRAY, MyColors.GRAY, lineColor, fillColor),
                new MineRockSprite(0x17, MyColors.DARK_GRAY, MyColors.GRAY, lineColor, fillColor)};
    }

    @Override
    public boolean breaksOnFailure() {
        return true;
    }
}
