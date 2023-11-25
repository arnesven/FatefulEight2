package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class BeardAndMustache extends ShaggyBeard {
    private final BigMustache mustache;
    public BeardAndMustache(MyColors color) {
        super(color);
        mustache = new BigMustache(color);
    }

    @Override
    public void apply(AdvancedAppearance appearance, Race race) {
        super.apply(appearance, race);
        mustache.apply(appearance, race);
    }
}
