package model.states.beangame;

import view.sprites.Sprite;

import java.util.Map;

public class UnicornBeanBoard extends PictureBeanBoard {
    private static final Sprite UNICORN_SPRITE = new Sprite("unicornboard", "beanboard.png",
            0, 0, 29, 90);
    private static final Map<Character, Character> UNICORN_COLOR_MAP = Map.of('0', ' ',
            '1', 'G', '2', 'w', '3', 'p', '4', 'b');
    private static final int POCKET_LENGTH = 5;
    private static final int POCKETS = 6;

    public UnicornBeanBoard() {
        super("Unicorn", UNICORN_SPRITE, UNICORN_COLOR_MAP, POCKETS, POCKET_LENGTH);
    }
}
