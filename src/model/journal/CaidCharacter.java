package model.journal;

import model.characters.GameCharacter;
import model.characters.appearance.*;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Equipment;
import model.items.accessories.ComfyShoes;
import model.items.clothing.LeatherArmor;
import model.items.weapons.Katana;
import model.races.Race;
import view.MyColors;
import view.party.CharacterCreationView;

public class CaidCharacter extends GameCharacter {
    public CaidCharacter() {
        super("Caid", "Sanchez", Race.NORTHERN_HUMAN, Classes.SWORD_MASTER,
                new CaidAppearance(), new CharacterClass[]{Classes.SWORD_MASTER, Classes.None, Classes.None, Classes.None},
                new Equipment(new Katana(), new LeatherArmor(), new ComfyShoes()));
    }

    private static class CaidAppearance extends AdvancedAppearance {
        public CaidAppearance() {
            super(Race.NORTHERN_HUMAN, false, MyColors.BEIGE, CharacterCreationView.mouthSet[4],
                    CharacterCreationView.noseSet[5], CharacterEyes.allEyes[2], HairStyle.allHairStyles[25],
                    Beard.allBeards[22]);
            setFaceDetail(new HeadBandDetail());
            setDetailColor(MyColors.DARK_RED);
        }
    }
}
