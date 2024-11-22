package model.states.beangame;

import view.sprites.Sprite;

import java.util.Map;

public class DragonBeanBoard extends PictureBeanBoard {
    private static final Sprite DRAGON_SPRITE = new Sprite("dragonboard", "beanboard2.png",
            0, 0, 29, 82);
    private static final Map<Character, Character> DRAGON_COLOR_MAP = Map.of('0', ' ',
            '1', 'k', '2', 'w', '3', 'p', '4', 'y');

    public DragonBeanBoard() {
        super("Dragon", DRAGON_SPRITE, DRAGON_COLOR_MAP, 5, 6);
    }
}
