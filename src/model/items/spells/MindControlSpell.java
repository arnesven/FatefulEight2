package model.items.spells;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;
import view.sprites.BlueSpellSprite;
import view.sprites.Sprite;

public class MindControlSpell extends SkillBoostingSpell {
    public static final String SPELL_NAME = "Mind Control";
    private static final Sprite SPRITE = new BlueSpellSprite(1, false);

    public MindControlSpell() {
        super(SPELL_NAME, 16, MyColors.BLUE, 8, 2);
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
