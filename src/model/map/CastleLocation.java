package model.map;

import model.Model;
import model.SteppingMatrix;
import model.achievements.Achievement;
import model.headquarters.Headquarters;
import model.races.Race;
import model.states.AcceptDeliveryEvent;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.beangame.BeanGameEvent;
import model.states.dailyaction.*;
import model.states.events.*;
import util.MyRandom;
import view.GameView;
import view.MyColors;
import view.help.CastleHelpDialog;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public abstract class CastleLocation extends HexLocation implements UrbanLocation {
    private final String lordName;
    private final MyColors castleColor;
    private final SubView subView;
    private final Sprite questSprite;
    private final Race lordRace;
    private final Headquarters headquarters;
    // FEATURE: Unique decorations for towns:  Sparse trees for Ardh, dunes for Sunblaze, thick trees for Bogdown, Snow covered trees for arkvale
    public CastleLocation(String castleName, MyColors castleColor, String lordName, Race lordRace) {
        super(castleName);
        this.castleColor = castleColor;
        this.lordName = lordName;
        subView = new ImageSubView("castle", "CASTLE", castleName, true);
        questSprite = new Sprite32x32("halfcastleq", "quest.png", 0x64,
                MyColors.BLACK, MyColors.LIGHT_GRAY, this.castleColor, MyColors.GREEN);
        this.lordRace = lordRace;
        this.headquarters = new Headquarters(this, Headquarters.MEDIUM_SIZE);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("castleupper", 0xC0, MyColors.BLACK, MyColors.LIGHT_GRAY, this.castleColor);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("castlelower", 0xD0, MyColors.BLACK, MyColors.LIGHT_GRAY, this.castleColor);
    }

    public SubView getImageSubView(Model model) {
        return subView;
    }

    @Override
    public void travelTo(Model model) { }

    @Override
    public void travelFrom(Model model) {
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
    public String getLordName() {
        return lordName;
    }

    @Override
    public String getPlaceName() {
        return getName();
    }

    @Override
    public Point getTavernPosition() {
        return new Point(6, 4);
    }

    @Override
    public boolean noBoat() {
        return true;
    }

    @Override
    public DailyActionSubView makeActionSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new CastleSubView(advancedDailyActionState, matrix, getPlaceName(), castleColor);
    }

    @Override
    public boolean givesQuests() {
        return true;
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        return new PsychicRitual(model); /*
        int dieRoll = MyRandom.rollD10();
        if (dieRoll >= 3) {
            List<DailyEventState> dailyEvents = new ArrayList<>(List.of(
                    new AcceptDeliveryEvent(model),
                    new ArcherEvent(model),
                    new ArcheryContestEvent(model, this),
                    new ArmoryEvent(model),
                    new ArtisanEvent(model),
                    new BakeryEvent(model),
                    new BarbershopEvent(model),
                    new BathHouseEvent(model),
                    new BeanGameEvent(model),
                    new CaptainEvent(model),
                    new FriendEvent(model),
                    new GuideEvent(model, 2),
                    new GuideEvent(model, 3),
                    new HorseRaceCup(model, this),
                    new InvestInShopEvent(model),
                    new JesterEvent(model),
                    new MagicDuelContestEvent(model, this),
                    new MayorEvent(model),
                    new NoblemanEvent(model),
                    new NoblemanEvent(model),
                    new PriestEvent(model),
                    new PsychicRitual(model),
                    new SmithEvent(model),
                    new ThiefEvent(model),
                    new TournamentEvent(model, this),
                    new TroubadourEvent(model),
                    new WantedPosterEvent(model)
                    // FEATURE: new BookShopEvent() (where you can order a spell book)
                    // new SewersEvent
                    // new BeautySalonEvent
            ));
            if (!LeagueOfMagesEvent.isMember(model)) {
                dailyEvents.add(new LeagueOfMagesEvent(model));
            }
            if (!GentlepersonsClubEvent.isMember(model)) {
                dailyEvents.add(new GentlepersonsClubEvent(model));
            }
            return MyRandom.sample(dailyEvents);
        }
        return new NoEventState(model); */
    }

    @Override
    public GameState getDailyActionState(Model model) {
        return new CastleDailyActionState(model, this);
    }

    @Override
    public GameState getEveningState(Model model, boolean freeLodge, boolean freeRations) {
        model.getMainStory().setVisitedCastle(true);
        return new CastleDailyActionState(model, this, freeLodge, freeRations);
    }

    @Override
    public Point getTravelNodePosition() {
        return new Point(AdvancedDailyActionState.TOWN_MATRIX_COLUMNS-1, AdvancedDailyActionState.TOWN_MATRIX_ROWS-1);
    }

    public String getLordTitle() {
        return lordName.split(" ")[0].toLowerCase();
    }

    @Override
    public Sprite getTownOrCastleSprite() {
        return questSprite;
    }

    @Override
    public String getLocationType() {
        return "castle";
    }

    @Override
    public String getLordDwelling() {
        return "Keep";
    }

    @Override
    public Sprite getExitSprite() {
        return KeepSubView.RUG;
    }

    public int charterBoatEveryNDays() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Point getCareerOfficePosition() {
        return null;
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new CastleHelpDialog(view);
    }

    public Race getLordRace() {
        return lordRace;
    }

    public static String placeNameToKingdom(String placeName) {
        return "kingdom of " + placeNameShort(placeName);
    }

    public static String placeNameShort(String placeName) {
        return placeName.replace("Castle ", "").replace(" Castle", "");
    }

    public MyColors getCastleColor() {
        return castleColor;
    }

    @Override
    public Headquarters getRealEstate() {
        return headquarters;
    }

    public abstract Point getLeaguePosition();

    public abstract Point getClubPosition();

    @Override
    public boolean bothBoatAndCarriage() {
        return false;
    }

    public abstract List<Point> getExtraKingdomPositions();

    @Override
    public HexLocation makePastSelf() {
        return null;
    }

    public Achievement.Data getDungeonAchievement() {
        return new Achievement.Data(getName(), "Dungeons of " + getName(),
                "You successfully cleared the dungeon under " + getName() +
                        " and vanquished the evil that dwelt there.");
    }
}
