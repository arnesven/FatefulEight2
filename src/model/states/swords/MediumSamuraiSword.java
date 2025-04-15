package model.states.swords;

import model.items.weapons.Katana;
import view.MyColors;

public class MediumSamuraiSword extends SamuraiSword {
    public MediumSamuraiSword(MyColors color, boolean inscription) {
        super("Katana", color, inscription, new Katana(), 0);
    }
}
