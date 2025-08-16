package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.races.Race;
import model.states.dailyaction.*;
import model.states.dailyaction.tavern.RecruitNode;
import model.states.dailyaction.tavern.TavernDailyActionState;
import sound.ClientSound;
import sound.SoundEffects;
import sprites.CombatSpeechBubble;
import util.MyPair;
import view.MyColors;
import view.combat.GrassCombatTheme;
import view.sprites.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static view.subviews.TownHallSubView.DOOR;
import static view.subviews.TownHallSubView.OPEN_DOOR;

public class TavernSubView extends RoomDailyActionSubView {
    public static final MyColors FLOOR_COLOR = MyColors.DARK_BROWN;

    public static final Sprite WALL = new Sprite32x32("tavernfarwall", "world_foreground.png", 0x44,
            MyColors.DARK_GRAY, MyColors.BROWN, MyColors.TAN);
    public static final Sprite SIDE_WALL = new Sprite32x32("sidewall", "world_foreground.png", 0x14,
            MyColors.DARK_GRAY, MyColors.BROWN, MyColors.BROWN);
    public static final Sprite FLOOR = new Sprite32x32("tavernfloor", "combat.png", 0x53,
            MyColors.BROWN, FLOOR_COLOR, MyColors.TAN);
    public static final Sprite LOWER_WALL = new Sprite32x32("lowerwall", "world_foreground.png", 0x24,
            MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, MyColors.TAN);
    private static final Sprite BAR_UPPER = new Sprite32x32("barupper", "world_foreground.png", 0x15,
            MyColors.BLACK, MyColors.TAN, Race.NORTHERN_HUMAN.getColor(), MyColors.BEIGE);
    private static final Sprite BAR_LOWER = new Sprite32x32("barlower", "world_foreground.png", 0x25,
            MyColors.BLACK, MyColors.TAN, MyColors.BROWN);
    private static final Sprite FIREPLACE = new FirePlaceSprite();
    private static final Sprite CHIMNEY = new Sprite32x32("fireplace", "world_foreground.png", 0x68,
            MyColors.DARK_GRAY, MyColors.YELLOW, MyColors.RED, MyColors.GRAY);
    private static final Sprite PLANT = new Sprite32x32("plant", "world_foreground.png", 0x45,
            MyColors.DARK_GRAY, MyColors.BLACK, MyColors.DARK_GREEN, MyColors.CYAN);
    private static final Sprite MERCHANT = new Sprite32x32("merchant", "world_foreground.png", 0x65,
            MyColors.BLACK, MyColors.GOLD, Race.NORTHERN_HUMAN.getColor(), MyColors.RED);
    private static final Sprite SIGN = new Sprite32x32("innsign", "world_foreground.png", 0x64,
            MyColors.BLACK, MyColors.BROWN, MyColors.BEIGE);
    private static final Sprite OVER_DOOR = new Sprite32x32("overdoor", "world_foreground.png", 0x06,
            MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, MyColors.TAN, MyColors.BLACK);

    private final boolean inTown;

    public TavernSubView(AdvancedDailyActionState state,
                         SteppingMatrix<DailyActionNode> matrix, boolean inTown) {
        super(state, matrix);
        this.inTown = inTown;
    }

    @Override
    protected void drawParty(Model model) {
        drawPartyArea(model, List.of(new Point(6, 6), new Point(6, 5),
                new Point(5, 6), new Point(6, 4), new Point(4, 6),
                new Point(5, 4), new Point(4, 5)));
    }

    @Override
    protected Point getDoorPosition() {
        return TavernDailyActionState.getDoorPosition();
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
    protected void drawBackgroundRoom(Model model, Random random) {
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                Point p = convertToScreen(new Point(col, row));
                if ((col == 0 || col == 7) && row < 7) {
                    model.getScreenHandler().put(p.x, p.y, SIDE_WALL);
                } else if (0 < row && row < 7) {
                    model.getScreenHandler().put(p.x, p.y, FLOOR);
                } else if (row == 0) {
                    if (col == 6) {
                        model.getScreenHandler().put(p.x, p.y, SIDE_WALL);
                    } else {
                        model.getScreenHandler().put(p.x, p.y, WALL);
                    }
                } else if (row == 7) {
                    model.getScreenHandler().put(p.x, p.y, LOWER_WALL);
                } else {
                    Sprite spr;
                    if (model.getTimeOfDay() == TimeOfDay.EVENING) {
                        spr = GrassCombatTheme.darkGrassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)];
                    } else {
                        spr = GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)];
                    }
                    model.getScreenHandler().put(p.x, p.y, spr);
                }
            }
        }
    }

    @Override
    protected void specificDrawDecorations(Model model) {
        drawForeground(model, 3, 0, CHIMNEY);
        drawForeground(model, 3, 1, FIREPLACE);
        drawForeground(model, 1, 1, PLANT);
        drawForeground(model, 1, 3, BAR_UPPER);
        drawForeground(model, 1, 4, BAR_LOWER);
        drawForeground(model, 5, 5, RecruitNode.TABLE);
        drawForeground(model, 5, 1, RecruitNode.TABLE);
        drawForeground(model, 4, 7, SIGN);
        if (!inTown) {
            drawForeground(model, 4, 1, MERCHANT);
        }
        for (int x = 2; x < 5; ++x) {
            Point p = convertToScreen(new Point(x, 7));
            model.getScreenHandler().register(OVER_DOOR.getName(), p, OVER_DOOR, 4);
        }
    }

    @Override
    protected String getPlaceType() {
        if (inTown) {
            return "TAVERN";
        }
        return "INN";
    }

    public void addCalloutAtBartender(int lengthOfLine) {
        addCallout(lengthOfLine, new Point(1, 3));
    }

    public void addCalloutAtTraveller(int length) {
        addCallout(length, new Point(2, 1));
    }

    public void addCalloutAtAgentOrGuide(int length) {
        addCallout(length, new Point(4, 1));
    }
}
