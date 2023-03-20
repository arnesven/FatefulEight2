package model.items.spells;

import model.classes.Skill;
import model.items.Item;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.Sprite;

public class AuraOfAllureSpell extends SkillBoostingSpell {
    private static final Sprite SPRITE = new CombatSpellSprite(2, 8, MyColors.BROWN, MyColors.BEIGE, MyColors.DARK_GRAY);

    public AuraOfAllureSpell() {
        super("Aura of Allure", 16, MyColors.WHITE, 8, 1);
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
