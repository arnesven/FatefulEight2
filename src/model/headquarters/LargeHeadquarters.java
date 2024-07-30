package model.headquarters;

import model.Model;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class LargeHeadquarters extends HeadquarterAppearance {
    private static final Sprite32x32 SPRITE = new Sprite32x32("headquarterslarge", "world_foreground.png", 0xD6,
            MyColors.YELLOW, MyColors.DARK_GRAY, MyColors.BROWN, MyColors.LIGHT_GRAY);
    private static final Sprite32x32 SPRITE_UPPER = new Sprite32x32("headquarterslargeupper", "world_foreground.png", 0xC6,
            MyColors.YELLOW, MyColors.DARK_GRAY, MyColors.BROWN, MyColors.LIGHT_GRAY);

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(p), SPRITE);
        Point p2 = new Point(p.x, p.y-4);
        model.getScreenHandler().register(SPRITE.getName(), new Point(p2), SPRITE_UPPER);
    }

    @Override
    public String getDescription() {
        return "It's a large house. There's plenty of space to be comfortable.";
    }
}
