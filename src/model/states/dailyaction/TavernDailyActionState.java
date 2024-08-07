package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.actions.Loan;
import model.states.GameState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import view.subviews.DailyActionSubView;
import view.subviews.TavernSubView;
import view.subviews.TownSubView;

import java.awt.*;

public class TavernDailyActionState extends AdvancedDailyActionState {
    private final boolean inTown;

    public TavernDailyActionState(Model model, boolean freeLodging, boolean inTown) {
        super(model);
        this.inTown = inTown;
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            addNode(2, 5, new CardGameNode());
        } else {
            addNode(2, 5, new RecruitNode(model));
        }
        addNode(6, 1, new LodgingNode(freeLodging));
        addNode(2, 3, new TalkToBartenderNode(inTown));
        addNode(5, 5, new TalkToPartyNode());
        if (model.getParty().getActiveTravellers().isEmpty()) {
            addNode(2, 2, new TravellerNode(model));
        }
        if (inTown) {
            Point doorPos = getDoorPosition();
            addNode(doorPos.x, doorPos.y, new ExitTavernNode());
            if (model.getDay() % 3 == 0 && model.getDay() < 100 - Loan.REPAY_WITHIN_DAYS * 2) {
                addNode(4, 2, new TakeLoanNode());
            } else {
                addNode(4, 2, new GuideNode());
            }
        } else {
            addNode(7, 8, new TravelFromInnNode(model,
                    TownSubView.GROUND_COLOR, TownSubView.GROUND_COLOR_NIGHT));
            addNode(1, 8, new CampOutsideOfTownNode(false, model,
                    TownSubView.GROUND_COLOR, TownSubView.GROUND_COLOR_NIGHT, "Make camp outside."));
            addNode(4, 2, new InnShoppingNode(model));
            addNode(6, 3, new SaveGameNode());
        }
    }

    @Override
    public GameState run(Model model) {
        if (inTown && isEvening()) {
            ClientSoundManager.playBackgroundMusic(BackgroundMusic.festiveSong);
        }
        GameState toReturn = super.run(model);
        return toReturn;
    }

    public static Point getDoorPosition() {
        return new Point(3, 7);
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(3, 4);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState,
                                             SteppingMatrix<DailyActionNode> matrix) {
        return new TavernSubView(advancedDailyActionState, matrix, inTown);
    }
}
