package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.sprites.FaceSprite;
import view.sprites.Sprite8x8;

import java.io.Serializable;

public abstract class StaffHandDetail implements Serializable {
    private final MyColors orbColor;
    private MyColors staffColor;

    public StaffHandDetail(MyColors staffColor, MyColors orbColor) {
        this.staffColor = staffColor;
        this.orbColor = orbColor;
    }

    public abstract void applyYourself(AdvancedAppearance appearance, Race race);

    protected void addSpriteOnTop(AdvancedAppearance appearance, int num, int x, int y, MyColors handColor) {
        Sprite8x8 left = new FaceSprite("staffhand", "clothes.png", num);
        left.setColor1(MyColors.BLACK);
        left.setColor2(handColor);
        left.setColor3(staffColor);
        left.setColor4(orbColor);
        appearance.addSpriteOnTop(x, y, left);
    }
}
