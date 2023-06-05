package model.items.potions;

import model.classes.Skill;
import model.combat.Combatant;
import model.items.Item;
import view.MyColors;
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

    private class CharismaBoostingCondition extends SkillBoostingCondition {
        public CharismaBoostingCondition() {
            super("Charisma Boost", "CHA");
        }

        @Override
        public Sprite getSymbol() {
            return CONDITION_SPRITE;
        }

        @Override
        protected void removeYourself(Combatant comb) {
            comb.removeCondition(CharismaBoostingCondition.class);
        }
    }
}
