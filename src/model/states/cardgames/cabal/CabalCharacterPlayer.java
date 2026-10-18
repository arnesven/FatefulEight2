package model.states.cardgames.cabal;

import model.Model;
import model.characters.GameCharacter;
import model.states.cardgames.CardGame;
import model.states.cardgames.CardGamePlayer;
import model.states.cardgames.CardGameState;

import java.util.ArrayList;
import java.util.List;

public class CabalCharacterPlayer extends CabalCardGamePlayer {
    public CabalCharacterPlayer(GameCharacter gc, int obols) {
        super(gc.getFirstName(), gc.getGender(), gc.getRace(), obols, false);
    }

    @Override
    public void takeTurn(Model model, CardGameState state, CardGame cardGame) {
        state.println("You've got the pick. Who do you want to pick from?");
        CabalCardGame cabal = (CabalCardGame)cardGame;
        List<CardGamePlayer> candidates = new ArrayList<>(super.getEligiblePlayers(cabal));

        CabalCardGamePlayer player = (CabalCardGamePlayer)state.selectPlayer(model, candidates);
        cabal.pickRandomCardFromPlayer(state, this, player);
    }

    @Override
    public void runStartOfGameHook(Model model, CardGameState cardGameState, CardGame cardGame) {

    }

    private void internalClaim(CardGameState state, int turn, String prompt) {
        state.print(prompt);
        if (state.yesNoInput()) {
            int stars = claimInput(state, 6 - turn, "Stars");
            int daggers = claimInput(state, 6 - turn, "Daggers");
            setClaim(stars, daggers);
        }
    }

    @Override
    public void makeStartOfTurnClaim(CardGameState state, CabalCardGame cabalCardGame, int round) {
        internalClaim(state, round, "Do you want to make a claim (Y/N): ");
    }

    @Override
    public void makeEndOfTurnClaim(CardGameState state, CabalCardGame cabalCardGame, int turn) {
        internalClaim(state, turn, "Do you want to update your claim (Y/N): ");
    }

    @Override
    public void updateSuspicionsForStartOfTurnClaim(CardGameState state, CabalCardGame cabalCardGame) { }

    @Override
    public void updateSuspicionsForEndOfTurnClaim(CardGameState state, CabalCardGame cabalCardGame) { }

    @Override
    public void updateSuspicionsForPick(CabalCardGame cabalCardGame, CardGamePlayer performer, CardGamePlayer target) {}

    @Override
    public void addToSuspicion(CabalCardGamePlayer target, double suspicionToAdd) {}

    @Override
    public void drawTeamConclusions(CabalCardGame cabalCardGame) {}
}
