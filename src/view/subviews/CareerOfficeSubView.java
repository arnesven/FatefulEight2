package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.races.Race;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.tavern.TavernDailyActionState;
import model.states.dailyaction.town.CareerOfficeState;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class CareerOfficeSubView extends RoomDailyActionSubView {

    public static final Sprite COACH1_SPRITE = new Sprite32x32("coach1", "world_foreground.png", 0x5B,
            MyColors.BLACK, MyColors.LIGHT_BLUE, Race.NORTHERN_HUMAN.getColor(), MyColors.YELLOW);
    public static final Sprite COACH2_SPRITE = new Sprite32x32("coach2", "world_foreground.png", 0x5B,
            MyColors.BLACK, MyColors.LIGHT_BLUE, Race.NORTHERN_HUMAN.getColor(), MyColors.ORANGE);
    public static final Sprite COACH3_SPRITE = new Sprite32x32("coach3", "world_foreground.png", 0x5B,
            MyColors.BLACK, MyColors.LIGHT_BLUE, Race.NORTHERN_HUMAN.getColor(), MyColors.GREEN);

    private static final Sprite LOWER_WALL = new Sprite32x32("lowerwall", "world_foreground.png", 0x24,
            MyColors.DARK_GRAY, MyColors.PINK, MyColors.TAN);

    private static final Sprite DOOR = new Sprite32x32("door", "world_foreground.png", 0x34,
            MyColors.DARK_GRAY, MyColors.PINK, MyColors.TAN, MyColors.DARK_RED);
    public static final Sprite OVER_DOOR = new Sprite32x32("overdoor", "world_foreground.png", 0x06,
            MyColors.DARK_GRAY, MyColors.PINK, MyColors.TAN, MyColors.BLACK);

    private static final Point COACH_POSITION = new Point(3, 2);
    private static final Point CLASS_EXPERT_POSITION = new Point(1, 2);
    private static final Point COURSE_COORDINATOR_POSITION = new Point(5, 2);

    public CareerOfficeSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix) {
        super(state, matrix);
    }

    @Override
    protected void drawBackgroundRoom(Model model, Random random) {
        super.drawSmallRoom(model, LOWER_WALL, DOOR, 3);
    }

    @Override
    protected void drawParty(Model model) {
        super.drawPartyArea(model, List.of(new Point(4, 4), new Point(2, 4),
                new Point(2, 5), new Point(4, 5), new Point(5, 4),
                new Point(5, 5), new Point(1, 5)));
    }

    @Override
    protected Sprite getOverDoorSprite() {
        return OVER_DOOR;
    }

    @Override
    protected void specificDrawDecorations(Model model) {
        super.drawBar(model);

        model.getScreenHandler().register("coach1", convertToScreen(CLASS_EXPERT_POSITION),
                COACH1_SPRITE);
        model.getScreenHandler().register("coach2", convertToScreen(COACH_POSITION),
                COACH2_SPRITE);
        model.getScreenHandler().register("coach3", convertToScreen(COURSE_COORDINATOR_POSITION),
                COACH3_SPRITE);


        drawForeground(model, 3, 1, TownHallSubView.WINDOW);
    }

    @Override
    protected Point getDoorPosition() {
        return CareerOfficeState.getDoorPosition();
    }

    @Override
    protected String getPlaceType() {
        return "CAREER OFFICE";
    }

    public void addCoachCallout(int length) {
        addCallout(length, COACH_POSITION);
    }
    
    public void addClassExpertCallout(int length) {
        addCallout(length, CLASS_EXPERT_POSITION);
    }

    public void addCourseCoordinatorCallout(int length) {
        addCallout(length, COURSE_COORDINATOR_POSITION);
    }
}
