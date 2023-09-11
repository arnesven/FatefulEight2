package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.states.cardgames.CardGame;
import model.states.cardgames.CardGameCard;
import model.states.cardgames.CardGameObject;
import model.states.cardgames.CardGamePlayer;
import view.BorderFrame;
import view.MyColors;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.awt.*;
import java.awt.event.KeyEvent;

public class CardGameSubView extends SubView {

    protected static final Sprite brownBlock = new FilledBlockSprite(MyColors.BROWN);
    private static final int PLAYER_CARDS_OFFSET = X_OFFSET + 2;
    private static final Sprite16x16 CURSOR = new Sprite16x16("cardcursor", "cardgame.png", 0x20,
            MyColors.YELLOW, MyColors.BROWN, MyColors.PINK, MyColors.BEIGE);
    private CardGame cardGame;
    private static CardHandSpriteSet cardHandSprites = new CardHandSpriteSet(MyColors.PINK);

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX, brownBlock);
        drawCorners(model);
        drawNPCs(model);
        drawPlayerArea(model);
        drawGameArea(model);
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
                drawNPCTop(model, cardGame.getNPC(2));
            case 2:
                drawNPCToLeft(model, cardGame.getNPC(0), 10);
                drawNPCToRight(model, cardGame.getNPC(1), 14);
                break;
            case 5:
                drawNPCToRight(model, cardGame.getNPC(4), 24);
            case 4:
                drawNPCToLeft(model, cardGame.getNPC(0), 20);
                drawNPCToLeft(model, cardGame.getNPC(1), 6);
                drawNPCTop(model, cardGame.getNPC(2));
                drawNPCToRight(model, cardGame.getNPC(3), 10);
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
}
