package model.items.potions;

import model.classes.Skill;
import model.combat.Combatant;
import model.items.Item;
import view.MyColors;
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

    private class StrengthBoostingCondition extends SkillBoostingCondition {
        public StrengthBoostingCondition() {
            super("Strength Boost", "STR");
        }

        @Override
        public Sprite getSymbol() {
            return CONDITION_SPRITE;
        }

        @Override
        protected void removeYourself(Combatant comb) {
            comb.removeCondition(StrengthBoostingCondition.class);
        }
    }
}
