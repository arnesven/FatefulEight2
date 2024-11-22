package model.states.beangame;

import view.sprites.Sprite;

import java.util.Map;

public class LadyBeanBoard extends PictureBeanBoard {

    private static final Sprite LADY_SPRITE = new Sprite("ladybeanboard", "beanboard4.png",
            0, 0, 29, 80);
    private static final Map<Character, Character> COLOR_MAP = Map.of('0', ' ',
            '1', 'k', '2', 'w', '3', 'y', '4', 'b');

    public LadyBeanBoard() {
        super("Lady", LADY_SPRITE, COLOR_MAP, 6, 5);
    }
}
