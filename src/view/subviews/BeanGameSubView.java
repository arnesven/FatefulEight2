package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.states.beangame.BeanGameBoard;
import model.states.beangame.BeanGameBoardMaker;
import sound.SoundEffects;
import util.Arithmetics;
import util.MyRandom;
import view.MyColors;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.Map;

public class BeanGameSubView extends SubView implements Animation {
    private static final int ROWS_PER_SCREEN = 38;
    private static final int ANIMATION_DELAY = 1;
    private static final int FIRST_SCREEN_GAP = 10;
    private static final double GRAVITY = 0.125;
    private static final double BOUNCE_BOARD_SIDE = 0.6;
    private static final double BOUNCE_PIN = 0.8;
    private static final double BOUNCE_BOARD_BOTTOM = 0.6;
    private static final Sprite BOARD_BG_SPRITE = new FilledBlockSprite(BeanGameBoardMaker.BACK_COLOR);
    private static final Sprite BOARD_BG_SIDE_SPRITE = new FilledBlockSprite(BeanGameBoardMaker.FRAME_COLOR);
    private static final Map<Character, Sprite> PIN_SPRITES = makePinSprites();
    private static final Map<Integer, Sprite> PRIZE_SPRITES = makePrizeSprites();

    private static final Sprite BOARD_UL_CORNER = new Sprite8x8("beanul", "bean.png", 0x01,
            MyColors.BLACK, BeanGameBoardMaker.FRAME_COLOR, MyColors.BLACK, BeanGameBoardMaker.FRAME_COLOR);
    private static final Sprite BOARD_UR_CORNER = new Sprite8x8("beanur", "bean.png", 0x02,
            MyColors.BLACK, BeanGameBoardMaker.FRAME_COLOR, MyColors.BLACK, BeanGameBoardMaker.FRAME_COLOR);
    private static final Sprite BOARD_LL_CORNER = new Sprite8x8("beanll", "bean.png", 0x02,
            MyColors.BLACK, BeanGameBoardMaker.FRAME_COLOR, BeanGameBoardMaker.FRAME_COLOR, BeanGameBoardMaker.FRAME_COLOR);
    private static final Sprite BOARD_LR_CORNER = new Sprite8x8("beanlr", "bean.png", 0x01,
            MyColors.BLACK, BeanGameBoardMaker.FRAME_COLOR, BeanGameBoardMaker.FRAME_COLOR, BeanGameBoardMaker.FRAME_COLOR);
    private static final Sprite BEAN_SPRITE = new Sprite8x8("beanitself", "bean.png", 0x03,
            MyColors.GREEN, MyColors.ORC_GREEN, MyColors.BEIGE, BeanGameBoardMaker.FRAME_COLOR);

    private final Sprite beanInHand;

    private final int numberOfScreens;
    private final BeanGameBoard gameBoard;
    private Point position = new Point(12, -1);
    private Point2D shift = new Point2D.Double(0, 0);
    private Point2D velocity = new Point2D.Double(0, 1);
    private int screen = 0;
    private int frameCount = 0;
    private boolean droppingBean = true;
    private boolean gameOver = false;
    private boolean telekinesisEnabled = false;
    private boolean paused = false;

    public BeanGameSubView(BeanGameBoard beanGameBoard, GameCharacter player) {
        this.gameBoard = beanGameBoard;
        this.numberOfScreens = 2 + (gameBoard.boardLength() - (ROWS_PER_SCREEN - 4)) / ROWS_PER_SCREEN; // TODO: This isn't quite right...
        this.beanInHand = new Sprite32x16("beaninhand", "bean.png", 0x10,
                MyColors.DARK_GRAY, player.getRace().getColor(), MyColors.GREEN, MyColors.ORC_GREEN);
    }

    @Override
    protected void drawArea(Model model) {
        drawBoard(model);
        drawBean(model);
        // BorderFrame.drawString(model.getScreenHandler(), ""+ screen, X_OFFSET, Y_MAX-2, MyColors.WHITE);
    }

