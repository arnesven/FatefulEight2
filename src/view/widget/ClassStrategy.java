package view.widget;

import model.characters.GameCharacter;
import model.classes.CharacterClass;
import util.MyLists;
import util.MyStringFunction;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ClassStrategy extends PieChartStrategy<CharacterClass> {
    private final String name;

    public ClassStrategy(String name, Map<String, MyColors> colorTable, MyStringFunction<CharacterClass> nameGetter) {
        super(colorTable, nameGetter);
        this.name = name;
    }

    @Override
    public CharacterClass getInner(GameCharacter gc) {
        return gc.getCharClass();
    }

    public abstract List<String> getDescription();

    protected static List<String> makeDescription(Map<String, List<CharacterClass>> nameMap) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, List<CharacterClass>> entry : nameMap.entrySet()) {
            result.add(entry.getKey() + ": " + MyLists.commaAndJoin(entry.getValue(), CharacterClass::getShortName));
        }
        return result;
    }

    public String getName() {
        return name;
    }
}
