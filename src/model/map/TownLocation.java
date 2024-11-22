package model.map;

import model.Model;
import model.SteppingMatrix;
import model.headquarters.Headquarters;
import model.states.*;
import model.states.beangame.BeanGameEvent;
import model.states.dailyaction.*;
import model.states.events.*;
import util.MyRandom;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.help.TownHelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.*;

import java.awt.*;
import java.util.List;

public abstract class TownLocation extends HexLocation implements UrbanLocation {
    private final String townName;
    private final SubView subView;
    private final String lordName;
    private final boolean isCoastal;
    private final Sprite QUEST_SPRITE = new Sprite32x32("halftownspriteqmb", "quest.png", 0x52,
            MyColors.BLACK, MyColors.LIGHT_YELLOW, MyColors.GRAY, MyColors.GREEN);
    private Headquarters headquarters;

    public TownLocation(String townName, String lordName, boolean isCoastal) {
        super("Town of " + townName);
        this.townName = townName;
        this.lordName = lordName;
        subView = new ImageSubView("town", "TOWN", "Town of " + townName, true);
        this.isCoastal = isCoastal;
        headquarters = Headquarters.makeRandomHeadquarters(this);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("townlower", 0x90, MyColors.BLACK, MyColors.LIGHT_YELLOW, MyColors.GRAY);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("townupper", 0x80, MyColors.BLACK, MyColors.LIGHT_YELLOW, MyColors.GRAY);
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public boolean hasLodging() {
        return true;
    }

    @Override
    public SubView getImageSubView() {
        return subView;
                }

    @Override
    public void travelTo(Model model) { }

    @Override
    public void travelFrom(Model model) { }

    @Override
    public String getLordName() {
        return lordName;
    }

    @Override
    public String getPlaceName() {
        return "the Town of " + townName;
    }

    @Override
    public boolean givesQuests() {
        return true;
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        return new BeanGameEvent(model); /*
        if (MyRandom.rollD10() >= 3) {
            return MyRandom.sample(List.of(
                    new AcceptDeliveryEvent(model),
                    new AlchemistEvent(model),
                    new ArcheryRangeEvent(model),
                    new ArtisanEvent(model),
                    new AssassinEvent(model),
                    new BakeryEvent(model),
                    new BarbershopEvent(model),
                    new BathHouseEvent(model),
                    new BeanGameEvent(model),
                    new BorrowedMoneyEvent(model),
                    new CatInATreeEvent(model),
                    new CharlatanEvent(model),
                    new ConstableEvent(model),
                    new CourierEvent(model),
                    new DollyEvent(model),
                    new FriendEvent(model),
                    new GamblerEvent(model),
                    new LoveLetterEvent(model),
                    new MageEvent(model),
                    new MarketEvent(model),
                    new MayorEvent(model),
                    new MuggingEvent(model),
                    new OrcRaidEvent(model),
                    new PlayEvent(model),
                    new PsychicRitual(model),
                    new SmithEvent(model),
                    new ThiefEvent(model),
                    new ThievesGuildEvent(model),
                    new TimedHorseRaceEvent(model),
                    new TroubadourEvent(model),
                    new WantedPosterEvent(model),
                    new WorkshopEvent(model),
                    new InvestInShopEvent(model),
                    new GardenMazeEvent(model),
                    new BoozersEvent(model),
                    new KidsWantFireworksEvent(model),
                    new GuideEvent(model, 1),
                    new GuideEvent(model, 2)
                    // WarehouseEvent = Sokoban
            ));
        }
        return new NoEventState(model); */
    }

    @Override
    public GameState getDailyActionState(Model model) {
        return new TownDailyActionState(model, isCoastal, this);
    }

    public Point getTavernPosition() {
        return new Point(1, 4);
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        model.getMainStory().setVisitedTown(true);
        return new TownDailyActionState(model, isCoastal, this, freeLodge, freeRations);
    }

    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 6, 1));
    }

    @Override
    public DailyActionSubView makeActionSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new TownSubView(advancedDailyActionState, matrix, isCoastal, getTownName());
    }

    @Override
    public Point getTravelNodePosition() {
        return new Point(AdvancedDailyActionState.TOWN_MATRIX_COLUMNS-1, AdvancedDailyActionState.TOWN_MATRIX_ROWS-2);
    }

    @Override
    public String getLocationType() {
        return "town";
    }

    @Override
    public String getLordDwelling() {
        return "Town Hall";
    }

    public String getTownName() {
        return townName;
    }

    public boolean noBoat() {
        return false;
    }

    @Override
    public Sprite getTownOrCastleSprite() {
        return QUEST_SPRITE;
    }

    @Override
    public String getLordTitle() {
        return getLordName().split(" ")[0].toLowerCase();
    }

    @Override
    public Sprite getExitSprite() {
        return TownHallSubView.DOOR;
    }

    public int charterBoatEveryNDays() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Point getCareerOfficePosition() { return null; }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new TownHelpDialog(view);
    }

    @Override
    public Headquarters getRealEstate() {
        return headquarters;
    }

    @Override
    public boolean bothBoatAndCarriage() {
        return false;
    }
}
