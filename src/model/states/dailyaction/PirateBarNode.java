package model.states.dailyaction;

import model.Model;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.items.PirateItem;
import model.states.GameState;
import model.states.NullGameState;
import model.states.events.SilentNoEventState;
import util.MyLists;
import view.MyColors;
import view.sprites.SignSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

import java.awt.*;

public class PirateBarNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("uncleshouse", "world_foreground.png", 0x22,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.DARK_RED, MyColors.PEACH);

    private static final Sprite INN_SIGN = new SignSprite("innisgn", 0x07, MyColors.BLACK, MyColors.WHITE);
    private boolean admitted;

    public PirateBarNode() {
        super("The Sunken Worlds");
        this.admitted = false;
    }

    private static boolean hasPirateClothing(GameCharacter chara) {
        return chara.getEquipment().getWeapon().isOfType(PirateItem.class) ||
                chara.getEquipment().getClothing() instanceof PirateItem ||
                chara.getEquipment().getAccessory() instanceof PirateItem;
    }

    private boolean hasPirateClothing(Model model) {
        return MyLists.all(model.getParty().getPartyMembers(), PirateBarNode::hasPirateClothing);
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new GoToPirateBarState(model, state);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public Sprite getForegroundSprite() {
        return INN_SIGN;
    }
    @Override
    public boolean returnNextState() {
        return admitted;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }
    
    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(getBackgroundSprite().getName(), new Point(p), getBackgroundSprite());
        Sprite fg = getForegroundSprite();
        if (fg != null) {
            p.x += 2;
            p.y += 2;
            model.getScreenHandler().register("objectforeground", p, fg, 1);
        }
    }

    private class GoToPirateBarState extends GameState {
        private final AdvancedDailyActionState prevState;

        public GoToPirateBarState(Model model, AdvancedDailyActionState state) {
            super(model);
            this.prevState = state;
        }

        @Override
        public GameState run(Model model) {
            if (model.getTimeOfDay() == TimeOfDay.MORNING || model.getTimeOfDay() == TimeOfDay.MIDDAY) {
                println("The Sunken World is not open during the day.");
                admitted = false;
                return prevState;
            }
            if (hasPirateClothing(model)) {
                println("The bouncer lets you into the bar.");
                admitted = true;
                return new VisitSunkenWorldsEvent(model);
            }
            admitted = true;
            return new BouncedAtSunkenWorldsEvent(model);
        }
    }
}
