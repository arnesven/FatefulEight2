package model.states.dailyaction.town;

import model.Model;
import model.states.dailyaction.DailyActionNode;
import view.MyColors;

public class SnowyCampOutsideOfTownNode extends CampOutsideOfTownNode {
    public SnowyCampOutsideOfTownNode(boolean freeRations, Model model, String text) {
        super(freeRations, model, MyColors.WHITE, MyColors.LIGHT_GRAY, text);
    }
}
