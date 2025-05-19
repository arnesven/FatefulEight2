package model.characters.appearance;

import model.races.Race;
import util.MyRandom;
import view.MyColors;

import java.util.List;

public class MakeUpColors {

    private static final MyColors[] MASCARA_COLORS = new MyColors[]{
            MyColors.BEIGE,
            MyColors.BLACK,
            MyColors.BLUE,
            MyColors.DARK_BLUE,
            MyColors.DARK_BROWN,
            MyColors.DARK_GREEN,
            MyColors.DARK_PURPLE,
            MyColors.DARK_RED,
            MyColors.DARK_RED,
            MyColors.GOLD,
            MyColors.GREEN,
            MyColors.LIGHT_BLUE,
            MyColors.LIGHT_GRAY,
            MyColors.LIGHT_GREEN,
            MyColors.LIGHT_RED,
            MyColors.LIGHT_RED,
            MyColors.LIGHT_RED,
            MyColors.ORANGE,
            MyColors.PEACH,
            MyColors.PINK,
            MyColors.PURPLE,
            MyColors.RED,
            MyColors.RED,
            MyColors.RED
    };

    public static MyColors randomMascaraColor(Race race) {
        MyColors result = race.getColor();
        while (result == race.getColor()) {
            result = MASCARA_COLORS[MyRandom.randInt(MASCARA_COLORS.length)];
        }
        return result;
    }

    public static MyColors randomLipColor(Race race) {
        return randomMascaraColor(race);
    }

    public static MyColors randomRougeColor(Race raceToUse) {
        if (raceToUse.id() == Race.NORTHERN_HUMAN.id()) {
            return MyRandom.flipCoin() ? MyColors.LIGHT_RED : MyColors.PEACH;
        }
        if (raceToUse.id() == Race.SOUTHERN_HUMAN.id()) {
            return MyRandom.flipCoin() ? MyColors.BROWN : MyColors.GOLD;
        }
        if (raceToUse.id() == Race.EASTERN_HUMAN.id()) {
            return MyRandom.flipCoin() ? MyColors.PURPLE : MyColors.LIGHT_RED;
        }
        if (raceToUse.id() == Race.HIGH_ELF.id()) {
            return MyRandom.flipCoin() ? MyColors.PINK : MyColors.LIGHT_RED;
        }
        if (raceToUse.id() == Race.WOOD_ELF.id()) {
            return MyRandom.flipCoin() ? MyColors.YELLOW : MyColors.LIGHT_GREEN;
        }
        if (raceToUse.id() == Race.DARK_ELF.id()) {
            return MyRandom.flipCoin() ? MyColors.GRAY_RED : MyColors.GRAY;
        }
        if (raceToUse.id() == Race.HALFLING.id()) {
            return MyRandom.flipCoin() ? MyColors.GOLD : MyColors.ORANGE;
        }
        if (raceToUse.id() == Race.DWARF.id()) {
            return MyRandom.flipCoin() ? MyColors.PURPLE : MyColors.LIGHT_GRAY;
        }
        if (raceToUse.id() == Race.HALF_ORC.id()) {
            return MyRandom.flipCoin() ? MyColors.TAN : MyColors.DARK_GREEN;
        }
        return MyColors.RED;
    }
}
