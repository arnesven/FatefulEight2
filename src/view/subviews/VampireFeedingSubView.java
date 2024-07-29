package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.enemies.BatEnemy;
import model.quests.*;
import model.states.feeding.VampireFeedingHouse;
import model.states.feeding.VampireFeedingState;
import view.MyColors;
import view.sprites.*;

import java.awt.*;
import java.util.Random;

public class VampireFeedingSubView extends AvatarSubView {
    private static final MyColors ROOF_COLOR = MyColors.BROWN;
    private static final MyColors HOUSE_CONTOUR_COLOR = MyColors.DARK_GRAY;
    private static final MyColors GROUND_COLOR = MyColors.DARK_GREEN;
    private static final Sprite[] SKY_SPRITES = new Sprite[]{
            new Sprite32x32("sky1", "quest.png", 0x08,
                    MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE),
            new Sprite32x32("sky2", "quest.png", 0x09,
                    MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE),
            new Sprite32x32("sky3", "quest.png", 0x0A,
                    MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE)};
    private static final Sprite MOON_SPRITE = new Sprite32x32("sky4", "quest.png", 0x0B,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.YELLOW, MyColors.DARK_BLUE);
    private static final Sprite SKY_SPRITE_16 = new Sprite16x16("feedingsky", "feeding.png", 0x14, MyColors.DARK_BLUE,
            MyColors.GRAY, MyColors.WHITE, MyColors.CYAN);
    private static final Sprite GROUND_SPRITE = new Sprite16x16("feedingground", "feeding.png", 0x14, GROUND_COLOR,
            MyColors.GRAY, MyColors.WHITE, MyColors.CYAN);
    private static final Sprite GROUND_DAY_SPRITE = new Sprite16x16("feedinggroundday", "feeding.png", 0x14, MyColors.GREEN,
            MyColors.GRAY, MyColors.WHITE, MyColors.CYAN);

    private final Sprite[][] houseSpritesLit;
    private final Sprite[][] houseSpritesBlack;

    private final GameCharacter character;
    private final VampireFeedingHouse house;
    private final VampireFeedingState state;
    private boolean avatarEnabled = true;
    private Random random = new Random(1234);
    private Point smokePosition;
    private RunOnceAnimationSprite puffAnimation;

    public VampireFeedingSubView(VampireFeedingState state, GameCharacter vampire, VampireFeedingHouse house) {
        this.state = state;
        this.character = vampire;
        this.house = house;
        houseSpritesLit = loadHouseSprites(MyColors.YELLOW, house.getColor());
        houseSpritesBlack = loadHouseSprites(MyColors.BLACK, house.getColor());
    }

    @Override
    protected void specificDrawArea(Model model) {
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
        drawBackground(model);
        drawEdges(model);
        drawSubScenes(model);
        drawPortrait(model);
        drawOngoingAnimations(model);
    }

    private void drawSubScenes(Model model) {
        for (QuestNode node : house.getNodes()) {
            Point conv = convertToScreen(new Point(node.getColumn(), node.getRow()));
            int xPos = conv.x;
            int yPos = conv.y;
            node.drawYourself(model, xPos, yPos);

            if (state.getCurrentPosition() == node && avatarEnabled) {
                model.getScreenHandler().register("feedingAvatar", new Point(xPos, yPos),
                        character.getAvatarSprite(), 2);
            }
        }
    }

    public static Point convertToScreen(Point p) {
        return new Point(X_OFFSET + p.x*4, Y_OFFSET + p.y*4 + 2);
    }

    private void drawEdges(Model model) {
        for (QuestJunction j : house.getJunctions()) {
            for (QuestEdge edge : j.getConnections()) {
                edge.drawYourself(model.getScreenHandler(), j, X_OFFSET, Y_OFFSET);
            }
        }
        for (QuestSubScene qss : house.getSubScenes()) {
            if (qss.getSuccessEdge() != null) {
                qss.getSuccessEdge().drawYourself(model.getScreenHandler(), qss, X_OFFSET, Y_OFFSET);
            }
            if (qss.getFailEdge() != null) {
                qss.getFailEdge().drawYourself(model.getScreenHandler(), qss, X_OFFSET, Y_OFFSET);
            }
        }
    }


