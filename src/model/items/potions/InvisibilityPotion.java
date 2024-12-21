package model.items.potions;

import model.classes.Skill;
import model.combat.Combatant;
import model.combat.conditions.InvisibilityCondition;
import model.items.Item;
import model.items.Prevalence;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.SkillBoostingPotionSprite;
import view.sprites.Sprite;

import java.util.List;

public class InvisibilityPotion extends SkillBoostingPotion {

    private static final Sprite SPRITE = new SkillBoostingPotionSprite(MyColors.GRAY);

    public InvisibilityPotion() {
        super("Invisibility Potion", List.of(Skill.Sneak));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new InvisibilityPotion();
    }

    @Override
    public int getBoostAmount() {
        return 8;
    }

    @Override
    protected SkillBoostingCondition getBoostCondition() {
        return new InvisibleFromPotionCondition();
    }

    private static final InvisibilityCondition invisCond = new InvisibilityCondition(5);

    private class InvisibleFromPotionCondition extends SkillBoostingCondition {
        public InvisibleFromPotionCondition() {
            super(invisCond.getName(), invisCond.getShortName());
        }

        @Override
        public Sprite getSymbol() {
            return invisCond.getSymbol();
        }

        @Override
        public ConditionHelpDialog getHelpView(GameView view) {
            return invisCond.getHelpView(view);
        }

        @Override
        protected void removeYourself(Combatant comb) {
            comb.removeCondition(InvisibleFromPotionCondition.class);
        }
    }
}
