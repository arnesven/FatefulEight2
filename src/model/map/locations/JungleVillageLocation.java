package model.map.locations;

import model.Model;
import model.SteppingMatrix;
import model.classes.Classes;
import model.mainstory.jungletribe.GainSupportOfJungleTribeTask;
import model.mainstory.jungletribe.RubiqPuzzleEvent;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.JungleVillageDailyActionState;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.events.*;
import util.MyRandom;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.*;

import java.awt.*;
import java.util.List;

public class JungleVillageLocation extends TownishLocation {
    private static final String DESCRIPTION = "A Village inhabited by the Jungle Tribe.";
    private final ImageSubView subView;

    public JungleVillageLocation() {
        super("Jungle Village");
        subView = new ImageSubView("junglevillage", "JUNGLE VILLAGE",
                "You are at the Jungle Village.", true);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("junglevillageupper", 0x18C, MyColors.BLACK, MyColors.BROWN, MyColors.GRAY, MyColors.DARK_GREEN);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("junglegilalgelower", 0x19C, MyColors.BLACK, MyColors.BROWN, MyColors.GRAY, MyColors.DARK_GREEN);
    }

    @Override
    public SubView getImageSubView(Model model) {
        return subView;
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new JungleVilalgeHelpDialog(view);
    }

    @Override
    public String getPlaceName() {
        return "Jungle Village";
    }

    @Override
    public Point getTavernPosition() {
        return new Point(4, 4);
    }

    @Override
    public boolean noBoat() {
        return true;
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of();
    }

    @Override
    public GameState getDailyActionState(Model model) {
        return new JungleVillageDailyActionState(model, this);
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        return new JungleVillageDailyActionState(model, this);
    }

    @Override
    public DailyActionSubView makeActionSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new JungleVillageSubView(advancedDailyActionState, matrix, getName());
    }

    @Override
    public int charterBoatEveryNDays() {
        return 0;
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        GainSupportOfJungleTribeTask task = GainSupportOfJungleTribeTask.getTask(model);
        if (task != null) {
            DailyEventState event = task.generateTribeCommonerEvent(model);
            if (event != null) {
                return event;
            }
        }
        return MyRandom.sample(List.of(
                new SmithEvent(model, Race.SOUTHERN_HUMAN),
                new AmazonEvent(model),
                new BarbarianEvent(model),
                new FrogmenScoutsEvent(model),
                new MarketEvent(model),
                new MosquitoesEvent(model),
                new MushroomsEvent(model),
                new ShrineEvent(model),
                new SpidersEvent(model),
                new VeteranEvent(model, true, Race.SOUTHERN_HUMAN),
                new MerchantEvent(model, true,
                        PortraitSubView.makeRandomPortrait(Classes.MERCHANT, Race.SOUTHERN_HUMAN))));
    }

    @Override
    public boolean hasLodging() {
        return true;
    }

    @Override
    public boolean bothBoatAndCarriage() {
        return false;
    }

    @Override
    public String getGeographicalDescription() {
        return DESCRIPTION;
    }

    private static class JungleVilalgeHelpDialog extends HelpDialog {
        public JungleVilalgeHelpDialog(GameView view) {
            super(view, "Jungle Village", DESCRIPTION);
        }
    }
}
