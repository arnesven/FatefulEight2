package model.states.dailyaction;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class TravelFromInnNode extends TravelNode {

    private static final Sprite SIGN = new Sprite32x32("travelsign", "world_foreground.png", 0x05,
            MyColors.BLACK, MyColors.BROWN, MyColors.BROWN, MyColors.LIGHT_YELLOW);

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register("travelsign", p, SIGN);
    }
}
