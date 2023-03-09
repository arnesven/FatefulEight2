package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatAction;
import model.combat.Combatant;
import model.states.CombatEvent;

import java.util.ArrayList;
import java.util.List;

public class CombatActionMenu extends ArrowMenuSubView {
    private final List<CombatAction> actions;
    private final Combatant target;
    private final CombatEvent combatEvent;
    private final SubView previous;

    public CombatActionMenu(SubView subView, List<CombatAction> actions, List<String> labels, int x, int y, int anchor,
                            CombatEvent combatEvent, Combatant target) {
        super(subView, addExit(labels), x, y, anchor);
        this.previous = subView;
        this.actions = actions;
        this.combatEvent = combatEvent;
        this.target = target;
    }

    private static List<String> addExit(List<String> labels) {
        labels.add("Back");
        return labels;
    }

    @Override
    protected void enterPressed(Model model, int cursorPos) {
        if (cursorPos < actions.size()) {
            actions.get(cursorPos).doAction(model, combatEvent, (GameCharacter) combatEvent.getCurrentCombatant(), target);
            combatEvent.unblock();
        }
        model.setSubView(previous);
    }
}
