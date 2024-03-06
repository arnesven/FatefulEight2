package model.items.potions;

import model.classes.Skill;
import model.combat.Combatant;
import model.combat.conditions.Condition;
import model.items.Item;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.SkillBoostingPotionSprite;
import view.sprites.Sprite;

public class StrengthPotion extends SkillBoostingPotion {
    private static final Sprite SPRITE = new SkillBoostingPotionSprite(MyColors.ORANGE);
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD4), MyColors.ORANGE, MyColors.BLACK, MyColors.CYAN);

    public StrengthPotion() {
        super("Strength Potion", new Skill[]{Skill.Acrobatics, Skill.Axes, Skill.BluntWeapons, Skill.Endurance, Skill.Labor});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new StrengthPotion();
    }

    @Override
    protected SkillBoostingCondition getBoostCondition() {
        return new StrengthBoostingCondition();
    }

    public Condition getCondition() {
        return new StrengthBoostingCondition();
    }

    private class StrengthBoostingCondition extends SkillBoostingCondition {
        public StrengthBoostingCondition() {
            super("Strength Boost", "STR");
        }

        @Override
        public Sprite getSymbol() {
            return CONDITION_SPRITE;
        }

        @Override
        public ConditionHelpDialog getHelpView(GameView view) {
            return new ConditionHelpDialog(view, this, "A condition indicating that the character is currently " +
                    "receiving a bonus to Strength-based skills, i.e. Acrobatics, Axes, BluntWeapons, Endurance and Labor.");
        }

        @Override
        protected void removeYourself(Combatant comb) {
            comb.removeCondition(StrengthBoostingCondition.class);
        }
    }
}
