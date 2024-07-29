package model.headquarters;

import model.Model;
import model.map.UrbanLocation;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;

public class MediumHeadquarters extends Headquarters {

    private final Sprite32x32 sprite;

    public MediumHeadquarters(UrbanLocation location) {
        super(location, 2);
        this.sprite = new Sprite32x32("headquarters", "world_foreground.png", 0x22,
                MyColors.YELLOW, MyColors.DARK_GRAY, MyColors.BROWN, MyColors.LIGHT_GRAY);
    }

    @Override
    public void specificDrawYourself(Model model, Point p) {
        model.getScreenHandler().register(sprite.getName(), new Point(p), sprite);
    }

}
