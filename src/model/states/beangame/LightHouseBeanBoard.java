package model.states.beangame;

import view.sprites.Sprite;

import java.util.Map;

public class LightHouseBeanBoard extends PictureBeanBoard {
    private static final Sprite SPRITE = new Sprite("lighthousebeanboard", "beanboard5.png",
            0, 0, 29, 83);
    private static final Map<Character, Character> COLOR_MAP = Map.of('0', ' ',
            '1', 'k', '2', 'w', '3', 'p', '4', 'b');

    public LightHouseBeanBoard() {
        super("Tower", SPRITE, COLOR_MAP, 6, 5);
    }
}
