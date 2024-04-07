package view;

import model.Model;
import model.items.books.BookItem;
import sound.SoundEffects;
import util.MyLists;
import util.MyPair;
import util.MyStrings;
import util.MyUnaryIntFunction;
import view.sprites.Sprite;
import view.sprites.Sprite8x8;
import view.widget.TopText;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.KeyEvent;
import java.util.Map;

public class ReadBookView extends GameView {
    private static final int BOOK_WIDTH = 28;
    private static final int BOOK_HEIGHT = 35;
    private static final MyColors PAGE_COLOR = MyColors.BEIGE;
    private static final int PARTITION_WIDTH = BOOK_WIDTH-1;
    private static final int LINES_PER_PAGE = BOOK_HEIGHT - 4;
    private final InventoryView previous;
    private final BookItem book;
    private TopText topText;

    private final Sprite[][] coverSprites;
    private final Sprite[][] singlePageSprites;
    private final Sprite[][] multiplePagesSprites;
    private int currentPagePair = 0;
    private int maxPagePair;
    private List<List<String>> content = List.of(
            List.of("First line", "Second line", "", "Fourth line"),
            List.of("Second page stuff"),
            List.of("Lorem ipsum lorem upsum", " ipsum Lorem", "long line", "greedy"),
            List.of("This is page 4"));
    private Map<String, Sprite> figures;

    public ReadBookView(Model model, InventoryView inventoryView, BookItem book) {
        super(true);
        this.topText = new BookViewTopText();
        this.previous = inventoryView;
        this.book = book;

        coverSprites = makeBookSprites(book.getCoverColor(), book.getCoverColor(),
                book.getCoverColor(), book.getCoverColor());
        singlePageSprites = makeBookSprites(book.getCoverColor(), PAGE_COLOR, book.getCoverColor(), PAGE_COLOR);
        multiplePagesSprites = makeBookSprites(book.getCoverColor(), PAGE_COLOR, PAGE_COLOR, MyColors.GRAY);

        makeContentsFromBook(book);
    }

    private void makeContentsFromBook(BookItem book) {
        List<List<String>> result = new ArrayList<>();

        List<String> firstPage = new ArrayList<>();
        addNewLines(firstPage, 10);
        addCentered(firstPage, MyStrings.partitionWithLineBreaks(book.getTitle(), PARTITION_WIDTH-2));
        addNewLines(firstPage, 2);
        addCentered(firstPage, MyStrings.partitionWithLineBreaks(book.getAuthor(), PARTITION_WIDTH-2));
        result.add(firstPage);

        String[] rest = MyStrings.partitionWithLineBreaks(book.getTextContent(), PARTITION_WIDTH);
        List<String> lines = new ArrayList<>(Arrays.asList(rest));
        while (!lines.isEmpty()) {
            List<String> page = MyLists.take(lines, LINES_PER_PAGE);
            result.add(page);
        }


        content = result;
        maxPagePair = (content.size() + 1) / 2 + 1;
        this.figures = book.getFigures();
    }

    private void addCentered(List<String> firstPage, String[] text) {
        for (String s : text) {
            int padding = Math.max(0, (PARTITION_WIDTH - s.length()) / 2 - 1);
            firstPage.add(String.format("%" + (padding + s.length()) + "s", s));
        }
    }

    private void addNewLines(List<String> firstPage, int amount) {
        for (int i = 0; i < amount; ++i) {
            firstPage.add("");
        }
    }


    @Override
    public void transitionedTo(Model model) { }

    @Override
    public void transitionedFrom(Model model) { }

    @Override
    protected void internalUpdate(Model model) {
        model.getScreenHandler().clearAll();
        topText.drawYourself(model);
        BorderFrame.drawFrameTop(model.getScreenHandler());
        if (currentPagePair == 0) {
            drawClosedBook(model);
        } else {
            drawOpenBook(model, currentPagePair - 1, maxPagePair - currentPagePair);
            drawPageNumbers(model);
            drawContent(model);
        }
    }


    private void drawContent(Model model) {
        int leftPage = currentPagePair*2 - 2;
        int rightPage = currentPagePair*2 - 1;
        int leftX = (DrawingArea.WINDOW_COLUMNS - 2 * BOOK_WIDTH) / 2 + 2;
        int rightX = (DrawingArea.WINDOW_COLUMNS - 2 * BOOK_WIDTH) / 2 + BOOK_WIDTH + 2;
        if (leftPage > 0 && leftPage-1 < content.size()) {
            drawText(model.getScreenHandler(), content.get(leftPage-1), leftX);
        }
        if (rightPage <= maxPagePair*2 - 1 && rightPage-1 < content.size()) {
            drawText(model.getScreenHandler(), content.get(rightPage-1), rightX);
        }
    }

    private void drawText(ScreenHandler screenHandler, List<String> strings, int xOff) {
        int yOff = getYOffset() + 2;
        for (int y = 0; y < strings.size(); ++y) {
            String line = strings.get(y);
            if (line.startsWith("<fig ")) {
                String key = line.replace("<fig ", "").replace(">", "");
                Sprite spr = figures.get(key);
                if (spr == null) {
                    throw new IllegalArgumentException("No figure for key: " + key);
                }
                int shift = (PARTITION_WIDTH - (spr.getWidth() / 8)) / 2 - 1;
                screenHandler.register(spr.getName(), new Point(xOff + shift, yOff + y), spr);
            } else {
                if (line.length() > 0 && line.charAt(line.length() - 1) == ' ') {
                    line = line.substring(0, line.length() - 1);
                }
                BorderFrame.drawString(screenHandler, line, xOff, yOff + y, MyColors.BLACK, PAGE_COLOR);
            }
        }
    }

