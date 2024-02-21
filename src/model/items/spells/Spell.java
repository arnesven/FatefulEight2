package model.items.spells;

import model.items.Inventory;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.states.GameState;
import sound.SoundEffects;
import view.*;

public abstract class Spell extends Item {
    public static final MyColors[] spellColors = new MyColors[]{MyColors.WHITE, MyColors.RED, MyColors.BLUE, MyColors.GREEN, MyColors.BLACK};
    private final MyColors color;
    private final int difficulty;
    private final int hpCost;
    private boolean isCastFromScroll = false;

    public Spell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost);
        this.color = color;
        this.difficulty = difficulty;
        this.hpCost = hpCost;
    }

    @Override
    public boolean canBeUsedFromMenu() {
        return true;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    public MyColors getColor() {
        return color;
    }

    public final String getShoppingDetails() {
        return ", " + getDescription();
    }

    public abstract String getDescription();

    public boolean castYourself(Model model, GameState state, GameCharacter caster) {
        state.println(caster.getName() + " tries to cast " + getName() + "...");
        model.getTutorial().spells(model);
        int health = hpCost;
        if (caster.getEquipment().getAccessory() != null) {
            health = Math.max(0, hpCost - caster.getEquipment().getAccessory().getSpellDiscount(this));
        }
        if (caster.hasCondition(BlackPactCondition.class)) {
            health = Math.max(0, health - 2);
        }
        if (!isCastFromScroll) {
            final boolean[] abort = {false};
            YesNoMessageView confirmDialog = new YesNoMessageView(model.getView(),
                    "WARNING: Casting " + getName() + " will kill " + caster.getFirstName() + "! Abort casting?") {
                @Override
                protected void doAction(Model model) {
                    abort[0] = true;
                }
            };
            if (abort[0]) {
                return false;
            }
            caster.addToHP(-health);
        }
        if (caster.isDead()) {
            state.println(caster.getFirstName() + " was killed by the effect of the spell!");
            model.getParty().remove(caster, true, false, 0);
            return false;
        }
        int castingBonus = caster.getRankForSkill(Skill.SpellCasting);
        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, state, caster,
                getSkillForColor(color), difficulty, getExperience(), castingBonus);
        if (result.isSuccessful()) {
            state.println(getName() + " was successfully cast.");
            SoundEffects.playSpellSuccess();
            successfullyCastHook(model, state, caster);
        } else {
            state.println(getName() + " failed.");
            SoundEffects.playSpellFail();
        }
        return result.isSuccessful();
    }

    protected void successfullyCastHook(Model model, GameState state, GameCharacter caster) { }

    protected abstract int getExperience();

    public static Skill getSkillForColor(MyColors color) {
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

    public Skill getSkill() {
        return getSkillForColor(color);
    }

    @Override
    public String getSound() {
        return "book";
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getHPCost() {
        return hpCost;
    }

    public String castFromMenu(Model model, GameCharacter gc) {
        if (!model.getSpellHandler().tryCast(this, gc)) {
            return "You cannot cast " + getName() + " right now.";
        }
        return gc.getFirstName() + " is casting " + getName() + "...";
    }

    public void setCastFromScroll(boolean b) {
        isCastFromScroll = b;
    }

    @Override
    public boolean isAnalyzable() {
        return true;
    }

    @Override
    public AnalyzeDialog getAnalysisDialog(Model model) {
        return new AnalyzeSpellDialog(model, this);
    }

    @Override
    public String getAnalysisType() {
        return "Cast Chance for";
    }

    @Override
    public int getWeight() {
        return 300;
    }
}
