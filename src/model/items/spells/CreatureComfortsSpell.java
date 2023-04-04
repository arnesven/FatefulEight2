package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.states.GameState;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CreatureComfortsSpell extends ImmediateSpell {
    private static final Sprite SPRITE = new ItemSprite(1, 8, MyColors.BROWN, MyColors.BLUE, MyColors.WHITE);

    public CreatureComfortsSpell() {
        super("Creature Comforts", 20, MyColors.BLUE, 7, 3);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CreatureComfortsSpell();
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        if (model.getSpellHandler().creatureComfortsCastToday(model)) {
            state.println("You have already cast " + getName() + " today.");
            return false;
        }
        if (caster.getHP() <= getHPCost()) {
            state.println(caster.getName() + " is too exhausted to cast " + getName() + "!");
            return false;
        }
        return true;
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        state.println(caster.getName() + " summons a majestic house-like tent, with " +
                "fancy furniture and real feather beds, " +
                "plenty of scrumptious food and even some musical instruments and games. " +
                "Tonight will be a wonderful evening for sure!");
        model.getSpellHandler().setCreatureComfortsCastOnDay(model.getDay());
    }

    @Override
    public String getDescription() {
        return "Summons tents, beds and food to accommodate the party for an evening.";
    }
}
