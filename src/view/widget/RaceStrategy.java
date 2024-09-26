package view.widget;

import model.characters.GameCharacter;
import model.races.Race;
import util.MyStringFunction;
import view.MyColors;

import java.util.Map;

public class RaceStrategy extends PieChartStrategy<Race> {
    public RaceStrategy(Map<String, MyColors> colorTable, MyStringFunction<Race> nameGetter) {
        super(colorTable, nameGetter);
    }

    @Override
    public Race getInner(GameCharacter gc) {
        return gc.getRace();
    }
}
