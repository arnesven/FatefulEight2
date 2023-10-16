package model.items;

import model.Model;
import model.states.GameState;
import model.states.QuestState;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Lockpick extends Item {
    public static final Sprite SPRITE = new ItemSprite(11, 11, MyColors.LIGHT_GRAY, MyColors.BEIGE);
    public static final String DETAIL_STRING = ", Greatly reduces the difficulty of picking locks.";
    private static final int SECURITY_DIFFICULTY_REDUCTION = 4;
    public static final int BREAK_CHANCE_SUCCESS = 10;
    public static final int BREAK_CHANCE_FAILURE = 70;

    public Lockpick() {
        super("Lockpick", 3);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return Inventory.WEIGHT_OF_LOCKPICKS;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.addToLockpicks(1);
    }

    @Override
    public String getShoppingDetails() {
        return DETAIL_STRING;
    }

    @Override
    public Item copy() {
        return new Lockpick();
    }

    @Override
    public String getSound() {
        return "unlock";
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }

    public static int askToUseLockpick(Model model, GameState state, int difficulty) {
        if (model.getParty().getInventory().getLockpicks() == 0) {
            return difficulty + SECURITY_DIFFICULTY_REDUCTION;
        }
        if (difficulty < 7) {
            state.print("The lock mechanism doesn't seem very complex");
        } else if (difficulty < 9) {
            state.print("The lock mechanism is of medium complexity");
        } else {
            state.print("The lock mechanism looks quite complex");
        }
        state.print(", do you want to use a lockpick on it? (Y/N) ");
        if (!state.yesNoInput()) {
            return difficulty + SECURITY_DIFFICULTY_REDUCTION;
        }
        return difficulty;
    }
    
    public static void checkForBreakage(Model model, GameState state, boolean skillCheckResult) {
        int target = 10 - ((skillCheckResult ? BREAK_CHANCE_SUCCESS : BREAK_CHANCE_FAILURE) / 10);
        if (MyRandom.rollD10() > target) {
            state.println("The lockpick broke!");
            model.getParty().getInventory().addToLockpicks(-1);
        }
    }
}
