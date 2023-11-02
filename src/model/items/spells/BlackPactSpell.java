package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.items.Item;
import model.states.CombatEvent;
import model.states.GameState;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class BlackPactSpell extends AuxiliarySpell {
    private static final Sprite SPRITE = new CombatSpellSprite(8, 8, MyColors.BROWN, MyColors.GRAY, MyColors.RED);

    public BlackPactSpell() {
        super("Black Pact", 16, MyColors.BLACK, 11, 3);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BlackPactSpell();
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        return !caster.hasCondition(BlackPactCondition.class);
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        caster.addCondition(new BlackPactCondition());
    }

    @Override
    public String getDescription() {
        return "For the remainder of the day, spells cost 2 less HP to cast.";
    }

    public String castFromMenu(Model model, GameCharacter gc) {
        model.getSpellHandler().acceptSpell(getName());
        model.getSpellHandler().tryCast(this, gc);
        return gc.getFirstName() + " is casting " + getName() + "...";
    }

    @Override
    public CombatSpell getCombatSpell() {
        return new CombatSpell(BlackPactSpell.this.getName(), BlackPactSpell.this.getCost(),
                BlackPactSpell.this.getColor(), BlackPactSpell.this.getDifficulty(),
                BlackPactSpell.this.getHPCost()) {
            @Override
            public boolean canBeCastOn(Model model, Combatant target) {
                return target instanceof GameCharacter && !target.hasCondition(BlackPactCondition.class);
            }

            @Override
            public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                applyAuxiliaryEffect(model, combat, performer);
            }

            @Override
            public String getDescription() {
                return BlackPactSpell.this.getDescription();
            }

            @Override
            protected Sprite getSprite() {
                return BlackPactSpell.this.getSprite();
            }

            @Override
            public Item copy() {
                throw new IllegalStateException("Should not be called!");
            }
        };
    }
}
