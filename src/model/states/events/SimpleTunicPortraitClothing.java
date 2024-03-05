package model.states.events;

import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.PortraitClothing;
import model.classes.Looks;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class SimpleTunicPortraitClothing implements PortraitClothing {
    @Override
    public void putClothesOn(CharacterAppearance appearance) {
        Looks.putOnTunic(appearance, MyColors.BEIGE);
    }

    @Override
    public void finalizeLook(CharacterAppearance appearance) {

    }

    @Override
    public boolean showFacialHair() {
        return true;
    }

    @Override
    public boolean coversEars() {
        return false;
    }

    @Override
    public boolean hasSpecialAvatar() {
        return false;
    }

    @Override
    public AvatarSprite makeAvatar(Race race, CharacterAppearance appearance) {
        return null;
    }
}
