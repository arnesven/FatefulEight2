package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class FullBeard extends Beard {

    private final Beard inner = new BeardAndMustache(MyColors.BLACK);

    public FullBeard() {
        super(4, 0x40);
    }

    @Override
    public void apply(AdvancedAppearance advancedAppearance, Race race) {
        super.apply(advancedAppearance, race);
        inner.apply(advancedAppearance, race);
    }
}
