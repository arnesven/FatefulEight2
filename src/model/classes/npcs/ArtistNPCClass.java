package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.FancyLongSleeve;
import view.MyColors;

public class ArtistNPCClass extends NPCClass {
    private static final MyColors CLOTHES_COLOR = MyColors.CYAN;
    private static final MyColors DETAIL_COLOR = MyColors.ORANGE;

    public ArtistNPCClass() {
        super("Artisan");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnFancyRobe(characterAppearance, CLOTHES_COLOR, DETAIL_COLOR);
        Looks.putOnFancyHat(characterAppearance, MyColors.BLUE, MyColors.CYAN, MyColors.BEIGE);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new FancyLongSleeve(CLOTHES_COLOR, DETAIL_COLOR);
    }
}
