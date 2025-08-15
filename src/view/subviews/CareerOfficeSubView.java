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
    public static final Sprite BAR = new Sprite32x32("bar", "world_foreground.png", 0x5A,
            MyColors.BLACK, MyColors.TAN, MyColors.BROWN);

    private static final Sprite DOOR = new Sprite32x32("door", "world_foreground.png", 0x34,
            MyColors.DARK_GRAY, MyColors.PINK, MyColors.TAN, MyColors.DARK_RED);
    public static final Sprite OPEN_DOOR = new Sprite32x32("door", "world_foreground.png", 0x6E,
            MyColors.DARK_GRAY, MyColors.PINK, MyColors.TAN, MyColors.DARK_RED);

    public CareerOfficeSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix) {
        super(state, matrix);
    }

    @Override
    protected void drawBackgroundRoom(Model model, Random random) {
        super.drawSmallRoom(model, LOWER_WALL);
    }

    @Override
    protected void drawParty(Model model) {
        super.drawPartyArea(model, List.of(new Point(4, 4), new Point(2, 4),
                new Point(2, 5), new Point(4, 5), new Point(5, 4),
                new Point(5, 5), new Point(1, 5)));
    }

    @Override
    protected void drawDecorations(Model model) {
        for (int x = 1; x < 6; ++x) {
            Point drawPos = convertToScreen(new Point(x, 3));
            model.getScreenHandler().register("bar", drawPos, BAR);
        }

        model.getScreenHandler().register("coach1", convertToScreen(new Point(1, 2)),
                COACH1_SPRITE);
        model.getScreenHandler().register("coach2", convertToScreen(new Point(3, 2)),
                COACH2_SPRITE);
        model.getScreenHandler().register("coach3", convertToScreen(new Point(5, 2)),
                COACH3_SPRITE);
    }

    @Override
    protected Point getDoorPosition() {
        return CareerOfficeState.getDoorPosition();
    }

    @Override
    protected Sprite getOpenDoorSprite() {
        return OPEN_DOOR;
    }

    @Override
    protected Sprite getClosedDoorSprite() {
        return DOOR;
    }

    @Override
    protected String getPlaceType() {
        return "CAREER OFFICE";
    }
}
