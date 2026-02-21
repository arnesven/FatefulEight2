package model.classes.npcs;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.BowInHandDetail;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.PitchForkInHandDetail;
import model.classes.Looks;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class FarmerNPCClass extends NPCClass {
    private PitchForkInHandDetail pitchFork;

    public FarmerNPCClass() {
        super("Farmer");
        pitchFork = new PitchForkInHandDetail();
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnLooseShirt(characterAppearance, MyColors.BEIGE);
        Looks.putOnFarmersHat(characterAppearance, MyColors.BROWN);
        if (characterAppearance instanceof AdvancedAppearance) {
            pitchFork.applyYourself((AdvancedAppearance) characterAppearance,
                    characterAppearance.getRace());
        }
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x220, MyColors.BEIGE, race.getColor(),
                appearance.getNormalHair(), appearance.getFullBackHair());
    }

    @Override
    public boolean coversEyebrows() {
        return true;
    }
}
