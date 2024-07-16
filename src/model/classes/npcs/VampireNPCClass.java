package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Looks;
import model.classes.normal.NobleClass;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class VampireNPCClass extends NPCClass {
    private static final MyColors CLOTHES_COLOR = MyColors.DARK_PURPLE;
    private static final MyColors DETAIL_COLOR = MyColors.DARK_RED;

    public VampireNPCClass() {
        super("Vampire");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnFancyRobe(characterAppearance, CLOTHES_COLOR, DETAIL_COLOR);
        NobleClass.putOnCrown(characterAppearance);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return Classes.NOB.getAvatar(race, appearance);
    }
}
