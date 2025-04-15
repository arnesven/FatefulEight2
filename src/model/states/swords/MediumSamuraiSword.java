package model.states.swords;

import model.items.weapons.Katana;
import view.MyColors;

public class MediumSamuraiSword extends SamuraiSword {
    public MediumSamuraiSword(MyColors color, boolean inscription) {
        super(color, inscription, new Katana(), 0);
    }

    @Override
    public int getCursorOffset() {
        return 2;
    }
}
