package model.enemies;

import model.Model;
import model.characters.WitchKingAppearance;
import model.classes.Classes;
import model.combat.BossCombatLoot;
import model.combat.CombatLoot;
import model.combat.StandardCombatLoot;
import model.enemies.behaviors.MultiMagicRangedAttackBehavior;
import model.races.Race;
import util.MyRandom;
import view.sprites.Sprite;

public class QuadSpiritEnemy extends Enemy {
    private static final Sprite SPRITE = Classes.WITCH_KING.getAvatar(Race.WITCH_KING, new WitchKingAppearance());

    public QuadSpiritEnemy(char c) {
        super(c, "Spirit of the Quad");
        setAttackBehavior(new MultiMagicRangedAttackBehavior(5));
    }

    @Override
    public int getMaxHP() {
        return 33;
    }

    @Override
    public int getDamageReduction() {
        return 2;
    }

    @Override
    public int getSpeed() {
        return 8;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new FinalBossCombatLoot(model);
    }

    private static class FinalBossCombatLoot extends StandardCombatLoot {
        public FinalBossCombatLoot(Model model) {
            super(model);
            for (int i = 5; i > 0; --i) {
                getItems().add(model.getItemDeck().getRandomItem(0.95));
                setGold(MyRandom.rollD10() + getGold());
            }
        }
    }
}
