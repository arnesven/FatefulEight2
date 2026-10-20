package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.races.Race;
import model.states.cardgames.*;
import view.BorderFrame;
import view.MyColors;
import view.sprites.*;

import java.util.List;
import java.awt.*;
import java.awt.event.KeyEvent;

public class CardGameSubView extends SubView {

    protected static final Sprite brownBlock = new FilledBlockSprite(MyColors.BROWN);
    private static final int PLAYER_CARDS_OFFSET = X_OFFSET + 2;
    private static final Sprite CURSOR = new CardGameCursor();
    private static final TableSeating BOTTOM_POSITION = new TableSeating(0, 20);
    private static final TableSeating TOP_RIGHT_POSITION = new TableSeating(180, 17);
    private static final TableSeating TOP_LEFT_POSITION = new TableSeating(180, 3);
    private static final TableSeating LOWER_LEFT_POSITION = new TableSeating(90, 28);
    private static final TableSeating UPPER_LEFT_POSITION = new TableSeating(90, 14);
    private static final TableSeating LOWER_RIGHT_POSITION = new TableSeating(270, 20);
    private static final TableSeating UPPER_RIGHT_POSITION = new TableSeating(270, 6);
    private static final Sprite[] BET_SPRITES = makeBetSprites();

    private CardGame cardGame;
    private static CardHandSpriteSet cardHandSprites = new CardHandSpriteSet(MyColors.PINK);
    private HandAnimation handAnimation = null;
    private CardAnimation cardDealtAnimation = null;

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX, brownBlock);
        drawCorners(model);
        drawNPCs(model);
        drawPlayerArea(model);
        drawGameArea(model);
        drawHandAnimation(model);
        drawCardDealtAnimation(model);

        BorderFrame.drawString(model.getScreenHandler(), cardGame.getDeck().size() + "",
                X_OFFSET+11, Y_OFFSET+18, MyColors.PINK, MyColors.BROWN);
    }

    private void drawCardDealtAnimation(Model model) {
        if (cardDealtAnimation != null) {
            cardDealtAnimation.drawYourself(model);
            if (cardDealtAnimation.checkForDone()) {
                cardDealtAnimation = null;
            }
        }
    }

    private void drawHandAnimation(Model model) {
        if (handAnimation != null) {
            handAnimation.drawYourself(model);

            if (handAnimation.checkForDone()) {
                handAnimation = null;
            }
        }
    }

    private void drawGameArea(Model model) {
        SteppingMatrix<CardGameObject> mat = cardGame.getMatrix();
        for (int row = 0; row < mat.getRows(); ++row) {
            for (int col = 0; col < mat.getColumns(); ++col) {
                if (mat.getElementAt(col, row) != null) {
                    CardGameObject card = mat.getElementAt(col, row);
                    Sprite spr = card.getSprite();
                    Point position = new Point(PLAYER_CARDS_OFFSET + col*2, Y_OFFSET + 3 + row*2);
                    model.getScreenHandler().register(spr.getName(), position, spr);
                    if (cardGame.cursorEnabled() && card == cardGame.getSelectedObject()) {
                        if (card.hasSpecialCursor()) {
                            model.getScreenHandler().register(card.getCursorSprite().getName(), position, card.getCursorSprite());
                        } else {
                            model.getScreenHandler().register(CURSOR.getName(), position, CURSOR);
                        }
                    }
                }
            }
        }
    }

    protected void drawPlayerArea(Model model) {
        String name = cardGame.getCharacterPlayer().getName();
        BorderFrame.drawString(model.getScreenHandler(), name, X_OFFSET + 16 - name.length(), Y_MAX-1, MyColors.WHITE, MyColors.BLACK);
        BorderFrame.drawString(model.getScreenHandler(), cardGame.getCharacterPlayer().getObols() + "",
                X_OFFSET + 17, Y_MAX-1, MyColors.LIGHT_GRAY, MyColors.BROWN);
        drawBet(model, cardGame.getCharacterPlayer().getBet(), X_OFFSET+18, Y_MAX-10);
        int col = X_OFFSET + 8;
        int increment = cardGame.getCharacterPlayer().getPlayArea().size() > 7 ? 1 : 2;
        drawCardsAt(model, cardGame.getCharacterPlayer().getPlayArea(), col, Y_MAX - 5, increment);
    }

    protected void drawCardsAt(Model model, List<CardGameCard> cards, int x, int y, int spacing) {
        int col = x;
        for (CardGameCard card : cards) {
            Sprite spr = card.getSprite();
            Point position = new Point(col, y);
            col += spacing;
            model.getScreenHandler().register(spr.getName(), position, spr);
        }
    }

    private void drawCorners(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_OFFSET+2, Y_OFFSET, Y_OFFSET+2, blackBlock);
        model.getScreenHandler().fillSpace(X_MAX-2, X_MAX, Y_OFFSET, Y_OFFSET+2, blackBlock);
        model.getScreenHandler().fillSpace(X_OFFSET, X_OFFSET+2, Y_MAX-2, Y_MAX, blackBlock);
        model.getScreenHandler().fillSpace(X_MAX-2, X_MAX,Y_MAX-2, Y_MAX, blackBlock);

        model.getScreenHandler().put(X_OFFSET+2, Y_OFFSET, blackBlock);
        model.getScreenHandler().put(X_OFFSET, Y_OFFSET+2, blackBlock);

        model.getScreenHandler().put(X_MAX-3, Y_OFFSET, blackBlock);
        model.getScreenHandler().put(X_MAX-1, Y_OFFSET+2, blackBlock);

        model.getScreenHandler().put(X_OFFSET+2, Y_MAX-1, blackBlock);
        model.getScreenHandler().put(X_OFFSET, Y_MAX-3, blackBlock);

        model.getScreenHandler().put(X_MAX-3, Y_MAX-1, blackBlock);
        model.getScreenHandler().put(X_MAX-1, Y_MAX-3, blackBlock);
    }


    /*
    2 Players         3 Players         4 Players

     NPC0                                   NPC1
                   NPC0            NPC0
        TABLE           TABLE           TABLE
                             NPC1            NPC2
        PLAYER          PLAYER          PLAYER


       5 Players        6 Players       7 Players

            NPC2              NPC2      NPC2 NPC3
     NPC1              NPC1     NPC3  NPC1     NPC4
         TABLE             TABLE          TABLE
     NPC0     NPC3     NPC0     NPC4  NPC0     NPC5
         PLAYER            PLAYER         PLAYER

     */

    private void drawNPCs(Model model) {
        switch (cardGame.getNumberOfNPCs()) {
            case 0:
                break;
            case 1:
                drawNPCTop(model, cardGame.getNPC(0), 8);
                break;
            case 3:
                drawNPCTop(model, cardGame.getNPC(1), 23);
            case 2:
                drawNPCToLeft(model, cardGame.getNPC(0), 8);
                drawNPCToRight(model, cardGame.getNPC(cardGame.getNumberOfNPCs()-1), 26);
                break;
            case 5:
                drawNPCToRight(model, cardGame.getNPC(3), 12);
            case 4:
                drawNPCToLeft(model, cardGame.getNPC(0), 22);
                drawNPCToLeft(model, cardGame.getNPC(1), 8);
                drawNPCTop(model, cardGame.getNPC(2), 23);
                drawNPCToRight(model, cardGame.getNPC(cardGame.getNumberOfNPCs()-1), 26);
                break;
            case 6:
                drawNPCToLeft(model, cardGame.getNPC(0), 22);
                drawNPCToLeft(model, cardGame.getNPC(1), 8);
                drawNPCTop(model, cardGame.getNPC(2), 8);
                drawNPCTop(model, cardGame.getNPC(3), 23);
                drawNPCToRight(model, cardGame.getNPC(4), 12);
                drawNPCToRight(model, cardGame.getNPC(cardGame.getNumberOfNPCs()-1), 26);
        }
    }

    protected void drawNPCTop(Model model, CardGamePlayer npc, int firstNPCX) {
        for (int i = 0; i < 3; ++i) {
            Sprite arm = cardHandSprites.getCardHandSprite(npc, i, 180);
            model.getScreenHandler().register(arm.getName(), new Point(X_OFFSET+firstNPCX-i*2, Y_OFFSET+1), arm);
        }
        Sprite sleeve = npc.getCardGameSleeve().getHandSprite(180);
        model.getScreenHandler().register(sleeve.getName(), new Point(X_OFFSET+firstNPCX-2, Y_OFFSET+1), sleeve);
        BorderFrame.drawString(model.getScreenHandler(), npc.getName(), X_OFFSET+firstNPCX+2-npc.getName().length(),
                Y_OFFSET, MyColors.WHITE, MyColors.BLACK);
        BorderFrame.drawString(model.getScreenHandler(), npc.getObols()+"",
                X_OFFSET+firstNPCX+3, Y_OFFSET, MyColors.LIGHT_GRAY, MyColors.BROWN);
        drawBet(model, npc.getBet(), X_OFFSET + firstNPCX-5, Y_OFFSET + 4);
        int col = X_OFFSET+firstNPCX-9;
        int increment = npc.getPlayArea().size() > 7 ? 1 : 2;
        for (CardGameCard card : npc.getPlayArea()) {
            Sprite spr = card.getSprite();
            Point position = new Point(col, Y_OFFSET+3);
            col += increment;
            model.getScreenHandler().register(spr.getName(), position, spr);
        }
    }

    protected void drawBet(Model model, int bet, int x, int y) {
        if (bet == 0) {
            return;
        }
        Sprite sprite = null;
        if (bet <= 5) {
            sprite = BET_SPRITES[bet-1];
        } else if (bet < 10) {
            sprite = BET_SPRITES[5];
        } else if (bet < 15) {
            sprite = BET_SPRITES[6];
        } else {
            sprite = BET_SPRITES[7];
        }
        model.getScreenHandler().register(sprite.getName(), new Point(x, y), sprite);
        BorderFrame.drawString(model.getScreenHandler(), String.format("%2d", bet),
                x, y+2, MyColors.LIGHT_GRAY, MyColors.BROWN);
    }

    protected void drawNPCToLeft(Model model, CardGamePlayer npc, int firstNPCY) {
        for (int i = 0; i < 3; ++i) {
            Sprite arm = cardHandSprites.getCardHandSprite(npc, i, 90);
            model.getScreenHandler().register(arm.getName(), new Point(X_OFFSET+1, Y_OFFSET + firstNPCY + i*2), arm);
        }
        Sprite sleeve = npc.getCardGameSleeve().getHandSprite(90);
        model.getScreenHandler().register(sleeve.getName(), new Point(X_OFFSET+1, Y_OFFSET + firstNPCY), sleeve);
        for (int i = 0; i < npc.getName().length(); ++i) {
            BorderFrame.drawString(model.getScreenHandler(), npc.getName().substring(i, i+1), X_OFFSET,
                    Y_OFFSET + firstNPCY + i, MyColors.WHITE, MyColors.BLACK);
        }
        BorderFrame.drawString(model.getScreenHandler(), npc.getObols() + "",
                X_OFFSET, Y_OFFSET + firstNPCY + Math.max(6, npc.getName().length()) + 1, MyColors.LIGHT_GRAY, MyColors.BROWN);
        drawBet(model, npc.getBet(), X_OFFSET + 6, Y_OFFSET + firstNPCY+6);
        int row = Y_OFFSET + firstNPCY;
        for (CardGameCard card : npc.getPlayArea()) {
            Sprite spr = card.getSprite();
            Point position = new Point(X_OFFSET+3, row++);
            model.getScreenHandler().register(spr.getName(), position, spr);
        }
    }

    protected void drawNPCToRight(Model model, CardGamePlayer npc, int firstNPCY) {
        for (int i = 0; i < 3; ++i) {
            Sprite arm = cardHandSprites.getCardHandSprite(npc, i, 270);
            model.getScreenHandler().register(arm.getName(), new Point(X_MAX-3, Y_OFFSET + firstNPCY - i*2 + 3), arm);
        }
        Sprite sleeve = npc.getCardGameSleeve().getHandSprite(270);
        model.getScreenHandler().register(sleeve.getName(), new Point(X_MAX-5, Y_OFFSET + firstNPCY + 1), sleeve);

        StringBuilder namePatched = new StringBuilder(npc.getName());
        while (namePatched.length() < 6) { // To fill out with black for players on the right (cover arm).
            namePatched.append(" ");
        }
        for (int i = 0; i < namePatched.length(); ++i) {
            BorderFrame.drawString(model.getScreenHandler(), namePatched.substring(i, i+1), X_MAX-1,
                    Y_OFFSET + firstNPCY + i - 1, MyColors.WHITE, MyColors.BLACK);
        }
        String obolsString = npc.getObols() + "";
        BorderFrame.drawString(model.getScreenHandler(), obolsString,
                X_MAX-obolsString.length()-1, Y_OFFSET + firstNPCY-3, MyColors.LIGHT_GRAY, MyColors.BROWN);
        drawBet(model, npc.getBet(), X_MAX-9, Y_OFFSET + firstNPCY-6);
        int row = Y_OFFSET + firstNPCY - 2;
        for (CardGameCard card : npc.getPlayArea()) {
            Sprite spr = card.getSprite();
            Point position = new Point(X_MAX-5, row++);
            model.getScreenHandler().register(spr.getName(), position, spr);
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return cardGame.getUnderText(model);
    }

    @Override
    protected String getTitleText(Model model) {
        return "CARD GAME";
    }

    public void setGame(CardGame cardGame) {
        this.cardGame = cardGame;
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return cardGame.handleKeyEvent(keyEvent, model);
    }

    public void addHandAnimationFor(CardGamePlayer currentPlayer, boolean cardIn, boolean cardOut, boolean coin) {
        TableSeating rotationAndOffset = getRotationAndOffsetForHandAnimation(currentPlayer);
        this.handAnimation = new HandAnimation(cardIn, cardOut, coin, currentPlayer, rotationAndOffset);
    }

    protected TableSeating getRotationAndOffsetForHandAnimation(CardGamePlayer currentPlayer) {
        if (currentPlayer == cardGame.getCharacterPlayer()) {
            return BOTTOM_POSITION;
        }
        if (currentPlayer == cardGame.getNPC(0)) {
            switch (cardGame.getNumberOfNPCs()) {
                case 1:
                    return TOP_LEFT_POSITION;
                case 2:
                case 3:
                    return UPPER_LEFT_POSITION;
                case 4:
                case 5:
                case 6:
                    return LOWER_LEFT_POSITION;
            }
        }
        if (currentPlayer == cardGame.getNPC(1)) {
            switch (cardGame.getNumberOfNPCs()) {
                case 2:
                    return LOWER_RIGHT_POSITION;
                case 3:
                    return TOP_RIGHT_POSITION;
                case 4:
                case 5:
                case 6:
                    return UPPER_LEFT_POSITION;
            }
        }
        if (currentPlayer == cardGame.getNPC(2)) {
            switch (cardGame.getNumberOfNPCs()) {
                case 3:
                    return LOWER_RIGHT_POSITION;
                case 4:
                case 5:
                    return TOP_RIGHT_POSITION;
                case 6:
                    return TOP_LEFT_POSITION;
            }
        }
        if (currentPlayer == cardGame.getNPC(3)) {
            switch (cardGame.getNumberOfNPCs()) {
                case 4:
                    return LOWER_RIGHT_POSITION;
                case 5:
                    return UPPER_RIGHT_POSITION;
                case 6:
                    return TOP_RIGHT_POSITION;
            }
        }
        if (currentPlayer == cardGame.getNPC(4)) {
            switch (cardGame.getNumberOfNPCs()) {
                case 5:
                    return LOWER_RIGHT_POSITION;
                case 6:
                    return UPPER_RIGHT_POSITION;
            }
        }
        return LOWER_RIGHT_POSITION;
    }

    public boolean handAnimationDone() {
        if (handAnimation == null) {
            return true;
        }
        return handAnimation.checkForDone();
    }

    public boolean handAnimationHalfWay() {
        return handAnimation.isHalfWay();
    }

    public void addCardDealtAnimation(CardGamePlayer p) {
        this.cardDealtAnimation = new CardAnimation(getRotationAndOffsetForHandAnimation(p));
    }

    public boolean cardDealtAnimationDone() {
        return this.cardDealtAnimation == null || this.cardDealtAnimation.checkForDone();
    }

    private static Sprite[] makeBetSprites() {
        Sprite[] result = new Sprite[8];
        for (int col = 0; col < 2; ++col) {
            for (int row = 0; row < 4; ++row)
                result[col*4 + row] = new Sprite16x16("betsprite"+col+"x"+row, "cardgame.png", 0x10*row + col + 0xE,
                        MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.BEIGE);
        }
        return result;
    }

    private static class HandAnimation {
        private static final Sprite COIN_SPRITE = new Sprite16x16("betcoin", "cardgame.png", 0x0D,
                MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.CYAN);
        private final HandAnimationSprite sprite;
        private final TableSeating rotationAndOffset;
        private final Sprite32x32 cardSprite;
        private final boolean cardIn;
        private final boolean cardOut;
        private final boolean coin;

        public HandAnimation(boolean cardIn, boolean cardOut, boolean coin, CardGamePlayer currentPlayer, TableSeating rotationAndOffset) {
            this.cardIn = cardIn;
            this.cardOut = cardOut;
            this.coin = coin;
            this.sprite = new HandAnimationSprite(currentPlayer.getRace().getColor(),
                    currentPlayer.getCardGameSleeve(),
                    currentPlayer.getRace().id() == Race.DWARF.id() ||
                            currentPlayer.getRace().id() == Race.HALF_ORC.id(),
                    rotationAndOffset);
            this.rotationAndOffset = rotationAndOffset;
            this.cardSprite = new Sprite32x32("handanicard", "cardgame.png", 0x15,
                    MyColors.BLACK, MyColors.PINK, MyColors.PINK, MyColors.CYAN);
            cardSprite.setRotation(rotationAndOffset.rotation);
        }

        public TableSeating getSeating() {
            return rotationAndOffset;
        }

        public boolean checkForDone() {
            if (sprite.isDone()) {
                AnimationManager.unregister(sprite);
            }
            return sprite.isDone();
        }

        public void drawYourself(Model model) {
            Point position = getSeating().getHandPosition();
            Point cardPos = getSeating().getCardPosition();
            Point dxdy = getSeating().getHandDirection();
            model.getScreenHandler().register(sprite.getName(), position, sprite);
            Point cardPoint = new Point(cardPos.x + dxdy.x * sprite.getArmLength(),
                    cardPos.y + dxdy.y * sprite.getArmLength());
            if ((cardIn && sprite.doingFirstHalf()) || (cardOut && sprite.doingSecondHalf())) {
                model.getScreenHandler().register(cardSprite.getName(), cardPoint, cardSprite, 1);
            }
            if (coin && sprite.doingFirstHalf()) {
                model.getScreenHandler().register(COIN_SPRITE.getName(), cardPoint, COIN_SPRITE, 1);
            }
        }

        public boolean isHalfWay() {
            return sprite.doingSecondHalf();
        }
    }

}
