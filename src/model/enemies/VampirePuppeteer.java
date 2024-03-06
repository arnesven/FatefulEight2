package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.enemies.behaviors.MixedAttackBehavior;
import model.races.Race;
import model.states.CombatEvent;
import model.states.GameState;
import util.MyRandom;
import util.MyStrings;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.ArrayList;


public class VampirePuppeteer extends UndeadEnemy {
    private static final Sprite SPRITE = new VampirePuppeteerSprite();

    public VampirePuppeteer(char a) {
        super(a, "Vampire Puppeteer", new MixedAttackBehavior());
        setFortified(true);
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
        return 5;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }

    @Override
    public void doUponDeath(Model model, CombatEvent combatEvent, GameCharacter killer) {
        boolean stillPuppeteer = false;
        for (Enemy e : combatEvent.getEnemies()) {
            if (e instanceof VampirePuppeteer) {
                stillPuppeteer = true;
                break;
            }
        }
        int count = 0;
        for (Enemy e : new ArrayList<>(combatEvent.getEnemies())) {
            if (e instanceof ThrallEnemy) {
                if (!stillPuppeteer || MyRandom.flipCoin()) {
                    count++;
                    combatEvent.retreatEnemy(e);
                }
            }
        }
        if (count > 0) {
            combatEvent.println("As the spell of the vampire puppeteer dissipates, " +
                    "the thralls seem to wake up from a deep slumber. " +
                    "Now " + MyStrings.numberWord(count) + " thralls retreat from combat!");
        }
    }

    private static class VampirePuppeteerSprite extends LoopingSprite {
        public VampirePuppeteerSprite() {
            super("vamppuppeteer", "enemies.png", 0xDC, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_PURPLE);
            setColor3(Race.HIGH_ELF.getColor());
            setColor4(MyColors.DARK_RED);
        }
    }

    @Override
    public String getStatus() {
        String extra = "";
        if (isFortified()) {
            extra = ", Unreachable";
        }
        return super.getStatus() + extra;
    }

    @Override
    public void conditionsEndOfCombatRoundTrigger(Model model, GameState state) {
        super.conditionsEndOfCombatRoundTrigger(model, state);
        if (state instanceof CombatEvent) {
            int thrallCount = 0;
            for (Enemy e : ((CombatEvent) state).getEnemies()) {
                if (e instanceof ThrallEnemy) {
                    thrallCount++;
                }
            }
            setFortified(thrallCount > 7);
        }
    }
}
