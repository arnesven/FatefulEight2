package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.items.weapons.Weapon;
import util.Arithmetics;
import util.MyPair;
import util.MyRandom;
import view.BorderFrame;
import view.MyColors;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;
import view.sprites.Sprite8x8;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ArcheryTargetSubView extends AimingSubView {
    public static final int ON_GROUND = -1;
    public static final int ON_LEG = -2;
    public static final int OVER_TARGET = -3;
    protected static final Sprite whiteBlock = new FilledBlockSprite(MyColors.WHITE);
    protected static final Sprite redBlock = new FilledBlockSprite(MyColors.RED);
    protected static final Sprite brownBlock = new FilledBlockSprite(MyColors.BROWN);
    private static final Sprite[][] bullseyeSprites = makeBullseyeSprites();
    private static final Sprite[][] windSprites = makeWindSprites();
    private static final Point WIND_POSITION = new Point(X_MAX-4, Y_OFFSET+1);
    private static final String[] POWER_NAMES = new String[]{"VERY WEAK", "WEAK", "MEDIUM", "STRONG", "VERY STRONG"};
    private static final String[] DIST_NAMES = new String[]{"V. SHORT", "SHORT", "MEDIUM", "FAR", "V. FAR"};
    private final Point wind;
    private final int distance;
    private final Weapon bow;
    private static final Point ORIGIN = new Point(X_OFFSET+OFFSETS.x, Y_OFFSET+OFFSETS.y);
    private double[] ringSizes = new double[]{3.6, 6.7, 9.8, 13.0, 16};
    private int currentPower;
    private boolean powerLocked = false;

    public ArcheryTargetSubView(Weapon bow, Point wind, int distance) {
        this.bow = bow;
        this.currentPower = bow.getDamageTable().length/2;
        this.wind = wind;
        this.distance = distance;
        setupMatrix();
    }

    private void setupMatrix() {

    }

    @Override
    protected void innerDrawArea(Model model) {
        drawTarget(model);
        drawBullseye(model);
        drawWind(model);
        drawDistance(model);
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE && !powerLocked) {
            currentPower = Arithmetics.incrementWithWrap(currentPower, this.bow.getDamageTable().length);
            return true;
        }
        return false;
    }

    private void drawTarget(Model model) {
        for (int y = -OFFSETS.y; y <= 20; ++y) {
            for (int x = -OFFSETS.x; x <= 16; ++x) {
                Sprite toDraw = spriteForDist(x, y);
                model.getScreenHandler().put(ORIGIN.x + x, ORIGIN.y + y, toDraw);
            }
        }
    }

    private void drawBullseye(Model model) {
        for (int j = 0; j < 3; ++j) {
            for (int i = 0; i < 3; ++i) {
                model.getScreenHandler().put(ORIGIN.x - 1 + i, ORIGIN.y - 1 + j, bullseyeSprites[i][j]);
            }
        }
    }

    private void drawWind(Model model) {
        Sprite toDraw = null;
        if (wind.y == 0) {
            if (wind.x > 0) {
                toDraw = windSprites[0][0];
            } else if (wind.x < 0) {
                toDraw = windSprites[1][0];
            }
        } else if (wind.x == 0) {
            if (wind.y > 0) {
                toDraw = windSprites[2][0];
            } else if (wind.y < 0) {
                toDraw = windSprites[3][0];
            }
        } else {
            int i = 0;
            if (wind.x < 0) {
                i += 2;
            }
            if (wind.y > 0) {
                i += 1;
            }
            toDraw = windSprites[i][1];
        }
        if (toDraw != null) { // If not no wind.
            model.getScreenHandler().register(toDraw.getName(), WIND_POSITION, toDraw);
        }
        BorderFrame.drawString(model.getScreenHandler(), "WIND " + getWindStrength(),
                X_MAX-6, Y_OFFSET+4, MyColors.BLACK, MyColors.CYAN);
    }


    private void drawDistance(Model model) {
        BorderFrame.drawString(model.getScreenHandler(), "DISTANCE",
                X_OFFSET+1, Y_OFFSET+1, MyColors.BLACK, MyColors.CYAN);
        BorderFrame.drawString(model.getScreenHandler(), DIST_NAMES[distance],
                X_OFFSET+2, Y_OFFSET+2, MyColors.BLACK, MyColors.CYAN);
    }

    private Sprite spriteForDist(int x, int y) {
        double dist = Math.sqrt(x*x + y*y);
        Sprite[] blocks = new Sprite[]{whiteBlock, redBlock, whiteBlock, redBlock, whiteBlock};
        for (int i = 0; i < ringSizes.length; ++i) {
            if (dist < ringSizes[i]) {
                return blocks[i];
            }
        }
        if (y >= 0) {
            double angle = Math.atan2(y, Math.abs(x));
            if (Math.PI/3.0 <= angle && angle <= Math.PI/3+Math.PI/18) {
                return brownBlock;
            }
        }
        if (y < -11) {
            return lightBlueBlock;
        }
        return greenBlock;
    }

    @Override
    protected String getUnderText(Model model) {
        String extra = "";
        if (powerLocked) {
            extra = " - Fixed";
        }
        return "Shot Power: " + POWER_NAMES[currentPower] + extra + " (" + bow.getName() + ")";
    }

    private int getWindStrength() {
        return (int)Math.ceil(wind.distance(0, 0));
    }

    @Override
    protected String getTitleText(Model model) {
        return "ARCHERY CONTEST - ROUND 1";
    }


    private static Sprite[][] makeBullseyeSprites() {
        Sprite[][] sprites = new Sprite[3][3];
        for (int j = 0; j < 3; ++j) {
            for (int i = 0; i < 3; ++i) {
                sprites[i][j] = new Sprite8x8("bullseye"+i+","+j, "arrows.png", 0x10*j + 0x20 + i,
                        MyColors.BLACK, MyColors.WHITE, MyColors.RED, MyColors.BEIGE);
            }
        }
        return sprites;
    }

    private static Sprite[][] makeWindSprites() {
        Sprite[][] sprites = new Sprite[4][2];
        for (int j = 0; j < 2; ++j) {
            for (int i = 0; i < 4; ++i) {
                sprites[i][j] = new Sprite16x16("wind"+i+","+j, "arrows.png", 0x13 + 0x10*j + i,
                        MyColors.BLACK, MyColors.WHITE, MyColors.PINK, MyColors.BEIGE);
            }
        }
        return sprites;
    }

    public int getSelectedPower() {
        return currentPower;
    }

    public int getResultForShot(Point result) {
        Sprite spr = spriteForDist(result.x, result.y);
        if (spr == greenBlock) {
            return ON_GROUND;
        }
        if (spr == brownBlock) {
            return ON_LEG;
        }
        if (spr == lightBlueBlock) {
            return OVER_TARGET;
        }
        double dist = Math.sqrt(result.x*result.x + result.y*result.y);
        if (dist <= 1.0001) {
            return 0;
        }
        for (int i = 0; i < ringSizes.length; ++i) {
            if (dist < ringSizes[i]) {
                return i+1;
            }
        }
        throw new IllegalStateException("Where did that arrow go? result was " + result.x + "," + result.y);
    }

    public void setPowerLocked(boolean b) {
        this.powerLocked = b;
    }
}
