package view.dev;

import model.Model;
import util.Arithmetics;
import util.MyPair;
import view.BorderFrame;
import view.GameView;
import view.MyColors;
import view.SpriteManager;
import view.party.CharacterCreationView;
import view.party.SelectableListMenu;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class SpritePreviewerView extends SelectableListMenu {

    private static final int COLUMN_SKIP = 15;
    private static final Integer INPUT_MAX_LENGTH = 20;
    private static final StringBuffer START_STRING = new StringBuffer("items.pngþþþþþþþþþþþ");
    private String path;
    private int[] sizeSet = new int[]{8, 16, 24, 32, 48, 64};
    private MyColors[] colorSet = MyColors.values();
    private String[] backgroundSetNames = new String[]{"BLACK", "BLUE", "WHITE", "PINK"};
    private Sprite[] backgroundSet = new Sprite[]{
            new FilledBlockSprite(MyColors.BLACK), new FilledBlockSprite(MyColors.BLUE), new FilledBlockSprite(MyColors.WHITE),
            new FilledBlockSprite(MyColors.PINK)};

    private int[] colors;
    private Point mapPos;
    private Dimension size;
    private Sprite latestSprite;
    private MyPair<StringBuffer, Integer> mapBuffer;
    private boolean mapPathOk = true;
    private int selectedBackground = 0;

    public SpritePreviewerView(GameView previous) {
        super(previous, 40, 20);
        this.path = "items.png";
        this.mapPos = new Point(0, 0);
        this.size = new Dimension(3, 3);

        this.colors = new int[]{0, 1, 2, 3};
        mapBuffer = new MyPair<>(START_STRING, 9);
        generateSprite();
    }

    public void generateSprite() {
        Dimension spriteSize = new Dimension(sizeSet[size.width], sizeSet[size.height]);
        String path = mapBuffer.first.toString();
        if (path.contains("þ")) {
            path = mapBuffer.first.substring(0, mapBuffer.first.indexOf("þ"));
        }
        Sprite oldSprite = this.latestSprite;
        try {
            this.latestSprite = new Sprite("previewsprite", path, mapPos.x, mapPos.y, spriteSize.width, spriteSize.height);
            mapPathOk = true;
            latestSprite.setColor1(colorSet[colors[0]]);
            latestSprite.setColor2(colorSet[colors[1]]);
            latestSprite.setColor3(colorSet[colors[2]]);
            latestSprite.setColor4(colorSet[colors[3]]);
        } catch (Exception e) {
            this.latestSprite = oldSprite;
            System.err.println("Error when reading sprite");
            mapPathOk = false;
        }
    }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
     }

    @Override
    public void transitionedFrom(Model model) {
     }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart+1, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                String title = "- SPRITE PREVIEWER -";
                BorderFrame.drawCentered(model.getScreenHandler(), title, y++, MyColors.WHITE, MyColors.BLUE);
                y += 2;
                int spriteX = 50 - latestSprite.getWidth() / 16;
                model.getScreenHandler().fillSpace(spriteX, spriteX + latestSprite.getWidth() / 8,
                        y+3, y+3+latestSprite.getHeight()/8, backgroundSet[selectedBackground]);
                model.getScreenHandler().register(latestSprite.getName(), new Point(spriteX, y+3), latestSprite);

                String[] labels = new String[]{"Map Path", "",
                "Width", "Height", "",
                "Column", "Row", "",
                "Color 1", "Color 2", "Color 3", "Color 4", "",
                "Background"};
                for (int i = 0; i < labels.length; ++i) {
                    BorderFrame.drawString(model.getScreenHandler(), labels[i], x+2, y++, MyColors.WHITE, MyColors.BLUE);
                }
                model.getScreenHandler().put(x+COLUMN_SKIP+20, yStart+4, mapPathOk?
                        CharacterCreationView.CHECK_SPRITE:CharacterCreationView.NOT_OK_SPRITE);
            }
        });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(new InputFieldContent(xStart + COLUMN_SKIP, yStart + 4),
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 6, String.format(" %2d ", sizeSet[size.width])) {

                    @Override
                    public void turnLeft(Model model) {
                        size.width = Arithmetics.decrementWithWrap(size.width, sizeSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        size.width = Arithmetics.incrementWithWrap(size.width, sizeSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 7, String.format(" %2d ", sizeSet[size.height])) {

                    @Override
                    public void turnLeft(Model model) {
                        size.height = Arithmetics.decrementWithWrap(size.height, sizeSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        size.height = Arithmetics.incrementWithWrap(size.height, sizeSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 9, String.format(" %2d ", mapPos.x)) {

                    @Override
                    public void turnLeft(Model model) {
                        mapPos.x = Arithmetics.decrementWithWrap(mapPos.x, 16);
                    }

                    @Override
                    public void turnRight(Model model) {
                        mapPos.x = Arithmetics.incrementWithWrap(mapPos.x, 16);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 10, String.format(" %2d ", mapPos.y)) {

                    @Override
                    public void turnLeft(Model model) {
                        mapPos.y = Arithmetics.decrementWithWrap(mapPos.y, 20);
                    }

                    @Override
                    public void turnRight(Model model) {
                        mapPos.y = Arithmetics.incrementWithWrap(mapPos.y, 20);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 12, colorSet[colors[0]].name()) {

                    @Override
                    public void turnLeft(Model model) {
                        colors[0] = Arithmetics.decrementWithWrap(colors[0], colorSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        colors[0] = Arithmetics.incrementWithWrap(colors[0], colorSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 13, colorSet[colors[1]].name()) {

                    @Override
                    public void turnLeft(Model model) {
                        colors[1] = Arithmetics.decrementWithWrap(colors[1], colorSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        colors[1] = Arithmetics.incrementWithWrap(colors[1], colorSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 14, colorSet[colors[2]].name()) {

                    @Override
                    public void turnLeft(Model model) {
                        colors[2] = Arithmetics.decrementWithWrap(colors[2], colorSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        colors[2] = Arithmetics.incrementWithWrap(colors[2], colorSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 15, colorSet[colors[3]].name()) {

                    @Override
                    public void turnLeft(Model model) {
                        colors[3] = Arithmetics.decrementWithWrap(colors[3], colorSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        colors[3] = Arithmetics.incrementWithWrap(colors[3], colorSet.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 17, backgroundSetNames[selectedBackground]) {

                    @Override
                    public void turnLeft(Model model) {
                        selectedBackground = Arithmetics.decrementWithWrap(selectedBackground, backgroundSet.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selectedBackground = Arithmetics.incrementWithWrap(selectedBackground, backgroundSet.length);
                    }
                });
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            setTimeToTransition(true);
        } else if (getSelectedRow() == 0) {
            if (' ' == keyEvent.getKeyChar() || '_' == keyEvent.getKeyChar() || '.' == keyEvent.getKeyChar() ||
                    ('a' <= keyEvent.getKeyChar() && keyEvent.getKeyChar() <= 'z') ||
                    ('A' <= keyEvent.getKeyChar() && keyEvent.getKeyChar() <= 'Z')) {
                if (mapBuffer.second < INPUT_MAX_LENGTH) {
                    mapBuffer.first.setCharAt(mapBuffer.second++, keyEvent.getKeyChar());
                    madeChanges();
                    generateSprite();
                }
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                mapBuffer.second = Math.max(0, mapBuffer.second - 1);
                mapBuffer.first.setCharAt(mapBuffer.second, 'þ');
                madeChanges();
                generateSprite();
            }
        }
        List<ListContent> content = buildContent(model, 0, 0);
        if (content.get(getSelectedRow()) instanceof CarouselListContent) {
            CarouselListContent carousel = (CarouselListContent) content.get(getSelectedRow());
            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                carousel.turnLeft(model);
                madeChanges();
                generateSprite();
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                carousel.turnRight(model);
                madeChanges();
                generateSprite();
            }
        }
    }

    private class InputFieldContent extends ListContent {
        public InputFieldContent(int x, int y) {
            super(x, y, mapBuffer.first.toString());
        }
    }
}
