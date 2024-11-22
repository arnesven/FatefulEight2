package model.states.beangame;

import view.sprites.Sprite;

import java.util.Map;

public class WaterfallBeanBoard extends PictureBeanBoard {
    private static final Sprite SPRITE = new Sprite("waterfall", "beanboard6.png",
            0, 0, 29, 84);
    private static final Map<Character, Character> COLOR_MAP = Map.of('0', ' ',
            '1', 'k', '2', 'w', '3', 'G', '4', 'b');

    public WaterfallBeanBoard() {
        super("Waterfall", SPRITE, COLOR_MAP, 5, 6);
    }
}
