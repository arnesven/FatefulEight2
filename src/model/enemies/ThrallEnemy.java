package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.PersonCombatLoot;
import model.races.Race;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.HashMap;
import java.util.Map;

public class ThrallEnemy extends HumanoidEnemy {

    private static final Map<Race, Sprite> maleSprites = makeThrallSprites(0xD4);
    private static final Map<Race, Sprite> femaleSprites = makeThrallSprites(0xD8);

    private Sprite sprite;

    public ThrallEnemy(char b) {
        super(b, "Thrall");
        if (MyRandom.flipCoin()) {
            this.sprite = maleSprites.get(MyRandom.sample(Race.getAllRaces()));
        } else {
            this.sprite = femaleSprites.get(MyRandom.sample(Race.getAllRaces()));
        }
    }

    @Override
    public int getMaxHP() {
        return 4;
    }

    @Override
    public int getSpeed() {
        return 2;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return 1;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }

    private static Map<Race, Sprite> makeThrallSprites(int num) {
        Map<Race, Sprite> result = new HashMap<>();
        for (Race r : Race.getAllRaces()) {
            result.put(r, new ThrallSprite(num, r.getColor()));
        }
        return result;
    }

    private static class ThrallSprite extends LoopingSprite {
        public ThrallSprite(int num, MyColors color) {
            super("thrall"+num+color.name(), "enemies.png", num, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.TAN);
            setColor3(color);
            setColor4(MyColors.GRAY);
        }
    }
}
