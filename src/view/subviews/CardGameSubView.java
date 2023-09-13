package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.states.cardgames.*;
import view.BorderFrame;
import view.MyColors;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class CardGameSubView extends SubView {

    protected static final Sprite brownBlock = new FilledBlockSprite(MyColors.BROWN);
    private static final int PLAYER_CARDS_OFFSET = X_OFFSET + 2;
    private static final Sprite CURSOR = new CardGameCursor();
    private static final TableSeating BOTTOM_POSITION = new TableSeating(0, 20);
    private static final TableSeating TOP_POSITION = new TableSeating(180, 10);
    private static final TableSeating LOWER_LEFT_POSITION = new TableSeating(90, 28);
    private static final TableSeating UPPER_LEFT_POSITION = new TableSeating(90, 14);
    private static final TableSeating LOWER_RIGHT_POSITION = new TableSeating(270, 20);
    private static final TableSeating UPPER_RIGHT_POSITION = new TableSeating(270, 6);

    private CardGame cardGame;
    private static CardHandSpriteSet cardHandSprites = new CardHandSpriteSet(MyColors.PINK);
    private HandAnimationSprite handAnimation = null;

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX, brownBlock);
        drawCorners(model);
        drawNPCs(model);
        drawPlayerArea(model);
        drawGameArea(model);
        drawHandAnimation(model);
    }

    private void drawHandAnimation(Model model) {
        if (handAnimation != null) {
            Point position = null;
            switch (handAnimation.getSeating().rotation) {
                case 0:
                    position = new Point(X_OFFSET + handAnimation.getSeating().offset, Y_MAX-8);
                    break;
                case 90:
                    position = new Point(X_OFFSET, Y_OFFSET + handAnimation.getSeating().offset);
                    break;
                case 180:
                    position = new Point(X_OFFSET + handAnimation.getSeating().offset, Y_OFFSET);
                    break;
                default:
                    position = new Point(X_MAX-8, Y_OFFSET + handAnimation.getSeating().offset);
                    break;
            }
            model.getScreenHandler().register(handAnimation.getName(), position, handAnimation);
            if (handAnimation.isDone()) {
                AnimationManager.unregister(handAnimation);
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
                        model.getScreenHandler().register(CURSOR.getName(), position, CURSOR);
                    }
                }
            }
        }
    }

    private void drawPlayerArea(Model model) {
        String name = cardGame.getCharacterPlayer().getName();
        BorderFrame.drawString(model.getScreenHandler(), name, X_OFFSET + 16 - name.length(), Y_MAX-1, MyColors.WHITE, MyColors.BLACK);
        BorderFrame.drawString(model.getScreenHandler(), cardGame.getCharacterPlayer().getObols() + "@",
                X_OFFSET + 17, Y_MAX-1, MyColors.LIGHT_GRAY, MyColors.BROWN);
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

    private void drawNPCs(Model model) {
        switch (cardGame.getNumberOfNPCs()) {
            case 0:
                break;
            case 1:
                drawNPCTop(model, cardGame.getNPC(0));
                break;
            case 3:
                drawNPCTop(model, cardGame.getNPC(1));
            case 2:
                drawNPCToLeft(model, cardGame.getNPC(0), 6);
                drawNPCToRight(model, cardGame.getNPC(cardGame.getNumberOfNPCs()-1), 26);
                break;
            case 5:
                drawNPCToRight(model, cardGame.getNPC(3), 12);
            case 4:
                drawNPCToLeft(model, cardGame.getNPC(0), 20);
                drawNPCToLeft(model, cardGame.getNPC(1), 6);
                drawNPCTop(model, cardGame.getNPC(2));
                drawNPCToRight(model, cardGame.getNPC(cardGame.getNumberOfNPCs()-1), 26);
                break;
        }
    }

    private void drawNPCTop(Model model, CardGamePlayer npc) {
        for (int i = 0; i < 3; ++i) {
            Sprite arm = cardHandSprites.getCardHandSprite(npc, i, 180);
            model.getScreenHandler().register(arm.getName(), new Point(X_OFFSET+17-i*2, Y_OFFSET+1), arm);
        }
        BorderFrame.drawString(model.getScreenHandler(), npc.getName(), X_OFFSET+19-npc.getName().length(),
                Y_OFFSET, MyColors.WHITE, MyColors.BLACK);
        BorderFrame.drawString(model.getScreenHandler(), npc.getObols() + "@",
                X_OFFSET+20, Y_OFFSET, MyColors.LIGHT_GRAY, MyColors.BROWN);
    }

    private void drawNPCToLeft(Model model, CardGamePlayer npc, int firstNPCY) {
        for (int i = 0; i < 3; ++i) {
            Sprite arm = cardHandSprites.getCardHandSprite(npc, i, 90);
            model.getScreenHandler().register(arm.getName(), new Point(X_OFFSET+1, Y_OFFSET + firstNPCY + i*2), arm);
        }
        for (int i = 0; i < npc.getName().length(); ++i) {
            BorderFrame.drawString(model.getScreenHandler(), npc.getName().substring(i, i+1), X_OFFSET,
                    Y_OFFSET + firstNPCY + i, MyColors.WHITE, MyColors.BLACK);
        }
        BorderFrame.drawString(model.getScreenHandler(), npc.getObols() + "@",
                X_OFFSET, Y_OFFSET + firstNPCY + Math.min(6, npc.getName().length()) + 1, MyColors.LIGHT_GRAY, MyColors.BROWN);
    }

    private void drawNPCToRight(Model model, CardGamePlayer npc, int firstNPCY) {
        for (int i = 0; i < 3; ++i) {
            Sprite arm = cardHandSprites.getCardHandSprite(npc, i, 270);
            model.getScreenHandler().register(arm.getName(), new Point(X_MAX-3, Y_OFFSET + firstNPCY - i*2 + 3), arm);
        }
        for (int i = 0; i < npc.getName().length(); ++i) {
            BorderFrame.drawString(model.getScreenHandler(), npc.getName().substring(i, i+1), X_MAX-1,
                    Y_OFFSET + firstNPCY + i - 1, MyColors.WHITE, MyColors.BLACK);
        }
        String obolsString = npc.getObols() + "@";
        BorderFrame.drawString(model.getScreenHandler(), obolsString,
                X_MAX-obolsString.length(), Y_OFFSET + firstNPCY-3, MyColors.LIGHT_GRAY, MyColors.BROWN);
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

    public void addHandAnimationFor(CardGamePlayer currentPlayer) {
        TableSeating rotationAndOffset = getRotationAndOffsetForHandAnimation(currentPlayer);
        this.handAnimation = new HandAnimationSprite(currentPlayer.getRace().getColor(), rotationAndOffset);
    }

    private TableSeating getRotationAndOffsetForHandAnimation(CardGamePlayer currentPlayer) {
        if (currentPlayer == cardGame.getCharacterPlayer()) {
            return BOTTOM_POSITION;
        }
        if (currentPlayer == cardGame.getNPC(0)) {
            switch (cardGame.getNumberOfNPCs()) {
                case 1:
                    return TOP_POSITION;
                case 2:
                case 3:
                    return UPPER_LEFT_POSITION;
                case 4:
                case 5:
                    return LOWER_LEFT_POSITION;

            }
        }
        if (currentPlayer == cardGame.getNPC(1)) {
            switch (cardGame.getNumberOfNPCs()) {
                case 2:
                    return LOWER_RIGHT_POSITION;
                case 3:
                    return TOP_POSITION;
                case 4:
                case 5:
                    return UPPER_LEFT_POSITION;
            }
        }
        if (currentPlayer == cardGame.getNPC(2)) {
            switch (cardGame.getNumberOfNPCs()) {
                case 3:
                    return LOWER_RIGHT_POSITION;
                case 4:
                case 5:
                    return TOP_POSITION;
            }
        }
        if (currentPlayer == cardGame.getNPC(3)) {
            switch (cardGame.getNumberOfNPCs()) {
                case 4:
                    return LOWER_RIGHT_POSITION;
                case 5:
                    return UPPER_RIGHT_POSITION;
            }
        }
        return LOWER_RIGHT_POSITION;
    }

    public boolean handAnimationDone() {
        if (handAnimation == null) {
            return true;
        }
        return handAnimation.isDone();
    }
}
