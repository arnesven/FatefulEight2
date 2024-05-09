package view.subviews;

import model.Model;
import model.actions.CombatAction;
import model.combat.Combatant;
import model.states.CombatEvent;

import java.util.ArrayList;
import java.util.List;

public class CombatActionMenu extends ArrowMenuSubView {
    private final List<CombatAction> actions;
    private final Combatant target;
    private final CombatEvent combatEvent;
    private final SubView previous;
    private final int x;
    private final int y;
    private final CombatSubView combatSubView;
    private final int anchor;
    private boolean inverter = false;

    public CombatActionMenu(SubView subView, List<CombatAction> actions, List<String> labels, int x, int y, int anchor,
                            CombatEvent combatEvent, Combatant target, CombatSubView combatSubView) {
        super(subView, addExit(labels), x, y, anchor);
        this.previous = subView;
        this.actions = actions;
        this.combatEvent = combatEvent;
        this.target = target;
        this.x = x;
        this.y = y;
        this.anchor = anchor;
        this.combatSubView = combatSubView;
        if (anchor == DailyActionMenu.SOUTH_WEST) {
            inverter = true;
        }
    }

    private static List<String> addExit(List<String> labels) {
        labels.add("Back");
        return labels;
    }

    @Override
    protected void enterPressed(Model model, int cursorPos) {
        if (cursorPos < actions.size()) {
            CombatAction selectedAction = actions.get(cursorPos);
            if (selectedAction.hasInnerMenu()) {
                List<CombatAction> innerActions = selectedAction.getInnerActions(model);
                int newY = y+cursorPos*2;
                if (inverter) {
                    newY = y - (actions.size()-cursorPos)*2 - 2;
                }
                model.setSubView(new CombatActionMenu(this, innerActions, toStringList(innerActions), x+2, newY,
                        anchor, combatEvent, target, combatSubView));
            } else {
                combatEvent.unblock(selectedAction, target);
                model.setSubView(combatSubView);
            }
        } else {
            model.setSubView(previous);
        }

    }

    public static List<String> toStringList(List<CombatAction> combatActions) {
        List<String> result = new ArrayList<>();
        for (CombatAction ca : combatActions) {
            result.add(ca.getName());
        }
        return result;
    }
}
