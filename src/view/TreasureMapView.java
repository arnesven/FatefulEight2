package view;

import model.Model;
import model.items.MysteriousMap;
import model.map.*;
import model.map.locations.WastelandLocation;
import util.MyLists;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;
import view.widget.TopText;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class TreasureMapView extends GameView {
    private static final Sprite[] DIRECTION_SPRITES = makeDirectionSprites(3);
    public static final Sprite[] RIVER_SPRITES = makeDirectionSprites(4);
    private static final Sprite[][] MAP_SPRITES = makeMapSprites();
    public static final Sprite TOWN_SPRITE = new TreasureMapSprite(3, 0);
    public static final Sprite WOODS_SPRITE = new TreasureMapSprite(3, 1);
    public static final Sprite HILLS_SPRITE = new TreasureMapSprite(3, 2);
    public static final Sprite MOUNTAIN_SPRITES = new TreasureMapSprite(4, 0);
    public static final Sprite SWAMP_SPRITE = new TreasureMapSprite(4, 2);
    public static final Sprite WASTELAND_SPRITE = new TreasureMapSprite(5, 2);
    private static final Sprite blackBlock = new FilledBlockSprite(MyColors.BLACK);
    private static final List<String> DIRECTION_SHORTS = List.of("SE", "S", "SW", "NW", "N", "NE");
    public static final Sprite FINAL_SPOT_SPRITE = new TreasureMapSprite(4, 1);

    private final GameView previous;
    private TopText topText;
    private MapContent[][] array;
    private int scrollX = 0;
    private int scrollY = 0;

    public TreasureMapView(GameView previous, MysteriousMap map, Model model) {
        super(true);
        this.previous = previous;
        this.topText = new TreasureMapTopText();
        makeMapContents(model, map);
    }

    private void makeMapContents(Model model, MysteriousMap map) {
        List<Point> path = map.getPath();

        Point max = new Point(MyLists.maximum(path, point -> point.x),
                MyLists.maximum(path, point -> point.y));
        Point min = new Point(MyLists.minimum(path, point -> point.x),
                MyLists.minimum(path, point -> point.y));

        System.out.println("Maximum " + max);
        System.out.println("Minimum " + min);

        int width = Math.max(4, max.x - min.x + 1);
        int height = Math.max(4, max.y - min.y + 1);

        this.array = new MapContent[width][height];
        for (int i = 0; i < path.size(); ++i) {
            Point p = path.get(i);
            MapContent mc = new MapContent(p, model.getWorld().getHex(p), i == 0);
            if (i > 0) {
                mc.setPrevious(path.get(i-1));
            }
            if (i < path.size()-1) {
                mc.setNext(path.get(i + 1));
            }
            this.array[p.x - min.x][p.y - min.y] = mc;
        }

    }

    @Override
    public void transitionedTo(Model model) {
        model.getScreenHandler().clearAll();
    }

    @Override
    public void transitionedFrom(Model model) { }

    @Override
    protected void internalUpdate(Model model) {
        model.getScreenHandler().clearAll();
        BorderFrame.drawFrameTop(model.getScreenHandler());
        drawMap(model);
        model.getScreenHandler().fillSpace(0, DrawingArea.WINDOW_COLUMNS, 0, 1, blackBlock);
        topText.drawYourself(model);
    }

    private void drawMap(Model model) {
        for (int y = -1; y <= array[0].length; ++y) {
            for (int x = -1; x <= array.length; ++x) {
                Point finalPos = convertToScreen(x + scrollX, y + scrollY);
                if (0 <= x && x < array.length && 0 <= y && y < array[0].length) {
                    model.getScreenHandler().put(finalPos.x, finalPos.y, MAP_SPRITES[1][1]);
                    if (array[x][y] != null) {
                        array[x][y].drawYourself(model.getScreenHandler(), finalPos);

                    }
                } else {
                    int xs = x == -1 ? 0 : (x < array.length ? 1 : 2);
                    int ys = y == -1 ? 0 : (y < array[0].length ? 1 : 2);
                    model.getScreenHandler().put(finalPos.x, finalPos.y, MAP_SPRITES[xs][ys]);
                }
            }
        }

    }

    private Point convertToScreen(int x, int y) {
        int xOffset = (DrawingArea.WINDOW_COLUMNS - (array.length+2) * 8) / 2;
        int yOffset = 1 + (DrawingArea.WINDOW_ROWS - (array[0].length+2) * 8) / 2;
        return new Point(xOffset + (x+1) * 8, yOffset + (y+1) * 8);
    }

    @Override
    public GameView getNextView(Model model) {
        return previous;
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setTimeToTransition(true);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            scrollX--;
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            scrollX++;
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            scrollY--;
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            scrollY++;
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            scrollX = 0;
            scrollY = 0;
            madeChanges();
        }
    }

    private static class TreasureMapTopText extends TopText {
        private static final String ARROW_STRING = (char)(0xB1) + "" + (char)(0xB5) + "" +
                (char)(0xB2) + "" + (char)(0xB0) + "";

        @Override
        protected void drawKeyTexts(Model model) {
            BorderFrame.drawString(model.getScreenHandler(),
                    "SPACE=center " +
                    ARROW_STRING +
                    "=pan ESC=exit", 50, 0, MyColors.WHITE);
        }
    }

    private static class MapContent {


        private final Point position;
        private final WorldHex hex;
        private final boolean finalSpot;
        private Sprite sprite;
        private Point previous;
        private Point next;

        public MapContent(Point p, WorldHex hex, boolean finalSpot) {
            this.position = p;
            this.hex = hex;
            this.finalSpot = finalSpot;
            HexLocation loc = hex.getLocation();
            if (loc != null) {
                if (loc instanceof TownLocation || loc instanceof CastleLocation) {
                    sprite = TOWN_SPRITE;
                } else if (loc instanceof WoodsLocation || loc instanceof DeepWoodsLocation || loc instanceof JungleLocation) {
                    sprite = WOODS_SPRITE;
                } else if (loc instanceof HillsLocation) {
                    sprite = HILLS_SPRITE;
                } else if (loc instanceof MountainLocation) {
                    sprite = MOUNTAIN_SPRITES;
                } else if (loc instanceof SwampLocation) {
                    sprite = SWAMP_SPRITE;
                } else if (loc instanceof WastelandLocation) {
                    sprite = WASTELAND_SPRITE;
                }
            }
        }

        public Sprite getSprite() {
            return sprite;
        }

        public Point getPosition() {
            return position;
        }

        public void drawYourself(ScreenHandler screenHandler, Point finalPos) {
            int y_extra = 4 * (1 - (position.x % 2));
            finalPos.y += y_extra;
            if (finalPos.y < 2) {
                return; // Don't draw at top of screen.
            }
            if (sprite != null) {
                screenHandler.register(sprite.getName(), finalPos, sprite, 2);
            }
            if (next != null) {
                Point dxDy = new Point(next.x - position.x, next.y - position.y);
                Sprite dirSprite = getSpriteForDxDy(dxDy, true);
                screenHandler.register(dirSprite.getName(), finalPos, dirSprite, 1);
            }
            if (previous != null) {
                Sprite dirSprite = getSpriteForDxDy(new Point(previous.x - position.x, previous.y - position.y), false);
                screenHandler.register(dirSprite.getName(), finalPos, dirSprite, 1);
            }

            if (finalSpot) {
                screenHandler.register(FINAL_SPOT_SPRITE.getName(), finalPos, FINAL_SPOT_SPRITE, 3);
            }
        }

        private Sprite getSpriteForDxDy(Point dxdy, boolean withRiver) {
            int dir = Direction.getDirectionForDxDy(position, dxdy);
            String shor = Direction.nameForDirection(dir);
            if (withRiver && hex.getRiversInDirection(dir)) {
                return RIVER_SPRITES[DIRECTION_SHORTS.indexOf(shor)];
            }
            return DIRECTION_SPRITES[DIRECTION_SHORTS.indexOf(shor)];
        }

        public void setPrevious(Point point) {
            this.previous = point;
        }

        public void setNext(Point point) {
            this.next = point;
        }
    }


    private static Sprite[][] makeMapSprites() {
        Sprite[][] sprites = new Sprite[3][3];
        for (int y = 0; y < sprites[0].length; y++) {
            for (int x = 0; x < sprites.length; x++) {
                sprites[x][y] = new TreasureMapSprite(x, y);
            }
        }
        return sprites;
    }

    private static Sprite[] makeDirectionSprites(int row) {
        Sprite[] result = new Sprite[6];
        for (int i = 0; i < 6; ++i) {
            result[i] = new TreasureMapSprite(i, row);
        }
        return result;
    }

    private static class TreasureMapSprite extends Sprite {
        public TreasureMapSprite(int col, int row) {
            super("treasuremap-"+col+"-"+row, "map.png", col, row, 64, 64);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BEIGE);
            setColor3(MyColors.RED);
            setColor4(MyColors.DARK_BLUE);
        }
    }

}
