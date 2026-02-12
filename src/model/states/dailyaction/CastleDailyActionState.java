package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.map.CastleLocation;
import model.states.dailyaction.castle.GentlepersonsClubNode;
import model.states.dailyaction.castle.GoToCastleActionNode;
import model.states.dailyaction.castle.LeagueOfMagesOfficeNode;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.dailyaction.tavern.TavernNode;
import model.states.dailyaction.town.CampOutsideOfTownNode;
import model.states.dailyaction.town.FlagPoleNode;
import model.states.events.GentlepersonsClubEvent;
import model.states.events.LeagueOfMagesEvent;
import sound.BackgroundMusic;
import view.subviews.CastleSubView;
import view.subviews.DailyActionSubView;

import java.awt.*;

public class CastleDailyActionState extends AdvancedDailyActionState {

    private final CastleLocation location;

    public CastleDailyActionState(Model model, CastleLocation castleLocation, boolean freeLodge, boolean freeRations) {
        super(model);
        this.location = castleLocation;
        super.addNode(3, 4, new StayHereNode());
        addNode(7, 1, new FlagPoleNode());
        super.addNode(location.getTavernPosition().x, location.getTavernPosition().y, new TavernNode(freeLodge));
        super.addNode(3, 2, new GoToCastleActionNode(location));
        if (model.getParty().hasHeadquartersIn(castleLocation)) {
            super.addNode(0, TOWN_MATRIX_ROWS - 1, new HeadquartersNode(model));
        } else {
            super.addNode(0, TOWN_MATRIX_ROWS-1, new CampOutsideOfTownNode(freeRations, model, CastleSubView.GROUND_COLOR,
                    CastleSubView.GROUND_COLOR_NIGHT, "Camp outside the castle walls"));
        }
        super.addNode(location.getTravelNodePosition().x, location.getTravelNodePosition().y,
                new TravelNode(model, CastleSubView.GROUND_COLOR, CastleSubView.GROUND_COLOR_NIGHT));
        super.addNode(6, 6, new WorkBenchNode(model, CastleSubView.GROUND_COLOR, CastleSubView.GROUND_COLOR_NIGHT));
        super.addNode(2, 6, new StableNode(model, CastleSubView.GROUND_COLOR, CastleSubView.GROUND_COLOR_NIGHT));
        addNode(4, 6, new SaveGameNode());
        addNode(7, 1, new FlagPoleNode());
        for (GeneralShopNode shop : location.getShops(model)) {
            addNode(shop.getColumn(), shop.getRow(), shop);
        }
        if (LeagueOfMagesEvent.isMember(model)) {
            Point league = location.getLeaguePosition();
            addNode(league.x, league.y, new LeagueOfMagesOfficeNode());
        }
        if (GentlepersonsClubEvent.isMember(model)) {
            Point club = location.getClubPosition();
            addNode(club.x, club.y, new GentlepersonsClubNode());
        }
    }

    public CastleDailyActionState(Model model, CastleLocation castleLocation) {
        this(model, castleLocation, false, false);
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(3, 5);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return location.makeActionSubView(model, advancedDailyActionState, matrix);
    }

    @Override
    protected BackgroundMusic getSound() {
        return BackgroundMusic.castleSong;
    }
}
