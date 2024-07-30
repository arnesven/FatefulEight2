package model.headquarters;

import model.Model;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class MajesticHeadquarters extends HeadquarterAppearance {
    private static final Sprite32x32 SPRITE = new Sprite32x32("hqmajestic", "world_foreground.png", 0xD8,
            MyColors.YELLOW, MyColors.DARK_GRAY, MyColors.BROWN, MyColors.LIGHT_GRAY);
    private static final Sprite32x32 SPRITE_UPPER_LEFT = new Sprite32x32("hqmajestic", "world_foreground.png", 0xC8,
            MyColors.YELLOW, MyColors.DARK_GRAY, MyColors.BROWN, MyColors.LIGHT_GRAY);
    private static final Sprite32x32 SPRITE_LOWER_RIGHT = new Sprite32x32("hqmajestic", "world_foreground.png", 0xD9,
            MyColors.YELLOW, MyColors.DARK_GRAY, MyColors.BROWN, MyColors.LIGHT_GRAY);
    private static final Sprite32x32 SPRITE_UPPER_RIGHT = new Sprite32x32("hqmajestic", "world_foreground.png", 0xC9,
            MyColors.YELLOW, MyColors.DARK_GRAY, MyColors.BROWN, MyColors.LIGHT_GRAY);

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(p), SPRITE);
        Point p2 = new Point(p.x, p.y-4);
        model.getScreenHandler().register(SPRITE.getName(), new Point(p2), SPRITE_UPPER_LEFT);
        p2 = new Point(p.x+4, p.y);
        model.getScreenHandler().register(SPRITE.getName(), new Point(p2), SPRITE_LOWER_RIGHT);
        p2 = new Point(p.x+4, p.y-4);
        model.getScreenHandler().register(SPRITE.getName(), new Point(p2), SPRITE_UPPER_RIGHT);
    }

    @Override
    public String getDescription() {
        return "It's a large house. There's plenty of space to be comfortable.";
    }
}
