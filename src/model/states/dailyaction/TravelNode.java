package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.states.GameState;
import model.states.TravelState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CastleSubView;
import view.subviews.TownSubView;

class TravelNode extends DailyActionNode {
    private final Sprite daySprite;
    private final Sprite32x32 nightSprite;
    private final Model model;

    public TravelNode(Model model, MyColors dayColor, MyColors nightColor) {
        super("Travel");
        this.model = model;
        daySprite = new Sprite32x32("travel", "world_foreground.png", 0x32,
                dayColor, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_YELLOW);
        nightSprite = new Sprite32x32("travel", "world_foreground.png", 0x32,
                nightColor, TownSubView.PATH_COLOR, MyColors.BLACK, MyColors.LIGHT_YELLOW);
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        model.getParty().setOnRoad(true);
        return new TravelState(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            return nightSprite;
        }
        return daySprite;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState townDailyActionState, Model model) {
        if (townDailyActionState.isMorning()) {
            if (model.getParty().getFood() < model.getParty().size()*3 && model.getParty().getGold() > 0) {
                townDailyActionState.print("You are about to leave town but are rather low on rations. Are you sure you want to leave? (Y/N) ");
                return townDailyActionState.yesNoInput();
            }
            return true;
        }
        townDailyActionState.println("It's too late in the day to travel. You will have to wait until tomorrow.");
        return false;
    }

    @Override
    public boolean exitsCurrentLocale() {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        // Done by state
    }

}
