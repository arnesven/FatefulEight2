package view.sprites;

import view.MyColors;

public class CharClassIconSprite extends Sprite32x32 {
    public CharClassIconSprite(int iconNumber, MyColors color) {
        super("charclassicon" + iconNumber, "classes.png", iconNumber,
                MyColors.BLACK, MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, color);
    }
}
