package model.headquarters;

import model.Model;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class SmallHeadquarters extends HeadquarterAppearance {
    private static final Sprite32x32 SPRITE = new Sprite32x32("headquarters", "world_foreground.png", 0xD5,
            MyColors.YELLOW, MyColors.DARK_GRAY, MyColors.BROWN, MyColors.LIGHT_GRAY);

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(p), SPRITE);
    }

    @Override
    public String getDescription() {
        return "It's a small, cozy house.";
    }
}
