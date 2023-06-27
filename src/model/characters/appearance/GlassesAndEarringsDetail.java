package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class GlassesAndEarringsDetail extends FaceDetail {
    private GlassesDetail glasses = new GlassesDetail();
    private EarringsDetail earrings = new EarringsDetail();

    public GlassesAndEarringsDetail() {
        super("G/E");
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race, boolean coversEars) {
        glasses.applyYourself(appearance, race, coversEars);
        earrings.applyYourself(appearance, race, coversEars);
    }

    @Override
    public void setColor(MyColors color) {
        glasses.setColor(color);
        earrings.setColor(color);
    }
}
