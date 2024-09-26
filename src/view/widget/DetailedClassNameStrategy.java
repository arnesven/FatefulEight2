package view.widget;

import model.classes.CharacterClass;
import model.classes.Classes;
import util.MyLists;
import util.MyStringFunction;
import view.MyColors;

import java.util.List;
import java.util.Map;

public class DetailedClassNameStrategy extends ClassStrategy {

    private static final Map<String, MyColors> DETAILED_COLORS = Map.of(
            "H. Fighter", MyColors.RED,
            "L. Fighter", MyColors.PEACH,
            "Rogue", MyColors.WHITE,
            "Talker", MyColors.YELLOW,
            "Mage", MyColors.LIGHT_GREEN,
            "Other", MyColors.BLACK);

    private static final Map<String, List<CharacterClass>> DETAILED_NAMES = Map.of(
            "H. Fighter",
            List.of(Classes.BBN, Classes.BKN, Classes.CAP, Classes.MIN, Classes.PAL),
            "L. Fighter",
            List.of(Classes.AMZ, Classes.FOR, Classes.MAR, Classes.ART),
            "Rogue",
            List.of(Classes.ASN, Classes.SPY, Classes.THF, Classes.BRD),
            "Talker",
            List.of(Classes.MAG, Classes.PRI, Classes.NOB),
            "Mage",
            List.of(Classes.DRU, Classes.WIZ, Classes.WIT, Classes.SOR));

    private static String getDetailedName(CharacterClass charClass) {
        for (Map.Entry<String, List<CharacterClass>> entry : DETAILED_NAMES.entrySet()) {
            if (MyLists.any(entry.getValue(), cc -> cc.getFullName().equals(charClass.getFullName()))) {
                return entry.getKey();
            }
        }
        return getOtherString();
    }

    public DetailedClassNameStrategy() {
        super(DETAILED_COLORS, DetailedClassNameStrategy::getDetailedName);
    }
}
