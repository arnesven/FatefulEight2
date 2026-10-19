package model.states.cardgames.sleeves;

import view.MyColors;

public class PlainLongSleeve extends CardGameSleeve {
    public PlainLongSleeve(MyColors color) {
        super(color, color, color, MyColors.TRANSPARENT);
    }
}
