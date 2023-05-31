package model.items.spells;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class MindControlSpell extends SkillBoostingSpell {
    private static final Sprite SPRITE = new ItemSprite(6, 8, MyColors.BROWN, MyColors.BLUE, MyColors.WHITE);

    public MindControlSpell() {
        super("Mind Control", 16, MyColors.BLUE, 8, 2);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new MindControlSpell();
    }

    @Override
    protected Skill getBoostingSkill() {
        return Skill.Persuade;
    }

    @Override
    protected int getBoostAmount() {
        return 6;
    }
}
