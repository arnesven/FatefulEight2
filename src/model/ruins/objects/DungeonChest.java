package model.ruins.objects;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.CombinedLoot;
import model.combat.loot.MonsterCombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.ruins.themes.DungeonTheme;
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
    public static final Sprite32x32 CHEST_OPEN = new Sprite32x32("chestopen", "dungeon.png", 0x42,
            MyColors.BLACK, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BROWN);
    public static final Sprite32x32 CHEST_CLOSED = new Sprite32x32("chestclosed", "dungeon.png", 0x41,
            MyColors.BLACK, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.BROWN);
    public static final Sprite32x32 BIG_CHEST_OPEN = new Sprite32x32("bigchestopen", "dungeon.png", 0x52,
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
    protected Sprite getSprite(DungeonTheme theme) {
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
    public void drawYourself(Model model, int xPos, int yPos, DungeonTheme theme) {
        model.getScreenHandler().register(getSprite(theme).getName(), new Point(xPos, yPos), getSprite(theme));
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
                boolean didUnlock = model.getParty().doSoloLockpickCheck(model, state, MyRandom.randInt(5, 7));
                if (didUnlock) {
                    unlockYourself();
                }
            }
        }

        if (isLocked) {
            return;
        }

        if (!opened) {
            openYourself();
            CombatLoot loot = getLoot(model);
            loot.giveYourself(model.getParty());
            state.println("The chest opens... You found " + loot.getText() + ".");
            SoundEffects.playSound("chestopen");
        } else {
            state.println("You've already opened the chest.");
        }
    }

    protected CombatLoot getLoot(Model model) {
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
        return combinedLoot;
    }

    public void openYourself() {
        opened = true;
    }

    public void unlockYourself() {
        setLocked(false);
        SoundEffects.playUnlock();
    }

    protected void setLocked(boolean b) {
        this.isLocked = b;
    }

    public boolean isOpen() {
        return opened;
    }
}
