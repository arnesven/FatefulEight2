package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.states.GameState;
import model.states.events.NoEventState;
import util.MyRandom;
import util.MyStrings;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

import java.util.ArrayList;
import java.util.List;

public class StableNode extends DailyActionNode {
    private final Sprite32x32 daySprite;
    private final Sprite32x32 nightSprite;
    private final Model model;
    private final List<Horse> horses;

    public StableNode(Model model, MyColors dayColor, MyColors nightColor) {
        super("Stables");
        this.model = model;
        daySprite = new Sprite32x32("stablestown", "world_foreground.png", 0xAA,
                dayColor, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.ORANGE);
        nightSprite = new Sprite32x32("stablestown", "world_foreground.png", 0xAA,
                nightColor, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.ORANGE);
        this.horses = makeHorses();
    }

    private List<Horse> makeHorses() {
        List<Horse> result = new ArrayList<>();
        int numberOfHorses = MyRandom.randInt(1, 3) + MyRandom.randInt(1, 4);
        for (int i = numberOfHorses; i > 0; --i) {
            result.add(HorseHandler.generateHorse());
        }
        return result;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new TradeHorsesState(model, horses);
    }
    @Override
    public Sprite getBackgroundSprite() {
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            return nightSprite;
        }
        return daySprite;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
        if (state.isMorning()) {
            model.setTimeOfDay(TimeOfDay.MIDDAY);
        }
    }
}
