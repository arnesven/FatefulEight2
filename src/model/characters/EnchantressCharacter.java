package model.characters;

import model.characters.appearance.*;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class EnchantressCharacter extends GameCharacter {
    public EnchantressCharacter() {
        super("Enchantress", "", Race.WOOD_ELF, Classes.ENCHANTRESS, new EnchantressAppearance(),
                new CharacterClass[]{Classes.ENCHANTRESS, Classes.None, Classes.None, Classes.None});
    }

    private static class EnchantressAppearance extends AdvancedAppearance {
        public EnchantressAppearance() {
            super(Race.WOOD_ELF, true, MyColors.PEACH, CharacterCreationView.mouthSet[1],
                    CharacterCreationView.noseSet[1], CharacterEyes.allEyes[8],
                    HairStyle.allHairStyles[1], Beard.allBeards[10]);
            setHasEarrings(true);
            setDetailColor(MyColors.LIGHT_GREEN);
        }
    }

    @Override
    public boolean canChangeClothing() {
        return false;
    }
}