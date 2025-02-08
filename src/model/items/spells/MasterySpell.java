package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.items.accessories.MasterRing;
import model.states.GameState;
import util.MyStrings;
import view.LogView;
import view.MyColors;

public abstract class MasterySpell extends Spell {

    private static final Integer[] DEFAULT_THRESHOLDS = new Integer[]{7, 10, 23, 40};

    public MasterySpell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost, color, difficulty, hpCost);
    }

    @Override
    protected void successfullyCastHook(Model model, GameState state, GameCharacter caster) {
        if (masteriesEnabled()) {
            int masteryTimes = getMasteryTimes(caster);
            for (int i = 0; i < masteryTimes; ++i) {
                boolean updated = caster.getMasteries().registerSuccessfullCast(model, this);
                if (updated) {
                    model.getLog().addAnimated(LogView.PURPLE_COLOR + caster.getName() + " has achieved mastery level " +
                            getMasteryLevel(caster) + " for " + getName() + "!\n" + LogView.DEFAULT_COLOR);
                    model.getTutorial().spellMasteries(model);
                }
            }
        }
    }

    protected int getMasteryTimes(GameCharacter caster) {
        if (caster.getEquipment().getAccessory() != null) {
            return caster.getEquipment().getAccessory().getMasteryFactor();
        }
        return 1;
    }

    protected int getMasteryLevel(GameCharacter character) {
        if (!masteriesEnabled()) {
            throw new IllegalStateException("Masteries have not been enabled for this spell!");
        }
        return character.getMasteries().getMasteryLevel(this);
    }

    public boolean masteriesEnabled() {
        return false;
    }

    public Integer[] getThresholds() {
        return DEFAULT_THRESHOLDS;
    }

    public static String getMagicExpertTips() {
        return "You usually need to cast a spell " + MyStrings.numberWord(DEFAULT_THRESHOLDS[0]) +
                " times to reach mastery level one. After you've cast the spell " + MyStrings.numberWord(DEFAULT_THRESHOLDS[1]) +
                " times, you'll reach level two. " + MyStrings.capitalize(MyStrings.numberWord(DEFAULT_THRESHOLDS[2])) +
                " casts are needed to reach level three. Although there are spells that deviate from this pattern.";
    }
}
