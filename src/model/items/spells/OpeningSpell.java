package model.items.spells;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;
import view.sprites.BlueSpellSprite;
import view.sprites.ItemSprite;
import view.sprites.SpellSprite;
import view.sprites.Sprite;

public class OpeningSpell extends SkillBoostingSpell {
    public static final String SPELL_NAME = "Oundi's Opening";
    private static final Sprite SPRITE = new BlueSpellSprite(7, false);

    public OpeningSpell() {
        super(SPELL_NAME, 44, MyColors.BLUE, 10, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new OpeningSpell();
    }

    @Override
    protected Skill getBoostingSkill() {
        return Skill.Security;
    }

    @Override
    protected int getBoostAmount() {
        return 6;
    }
}
