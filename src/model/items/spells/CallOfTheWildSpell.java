package model.items.spells;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CallOfTheWildSpell extends SkillBoostingSpell {
    private static final Sprite SPRITE = new ItemSprite(2, 8, MyColors.BEIGE, MyColors.GREEN, MyColors.WHITE);

    public CallOfTheWildSpell() {
        super("Call of the Wild", 14, MyColors.GREEN, 10, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CallOfTheWildSpell();
    }

    @Override
    protected Skill getBoostingSkill() {
        return Skill.Survival;
    }

    @Override
    protected int getBoostAmount() {
        return 4;
    }
}
