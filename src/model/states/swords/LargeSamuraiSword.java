package model.states.swords;

import model.Model;
import model.items.weapons.DaiKatana;
import view.MyColors;
import view.sprites.Sprite;

public class LargeSamuraiSword extends SamuraiSword {
    public LargeSamuraiSword(MyColors color, boolean inscription) {
        super("Dai-Katana", color, inscription, new DaiKatana(), 1);
    }
}
