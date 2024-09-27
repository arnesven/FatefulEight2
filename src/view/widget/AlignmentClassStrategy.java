package view.widget;

import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlignmentClassStrategy extends ClassStrategy {
    private static final Map<String, MyColors> COLOR_TABLE =
            Map.of("Shady", MyColors.RED,
                    "Good", MyColors.YELLOW,
                    "Evil", MyColors.BLACK,
                    "Neutral", MyColors.GRAY);

    private List<String> description;

    public AlignmentClassStrategy() {
        super("Alignmt", COLOR_TABLE, AlignmentClassStrategy::getAlignmentName);
        description = makeDescription();
    }

    public List<String> makeDescription() {
        Map<String, List<CharacterClass>> classSets = Map.of("Evil", new ArrayList<>(),
                "Shady", new ArrayList<>(), "Good", new ArrayList<>());
        for (CharacterClass cc : Classes.allClasses) {
            List<CharacterClass> list = classSets.get(getAlignmentName(cc));
            if (cc != Classes.None && list != null) {
                list.add(cc);
            }
        }
        List<String> result = makeDescription(classSets);
        result.add("Neutral: Others");
        return result;
    }

    public static String getAlignmentName(CharacterClass cls) {
        if (Classes.ALIGNMENT.containsKey(cls.id())) {
            int align = Classes.ALIGNMENT.get(cls.id());
            switch (align) {
                case -1:
                    return "Shady";
                case 1:
                    return "Good";
                case -2:
                    return "Evil";
            }
        }
        return "Neutral";
    }

    @Override
    public List<String> getDescription() {
        return description;
    }
}