    private void drawBean(Model model) {
        int startRow = screen * ROWS_PER_SCREEN - FIRST_SCREEN_GAP;
        if (screen == 0) {
            startRow = 0;
        }
        Point finalPos = toScreenCoordinates(position.x, position.y - startRow);
        Sprite spriteToUse = BEAN_SPRITE;
        if (droppingBean) {
            spriteToUse = beanInHand;
            finalPos.x -= 1;
            finalPos.y -= 2;
        }
        model.getScreenHandler().register(spriteToUse.getName(), finalPos, spriteToUse, 1,
                (int) shift.getX(), (int) shift.getY());
    }

    private void drawBoard(Model model) {
        if (screen == 0) {
            Point position = toScreenCoordinates(-1, -1);
            model.getScreenHandler().put(position.x, position.y, BOARD_UL_CORNER);
            position = toScreenCoordinates(gameBoard.boardWidth(), -1);
            model.getScreenHandler().put(position.x, position.y, BOARD_UR_CORNER);
        }

        int startRow = screen * ROWS_PER_SCREEN - FIRST_SCREEN_GAP;
        int rowsToDraw = ROWS_PER_SCREEN;
        if (screen == 0) {
            startRow = 0;
            rowsToDraw -= FIRST_SCREEN_GAP;
        }
        for (int row = startRow; row < startRow + rowsToDraw && row < gameBoard.boardLength(); ++row) {
            for (int col = -1; col < gameBoard.boardWidth() + 1; ++col) {
                Point position = toScreenCoordinates(col, row - startRow);
                if (col == -1 || col == gameBoard.boardWidth()) {
                    model.getScreenHandler().put(position.x, position.y, BOARD_BG_SIDE_SPRITE);
                } else {
                    model.getScreenHandler().put(position.x, position.y, BOARD_BG_SPRITE);
                    if (gameBoard.isAPin(col, row)) {
                        Sprite sprite = PIN_SPRITES.get(gameBoard.getCell(col, row));
                        model.getScreenHandler().register(sprite.getName(), position, sprite, 2);
                    }
                }
            }
            Point pos = toScreenCoordinates(1, row - startRow);
           // BorderFrame.drawString(model.getScreenHandler(), "" + row, X_MAX-2, pos.y, MyColors.GRAY);
        }
        if (screen == numberOfScreens - 1) {
            int row = gameBoard.boardLength();
            for (int col = -1; col < gameBoard.boardWidth() + 1; ++col) {
                Point position = toScreenCoordinates(col, row - startRow);
                if (col == -1) {
                    model.getScreenHandler().put(position.x, position.y, BOARD_LL_CORNER);
                } else if (col == gameBoard.boardWidth()) {
                    model.getScreenHandler().put(position.x, position.y, BOARD_LR_CORNER);
                } else {
                    model.getScreenHandler().put(position.x, position.y, BOARD_BG_SIDE_SPRITE);
                }
            }

            for (int pocket = 0; pocket < gameBoard.getNumberOfPockets(); ++pocket) {
                if (gameBoard.getPrize(pocket) > 0) {
                    Point pos = toScreenCoordinates(pocket * gameBoard.pocketLength() + 1,
                            gameBoard.boardLength() - startRow - 2);
                    Sprite spr = PRIZE_SPRITES.get(gameBoard.getPrize(pocket));
                    model.getScreenHandler().register(spr.getName(), pos, spr);
                }
            }

//            for (int col = 0; col < matrix.length; ++col) {
//                Point position = toScreenCoordinates(col, row - startRow + 1);
//                BorderFrame.drawString(model.getScreenHandler(),
//                        ((col+1) / pocketLength + 1) + "",
//                        position.x, position.y, MyColors.BLUE);
//            }
        }
    }

    private Point toScreenCoordinates(int col, int row) {
        int xOffset = 2;
        int yOffset = screen == 0 ? FIRST_SCREEN_GAP : 0;
        return new Point(X_OFFSET + xOffset + col, Y_OFFSET + yOffset + row);
    }

