package view.sprites;

import view.MyColors;

public class
CurlySpiralAnimation extends RunOnceAnimationSprite {
    public CurlySpiralAnimation(MyColors color) {
        super("curlyspiralanimation", "combat.png",
                8, 13, 32, 32, 8, color);
        setAnimationDelay(5);
    }
}
