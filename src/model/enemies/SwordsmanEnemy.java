package model.enemies;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.DefaultAppearance;
import model.combat.loot.CombatLoot;
import model.combat.loot.StandardCombatLoot;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;

public class SwordsmanEnemy extends HumanoidEnemy {
    private final Sprite sprite;

    public SwordsmanEnemy(char a, Race race) {
        super(a, "Swordsman");
        sprite = new AvatarSprite(race, 0x120, MyColors.GRAY_RED, race.getColor(), race.getColor(),
                new DefaultAppearance().getNormalHair(), CharacterAppearance.noHair());
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

    private static class SwordsmanCombatLoot extends StandardCombatLoot {
        public SwordsmanCombatLoot(Model model) {
            super(model);
        }

        @Override
        public int getGold() {
            return super.getGold() + 10;
        }
    }
}