    private void drawPortrait(Model model) {
        if (house.getPortrait() != null) {
            Point p = convertToScreen(new Point(5, 5));
            p.x += 2;
            p.y += 2;
            house.getPortrait().drawYourself(model.getScreenHandler(), p.x, p.y);
            if (!house.isOpenEyes()) {
                house.getPortrait().drawBlink(model.getScreenHandler(), p.x + 3, p.y + 3);
            }
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return character.getFirstName() + " is on the prowl...";
    }

    @Override
    protected String getTitleText(Model model) {
        return "VAMPIRE - FEEDING";
    }

    public void moveAlongEdge(Point from, QuestEdge edge) {
        avatarEnabled = false;
        Sprite spr = character.getAvatarSprite();
        boolean wasBat = house.isBatForm();
        if (wasBat) {
            spr = new BatSprite();
            smokePosition = from;
            puffAnimation = new SmokePuffAnimation();
        }
        QuestSubView.animateAvatarAlongEdge(this, from, edge, spr);
        house.setBatForm(false);
        if (wasBat) {
            smokePosition = edge.getNode().getPosition();
            puffAnimation = new SmokePuffAnimation();
        }
        avatarEnabled = true;
    }

    private void drawBackground(Model model) {
        drawSky(model);
        drawGroundNight(model);
        drawHouse(model);
    }

    private void drawOngoingAnimations(Model model) {
        if (puffAnimation != null) {
            Point p = convertToScreen(smokePosition);
            model.getScreenHandler().register("smokepuff", p, puffAnimation, 5);
            if (puffAnimation.isDone()) {
                AnimationManager.unregister(puffAnimation);
                puffAnimation = null;
                smokePosition = null;
            }
        }
    }

    private void drawHouse(Model model) {
        this.random = new Random(1234);
        int houseBase = 2;
        // Roof
        Point p = convertToScreen(new Point(4, houseBase - house.getStories()/2));
        p.y -= (house.getStories() % 2) * 2;
        model.getScreenHandler().put(p.x, p.y, getHouseSprite(0, 0));
        for (int i = 0; i < house.getWidth(); ++i) {
            p.x += 2;
            model.getScreenHandler().put(p.x, p.y, getHouseSprite(1, 0));
            if (i == house.getWidth()-1) {
                Sprite chimney = getHouseSprite(3, 0);
                model.getScreenHandler().register(chimney.getName(), new Point(p.x, p.y-1), chimney);
            }
        }
        p.x += 2;
        model.getScreenHandler().put(p.x, p.y, getHouseSprite(2, 0));

        // First and second floor.
        for (int floor = 1; floor < house.getStories(); floor++) {
            p = convertToScreen(new Point(4, houseBase - floor/2));
            p.y += -(floor % 2) * 2;
            model.getScreenHandler().put(p.x, p.y, getHouseSprite(0, 1));
            for (int x = 0; x < house.getWidth(); ++x) {
                p.x += 2;
                model.getScreenHandler().put(p.x, p.y, getHouseSprite(1, 1));
                if (floor == house.getOpenWindow() && x == 0) {
                    Sprite openWindow = getHouseSprite(3, 1);
                    model.getScreenHandler().register(openWindow.getName(), new Point(p.x, p.y), openWindow);
                }
            }
            p.x += 2;
            model.getScreenHandler().put(p.x, p.y, getHouseSprite(2, 1));
        }

        // Ground Floor
        p = convertToScreen(new Point(4, houseBase));
        model.getScreenHandler().put(p.x, p.y, getHouseSprite(0, 2));
        p.x += 2;
        for (int x = 0; x < house.getWidth(); ++x) {
            if (x == house.getWidth() / 2) {
                model.getScreenHandler().put(p.x, p.y, getHouseSprite(3, 2)); // Front Door
            } else {
                model.getScreenHandler().put(p.x, p.y, getHouseSprite(1, 2));
            }
            p.x += 2;
        }
        model.getScreenHandler().put(p.x, p.y, getHouseSprite(2, 2));
    }

    private Sprite getHouseSprite(int x, int y) {
        if (!house.isSleepInfoGiven()) {
            return houseSpritesLit[x][y];
        }
        double sleepRatio = (double)house.getSleeping() / (double)house.getDwellers();
        if (random.nextDouble() < sleepRatio) {
            return houseSpritesBlack[x][y];
        }
        return houseSpritesLit[x][y];
    }

    public static void drawSkyNight(Model model, Random random) {
        for (int x = 0; x < 8; ++x) {
            Point p = convertToScreen(new Point(x, 0));
            Sprite spr;
            if (x == 7) {
                spr = MOON_SPRITE;
            } else {
                spr = SKY_SPRITES[random.nextInt(SKY_SPRITES.length)];
            }
            model.getScreenHandler().put(p.x, p.y, spr);
            p.y += 4;
            putFour(model, p, SKY_SPRITE_16);
        }
    }

    private void drawSky(Model model) {
        random = new Random(1234);
        drawSkyNight(model, random);
    }

    private static void putFour(Model model, Point p, Sprite sprite) {
        model.getScreenHandler().put(p.x, p.y, sprite);
        model.getScreenHandler().put(p.x+2, p.y, sprite);
        model.getScreenHandler().put(p.x, p.y+2, sprite);
        model.getScreenHandler().put(p.x+2, p.y+2, sprite);
    }

    private static void drawGround(Model model, Sprite groundSprite) {
        for (int y = 2; y < 6; ++y) {
            for (int x = 0; x < 8; ++x) {
                Point p = convertToScreen(new Point(x, y));
                putFour(model, p, groundSprite);
            }
        }
    }

    public static void drawGroundNight(Model model) {
        drawGround(model, GROUND_SPRITE);
    }

    public static void drawGroundDay(Model model) {
        drawGround(model, GROUND_DAY_SPRITE);
    }

    private static Sprite[][] loadHouseSprites(MyColors windowColor, MyColors facadeColor) {
        Sprite[][] result = new Sprite[4][3];
        result[0][0] = new Sprite16x16("houseUL", "feeding.png", 0x00,
                MyColors.DARK_BLUE, HOUSE_CONTOUR_COLOR, ROOF_COLOR, facadeColor);
        result[1][0] = new Sprite16x16("houseTop", "feeding.png", 0x01,
                windowColor, HOUSE_CONTOUR_COLOR, ROOF_COLOR, facadeColor);
        result[2][0] = new Sprite16x16("houseUR", "feeding.png", 0x02,
                MyColors.DARK_BLUE, HOUSE_CONTOUR_COLOR, ROOF_COLOR, facadeColor);
        result[3][0] = new Sprite16x16("chimney", "feeding.png", 0x03,
                MyColors.BLACK, HOUSE_CONTOUR_COLOR, ROOF_COLOR, MyColors.CYAN);

        for (int x = 0; x < 3; ++x) {
            result[x][1] = new Sprite16x16("housemid" + x, "feeding.png", 0x10 + x,
                    MyColors.BLACK, HOUSE_CONTOUR_COLOR, windowColor, facadeColor);
        }
        result[3][1] = new Sprite16x16("openWindow", "feeding.png", 0x13,
                MyColors.BLACK, HOUSE_CONTOUR_COLOR, windowColor, MyColors.CYAN);

        for (int x = 0; x < 3; ++x){
            result[x][2] = new Sprite16x16("houseLow"+x, "feeding.png", 0x20 +x,
                    GROUND_COLOR, HOUSE_CONTOUR_COLOR, windowColor, facadeColor);
        }
        result[3][2] = new Sprite16x16("houseDoor", "feeding.png", 0x23,
                GROUND_COLOR, HOUSE_CONTOUR_COLOR, ROOF_COLOR, facadeColor);
        return result;
    }
}
