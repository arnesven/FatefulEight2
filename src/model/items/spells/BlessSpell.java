package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.BlessedCondition;
import model.combat.conditions.VampirismCondition;
import model.items.Item;
import model.states.DailyEventState;
import model.states.GameState;
import util.MyStrings;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.sprites.WhiteSpellSprite;

public class BlessSpell extends ImmediateSpell {
    public static final String SPELL_NAME = "Bless";
    private static final Sprite SPRITE = new WhiteSpellSprite(6, false);
    private static final int DURATION_DAYS = 3;
    private GameCharacter target;

    public BlessSpell() {
        super(SPELL_NAME, 14, MyColors.WHITE, 7, 2);
    }

    public static String getMagicExpertTips() {
        return "A master of the Bless spell will cast it with prolonged duration.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BlessSpell();
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        state.println("Who should be the target of " + getName() + "?");
        this.target = model.getParty().partyMemberInput(model, state, caster);
        return true;
    }

    @Override
    public boolean masteriesEnabled() {
        return true;
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        if (target.hasCondition(BlessedCondition.class)) {
            state.println(getName() + " has no effect on " + target.getName() + ".");
        } else {
            applyBlessing(model, state, DURATION_DAYS + getMasteryLevel(caster), target);
        }
    }

    public static void applyBlessing(Model model, GameState state, int durationDays, GameCharacter target) {
        if (target.hasCondition(VampirismCondition.class)) {
            state.println(target.getName() + " cannot be blessed! (vampirism)");
            return;
        }
        if (!target.hasCondition(BlessedCondition.class)) {
            target.addCondition(new BlessedCondition(model.getDay() + durationDays));
            target.addToSP(1);
            state.println(target.getName() + " was blessed, " + DailyEventState.hisOrHer(target.getGender()) +
                    " maximum Stamina Points is increased.");
        }
    }

    @Override
    public String getDescription() {
        return "Raises a characters maximum Stamina Points by 1 for " +
                MyStrings.numberWord(DURATION_DAYS) + " days.";
    }
}
