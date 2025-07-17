package model.characters.appearance;

import model.classes.Looks;
import model.races.Race;
import util.MyRandom;
import view.MyColors;
import view.sprites.AvatarSprite;

import java.util.List;

public class SwimAttire implements PortraitClothing {

    private final MyColors swimsuitColor;

    public SwimAttire(MyColors swimsuitColor) {
        this.swimsuitColor = swimsuitColor;
    }

    public SwimAttire() {
        this(randomSwimSuitColor());
    }

    @Override
    public void putClothesOn(CharacterAppearance appearance) {
        if (appearance.getGender()) {
            Looks.putOnHideLeft(appearance, swimsuitColor);
            Looks.putOnHideRight(appearance, swimsuitColor);
        }
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
        return true;
    }

    @Override
    public AvatarSprite makeAvatar(Race race, CharacterAppearance appearance) {
        if (!appearance.getGender()) {
            return new AvatarSprite(race, 0x2A0, swimsuitColor, race.getColor(), appearance.getNormalHair(), appearance.getFullBackHair());
        }
        return new AvatarSprite(race, 0x280, swimsuitColor, race.getColor(), appearance.getNormalHair(), appearance.getFullBackHair());
    }

    public static MyColors randomSwimSuitColor() {
        return MyRandom.sample(List.of(MyColors.PURPLE, MyColors.GREEN, MyColors.BLUE,
                MyColors.YELLOW, MyColors.RED, MyColors.BEIGE, MyColors.DARK_GRAY));
    }
}
