package model.items.potions;

import model.classes.Skill;
import model.combat.Combatant;
import model.items.Item;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.SkillBoostingPotionSprite;
import view.sprites.Sprite;

public class DexterityPotion extends SkillBoostingPotion {
    private static final Sprite SPRITE = new SkillBoostingPotionSprite(MyColors.GREEN);
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD4), MyColors.LIGHT_GREEN, MyColors.BLACK, MyColors.CYAN);

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

    private class DexterityBoostingCondition extends SkillBoostingCondition {
        public DexterityBoostingCondition() {
            super("Dexterity Boost", "DEX");
        }

        @Override
        public Sprite getSymbol() {
            return CONDITION_SPRITE;
        }

        @Override
        protected void removeYourself(Combatant comb) {
            comb.removeCondition(DexterityBoostingCondition.class);
        }
    }
}
