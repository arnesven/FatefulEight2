package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.Combatant;
import model.items.Item;
import model.states.CombatEvent;
import model.states.GameState;
import util.MyLists;
import util.MyPair;
import view.MyColors;
import view.sprites.CombatSpellSprite;
import view.sprites.Sprite;
import view.subviews.CombineSpellSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombineSpell extends AuxiliarySpell {

    private static final String COMBINE_COMBAT_SPELL_NAME = "COMBO";
    private static final Sprite SPRITE = new CombatSpellSprite(7, 8, MyColors.BROWN, MyColors.PEACH, MyColors.BLACK);
    private List<Spell> combinedResult = null;

    public CombineSpell() {
        super("Combine", 68, COLORLESS, 5, 0);
    }

    public static int getResultingHealthCost(List<Spell> combined) {
        return MyLists.maximum(combined, Spell::getHPCost) + combined.size() - 1;
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
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CombineSpell();
    }

    @Override
    protected boolean preCast(Model model, GameState state, GameCharacter caster) {
        state.println(caster.getName() + " is preparing to cast " + getName() + ".");
        List<Spell> uniqueSpells = findUniqueSpells(model);
        uniqueSpells.remove(this);
        if (uniqueSpells.isEmpty()) {
            state.println("But you do not have any other spells to combine.");
            return false;
        } else if (uniqueSpells.size() == 1) {
            state.println("But you only have one other spell!");
            return false;
        }

        SubView oldSubview = model.getSubView();
        int masteryLevel = caster.getMasteries().getMasteryLevel(COMBINE_COMBAT_SPELL_NAME);
        CombineSpellSubView combineSubView = new CombineSpellSubView(2 + masteryLevel, uniqueSpells);
        model.setSubView(combineSubView);
        do {
            state.waitForReturnSilently();
        } while (combineSubView.getTopIndex() == -1);

        model.setSubView(oldSubview);
        if (combineSubView.didCancel()) {
            state.println("The casting of Combine was aborted.");
            return false;
        }
        if (combineSubView.selectionInvalid()) {
            state.println("Your selection of spells to combine was invalid, casting aborted.");
            return false;
        }
        this.combinedResult = combineSubView.getCombinedSpells();
        return true;
    }

    private List<Spell> findUniqueSpells(Model model) {
        List<Spell> result = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (Spell sp : model.getParty().getInventory().getSpells()) {
            if (isCombatSpell(sp) && !set.contains(sp.getName())) {
                result.add(sp);
                set.add(sp.getName());
            }
        }
        return result;
    }

    private boolean isCombatSpell(Spell sp) {
        if (sp instanceof CombineSpell) {
            return false;
        }
        return sp instanceof CombatSpell || (sp instanceof AuxiliarySpell && ((AuxiliarySpell) sp).canBeCastInCombat());
    }

    @Override
    protected void applyAuxiliaryEffect(Model model, GameState state, GameCharacter caster) {
        MyPair<Skill, Integer> skillAndInteger = getResultingSkill(combinedResult);
        state.println("The selected spells were combined into one; 'Combo'!");
        state.println("Combo is a " + skillAndInteger.first.getName() + " " +
                skillAndInteger.second + " Combat Spell with a Health Point cost of " +
                getResultingHealthCost(combinedResult));
        state.println("Cast Combo in combat for the compounded effects!");
    }

    @Override
    public String getDescription() {
        return "Enables the effects of several spells to be chained with a single cast.";
    }

    public static MyPair<Skill, Integer> getResultingSkill(List<Spell> combined) {
        return new MyPair<>(combined.get(0).getSkill(),
                (int)(MyLists.doubleAccumulate(combined, Spell::getDifficulty) / combined.size()) + combined.size() - 1);
    }

    @Override
    public CombatSpell getCombatSpell() {
        if (combinedResult == null) { // Combine cannot be cast until configured outside of combat
            return null;
        }
        MyPair<Skill, Integer> skillAndInteger = getResultingSkill(combinedResult);
        return new CombineCombatSpell(skillAndInteger.first, skillAndInteger.second, getResultingHealthCost(combinedResult), combinedResult);
    }

    private static class CombineCombatSpell extends CombatSpell {

        private final List<Spell> innerSpells;

        public CombineCombatSpell(Skill magicSkill, int difficulty, int hpCost, List<Spell> innerSpells) {
            super(COMBINE_COMBAT_SPELL_NAME, 0, CombineSpell.getColorForSkill(magicSkill), difficulty, hpCost);
            this.innerSpells = innerSpells;
        }

        @Override
        protected boolean masteriesEnabled() {
            return true;
        }

        @Override
        protected Sprite getSprite() {
            throw new IllegalStateException("Should not be called!");
        }

        @Override
        public Item copy() {
            throw new IllegalStateException("Should not be called!");
        }

        @Override
        public boolean canBeCastOn(Model model, Combatant target) {
            return true;
        }

        @Override
        public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
            for (Spell sp : innerSpells) {
                if (sp instanceof CombatSpell) {
                    applyCombatSpellIfAble(model, combat, performer, target, (CombatSpell)sp);
                } else {
                    if (sp instanceof AuxiliarySpell && ((AuxiliarySpell) sp).canBeCastInCombat()) {
                        applyCombatSpellIfAble(model, combat, performer, target,
                                ((AuxiliarySpell) sp).getCombatSpell());
                    } else {
                        combat.println(sp.getName() + " failed.");
                    }
                }
            }
        }

        private void applyCombatSpellIfAble(Model model, CombatEvent combat, GameCharacter performer, Combatant target, CombatSpell csp) {
            if (!csp.canBeCastOn(model, target)) {
                combat.println(target.getName() + " cannot be targeted by " + csp.getName() + ", it is skipped.");
            } else {
                csp.applyCombatEffect(model, combat, performer, target);
            }
        }

        @Override
        public String getDescription() {
            throw new IllegalStateException("Should not be called!");
        }
    }
}
