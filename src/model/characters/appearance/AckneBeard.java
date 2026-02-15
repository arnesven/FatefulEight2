package model.characters.appearance;

import model.races.Race;

public class AckneBeard extends Beard {
    public AckneBeard() {
        super(9, 0x00, false);
    }

    @Override
    public void apply(AdvancedAppearance advancedAppearance, Race race) {
        advancedAppearance.getSprite(2, 4).setColor2(race.getFreckleColor());
        advancedAppearance.getSprite(4, 4).setColor2(race.getFreckleColor());
    }
}
