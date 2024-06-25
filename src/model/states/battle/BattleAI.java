package model.states.battle;

import model.Model;
import util.MyLists;
import view.subviews.BattleSubView;

import java.util.ArrayList;
import java.util.List;

public abstract class BattleAI {
    private boolean isDoneWithTurn;
    private ArrayList<BattleUnit> currentUnits;

    public BattleAI() {
        this.isDoneWithTurn = false;
    }

    protected abstract void startTurnHook(Model model, BattleSubView subView,
                                          BattleState battleState, ArrayList<BattleUnit> currentUnits);

    protected abstract void specificActivateUnit(Model model, BattleSubView subView,
                                                 BattleState battleState, ArrayList<BattleUnit> currentUnits);

    public void startTurn(Model model, BattleSubView subView, BattleState battleState, List<BattleUnit> opponentUnits) {
        this.isDoneWithTurn = false;
        this.currentUnits = new ArrayList<>(opponentUnits);
        startTurnHook(model, subView, battleState, currentUnits);
    }

    public boolean isDone() {
        return isDoneWithTurn;
    }

    public final void activateUnit(Model model, BattleSubView subView, BattleState battleState) {
        specificActivateUnit(model, subView, battleState, currentUnits);
        isDoneWithTurn = MyLists.all(currentUnits, (BattleUnit bu) -> bu.getMP() == 0);
    }

}
