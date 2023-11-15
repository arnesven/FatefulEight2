package model.characters.appearance;

import model.races.Race;
import view.sprites.AvatarSprite;

public interface PortraitClothing {
    void putClothesOn(CharacterAppearance appearance);

    void finalizeLook(CharacterAppearance appearance);

    boolean showFacialHair();

    boolean coversEars();

    boolean hasSpecialAvatar();

    AvatarSprite makeAvatar(Race race, CharacterAppearance appearance);
}
