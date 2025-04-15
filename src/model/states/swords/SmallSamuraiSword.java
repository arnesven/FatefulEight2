package model.states.swords;

import model.items.weapons.Wakizashi;
import view.MyColors;

public class SmallSamuraiSword extends SamuraiSword {
    public SmallSamuraiSword(MyColors color, boolean inscription) {
        super("Wakizashi", color, inscription, new Wakizashi(), 2);
    }
}
