package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.Item;
import model.states.GameState;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CallOfTheWildSpell extends SkillBoostingSpell {
    private static final Sprite SPRITE = new ItemSprite(2, 8, MyColors.BEIGE, MyColors.GREEN);
    private GameCharacter target;

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
    public String getDescription() {
        return "Temporarily raises a character's rank in the skill Survival.";
    }

    @Override
    protected void preCast(Model model, GameState state, GameCharacter caster) {
        state.println("");
        state.print("Who do you want to target with " + getName() + "? ");
        this.target = model.getParty().partyMemberInput(model, state, caster);
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        state.println(target.getName() + " Survival temporarily raised.");
        target.addTemporaryBonus(Skill.Survival, 4);
    }

    @Override
    protected Skill getBoostingSkill() {
        return Skill.Survival;
    }
}
