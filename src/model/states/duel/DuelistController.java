package model.states.duel;

import model.Model;

public interface DuelistController {
    MagicDuelAction selectNormalTurnAction(Model model, MagicDuelEvent state);

    BeamOptions selectBeamTurnAction(Model model, MagicDuelEvent magicDuelEvent);
}
