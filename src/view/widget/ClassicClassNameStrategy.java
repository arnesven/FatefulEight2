package view.widget;

import model.classes.CharacterClass;
import model.classes.Classes;
import util.MyLists;
import view.MyColors;

import java.util.List;
import java.util.Map;

public class ClassicClassNameStrategy extends ClassStrategy {

    private static final Map<String, MyColors> CLASSIC_COLORS = Map.of(
                "Fighting", MyColors.TAN,
                "Stealth", MyColors.LIGHT_GRAY,
                "Magic", MyColors.YELLOW,
                "Other", MyColors.BLACK);

    private static final Map<String, List<CharacterClass>> CLASSIC_TYPE_NAMES = Map.of(
            "Fighting",
            List.of(Classes.AMZ, Classes.BBN, Classes.BKN, Classes.CAP, Classes.FOR, Classes.MIN, Classes.PAL),
            "Stealth",
            List.of(Classes.ASN, Classes.SPY, Classes.THF, Classes.BRD, Classes.NOB, Classes.ART, Classes.MAR),
            "Magic",
            List.of(Classes.DRU, Classes.MAG, Classes.PRI, Classes.WIZ, Classes.WIT, Classes.SOR));

    private static String getClassicClassTypeName(CharacterClass charClass) {
        for (Map.Entry<String, List<CharacterClass>> entry : CLASSIC_TYPE_NAMES.entrySet()) {
            if (MyLists.any(entry.getValue(), cc -> cc.getFullName().equals(charClass.getFullName()))) {
                return entry.getKey();
            }
        }
        return getOtherString();
    }

    public ClassicClassNameStrategy() {
        super(CLASSIC_COLORS, ClassicClassNameStrategy::getClassicClassTypeName);
    }

}
