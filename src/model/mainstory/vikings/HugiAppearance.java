package model.mainstory.vikings;

import model.characters.appearance.*;
import model.races.Race;
import model.races.SlenderShoulders;
import view.MyColors;

public class HugiAppearance extends AdvancedAppearance {
    public HugiAppearance() {
        super(Race.HALF_ORC, true, MyColors.GRAY, 2, 4, new ElfinEyes(),
                new CurlyHair(), new NoBeard());
        setShoulders(new SlenderShoulders(true));
        setSpecificClothing(new SwimAttire(MyColors.DARK_GREEN));
        setNeck(new SlenderNeck());
    }
}
