package model.enemies;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.enemies.behaviors.BleedAttackBehavior;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;

public class WerewolfEnemy extends BeastEnemy {
    private static final Sprite SPRITE = new AvatarSprite(Race.NORTHERN_HUMAN, 0x1A8,
            MyColors.DARK_GRAY, Race.NORTHERN_HUMAN.getColor(),
            MyColors.LIGHT_GRAY, CharacterAppearance.noHair(), CharacterAppearance.noHair());

    public WerewolfEnemy(char a) {
        super(a, "Werewolf", HOSTILE, new BleedAttackBehavior(5));
    }

    @Override
    public int getMaxHP() {
        return 12;
    }

    @Override
    public int getSpeed() {
        return 6;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }
}
