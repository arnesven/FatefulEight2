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

public class CharismaPotion extends SkillBoostingPotion {
    private static final Sprite SPRITE = new SkillBoostingPotionSprite(MyColors.BLUE);
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD4), MyColors.BLUE, MyColors.BLACK, MyColors.CYAN);

    public CharismaPotion() {
        super("Charisma Potion", new Skill[]{Skill.Entertain, Skill.Leadership, Skill.Persuade, Skill.SeekInfo});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CharismaPotion();
    }

    @Override
    protected SkillBoostingCondition getBoostCondition() {
        return new CharismaBoostingCondition();
    }

    public Condition getCondition() {
        return new CharismaBoostingCondition();
    }

    private class CharismaBoostingCondition extends SkillBoostingCondition {
        public CharismaBoostingCondition() {
            super("Charisma Boost", "CHA");
        }

        @Override
        public Sprite getSymbol() {
            return CONDITION_SPRITE;
        }

        @Override
        public ConditionHelpDialog getHelpView(GameView view) {
            return new ConditionHelpDialog(view, this, "A condition indicating that the character is currently " +
                    "receiving a bonus to Charisma-based skills, i.e. Entertain, Leadership, Persuade, and Seek Info.");
        }

        @Override
        protected void removeYourself(Combatant comb) {
            comb.removeCondition(CharismaBoostingCondition.class);
        }
    }
}
