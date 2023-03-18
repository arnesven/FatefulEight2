package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.QuestState;
import model.states.SpellCastException;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.*;
import java.util.List;

public class QuestDecisionPoint extends QuestJunction {

    private final String leaderTalk;
    private final Map<String, SpellCallback> spellCallbacks = new HashMap<>();

    public QuestDecisionPoint(int column, int row, List<QuestEdge> connections, String leaderTalk) {
        super(column, row);
        for (QuestEdge c : connections) {
            connectTo(c);
        }
        this.leaderTalk = leaderTalk;
    }

    private final Sprite32x32 SPRITE = new Sprite32x32("decisionpoint", "quest.png", 0x12, MyColors.BLACK, MyColors.WHITE, MyColors.RED);

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE, 1);
    }


    @Override
    public String getDescription() {
        return "Leader decision point: Solo Leadership 6";
    }

    public QuestEdge questNodeInput(Model model, QuestState state) {
        state.setSelectedElement(getConnections().get(0).getNode());
        do {
            state.print("Please select which location to advance to.");
            state.waitForReturn(true);
            for (QuestEdge edge : getConnections()) {
                if (edge.getNode() == state.getSelectedElement()) {
                    if (edge.getNode().isEligibleForSelection(model, state)) {
                        return edge;
                    }
                }
            }
            state.println("The selected location is not reachable from your current position.");
        } while (true);
    }

    public void addSpellCallback(String s, SpellCallback callback) {
        spellCallbacks.put(s, callback);
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        if (!leaderTalk.equals("")) {
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), leaderTalk);
        }

        if (leadershipTestFailed(model, state)) {
            return getConnection(0);
        }

        acceptAllSpells(model);
        QuestEdge finalEdge;
        do {
            try {
                finalEdge = questNodeInput(model, state);
                break;
            } catch (SpellCastException sce) {
                state.println("");
                boolean success = sce.getSpell().castYourself(model, state, sce.getCaster());
                model.getLog().waitForAnimationToFinish();
                if (success) {
                    finalEdge = spellCallbacks.get(sce.getSpell().getName()).run(model, state, sce.getSpell(), sce.getCaster());
                    break;
                }
            }
        } while (true);

        unacceptAllSpells(model);
        return finalEdge;
    }

    private boolean leadershipTestFailed(Model model, QuestState state) {
        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, state, model.getParty().getLeader(), Skill.Leadership, 6, 0, 0);
        model.getLog().waitForAnimationToFinish();
        return !result.isSuccessful();
    }

    private void acceptAllSpells(Model model) {
        boolean atLeastOneSpell = false;
        for (String spellName : spellCallbacks.keySet()) {
            model.getSpellHandler().acceptSpell(spellName);
            atLeastOneSpell = true;
        }

        List<GameCharacter> partyMembers = new ArrayList<>(model.getParty().getPartyMembers());
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


    private void unacceptAllSpells(Model model) {
        for (String spellName : spellCallbacks.keySet()) {
            model.getSpellHandler().unacceptSpell(spellName);
        }
    }
}
