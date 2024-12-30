package model.states.duel;

import model.Model;
import model.states.duel.actions.BeamOptions;
import model.states.duel.actions.MagicDuelAction;

public interface DuelistController {
    MagicDuelAction selectNormalTurnAction(Model model, MagicDuelEvent state);

    BeamOptions selectBeamTurnAction(Model model, MagicDuelEvent magicDuelEvent);

    MagicDuelist getDuelist();
}
