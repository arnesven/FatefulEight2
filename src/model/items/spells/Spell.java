package model.items.spells;

import model.items.Inventory;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.states.CombatEvent;
import model.states.DailyEventState;
import model.states.GameState;
import sound.SoundEffects;
import view.*;
import view.sprites.DamageValueEffect;

public abstract class Spell extends Item {
    public static final MyColors COLORLESS = MyColors.PURPLE;
    public static final MyColors[] spellColors = new MyColors[]{MyColors.WHITE, MyColors.RED, MyColors.BLUE, MyColors.GREEN, MyColors.BLACK, COLORLESS};
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
            caster.addToHP(-health);
            if (state instanceof CombatEvent) {
                ((CombatEvent) state).addFloatyDamage(caster, health, DamageValueEffect.MAGICAL_DAMAGE);
            }
        }
        if (caster.isDead()) {
            state.println(caster.getFirstName() + " was killed by the effect of the spell!");
            if (!model.isInCombat() && !DailyEventState.didResurrect(model, state, caster)) {
                model.getParty().remove(caster, true, false, 0);
            }
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
        if (color == COLORLESS) {
            return Skill.MagicAny;
        }
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

    public static MyColors getColorForSkill(Skill skill) {
        switch (skill) {
            case MagicBlack:
                return MyColors.BLACK;
            case MagicBlue:
                return MyColors.BLUE;
            case MagicRed:
                return MyColors.RED;
            case MagicGreen:
                return MyColors.GREEN;
            case MagicWhite:
                return MyColors.WHITE;
            case MagicAny:
                return COLORLESS;
            default:
                throw new IllegalStateException("Unrecognized magic skill " + skill.getName());
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

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public boolean isSellable() {
        return true;
    }
}
