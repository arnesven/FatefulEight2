package model.states;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.map.HexLocation;
import model.states.dailyaction.*;
import view.subviews.DailyActionSubView;
import view.subviews.PartyManagementEveningSubView;

import java.awt.*;

public class PartyManagementEveningState extends AdvancedDailyActionState {
    private final boolean freeLodging;
    private final boolean freeRations;

    public PartyManagementEveningState(Model model, boolean freeLodging, boolean freeRations) {
        super(model);
        this.freeLodging = freeLodging;
        this.freeRations = freeRations;
        addNode(4, 2, new GoToSleepNode(freeRations, model));
        addNode(PartyManagementEveningSubView.CAMP_FIRE_POS.x, PartyManagementEveningSubView.CAMP_FIRE_POS.y-1,
                new TalkToPartyAtCampFireNode());
        if (!model.getParty().getHorseHandler().isEmpty()) {
            addNode(1, 2, new CheckOnHorsesNode(model.getParty().getHorseHandler()));
        }

        for (int i = 0; i < model.getParty().getActiveTravellers().size() && i < 5; ++i) {
            addNode(6, 7-i, new TalkToTravellerNode(model.getParty().getActiveTravellers().get(i)));
        }

        int dragonIndex = 0;
        for (GameCharacter gc : model.getParty().getTamedDragons().keySet()) {
             addNode(1, 7-dragonIndex, new CheckOutTamedDragonNode(model.getParty().getTamedDragons().get(gc)));
             dragonIndex++;
             if (dragonIndex >= 4) {
                 break;
             }
        }
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(4, 3);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState,
                                             SteppingMatrix<DailyActionNode> matrix) {
        return new PartyManagementEveningSubView(model, advancedDailyActionState, matrix);
    }

    @Override
    public GameState run(Model model) {
        if (model.getSettings().skipPartyManagementEveningState()) {
            return getEveningState(model);
        }
        super.run(model);
        return getEveningState(model);
    }

    private GameState getEveningState(Model model) {

        return new EveningState(model, freeLodging, freeRations, true);
    }
}
