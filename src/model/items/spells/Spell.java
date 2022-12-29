package model.items.spells;

import model.Inventory;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.map.CastleLocation;
import model.states.GameState;
import view.MyColors;

public abstract class Spell extends Item {
    private final MyColors color;
    private final int difficulty;
    private final int hpCost;

    public Spell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost);
        this.color = color;
        this.difficulty = difficulty;
        this.hpCost = hpCost;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    public MyColors getColor() {
        return color;
    }

    public boolean castYourself(Model model, GameState state, GameCharacter caster) {
        state.println(caster.getName() + " tries to cast " + getName() + "...");
        caster.addToHP(-hpCost);
        if (caster.isDead()) {
            state.println(caster.getFirstName() + " was killed by the effect of the spell!");
            return false;
        }
        SkillCheckResult result = caster.testSkill(getSkillForColor(color), difficulty);
        if (result.isSuccessful()) {
            state.println(getName() + " was successfully cast (" + result.asString() + ")");
        } else {
            state.println(getName() + " failed (" + result.asString() + ")");
        }
        return result.isSuccessful();
    }

    private static Skill getSkillForColor(MyColors color) {
        switch (color) {
            case BLACK:
                return Skill.MagicBlack;
            case BLUE:
                return Skill.MagicBlue;
            case RED:
                return Skill.MagicRed;
            case GREEN:
                return Skill.MagicGreen;
            case WHITE:
                return Skill.MagicWhite;
            default:
                throw new IllegalStateException("Unrecognized magic color " + color.toString());
        }
    }
}
