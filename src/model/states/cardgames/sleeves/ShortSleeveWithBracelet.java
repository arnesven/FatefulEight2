package model.states.cardgames.sleeves;

import view.MyColors;

public class ShortSleeveWithBracelet extends CardGameSleeve {
    public ShortSleeveWithBracelet(MyColors sleeveColor, MyColors braceletColor) {
        super(sleeveColor, MyColors.TRANSPARENT, MyColors.TRANSPARENT, braceletColor);
    }
}
