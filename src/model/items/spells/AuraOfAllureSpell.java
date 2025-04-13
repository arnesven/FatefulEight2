package model.items.spells;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.WhiteSpellSprite;

public class AuraOfAllureSpell extends SkillBoostingSpell {
    public static final String SPELL_NAME = "Aura of Allure";
    private static final Sprite SPRITE = new WhiteSpellSprite(5, false);
    public AuraOfAllureSpell() {
        super(SPELL_NAME, 12, MyColors.WHITE, 8, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new AuraOfAllureSpell();
    }

    @Override
    protected Skill getBoostingSkill() {
        return Skill.Entertain;
    }

    @Override
    protected int getBoostAmount() {
        return 3;
    }
}
