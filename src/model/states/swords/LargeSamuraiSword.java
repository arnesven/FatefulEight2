package model.states.swords;

import model.items.weapons.DaiKatana;
import view.MyColors;

public class LargeSamuraiSword extends SamuraiSword {
    public LargeSamuraiSword(MyColors color, boolean inscription) {
        super(color, inscription, new DaiKatana(), 1);
    }

    @Override
    public int getCursorOffset() {
        return 0;
    }
}
