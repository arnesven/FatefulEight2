package view.sprites;

import model.characters.appearance.CharacterAppearance;
import model.races.Race;
import view.MyColors;

public class LimitedAvatarSprite extends AvatarSprite {
    public LimitedAvatarSprite(Race race, int num, MyColors color2, MyColors color3, MyColors color4, Sprite normalHair) {
        super(race, num, color2, color3, color4, normalHair, CharacterAppearance.noHair());
    }

    @Override
    protected int getDeadSpriteOffset() {
        return 3;
    }
}
