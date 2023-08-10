package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.races.HighElf;
import model.races.Race;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.List;

public class VampirePuppeteer extends UndeadEnemy {
    private static final Sprite SPRITE = new VampirePuppeteerSprite();

    public VampirePuppeteer(char a) {
        super(a, "Vampire Puppeteer");
    }

    @Override
    public int getMaxHP() {
        return 0;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return null;
    }

    private static class VampirePuppeteerSprite extends LoopingSprite {
        public VampirePuppeteerSprite() {
            super("vamppuppeteer", "enemies.png", 0xDC, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_PURPLE);
            setColor2(Race.HIGH_ELF.getColor());
            setColor4(MyColors.DARK_RED);
        }
    }
}
