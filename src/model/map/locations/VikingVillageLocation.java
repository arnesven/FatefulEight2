package model.map.locations;

import model.Model;
import model.SteppingMatrix;
import model.journal.PartSixStoryPart;
import model.journal.StoryPart;
import model.mainstory.GainSupportOfVikingsTask;
import model.races.AllRaces;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.beangame.BeanGameEvent;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.VikingVillageDailyActionState;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.events.*;
import model.states.warehouse.WarehouseEvent;
import util.MyLists;
import util.MyRandom;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.DailyActionSubView;
import view.subviews.ImageSubView;
import view.subviews.SubView;
import view.subviews.VikingVillageSubView;

import java.awt.*;
import java.util.List;

public class VikingVillageLocation extends TownishLocation {

    private static final String DESCRIPTION = "The village of the Vikings of the North.";
    private final ImageSubView subView;

    public VikingVillageLocation() {
        super("Viking Village");
        subView = new ImageSubView("vikingvillage", "VIKING VILLAGE", "You are at the Viking Village", true);
    }

    @Override
    public SubView getImageSubView(Model model) {
        return subView;
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("vikingvillageupper", 0x1AB, MyColors.BLACK, MyColors.GOLD, MyColors.WHITE, MyColors.TAN);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("vikingvillagelower", 0x1BB, MyColors.BLACK, MyColors.GOLD, MyColors.WHITE, MyColors.TAN);
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new VikingVillageHelpDialog(view);
    }

    @Override
    public String getPlaceName() {
        return "the Viking Village";
    }

    @Override
    public GameState getDailyActionState(Model model) {
        return new VikingVillageDailyActionState(model, this);
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        return new VikingVillageDailyActionState(model, this);
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        GainSupportOfVikingsTask task = getVikingTask(model);
        if (task != null && !task.isCompleted()) {
            DailyEventState event = task.generateEvent(model, false);
            if (event != null) {
                return event;
            }
        }
        if (MyRandom.rollD10() >= 4) {
            return MyRandom.sample(List.of(
                    new BeanGameEvent(model, AllRaces.NORTHERN_HUMAN),
                    new MarketEvent(model),
                    new MuggingEvent(model),
                    new SmithEvent(model),
                    new ThiefEvent(model),
                    new WorkshopEvent(model),
                    new BoozersEvent(model),
                    new WarehouseEvent(model),
                    new GuideEvent(model, 1)

            ));
        }
        return super.generateEvent(model);
    }

    public static GainSupportOfVikingsTask getVikingTask(Model model) {
        StoryPart part = MyLists.last(model.getMainStory().getStoryParts());
        if (part instanceof PartSixStoryPart) {
            PartSixStoryPart partSix = (PartSixStoryPart) part;
            if (partSix.getRemotePeopleTask() instanceof GainSupportOfVikingsTask) {
                return (GainSupportOfVikingsTask) partSix.getRemotePeopleTask();
            }
        }
        return null;
    }

    @Override
    public boolean inhibitOnRoadSubview() {
        return true;
    }

    @Override
    public Point getTavernPosition() {
        return new Point(2, 7);
    }

    @Override
    public boolean noBoat() {
        return false;
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 6, 6)); // TODO: Viking shop
    }

    @Override
    public DailyActionSubView makeActionSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new VikingVillageSubView(advancedDailyActionState, matrix, getName());
    }

    @Override
    public int charterBoatEveryNDays() {
        return 0;
    }

    @Override
    public boolean bothBoatAndCarriage() {
        return false;
    }

    @Override
    public String getGeographicalDescription() {
        return DESCRIPTION;
    }

    private static class VikingVillageHelpDialog extends HelpDialog {
        public VikingVillageHelpDialog(GameView view) {
            super(view, "Viking Village", DESCRIPTION);
        }
    }
}
