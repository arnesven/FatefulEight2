package model.enemies;

import model.Model;
import model.characters.appearance.DefaultAppearance;
import model.classes.Classes;
import model.combat.CombatLoot;
import model.combat.NoCombatLoot;
import model.races.Race;
import view.sprites.Sprite;

public class InterloperEnemy extends HumanoidEnemy {
    private static Sprite avatar = Classes.BRD.getAvatar(Race.NORTHERN_HUMAN, new DefaultAppearance());

    public InterloperEnemy(char a) {
        super(a, "Interloper");
    }

    @Override
    public int getMaxHP() {
        return 4;
    }

    @Override
    public int getSpeed() {
        return 3;
    }

    @Override
    protected Sprite getSprite() {
        return avatar;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}
