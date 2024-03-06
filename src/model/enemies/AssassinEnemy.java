package model.enemies;

import model.Model;
import model.classes.Classes;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.enemies.behaviors.BleedAttackBehavior;
import util.MyRandom;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;

public class AssassinEnemy extends HumanoidEnemy {
    private final AvatarSprite sprite;

    public AssassinEnemy(char a) {
        super(a, "Assassin", new BleedAttackBehavior(4));
        this.sprite = Classes.ASN.getAvatar(new MyRandom().nextRace(), null);
    }

    @Override
    public int getMaxHP() {
        return 3;
    }

    @Override
    public int getSpeed() {
        return 8;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return 6;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }
}
