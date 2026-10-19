package model.states.cardgames.sleeves;

import view.MyColors;

public class FancyLongSleeve extends CardGameSleeve {
    public FancyLongSleeve(MyColors base, MyColors cuff) {
        super(base, cuff, base, MyColors.TRANSPARENT);
    }
}
