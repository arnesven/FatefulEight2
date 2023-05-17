package model.enemies;

import model.Model;
import model.classes.Classes;
import model.combat.CombatLoot;
import model.combat.PersonCombatLoot;
import model.races.Race;
import view.sprites.Sprite;

public class MurdererEnemy extends Enemy {
    private static Sprite avatar = Classes.ASN.getAvatar(Race.DWARF, null);

    public MurdererEnemy(char a) {
        super(a, "Murderer");
    }

    @Override
    public int getMaxHP() {
        return 10;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    protected Sprite getSprite() {
        return avatar;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }
}
