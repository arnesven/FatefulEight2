package model.enemies;

import model.characters.appearance.HairStyle;
import model.characters.appearance.SwimAttire;
import model.races.Race;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class RowdyGirlEnemy extends RowdyEnemyEnemy {
    private final RowdyGirlSprite sprite;

    public RowdyGirlEnemy(char a) {
        super(a, "Rowdy Girl");
        sprite = new RowdyGirlSprite(SwimAttire.randomSwimSuitColor());
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public String getDeathSound() {
        return "female_scream";
    }

    private static class RowdyGirlSprite extends LoopingSprite {

        public RowdyGirlSprite(MyColors color) {
            super("rowdygirl", "enemies.png", 0xE8, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(color);
            setColor3(MyRandom.sample(Race.getAllRaces()).getColor());
            setColor4(HairStyle.randomHairColor());
            setFrames(4);
        }
    }
}
