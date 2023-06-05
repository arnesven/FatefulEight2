package model.items.potions;

import model.classes.Skill;
import model.combat.Combatant;
import model.items.Item;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.SkillBoostingPotionSprite;
import view.sprites.Sprite;

public class WitsPotion extends SkillBoostingPotion {
    private static final Sprite SPRITE = new SkillBoostingPotionSprite(MyColors.GOLD);
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD4), MyColors.GOLD, MyColors.BLACK, MyColors.CYAN);

    public WitsPotion() {
        super("Wits Potion", new Skill[]{Skill.Logic, Skill.Perception, Skill.Search, Skill.SpellCasting, Skill.Survival});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new WitsPotion();
    }

    @Override
    protected SkillBoostingCondition getBoostCondition() {
        return new WitsBoostingCondition();
    }

    private class WitsBoostingCondition extends SkillBoostingCondition {
        public WitsBoostingCondition() {
            super("Wits Boost", "WIT");
        }

        @Override
        public Sprite getSymbol() {
            return CONDITION_SPRITE;
        }

        @Override
        protected void removeYourself(Combatant comb) {
            comb.removeCondition(WitsBoostingCondition.class);
        }
    }
}
