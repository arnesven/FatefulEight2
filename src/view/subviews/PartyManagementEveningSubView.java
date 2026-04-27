package view.subviews;

import model.Model;
import model.Party;
import model.SteppingMatrix;
import model.horses.DogHorse;
import model.journal.InitialStoryPart;
import model.journal.PartSixStoryPart;
import model.journal.StoryPart;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.TalkToTravellerNode;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.combat.CombatTheme;
import view.sprites.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartyManagementEveningSubView extends DailyActionSubView {

    private final static Sprite32x32 TENT_LEFT = new PartyManagementTentSprite(false);
    private final static Sprite32x32 TENT_RIGHT = new PartyManagementTentSprite(true);

    public static final Point CAMP_FIRE_POS = new Point(4, 5);
    private static final List<Point> AROUND_CAMP_FIRE = List.of(
            new Point(CAMP_FIRE_POS.x - 1, CAMP_FIRE_POS.y - 1),
            new Point(CAMP_FIRE_POS.x + 1, CAMP_FIRE_POS.y - 1),
            new Point(CAMP_FIRE_POS.x - 1, CAMP_FIRE_POS.y),
            new Point(CAMP_FIRE_POS.x + 1, CAMP_FIRE_POS.y),
            new Point(CAMP_FIRE_POS.x - 1, CAMP_FIRE_POS.y + 1),
            new Point(CAMP_FIRE_POS.x, CAMP_FIRE_POS.y + 1),
            new Point(CAMP_FIRE_POS.x +1, CAMP_FIRE_POS.y + 1));
    private static final List<Point> DOG_POSITIONS = List.of(new Point(3, 3), new Point(5, 3),
            new Point(2, 3), new Point(2, 4));
    private static final Sprite ROAD_SPRITE = new Sprite32x32("roadoverlay", "combat.png", 0x7D,
            MyColors.TRANSPARENT, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.BROWN);
    private final ArrayList<Point> campfirePositions;
    private final List<MyPair<RunOnceAnimationSprite, Point>> callouts = new ArrayList<>();
    private final Point dogPosition;
    private final boolean isOnRoad;
    private Sprite dogMini = null;
    private CombatTheme theme = null;

    public PartyManagementEveningSubView(Model model, AdvancedDailyActionState advancedDailyActionState,
                                         SteppingMatrix<DailyActionNode> matrix) {
        super(advancedDailyActionState, matrix, DailyActionSubView.DIRECT_MOVEMENT);
        this.campfirePositions = new ArrayList<>(AROUND_CAMP_FIRE);
        Collections.shuffle(campfirePositions);
        dogPosition = MyRandom.sample(DOG_POSITIONS);
        theme = model.getCurrentHex().getNightTimeCombatTheme();
        this.isOnRoad = model.getParty().isOnRoad();
    }

    @Override
    protected void drawBackground(Model model) {
        theme.drawBackground(model, X_OFFSET, Y_OFFSET);
        if (isOnRoad) {
            drawRoad(model);
        }
        drawExtraTents(model);
        drawPartyArea(model, campfirePositions);
        drawDog(model);
        drawMainStoryThings(model);
        drawCallouts(model);
    }

    private void drawMainStoryThings(Model model) {
        // FEATURE: Let story part draw things between tent 1 and 2,
        // For example, draw Everix in Part Six, first lying down and then standing up
        // after talking to her.
    }

    private void drawRoad(Model model) {
        for (int i = 0; i < 8; ++i) {
            Point p = convertToScreen(new Point(i, 8));
            model.getScreenHandler().register("road", p, ROAD_SPRITE);
        }
    }

    private void drawDog(Model model) {
        if (model.getParty().hasDog()) {
            DogHorse dog = model.getParty().getDog();
            if (dogMini == null) {
                this.dogMini = dog.getMiniSprite();
            }
            Point p = convertToScreen(dogPosition);
            model.getScreenHandler().register(dogMini.getName(), p, dogMini);
        }
    }

    private void drawExtraTents(Model model) {
        if (model.getParty().getInventory().getTentSize() > 6) {
            Point p = convertToScreen(new Point(2, 2));
            model.getScreenHandler().register(TENT_LEFT.getName(), p, TENT_LEFT);
        }
        if (model.getParty().getInventory().getTentSize() > 4) {
            Point p = convertToScreen(new Point(5, 2));
            model.getScreenHandler().register(TENT_RIGHT.getName(), p, TENT_RIGHT);
        }

    }

    @Override
    protected String getPlaceType() {
        return "CAMP";
    }

    private synchronized void drawCallouts(Model model) {
        for (MyPair<RunOnceAnimationSprite, Point> effect : new ArrayList<>(callouts)) {
            model.getScreenHandler().register(effect.first.getName(), effect.second, effect.first, 3);
            if (effect.first.isDone()) {
                callouts.remove(effect);
                AnimationManager.unregister(effect.first);
            }
        }
    }

    public synchronized void addCalloutAtNode(TalkToTravellerNode node, int length) {
        Point p = getMatrix().getPositionFor(node);
        Point p2 = new Point(p.x+1, p.y);
        callouts.add(new MyPair<>(new NonCombatSpeechBubble(length), convertToScreen(p2)));
    }

    private static class PartyManagementTentSprite extends Sprite32x32 {
        public PartyManagementTentSprite(boolean flipped) {
            super("tent" + (flipped ? "left" : "right"), "world_foreground.png",
                    0xEA, MyColors.TRANSPARENT, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.YELLOW);
            setFlipHorizontal(flipped);
        }
    }
}
