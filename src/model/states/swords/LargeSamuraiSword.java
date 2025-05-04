package model.states.swords;

import model.items.weapons.DaiKatana;
import model.mainstory.honorable.ShingenWeapon;
import view.MyColors;

public class LargeSamuraiSword extends SamuraiSword {
    public LargeSamuraiSword(MyColors color, boolean inscription) {
        super(color, inscription, new DaiKatana(), 1);
    }

    @Override
    public int getCursorOffset() {
        return 0;
    }

    @Override
    public boolean matchesWeaponType(ShingenWeapon shingenWeapon) {
        return shingenWeapon == ShingenWeapon.DaiKatana;
    }
}
