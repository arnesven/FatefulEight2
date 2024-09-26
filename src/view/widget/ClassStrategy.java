package view.widget;

import model.characters.GameCharacter;
import model.classes.CharacterClass;
import util.MyStringFunction;
import view.MyColors;

import java.util.Map;

public class ClassStrategy extends PieChartStrategy<CharacterClass> {
    public ClassStrategy(Map<String, MyColors> colorTable, MyStringFunction<CharacterClass> nameGetter) {
        super(colorTable, nameGetter);
    }

    @Override
    public CharacterClass getInner(GameCharacter gc) {
        return gc.getCharClass();
    }
}
