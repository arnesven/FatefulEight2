package model.enemies;

import model.Model;
import model.characters.appearance.DefaultAppearance;
import model.classes.Classes;
import model.combat.conditions.RowdyCondition;
import model.combat.loot.CombatLoot;
import model.combat.loot.NoCombatLoot;
import model.races.Race;
import view.sprites.Sprite;

public class InterloperEnemy extends HumanoidEnemy {
    private static Sprite avatar = Classes.BRD.getAvatar(Race.NORTHERN_HUMAN, new DefaultAppearance());

    public InterloperEnemy(char a) {
        super(a, "Interloper");
        addCondition(new RowdyCondition());
    }

    @Override
    public int getMaxHP() {
        return 3;
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
