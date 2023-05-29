package model.quests;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.spells.Spell;
import model.states.QuestState;
import model.states.SpellCastException;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.*;
import java.util.List;

public class QuestDecisionPoint extends QuestJunction {

    private final String leaderTalk;

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

    @Override
    public QuestEdge run(Model model, QuestState state) {
        if (!leaderTalk.equals("")) {
            state.leaderSay(leaderTalk);
        }

        if (model.getParty().size() == 1) {
            state.println("(Since you are alone in your party you automatically succeed a the decision point).");
        } else {
            if (leadershipTestFailed(model, state)) {
                return getConnection(0);
            }
        }

        acceptAllSpells(model);
        QuestEdge finalEdge;
        do {
            try {
                finalEdge = questNodeInput(model, state);
                break;
            } catch (SpellCastException sce) {
                QuestEdge edge = tryCastSpell(model, state, sce);
                if (edge != null) {
                    return edge;
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


}
