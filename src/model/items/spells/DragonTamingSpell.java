package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.characters.TamedDragonCharacter;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.combat.conditions.TamedCondition;
import model.enemies.BeastEnemy;
import model.enemies.DragonEnemy;
import model.items.Item;
import model.items.TamedDragonItemAdapter;
import model.map.DesertHex;
import model.map.HillsHex;
import model.map.MountainHex;
import model.map.TundraHex;
import model.states.CombatEvent;
import model.states.GameState;
import model.states.RunAwayState;
import model.states.events.DragonEvent;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.List;

public class DragonTamingSpell extends AuxiliarySpell {
    private static final Sprite SPRITE = new CombatSpellSprite(12, 8, MyColors.BROWN, MyColors.PEACH, MyColors.BLACK);
    private static final int DIFFICULTY = 8;
    private static final int HP_COST = 1;
    private static final String DESCRIPTION = "Attract a dragon or try to tame it.";

    public DragonTamingSpell() {
        super("Dragon Taming", 85, COLORLESS, DIFFICULTY, HP_COST);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    public String castFromMenu(Model model, GameCharacter gc) {
        if (model.isInCombat()) {
            return getName() + " must be cast as a combat action while in combat.";
        }
        model.getSpellHandler().acceptSpell(getName());
        model.getSpellHandler().tryCast(this, gc);
        return gc.getFirstName() + " is casting " + getName() + "...";
    }

    @Override
    public Item copy() {
        return new DragonTamingSpell();
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        return true;
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        if (!canAttractDragonsInHex(model) || model.getParty().getTamedDragons().get(caster) != null) {
            state.println("... But nothing happened?");
            state.partyMemberSay(caster, "Hmph! Must not be any dragons around...");
            return;
        }
        DragonEvent event = new DragonEvent(model);
        event.doTheEvent(model);
        if (event.haveFledCombat()) {
            new RunAwayState(model).run(model);
        }
        GameState.setCurrentTerrainSubview(model);
    }

    private boolean canAttractDragonsInHex(Model model) {
        return model.getCurrentHex() instanceof HillsHex || model.getCurrentHex() instanceof MountainHex ||
                model.getCurrentHex() instanceof TundraHex || model.getCurrentHex() instanceof DesertHex;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public CombatSpell getCombatSpell() {
        return new DragonTamingCombatSpell();
    }

    private static class DragonTamingCombatSpell extends CombatSpell {
        public DragonTamingCombatSpell( ) {
            super("Dragon Taming", 0, COLORLESS, DIFFICULTY, HP_COST, false);
        }

        @Override
        public boolean masteriesEnabled() {
            return true;
        }

        @Override
        protected Sprite getSprite() {
            return SPRITE;
        }

        @Override
        public Item copy() {
            throw new IllegalStateException("Should not copy this spell!");
        }

        @Override
        public boolean canBeCastOn(Model model, Combatant target) {
            return target instanceof DragonEnemy;
        }

        @Override
        public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
            if (!(target instanceof DragonEnemy)) {
                return;
            }
            if (target.hasCondition(TamedCondition.class)) {
                combat.println(getName() + " had no effect on " + target.getName() + "!");
                return;
            }
            combat.println("The dragon is being subdued by the spell. Now " + performer.getFirstName() + " must " +
                    "tame it once and for all!");
            DragonEnemy dragon = (DragonEnemy) target;
            int difficulty = calculatedDifficulty(dragon) - getMasteryLevel(performer) * 2;
            SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, combat, performer, dragon.getMagicSkill(), difficulty, 20, 0);
            if (result.isSuccessful()) {
                combat.println(performer.getFirstName() + " successfully tamed the dragon!");
                model.getTutorial().dragonTaming(model);
                combat.partyMemberSay(performer, "You're mine now.");
                if (combat.getEnemies().size() == 1) {
                    combat.setTimeLimit(0);
                }
                dragon.addCondition(new TamedCondition());
                model.getParty().getTamedDragons().put(performer, new TamedDragonCharacter(performer, dragon));
            } else {
                combat.println("The spell broke! The dragon has not been tamed!");
                combat.partyMemberSay(performer, "Oh no! It seems too agitated to be tamed.");
            }
        }

        private int calculatedDifficulty(DragonEnemy dragon) {
            switch (dragon.getAggressiveness()) {
                case BeastEnemy.RAMPAGING:
                    return 16;
                case BeastEnemy.HOSTILE:
                    return 14;
                case BeastEnemy.NORMAL:
                    return 12;
            }
            return 8; // DOCILE
        }

        @Override
        public String getDescription() {
            return DESCRIPTION;
        }
    }


    public static List<? extends Item> tamedDragonsAsItems(Model model) {
        List<TamedDragonItemAdapter> result = new ArrayList<>();
        for (TamedDragonCharacter dragon : model.getParty().getTamedDragons().values()) {
            result.add(new TamedDragonItemAdapter(dragon));
        }
        return result;
    }
}
