package model.quests;

import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;
import view.subviews.QuestSubView;

import java.awt.*;
import java.io.Serializable;

public class QuestEdge implements Serializable {
    public static final boolean HORIZONTAL = true;
    public static final boolean VERTICAL = false;
    private final Sprite horizontalSprite;
    private final Sprite verticalSprite;
    private static final Sprite[] specialSpritesWhite = makeSpecialSprites(MyColors.WHITE);
    private static final Sprite[] specialSpritesRed = makeSpecialSprites(MyColors.LIGHT_RED);
    private static final Sprite[] specialSpritesGreen = makeSpecialSprites(MyColors.LIGHT_GREEN);
    private final Sprite[] specialSprites;

    private static Sprite[] makeSpecialSprites(MyColors color2) {
        Sprite[] result = new Sprite[10];
        for (int i = 0; i < 8; ++i) {
            result[i] = new Sprite16x16("specialedge"+i, "quest.png", 0x42 + i,
                    MyColors.BLACK, color2, MyColors.PINK, MyColors.BEIGE);
        }
        result[8] = new Sprite16x16("specialedge8", "quest.png", 0x40,
                MyColors.BLACK, color2, MyColors.PINK, MyColors.BEIGE);
        result[9] = new Sprite16x16("specialedge9", "quest.png", 0x41,
                MyColors.BLACK, color2, MyColors.PINK, MyColors.BEIGE);
        return result;
    }

    private QuestNode node;
    private boolean alignment;

    public QuestEdge(QuestNode node, boolean align, MyColors lineColor) {
        this.node = node;
        this.alignment = align;
        if (lineColor == MyColors.LIGHT_RED) {
            specialSprites = specialSpritesRed;
        } else if (lineColor == MyColors.LIGHT_GREEN) {
            specialSprites = specialSpritesGreen;
        } else {
            specialSprites = specialSpritesWhite;
        }
        horizontalSprite = specialSprites[9];
        verticalSprite = specialSprites[8];
    }

    public QuestEdge(QuestNode node, boolean align) {
        this(node, align, MyColors.WHITE);
    }

    public QuestEdge(QuestNode node) {
        this(node, QuestEdge.HORIZONTAL, MyColors.WHITE);
    }

    public QuestNode getNode() {
        return node;
    }

    public void drawYourself(ScreenHandler screenHandler, QuestNode startPoint, int xOffset, int yOffset) {
        int dx = node.getColumn() - startPoint.getColumn();
        int dy = node.getRow() - startPoint.getRow();
        Point from = startPoint.getPosition();
        Point to = node.getPosition();
        if (dx == 0 && dy != 0) {
            goDownWithArrow(screenHandler, from, to, xOffset, yOffset, dy);
        } else if (dy == 0 && dx != 0) {
            goRightWithArrow(screenHandler, from, to, xOffset, yOffset, dx);
        } else {
            Point mid;
            int cornerNum;
            if (alignment) {
                mid = new Point(to.x, from.y);
                goRight(screenHandler, from, mid, xOffset, yOffset, dx);
                goDownWithArrow(screenHandler, mid, to, xOffset, yOffset, dy);
                cornerNum = (dx<0?0:1) + (dy<0?2:0);
            } else {
                mid = new Point(from.x, to.y);
                goDown(screenHandler, from, mid, xOffset, yOffset, dy);
                goRightWithArrow(screenHandler, mid, to, xOffset, yOffset, dx);
                cornerNum = (dx<0?1:0) + (dy<0?0:2);
            }
            Point conv = QuestSubView.convertToScreen(mid);
            screenHandler.register("corner", new Point(conv.x + 1, conv.y + 1), specialSprites[cornerNum]);
        }
    }

    private void goRight(ScreenHandler screenHandler, Point from, Point to, int xOffset, int yOffset, int dx) {
        if (dx < 0) {
            goStraight(screenHandler, to, from, xOffset, yOffset, true);
        } else {
            goStraight(screenHandler, from, to, xOffset, yOffset, true);
        }
    }

