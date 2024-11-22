package model.states.beangame;

import view.sprites.Sprite;

import java.util.Map;

public class SwordBeanBoard extends PictureBeanBoard {
    private static final Sprite SWORD_SPRITE = new Sprite("sword", "beanboard3.png",
            0, 0, 27, 84);
    private static final Map<Character, Character> COLOR_MAP = Map.of('0', ' ',
            '1', 'k', '2', 'w', '3', 'G', '4', 'g');

    public SwordBeanBoard() {
        super("Sword", SWORD_SPRITE, COLOR_MAP, 7, 4);
    }
}
