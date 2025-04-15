package model.map.locations;

import model.Model;
import model.SteppingMatrix;
import model.headquarters.Headquarters;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.beangame.BeanGameEvent;
import model.states.dailyaction.*;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.events.*;
import model.states.warehouse.WarehouseEvent;
import util.MyRandom;
import view.GameView;
import view.MyColors;
import view.ScreenHandler;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.PirateHavenBobbingShipSprite;
import view.sprites.Sprite;
import view.subviews.*;

import java.awt.*;
import java.util.List;

public class PirateHavenLocation extends TownishLocation {

    private static final Sprite BOBBING_SHIP = new PirateHavenBobbingShipSprite();
    private static final String DESCRIPTION = "A shanty town where pirates, robbers and bandits have their hideouts.";
    private final SubView subView;

    public PirateHavenLocation() {
        super("Pirate Haven");
        subView = new ImageSubView("piratehaven", "PIRATE HAVEN",
                "You are at the Pirate Haven.", true);
    }

    @Override
    public SubView getImageSubView() {
        return subView;
    }

    @Override
    public boolean hasLodging() {
        return true;
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("piratehavenupper", 0x1A2, MyColors.BLACK, MyColors.PEACH, MyColors.DARK_RED, MyColors.LIGHT_BLUE);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("piratehavenlower", 0x1B2, MyColors.BLACK, MyColors.PEACH, MyColors.DARK_RED, MyColors.LIGHT_BLUE);
    }

    @Override
    public void drawUpperHalf(ScreenHandler screenHandler, int x, int y, int flag) {
        super.drawUpperHalf(screenHandler, x, y, flag);
        screenHandler.register(BOBBING_SHIP.getName(), new Point(x, y), BOBBING_SHIP, 1, 4, -5);
    }

    @Override
    public GameState getDailyActionState(Model model) {
        return new PirateHavenDailyActionState(model, this);
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        return new PirateHavenDailyActionState(model, this);
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new PirateHavenHelpDialog(view);
    }

    @Override
    public String getPlaceName() {
        return "the Pirate Haven";
    }

    @Override
    public Point getTavernPosition() {
        return new Point(4, 3);
    }

    @Override
    public boolean noBoat() {
        return false;
    }

    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new PirateShop(model, 2, 4));
    }

    @Override
    public DailyActionSubView makeActionSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new PirateHavenSubView(advancedDailyActionState, matrix, true, getName());
    }

    @Override
    public String getLocationType() {
        return "Town";
    }

    @Override
    public int charterBoatEveryNDays() {
        return 1;
    }

    @Override
    public boolean bothBoatAndCarriage() {
        return false;
    }

    @Override
    public String getGeographicalDescription() {
        return DESCRIPTION;
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        if (MyRandom.rollD10() >= 3) {
            return MyRandom.sample(List.of(
                    new ArtisanEvent(model),
                    new AssassinEvent(model),
                    new BanditEvent(model),
                    new BeanGameEvent(model),
                    new GamblerEvent(model),
                    new LottoHouseEvent(model),
                    new MarketEvent(model),
                    new MerchantEvent(model),
                    new MuggingEvent(model),
                    new SmugglersEvent(model),
                    new ThiefEvent(model),
                    new BoozersEvent(model),
                    new KidsWantFireworksEvent(model),
                    new WarehouseEvent(model),
                    new PirateFightEvent(model),
                    new DrunkenPirateEvent(model),
                    new GuideEvent(model, 1)
            ));
        }
        return new NoEventState(model);
    }

    private static class PirateHavenHelpDialog extends HelpDialog {
        public PirateHavenHelpDialog(GameView view) {
            super(view, "Pirate Haven", DESCRIPTION);
        }
    }
}