    protected boolean drawArrow() {
        return true;
    }

    private void goRightWithArrow(ScreenHandler screenHandler, Point from, Point to, int xOffset, int yOffset, int dx) {
        Point conv = QuestSubView.convertToScreen(to);
        if (dx < 0) {
            goStraight(screenHandler, to, from, xOffset, yOffset, true);
            screenHandler.clearForeground(conv.x + 2, conv.x + 3,
                    conv.y+1, conv.y+1);
            if (drawArrow()) {
                screenHandler.register("arrow", new Point(conv.x + 3, conv.y + 1), specialSprites[6]);
            } else {
                screenHandler.register("horipath1", new Point(conv.x + 3, conv.y + 1), horizontalSprite);
            }
        } else {
            goStraight(screenHandler, from, to, xOffset, yOffset, true);
            screenHandler.clearForeground(conv.x - 1, conv.x,
                    conv.y+1, conv.y+1);
            if (drawArrow()) {
                screenHandler.register("arrow", new Point(conv.x - 1, conv.y + 1), specialSprites[5]);
            } else {
                screenHandler.register("horipath1", new Point(conv.x - 1, conv.y + 1), horizontalSprite);
            }
        }
    }


    private void goDown(ScreenHandler screenHandler, Point from, Point to, int xOffset, int yOffset, int dy) {
        if (dy < 0) {
            goStraight(screenHandler, to, from, xOffset, yOffset, false);
        } else {
            goStraight(screenHandler, from, to, xOffset, yOffset, false);
        }
    }

    private void goDownWithArrow(ScreenHandler screenHandler, Point from, Point to, int xOffset, int yOffset, int dy) {
        Point conv = QuestSubView.convertToScreen(to);
        if (dy < 0) {
            goStraight(screenHandler, to, from, xOffset, yOffset, false);
            screenHandler.clearForeground(conv.x + 1,conv.x + 1,
                    conv.y+2, conv.y+3);
            if (drawArrow()) {
                screenHandler.register("arrow", new Point(conv.x + 1, conv.y + 3), specialSprites[4]);
            } else {
                screenHandler.register("vertipath1", new Point(conv.x + 1, conv.y + 3), verticalSprite);
            }
        } else {
            goStraight(screenHandler, from, to, xOffset, yOffset, false);
            screenHandler.clearForeground(conv.x + 1,conv.x + 1,
                    conv.y-1, conv.y);
            if (drawArrow()) {
                screenHandler.register("arrow", new Point(conv.x + 1, conv.y - 1), specialSprites[7]);
            } else {
                screenHandler.register("vertipath1", new Point(conv.x + 1, conv.y - 1), verticalSprite);
            }
        }
    }

    private void goStraight(ScreenHandler screenHandler, Point from, Point to, int xOffset, int yOffset, boolean horizontal) {
        from = new Point(from);
        to = new Point(to);
        int times = 0;
        while ((from.x != to.x && horizontal) || (from.y != to.y && !horizontal)) {
            Point conv = QuestSubView.convertToScreen(from);
            if (horizontal) {
                screenHandler.register("horipath1", new Point(conv.x + 2, conv.y + 1), horizontalSprite);
                screenHandler.register("horipath2", new Point(conv.x + 4, conv.y + 1), horizontalSprite);
                from.x++;
            } else {
                screenHandler.register("vertipath1", new Point(conv.x + 1, conv.y + 2), verticalSprite);
                screenHandler.register("vertipath2", new Point(conv.x + 1, conv.y + 4), verticalSprite);
                from.y++;
            }
            if (times++ > 100) {
                throw new IllegalStateException("Drew Edge 100 times, something is wrong!");
            }
        }
    }

    public boolean getAlignment() {
        return alignment;
    }
}
