package model.states.duel;

import model.Model;
import model.states.duel.actions.AttackMagicDuelAction;
import model.states.duel.actions.BeamOptions;
import model.states.duel.actions.MagicDuelAction;

public class AIDuelistController implements DuelistController {
    private final MagicDuelist duelist;

    public AIDuelistController(MagicDuelist magicDuelist) {
        this.duelist = magicDuelist;
    }

    @Override
    public MagicDuelist getDuelist() {
        return duelist;
    }

    @Override
    public MagicDuelAction selectNormalTurnAction(Model model, MagicDuelEvent state) {
        return new AttackMagicDuelAction();
    }

    @Override
    public BeamOptions selectBeamTurnAction(Model model, MagicDuelEvent magicDuelEvent) {
        return BeamOptions.Release;
    }
}
