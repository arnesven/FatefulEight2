package model.states.dailyaction.town;

import model.Model;
import model.SteppingMatrix;
import model.Summon;
import model.TimeOfDay;
import model.items.puzzletube.FindPuzzleDestinationTask;
import model.map.UrbanLocation;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.VisitLordDailyActionState;
import model.states.events.SilentNoEventState;
import model.tasks.MonsterHuntDestinationTask;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.DailyActionSubView;
import view.subviews.TownHallSubView;
import view.subviews.TownSubView;

import java.awt.*;

public class TownHallNode extends DailyActionNode {

    private static final Sprite SPRITE = new Sprite32x32("townhallleft", "world_foreground.png", 0x62,
            TownSubView.STREET_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
    private static final Sprite SPRITE2 = new Sprite32x32("townhallright", "world_foreground.png", 0x63,
            TownSubView.STREET_COLOR, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
    private final boolean lordIsIn;
    private boolean admitted = false;

    public TownHallNode() {
        super("Town Hall");
        this.lordIsIn = MyRandom.flipCoin();
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        UrbanLocation location = ((UrbanLocation)model.getCurrentHex().getLocation());
        if (model.getParty().getSummons().containsKey(location.getPlaceName()) ||
                MonsterHuntDestinationTask.hasTaskAtCurrentLocation(model) ||
                FindPuzzleDestinationTask.hasTaskAtCurrentLocation(model)) {
            state.println("You have been admitted to town hall!");
            admitted = true;
            return new VisitMayorDailyActionState(model,
                    model.getParty().getSummons().get(location.getPlaceName()), location, false);
        }
        if (model.getTimeOfDay() == TimeOfDay.EVENING || !lordIsIn) {
            state.print("Town hall is closed, would you like to attempt to break in? (Y/N) ");
            if (state.yesNoInput()) {
                boolean success = model.getParty().doSoloLockpickCheck(model, state, 7);
                if (success) {
                    state.println("You broke into town hall!");
                    admitted = true;
                    return new BreakIntoTownHallDailyActionState(model, null, location);
                }
            }
        } else {
            state.print("You are not admitted to the town hall, would you like to attempt to break in? (Y/N) ");
            if (state.yesNoInput()) {
                boolean success = model.getParty().doSoloLockpickCheck(model, state, 7);
                if (success) {
                    state.println("You broke into town hall!");
                    admitted = true;
                    return new VisitMayorDailyActionState(model, null, location, false);
                }
            }
        }
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            admitted = true;
            return new SilentNoEventState(model);
        }
        return model.getCurrentHex().getDailyActionState(model);
    }

    @Override
    public boolean returnNextState() {
        return admitted;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().put(p.x, p.y, getBackgroundSprite());
        model.getScreenHandler().put(p.x+4, p.y, SPRITE2);
    }

    @Override
    public Point getCursorShift() {
        Point p = super.getCursorShift();
        p.x += 2;
        return p;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState townDailyActionState, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }

    private static class VisitMayorDailyActionState extends VisitLordDailyActionState {
        private final boolean breakIn;

        public VisitMayorDailyActionState(Model model, Summon summon, UrbanLocation location, boolean breakIn) {
            super(model, summon, location, breakIn);
            this.breakIn = breakIn;
        }

        @Override
        protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
            return new TownHallSubView(advancedDailyActionState, matrix, !breakIn);
        }
    }

    private static class BreakIntoTownHallDailyActionState extends VisitMayorDailyActionState {
        public BreakIntoTownHallDailyActionState(Model model, Summon summon, UrbanLocation location) {
            super(model, summon, location, true);
        }
    }
}
