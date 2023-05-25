package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.spells.Spell;
import model.states.QuestState;
import model.states.SpellCastException;
import view.sprites.CastingEffectSprite;
import view.sprites.MiscastEffectSprite;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class QuestNode {
    private final Map<String, SpellCallback> spellCallbacks = new HashMap<>();

    public abstract int getColumn();
    public abstract int getRow();
    public abstract void drawYourself(Model model, int xPos, int yPos);
    public abstract String getDescription();
    public abstract QuestEdge run(Model model, QuestState state);

    public Point getPosition() {
        return new Point(getColumn(), getRow());
    }

    protected boolean isEligibleForSelection(Model model, QuestState state) {
        return true;
    }

    public void addSpellCallback(String nameOfSpell, SpellCallback callback) {
        spellCallbacks.put(nameOfSpell, callback);
    }

    protected void acceptAllSpells(Model model) {
        boolean atLeastOneSpell = false;
        for (String spellName : spellCallbacks.keySet()) {
            model.getSpellHandler().acceptSpell(spellName);
            for (Spell sp : model.getParty().getInventory().getSpells()) {
                if (sp.getName().equals(spellName)) {
                    atLeastOneSpell = true;
                }
            }
        }

        java.util.List<GameCharacter> partyMembers = new ArrayList<>(model.getParty().getPartyMembers());
        Collections.shuffle(partyMembers);
        if (atLeastOneSpell) {
            for (GameCharacter gc : partyMembers) {
                if (gc.getRankForSkill(Skill.MagicAny) > 0) {
                    model.getParty().partyMemberSay(model, gc,
                            List.of("Don't we have a spell for this?",
                                    "If only some magic could help us here...",
                                    "I know just the magic trick for this!",
                                    "I'm getting a tingling sensation...",
                                    "Wait... can we cast a spell here?"));
                    break;
                }
            }
        }
    }

    protected void unacceptAllSpells(Model model) {
        for (String spellName : spellCallbacks.keySet()) {
            model.getSpellHandler().unacceptSpell(spellName);
        }
    }

    protected SpellCallback getSpellCallback(String spellName) {
        return spellCallbacks.get(spellName);
    }

    protected QuestEdge tryCastSpell(Model model, QuestState state, SpellCastException sce) {
        state.println("");
        boolean spellSuccess = sce.getSpell().castYourself(model, state, sce.getCaster());
        Point position = new Point(state.getCurrentPosition().getColumn(), state.getCurrentPosition().getRow());
        if (spellSuccess) {
            state.getSubView().addSpecialEffect(position, new CastingEffectSprite());
        } else {
            state.getSubView().addSpecialEffect(position, new MiscastEffectSprite());
        }
        model.getLog().waitForAnimationToFinish();
        if (spellSuccess) {
            unacceptAllSpells(model);
            return getSpellCallback(sce.getSpell().getName()).run(model, state, sce.getSpell(), sce.getCaster());
        }
        return null;
    }
}
