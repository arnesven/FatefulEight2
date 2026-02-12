package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.items.Item;
import model.states.CombatEvent;
import model.states.GameState;
import util.MyLists;
import util.MyStrings;
import view.sprites.ColorlessSpellSprite;
import view.sprites.Sprite;
import view.subviews.ArrowMenuSubView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MassSpell extends ImmediateSpell {
    public static final String SPELL_NAME = "Mass";
    private static final Sprite SPRITE = new ColorlessSpellSprite(9, false);
    private static final int DIFFICULTY_INCREASE = 3;
    private static final int HP_COST_INCREASE = 1;
    private CombatSpell configuredSpell = null;

    public MassSpell() {
        super(SPELL_NAME, 62, COLORLESS, 6, 0);
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        List<Spell> spells = getMassSpells(model);
        if (spells.isEmpty()) {
            state.println("You do not have any spells to use " + SPELL_NAME + " on.");
            return false;
        }
        return true;
    }

    private List<Spell> getMassSpells(Model model) {
        List<Spell> spells = model.getParty().getSpells();
        spells.remove(this);
        spells.removeIf(sp -> !(sp instanceof CombatSpell));
        spells.removeIf(sp -> !((CombatSpell)sp).canBeUsedWithMass());
        return spells;
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        state.println("Which spell do you want to configure " + SPELL_NAME + " with?");
        List<Spell> spells = getMassSpells(model);
        List<String> options = MyLists.transform(spells, Item::getName);
        Spell[] selected = new Spell[]{null};
        model.setSubView(new ArrowMenuSubView(model.getSubView(), options, 24, 36 - options.size() * 2, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selected[0] = spells.get(cursorPos);
                model.setSubView(getPrevious());
            }
        });
        state.waitForReturnSilently();
        configuredSpell = (CombatSpell) selected[0];
        state.println("You can now cast 'Mass " + configuredSpell.getName() + "' in combat. It is a difficulty " +
                (configuredSpell.getDifficulty() + DIFFICULTY_INCREASE) + " " + Spell.getSkillForColor(configuredSpell.getColor()).getName() +
                " spell, with a HP cost of " + (configuredSpell.getHPCost() + HP_COST_INCREASE) + ".");
    }

    @Override
    public String getDescription() {
        return "Target multiple with another spell. Current: " + (configuredSpell == null ? "*None*" : configuredSpell.getName());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new MassSpell();
    }

    public static String getMagicExpertTips() {
        return "Mass is a spell which lets you cast a combat spell on multiple enemies, or multiple allies for that matter.";
    }

    @Override
    public CombatSpell getCombatSpell() {
        if (configuredSpell == null) {
            return null;
        }
        return new MassXSpell(configuredSpell);
    }

    private static class MassXSpell extends CombatSpell {
        private final CombatSpell configuredSpell;

        public MassXSpell(CombatSpell configuredSpell) {
            super("Mass " + configuredSpell.getName(), 0, configuredSpell.getColor(),
                    configuredSpell.getDifficulty() + MassSpell.DIFFICULTY_INCREASE,
                    configuredSpell.getHPCost() + MassSpell.HP_COST_INCREASE);
            this.configuredSpell = configuredSpell;
        }

        @Override
        public boolean canBeCastOn(Model model, Combatant target) {
            return configuredSpell.canBeCastOn(model, target);
        }

        @Override
        public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
            List<Combatant> targets = new ArrayList<>();
            targets.add(target);
            List<Enemy> enemiesRemaining = new ArrayList<>(combat.getEnemies());
            enemiesRemaining.remove(target);
            enemiesRemaining.removeIf(enm -> !configuredSpell.canBeCastOn(model, enm));
            Collections.shuffle(enemiesRemaining);

            List<Combatant> alliesRemaining = new ArrayList<>(combat.getAllCombatants());
            alliesRemaining.removeAll(combat.getEnemies());
            alliesRemaining.removeIf(comb -> !configuredSpell.canBeCastOn(model, comb));
            alliesRemaining.remove(target);
            Collections.shuffle(alliesRemaining);

            for (int i = 0; i < getMasteryLevel(performer) + 1; i++) {
                if (target instanceof Enemy) {
                    if (enemiesRemaining.isEmpty()) {
                        break;
                    } else {
                        Enemy e = enemiesRemaining.removeFirst();
                        targets.add(e);
                    }
                } else {
                    if (alliesRemaining.isEmpty()) {
                        break;
                    } else {
                        Combatant comb = alliesRemaining.removeFirst();
                        targets.add(comb);
                    }
                }
            }

            combat.println(getName() + " targets " + targets.size() +
                    (target instanceof Enemy ? " enemies" : " allies") + "!");

            for (Combatant newTarget : targets) {
                configuredSpell.applyCombatEffect(model, combat, performer, newTarget);
            }
        }

        @Override
        public boolean masteriesEnabled() {
            return true;
        }

        @Override
        public Integer[] getThresholds() {
            return new Integer[]{3, 4, 5, 10, 15, 20};
        }

        @Override
        public boolean canBeUsedWithMass() {
            return false;
        }

        @Override
        public String getDescription() {
            return "NOT USED";
        }

        @Override
        protected Sprite getSprite() {
            throw new IllegalStateException("Should not be called!");
        }

        @Override
        public Item copy() {
            throw new IllegalStateException("Should not be called!");
        }
    }
}
