package model.states.duel;

import model.Model;

public class AIDuelistController implements DuelistController {
    private final MagicDuelist duelist;

    public AIDuelistController(MagicDuelist magicDuelist) {
        this.duelist = magicDuelist;
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
