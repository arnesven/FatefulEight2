package model.enemies;

import model.Model;
import model.characters.appearance.HairStyle;
import model.characters.appearance.SwimAttire;
import model.combat.CombatLoot;
import model.combat.NoCombatLoot;
import model.races.Race;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class RowdyBoyEnemy extends RowdyEnemyEnemy {

    private final RowdyBoySprite sprite;

    public RowdyBoyEnemy(char a) {
        super(a, "Rowdy Boy");
        sprite = new RowdyBoySprite(SwimAttire.randomSwimSuitColor());
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    private static class RowdyBoySprite extends LoopingSprite {

        public RowdyBoySprite(MyColors color) {
            super("rowdyboy", "enemies.png", 0xE4, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(color);
            setColor3(MyRandom.sample(Race.getAllRaces()).getColor());
            setColor4(HairStyle.randomHairColor());
            setFrames(4);
        }
    }
}
