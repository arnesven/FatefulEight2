package model.states.swords;

import model.items.weapons.Wakizashi;
import model.mainstory.honorable.ShingenWeapon;
import view.MyColors;

public class SmallSamuraiSword extends SamuraiSword {
    public SmallSamuraiSword(MyColors color, boolean inscription) {
        super(color, inscription, new Wakizashi(), 2);
    }

    @Override
    public int getCursorOffset() {
        return 5;
    }

    @Override
    public boolean matchesWeaponType(ShingenWeapon shingenWeapon) {
        return shingenWeapon == ShingenWeapon.Wakisashis;
    }
}
