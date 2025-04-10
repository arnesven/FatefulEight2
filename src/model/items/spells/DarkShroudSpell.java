package model.items.spells;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class DarkShroudSpell extends SkillBoostingSpell {

    public static final String SPELL_NAME = "Dark Shroud";
    private static final Sprite SPRITE = new ItemSprite(13, 8, MyColors.BROWN, MyColors.GRAY, MyColors.RED);

    public DarkShroudSpell() {
        super(SPELL_NAME, 16, MyColors.BLACK, 9, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new DarkShroudSpell();
    }

    @Override
    protected int getBoostAmount() {
        return 3;
    }

    @Override
    protected Skill getBoostingSkill() {
        return Skill.Sneak;
    }
}
