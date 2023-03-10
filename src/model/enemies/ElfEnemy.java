package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.StandardCombatLoot;
import model.races.Race;
import view.sprites.ElfEnemySprite;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class ElfEnemy extends Enemy {
    private static final LoopingSprite elfSprite = new ElfEnemySprite(Race.WOOD_ELF.getColor());

    public ElfEnemy(char a) {
        super(a, "Elf");
    }

    @Override
    public int getMaxHP() {
        return 3;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    protected Sprite getSprite() {
        return elfSprite;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new StandardCombatLoot(model);
    }
}
