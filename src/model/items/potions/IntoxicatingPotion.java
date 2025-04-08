package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.conditions.IntoxicatedCondition;
import model.races.Race;

public abstract class IntoxicatingPotion extends Potion {
    private static final int HEALING_AMOUNT = 3;
    private String dislikedBy;

    public abstract boolean doesReject(Model model, Race race);

    public IntoxicatingPotion(String name, int cost, String dislikedBy) {
        super(name, cost);
        this.dislikedBy = dislikedBy;
    }

    public String getTypicalGlass() {
        return "glass";
    }

    @Override
    public String getShoppingDetails() {
        return ", Restores " + HEALING_AMOUNT + " HP, may intoxicate character. Disliked by " + dislikedBy + ".";
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        if (doesReject(model, gc.getRace())) {
            return gc.getName() + " rejected the " + getName().toLowerCase() + "!";
        }

        String extra = "";
        if (!gc.testSkillHidden(Skill.Endurance, getStrength() + 2 - gc.getSP(), 0).isSuccessful()) {
            if (gc.hasCondition(IntoxicatedCondition.class)) {
                if (gc.getSP() == 0) {
                    if (gc.getHP() < 2) {
                        return "No effect";
                    }
                    gc.addToHP(-1);
                    return gc.getName() + " lost 1 health!";
                }
                gc.addToSP(-1);
                return  gc.getName() + " lost 1 stamina!";
            }
            gc.addCondition(new IntoxicatedCondition());
            IntoxicatedCondition.addSkillModifier(gc);
            extra = " " + gc.getName() + " has become intoxicated!";
        }
        int hpBefore = gc.getHP();
        gc.addToHP(HEALING_AMOUNT);
        return gc.getName() + " recovers " + (gc.getHP() - hpBefore) + " health." + extra;
    }

    protected int getStrength() {
        return 5;
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return !target.isDead();
    }

}
