package view.widget;

import model.characters.GameCharacter;
import util.MyStringFunction;
import view.MyColors;

import java.util.Map;

public abstract class PieChartStrategy<E> {
    private final Map<String, MyColors> colorTable;
    private final MyStringFunction<E> nameGetter;

    public PieChartStrategy(Map<String, MyColors> colorTable, MyStringFunction<E> nameGetter) {
        this.colorTable = colorTable;
        this.nameGetter = nameGetter;
    }

    public abstract E getInner(GameCharacter gc);

    public static String getOtherString() {
        return "Other";
    }

    public Map<String, MyColors> getColorTable() {
        return colorTable;
    }

    public MyStringFunction<E> getNameGetter() {
        return nameGetter;
    }
}
