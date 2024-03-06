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

public class DexterityPotion extends SkillBoostingPotion {
    private static final Sprite SPRITE = new SkillBoostingPotionSprite(MyColors.GREEN);
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD4), MyColors.GREEN, MyColors.BLACK, MyColors.CYAN);

    public DexterityPotion() {
        super("Dexterity Potion", new Skill[]{Skill.Blades, Skill.Bows, Skill.Polearms, Skill.Sneak, Skill.Security});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new DexterityPotion();
    }

    @Override
    protected SkillBoostingCondition getBoostCondition() {
        return new DexterityBoostingCondition();
    }

    public Condition getCondition() {
        return new DexterityBoostingCondition();
    }

    private class DexterityBoostingCondition extends SkillBoostingCondition {
        public DexterityBoostingCondition() {
            super("Dexterity Boost", "DEX");
        }

        @Override
        public Sprite getSymbol() {
            return CONDITION_SPRITE;
        }

        @Override
        public ConditionHelpDialog getHelpView(GameView view) {
            return new ConditionHelpDialog(view, this, "A condition indicating that the character is currently " +
                    "receiving a bonus to Dexterity-based skills, i.e. Blades, Bows, Polearms, Sneak and Security.");
        }

        @Override
        protected void removeYourself(Combatant comb) {
            comb.removeCondition(DexterityBoostingCondition.class);
        }
    }
}
