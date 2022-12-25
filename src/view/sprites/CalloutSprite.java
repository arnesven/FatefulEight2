package view.sprites;

import model.Model;
import view.MyColors;

public class CalloutSprite extends TimedAnimationSprite {

    public CalloutSprite(int num) {
        super("callout"+num, "callouts.png", num, 300, MyColors.BLACK, MyColors.WHITE, MyColors.RED);
    }
}