    @Override
    protected String getUnderText(Model model) {
        String extra = " Telekinesis activated.";
        return "You are playing the bean game." + (telekinesisEnabled ? extra : "");
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENT - BEAN GAME";
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (droppingBean) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                position.x = Arithmetics.decrementWithWrap(position.x, gameBoard.boardWidth());
                return true;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                position.x = Arithmetics.incrementWithWrap(position.x, gameBoard.boardWidth());
                return true;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                screen = Arithmetics.incrementWithWrap(screen, numberOfScreens);
                return true;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                screen = Arithmetics.decrementWithWrap(screen, numberOfScreens);
                return true;
            }
        } else if (telekinesisEnabled) {
            double nudgeSpeed = 2.0;
            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                velocity = new Point2D.Double(-nudgeSpeed, velocity.getY());
                return true;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                velocity = new Point2D.Double(nudgeSpeed, velocity.getY());
                return true;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                velocity = new Point2D.Double(velocity.getX(), nudgeSpeed);
                return true;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                velocity = new Point2D.Double(velocity.getX(), -nudgeSpeed);
                return true;
            }
        }
        return super.handleKeyEvent(keyEvent, model);
    }

    public void start() {
        droppingBean = false;
        screen = 0;
        AnimationManager.registerPausable(this);
    }

    public void setPause(boolean enabled) {
        this.paused = enabled;
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        if (paused) {
            return;
        }
        frameCount++;
        if (frameCount % ANIMATION_DELAY == 0) {
            updateState();
        }
    }

    private void updateState() {
        Point alreadyHitPoint = null;
        if (beanHitPin(position, shift)) {
            alreadyHitPoint = hitPoint(position, shift);
        }
        updatePosition();
        updateScreen();
        updateVelocity(alreadyHitPoint);
    }

    private void updatePosition() {
        Point2D newPos = new Point2D.Double(position.x + (shift.getX() + velocity.getX()) / 8.0,
                position.y + (shift.getY() + velocity.getY()) / 8);
        position.x = (int)Math.floor(newPos.getX());
        position.y = (int)Math.floor(newPos.getY());
        shift = new Point2D.Double(8 * (newPos.getX() - position.x),
                                   8 * (newPos.getY() - position.y));
    }

    private void updateScreen() {
        if (position.y < ROWS_PER_SCREEN - FIRST_SCREEN_GAP) {
            screen = 0;
        } else {
            for (screen = 1; screen < numberOfScreens; ++screen) {
                if (position.y < (screen + 1) * ROWS_PER_SCREEN - FIRST_SCREEN_GAP) {
                    break;
                }
            }
        }
    }

    private void updateVelocity(Point oldHitPoint) {
        double velocityAngle = Math.atan2(velocity.getY(), velocity.getX());
        if (beanHitPin(position, shift) && !hitPoint(position, shift).equals(oldHitPoint)) { // hit pin
            velocityAngle += Math.PI;
            if (shift.getX() > 4.0) {
                velocityAngle -= MyRandom.nextDouble() * 0.5 * Math.PI;
            } else {
                velocityAngle += MyRandom.nextDouble() * 0.5 * Math.PI;
            }
            reangleVelocity(velocity, velocityAngle, BOUNCE_PIN);
            SoundEffects.playClickSound();
        } else if (position.x < 0) {
            position.x = 0;
            shift = new Point2D.Double(0, shift.getY());
            velocityAngle = Math.PI - velocityAngle;
            reangleVelocity(velocity, velocityAngle, BOUNCE_BOARD_SIDE);
            velocity = new Point2D.Double(velocity.getX() + 0.1, velocity.getY());
            SoundEffects.playSound("wood-small");
        } else if (position.x >= gameBoard.boardWidth() - 1) {
            position.x = gameBoard.boardWidth() - 1;
            shift = new Point2D.Double(0, shift.getY());
            velocityAngle = Math.PI - velocityAngle;
            reangleVelocity(velocity, velocityAngle, BOUNCE_BOARD_SIDE);
            velocity = new Point2D.Double(velocity.getX() - 0.1, velocity.getY());
            SoundEffects.playSound("wood-small");
        } else if (position.y >= gameBoard.boardLength() - 1) {
            position.y = gameBoard.boardLength() - 1;
            shift = new Point2D.Double(shift.getX(), 0);
            velocityAngle = -velocityAngle;
            reangleVelocity(velocity, velocityAngle, BOUNCE_BOARD_BOTTOM);
            if (velocity.distance(0, 0) < 0.1) {
                handleGameOver();
            } else {
                SoundEffects.playSound("wood-small");
            }
        }
        velocity = new Point2D.Double(velocity.getX(), velocity.getY() + GRAVITY); // Gravity
    }

    private void handleGameOver() {
        System.out.println("Game is over!");
        gameOver = true;
    }

    private void reangleVelocity(Point2D velocity, double velocityAngle, double bounce) {
        double r = bounce * velocity.distance(0, 0);
        this.velocity = new Point2D.Double(r * Math.cos(velocityAngle),
                                           r * Math.sin(velocityAngle));
    }


    private boolean beanHitPin(Point position, Point2D shift) {
        if (3.5 <= shift.getX() && shift.getX() < 4.5) {
            return false;
        }
        if (3.5 <= shift.getY() && shift.getY() < 4.5) {
            return false;
        }
        Point p = hitPoint(position, shift);
        return gameBoard.isAPin(p.x, p.y);
    }

    private Point hitPoint(Point position, Point2D shift) {
        int x = position.x;
        int y = position.y;
        if (shift.getX() > 4.0) {
            x++;
        }
        if (shift.getY() > 4.0) {
            y++;
        }
        return new Point(x, y);
    }


    @Override
    public void synch() { }

    public boolean gameIsOver() {
        return gameOver;
    }

    public int getWinPocket() {
        return (position.x + 1) / gameBoard.pocketLength() + 1;
    }

    public void stop() {
        AnimationManager.unregister(this);
    }

    private static Map<Character, Sprite> makePinSprites() {
        return Map.of(
                'g', new PinSprite(MyColors.DARK_GRAY, BeanGameBoardMaker.getColor('g')),
                'p', new PinSprite(MyColors.DARK_GRAY, BeanGameBoardMaker.getColor('p')),
                'y', new PinSprite(MyColors.GOLD, BeanGameBoardMaker.getColor('y')),
                'b', new PinSprite(MyColors.DARK_GRAY, BeanGameBoardMaker.getColor('b')),
                'G', new PinSprite(MyColors.DARK_GRAY, BeanGameBoardMaker.getColor('G')),
                'w', new PinSprite(MyColors.GRAY, BeanGameBoardMaker.getColor('w')),
                'k', new PinSprite(MyColors.BLACK, BeanGameBoardMaker.getColor('k')));
    }

    public void enableTelekinesis() {
        this.telekinesisEnabled = true;
    }

    private static class PinSprite extends Sprite8x8{
        public PinSprite(MyColors darkColor, MyColors lightColor) {
            super("beangamepin"+lightColor.name(), "bean.png", 0x00, darkColor, lightColor,
                    MyColors.BEIGE, MyColors.GRAY_RED);
        }
    }

    private static Map<Integer, Sprite> makePrizeSprites() {
        return Map.of(2, new PrizeSprite(0x02, MyColors.BLUE),
                3, new PrizeSprite(0x03, MyColors.GREEN),
                5, new PrizeSprite(0x12, MyColors.RED),
                7, new PrizeSprite(0x13, MyColors.PURPLE));
    }

    private static class PrizeSprite extends Sprite16x16 {
        public PrizeSprite(int num, MyColors textColor) {
            super("beangameprize" + num, "bean.png", num,
                    MyColors.BLACK, MyColors.BEIGE, textColor, MyColors.GRAY_RED);
        }
    }
}
