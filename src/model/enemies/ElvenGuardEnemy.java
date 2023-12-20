package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.PersonCombatLoot;
import model.races.AllRaces;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class ElvenGuardEnemy extends HumanoidEnemy {
    private static final Sprite SPRITE = new ElvenGuardSprite();

    public ElvenGuardEnemy(char a) {
        super(a, "Elven Guard");
    }

    @Override
    public int getMaxHP() {
        return 4;
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
        return 5;
    }

    @Override
    public int getDamageReduction() {
        return 1;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }

    private static class ElvenGuardSprite extends LoopingSprite {
        public ElvenGuardSprite() {
            super("elvenguard", "enemies.png", 0xBB, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.WHITE);
            setColor3(AllRaces.HIGH_ELF.getColor());
            setColor4(MyColors.GRAY);
            setFrames(4);
        }
    }
}
