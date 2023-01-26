package model.enemies;

import model.Model;
import model.characters.appearance.DefaultAppearance;
import model.combat.CombatLoot;
import model.combat.StandardCombatLoot;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;

public class SwordsmanEnemy extends Enemy {
    private final Sprite sprite;

    public SwordsmanEnemy(char a, Race race) {
        super(a, "Swordsman");
        sprite = new AvatarSprite(race, 0x07, MyColors.GRAY_RED, new DefaultAppearance().getNormalHair());
    }

    @Override
    public int getMaxHP() {
        return 6;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new SwordsmanCombatLoot(model);
    }

    private class SwordsmanCombatLoot extends StandardCombatLoot {
        public SwordsmanCombatLoot(Model model) {
            super(model);
        }

        @Override
        public int getGold() {
            return super.getGold() + 10;
        }
    }
}
