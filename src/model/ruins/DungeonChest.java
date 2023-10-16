package model.ruins;

import model.Model;
import model.classes.Skill;
import model.combat.CombatLoot;
import model.combat.CombinedLoot;
import model.combat.MonsterCombatLoot;
import model.combat.PersonCombatLoot;
import model.items.Lockpick;
import model.states.ExploreRuinsState;
import sound.SoundEffects;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class DungeonChest extends CenterDungeonObject {
    private static final Sprite32x32 CHEST_OPEN = new Sprite32x32("chestopen", "dungeon.png", 0x42,
            MyColors.BLACK, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BROWN);
    private static final Sprite32x32 CHEST_CLOSED = new Sprite32x32("chestclosed", "dungeon.png", 0x41,
            MyColors.BLACK, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BROWN);
    private static final Sprite32x32 BIG_CHEST_OPEN = new Sprite32x32("bigchestopen", "dungeon.png", 0x52,
            MyColors.BLACK, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BROWN);
    public static final Sprite32x32 BIG_CHEST_CLOSED = new Sprite32x32("bigchestclosed", "dungeon.png", 0x51,
            MyColors.BLACK, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BROWN);
    private static final int BIG_CHEST_LOOTS = 7;
    private static final int SMALL_CHEST_LOOTS = 3;

    private int loots;
    private boolean isLocked;
    private boolean alreadyTried = false;

    private boolean opened = false;

    public DungeonChest(Random random) {
        this.isLocked = random.nextDouble() > 0.5;
        this.loots = random.nextBoolean() ? BIG_CHEST_LOOTS : SMALL_CHEST_LOOTS;
    }

    @Override
    protected Sprite getSprite() {
        if (loots == BIG_CHEST_LOOTS) {
            if (opened) {
                return BIG_CHEST_OPEN;
            }
            return BIG_CHEST_CLOSED;
        }
        if (opened) {
            return CHEST_OPEN;
        }
        return CHEST_CLOSED;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(getSprite().getName(), new Point(xPos, yPos), getSprite());
    }

    @Override
    public String getDescription() {
        return "A chest";
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        if (isLocked) {
            if (alreadyTried) {
                model.getParty().randomPartyMemberSay(model, List.of(
                        "I really want to know what's in there.",
                        "It sucks that we can't get it open.",
                        "Did anyone see a key?"));
            } else {
                alreadyTried = true;
                int difficulty = Lockpick.askToUseLockpick(model, state, MyRandom.randInt(5, 7));
                boolean didUnlock = model.getParty().doSoloSkillCheck(model, state, Skill.Security, difficulty);
                if (didUnlock) {
                    isLocked = false;
                    SoundEffects.playUnlock();
                }
            }
        }

        if (isLocked) {
            return;
        }

        if (!opened) {
            opened = true;
            CombinedLoot combinedLoot = new CombinedLoot();
            for (int i = 0; i < loots; ++i) {
                CombatLoot loot = null;
                if (MyRandom.randInt(2) == 0) {
                    loot = new PersonCombatLoot(model);
                } else {
                    loot = new MonsterCombatLoot(model);
                }
                combinedLoot.add(loot);
            }
            combinedLoot.giveYourself(model.getParty());
            state.println("The chest opens... You found " + combinedLoot.getText() + ".");
            SoundEffects.playSound("chestopen");
        } else {
            state.println("You've already opened the chest.");
        }
    }
}