    private void drawPageNumbers(Model model) {
        int y = getYOffset() + BOOK_HEIGHT + 2;
        int leftX = (DrawingArea.WINDOW_COLUMNS - 2 * BOOK_WIDTH) / 2;
        int rightX = (DrawingArea.WINDOW_COLUMNS - 2 * BOOK_WIDTH) / 2 + 2 * BOOK_WIDTH - 2;
        int leftPage = currentPagePair*2 - 2;
        int rightPage = currentPagePair*2 - 1;
        if (leftPage > 0) {
            BorderFrame.drawString(model.getScreenHandler(), "" + leftPage, leftX, y, MyColors.GRAY);
        }
        if (rightPage < maxPagePair*2 - 1) {
            BorderFrame.drawString(model.getScreenHandler(), String.format("%2d", rightPage), rightX, y, MyColors.GRAY);
        }
    }

    private void drawClosedBook(Model model) {
        int xStart = (DrawingArea.WINDOW_COLUMNS - BOOK_WIDTH) / 2;
        int yStart = getYOffset();

        generalDrawPage(model.getScreenHandler(), new Point(xStart, yStart), coverSprites,
                x -> x == 2 ? 2 : (x == BOOK_WIDTH - 1 ? 4 : 3));
    }

    private int getYOffset() {
        return (DrawingArea.WINDOW_ROWS - BOOK_HEIGHT) / 2;
    }

    private void drawOpenBook(Model model, int pagesLeft, int pagesRight) {
        int xStart = (DrawingArea.WINDOW_COLUMNS - BOOK_WIDTH * 2) / 2;
        int yStart = (DrawingArea.WINDOW_ROWS - BOOK_HEIGHT) / 2;

        if (pagesLeft == 0) {
            generalDrawPage(model.getScreenHandler(), new Point(xStart, yStart), coverSprites,
                    x -> x == 0 ? 0 : 3);
        } else if (pagesLeft == 1) {
            generalDrawPage(model.getScreenHandler(), new Point(xStart, yStart), singlePageSprites,
                    x -> x == BOOK_WIDTH - 1 ? 1 : (x == 0 ? 0 : 3));
        } else {
            generalDrawPage(model.getScreenHandler(), new Point(xStart, yStart), multiplePagesSprites,
                    x -> x == BOOK_WIDTH - 1 ? 1 : (x == 0 ? 0 : 3));
        }

        if (pagesRight == 0) {
            generalDrawPage(model.getScreenHandler(), new Point(xStart + BOOK_WIDTH, yStart), coverSprites,
                    x -> x == BOOK_WIDTH - 1 ? 4 : 3);
        } else if (pagesRight == 1) {
            generalDrawPage(model.getScreenHandler(), new Point(xStart + BOOK_WIDTH, yStart), singlePageSprites,
                    x -> x == 0 ? 2 : (x == BOOK_WIDTH - 1 ? 4 : 3));
        } else {
            generalDrawPage(model.getScreenHandler(), new Point(xStart + BOOK_WIDTH, yStart), multiplePagesSprites,
                    x -> x == 0 ? 2 : (x == BOOK_WIDTH - 1 ? 4 : 3));
        }
    }

    private void generalDrawPage(ScreenHandler screenHandler, Point start, Sprite[][] sprites,
                                 MyUnaryIntFunction<Integer> xFun) {
        for (int y = 0; y < BOOK_HEIGHT; ++y) {
            for (int x = 0; x < BOOK_WIDTH; ++x) {
                int spriteY = y == 0 ? 0 : (y == BOOK_HEIGHT - 1 ? 2 : 1);
                Sprite spr = sprites[xFun.apply(x)][spriteY];
                screenHandler.put(start.x + x, start.y + y, spr);
            }
        }
    }



    @Override
    public GameView getNextView(Model model) {
        return previous;
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setTimeToTransition(true);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            currentPagePair = Math.min(currentPagePair + 1, maxPagePair);
            SoundEffects.playSound(book.getSound());
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            currentPagePair = Math.max(currentPagePair - 1, 0);
            SoundEffects.playSound(book.getSound());
            madeChanges();
        }
    }


    private Sprite[][] makeBookSprites(MyColors c1, MyColors c2, MyColors c3, MyColors c4) {
        Sprite[][] result = new Sprite[5][3];
        for (int y = 0; y < result[0].length; ++y) {
            for (int x = 0; x < result.length; ++x) {
                result[x][y] = new Sprite8x8("book"+x+"-"+y,
                        "book.png", 0x10 * y + x, c1, c2, c3, c4);
            }
        }
        return result;
    }


    private static class BookViewTopText extends TopText {
        private static final String ARROW_STRING = (char)(0xB1) + "" + (char)(0xB0) + "";

        @Override
        protected void drawKeyTexts(Model model) {
            BorderFrame.drawString(model.getScreenHandler(),
                            ARROW_STRING +
                            "=page ESC=exit", 64, 0, MyColors.WHITE);
        }
    }
}
