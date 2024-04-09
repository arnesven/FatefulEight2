package model.characters.appearance;

import model.races.Race;
import view.sprites.AvatarSprite;

public class NoClothing implements PortraitClothing {
    @Override
    public void putClothesOn(CharacterAppearance appearance) { }

    @Override
    public void finalizeLook(CharacterAppearance appearance) { }

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
