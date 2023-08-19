package model.map;

import model.Model;
import model.SteppingMatrix;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.dailyaction.*;
import model.states.events.*;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyRandom;
import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.*;

import java.awt.Point;
import java.util.List;

public abstract class CastleLocation extends HexLocation implements UrbanLocation {
    private final String lordName;
    private final MyColors castleColor;
    private final SubView subView;
    private final Sprite questSprite;

    public CastleLocation(String castleName, MyColors castleColor, String lordName) {
        super(castleName);
        this.castleColor = castleColor;
        this.lordName = lordName;
        subView = new ImageSubView("castle", "CASTLE", castleName, true);
        questSprite = new Sprite32x32("halfcastleq", "quest.png", 0x64,
                MyColors.BLACK, MyColors.LIGHT_GRAY, this.castleColor, MyColors.GREEN);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("castleupper", 0xC0, MyColors.BLACK, MyColors.LIGHT_GRAY, this.castleColor);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("castlelower", 0xD0, MyColors.BLACK, MyColors.LIGHT_GRAY, this.castleColor);
    }

    public SubView getImageSubView() {
        return subView;
    }

    @Override
    public void travelTo(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.citySong);
    }

    @Override
    public void travelFrom(Model model) {
        model.playMainSong();
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
        int dieRoll = MyRandom.rollD10();
        if (dieRoll >= 3) {
            return MyRandom.sample(List.of(
                    new CaptainEvent(model),
                    new SmithEvent(model),
                    new ArcherEvent(model),
                    new NoblemanEvent(model),
                    new NoblemanEvent(model),
                    new PriestEvent(model),
                    new CourtWizardEvent(model),
                    new ArmoryEvent(model),
                    new JesterEvent(model),
                    new MayorEvent(model),
                    new TournamentEvent(model, this),
                    new GuideEvent(model),
                    new GuideEvent(model)
            ));
        }
        return new NoEventState(model);
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
}
