package view.subviews;

import control.FatefulEight;
import model.Model;
import model.states.cardgames.CardGameCard;
import model.states.cardgames.CardGamePlayer;
import model.states.cardgames.cabal.CabalCardGame;
import model.states.cardgames.cabal.CabalCardGamePlayer;
import model.states.cardgames.cabal.DaggerCabalCard;
import model.states.cardgames.cabal.StarCabalCard;
import util.MyPair;
import view.BorderFrame;
import view.MyColors;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class CabalCardGameSubView extends CardGameSubView {

    private Sprite32x32 bagSprite = new Sprite32x32("cabalbag", "items.png", 0xD2,
            MyColors.TRANSPARENT, MyColors.DARK_PURPLE, MyColors.DARK_RED, MyColors.BLACK);

    private StarCabalCard star = new StarCabalCard();
    private DaggerCabalCard dagger = new DaggerCabalCard();
    private static final Sprite BIG_POT_SPRITE =
            new Sprite32x16("bigpotsprite", "cardgame.png", 0x16,
                    MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.BEIGE);
    private static final Sprite16x16 blackStone = new Sprite16x16("traitorstone",
            "lotto.png", 0x26, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.GRAY_RED, MyColors.DARK_GRAY);
    private static final Sprite16x16 whiteStone = new Sprite16x16("friendstone",
            "lotto.png", 0x23, MyColors.GRAY, MyColors.WHITE, MyColors.GRAY_RED, MyColors.DARK_GRAY);
    private final CabalCardGame game;
    private FallingStoneAnimation fallingStoneAnimation = null;
    private CardAnimation playedCardAnimation;
    private boolean cheatsOn = false;

    public CabalCardGameSubView(CabalCardGame game) {
        this.game = game;
    }

    @Override
    protected void drawArea(Model model) {
        super.drawArea(model);
        if (game.showBag()) {
            model.getScreenHandler().register(bagSprite.getName(), new Point(X_OFFSET + 13, Y_OFFSET + 15),
                    bagSprite);
        }
        if (cardDealtAnimationDone()) {
            BorderFrame.drawString(model.getScreenHandler(), "  ",
                    X_OFFSET + 11, Y_OFFSET + 18, MyColors.BROWN, MyColors.BROWN);
        } else {
            BorderFrame.drawString(model.getScreenHandler(), "TURN " + game.getTurn(),
                    X_OFFSET + 11, Y_OFFSET + 16, MyColors.PINK, MyColors.BROWN);
        }

        if (game.getTurn() > 0) {
            drawBigPot(model);
        }

        drawPickedCards(model);
        drawPreviousCards(model);
        if (playedCardAnimation != null) {
            playedCardAnimation.drawYourself(model);
            if (playedCardAnimation.checkForDone()) {
                playedCardAnimation = null;
            }
        }
    }

    private void drawBigPot(Model model) {
        int x = X_OFFSET + 4;
        int y = Y_MAX - 6;
        model.getScreenHandler().register(BIG_POT_SPRITE.getName(),
                new Point(x, y), BIG_POT_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("%2d", game.getPotSize()),
                x, y + 2, MyColors.LIGHT_GRAY, MyColors.BROWN);
    }

    @Override
    protected void drawBet(Model model, int bet, int x, int y) {
        if (game.getTurn() == 0) {
            super.drawBet(model, bet, x, y);
        }
    }

    @Override
    protected void drawPlayerArea(Model model) {
        super.drawPlayerArea(model);
        CabalCardGamePlayer player = (CabalCardGamePlayer) game.getCharacterPlayer();
        Point loyaltyLocation = new Point(X_OFFSET + 17, Y_MAX - 1);
        if (game.isShowStonesAndCards()) {
            BorderFrame.drawString(model.getScreenHandler(), "    ", loyaltyLocation.x, loyaltyLocation.y,
                    MyColors.BLACK, MyColors.BROWN); // Cover obols.
            Sprite sp = player.isTraitor() ? blackStone : whiteStone;
            model.getScreenHandler().register(sp.getName(), new Point(loyaltyLocation.x + 3, loyaltyLocation.y - 1), sp);
        } else {
            if (player.isLoyaltySet()) {
                if (player.isTraitor()) {
                    BorderFrame.drawString(model.getScreenHandler(), "TRAITOR", loyaltyLocation.x, loyaltyLocation.y,
                            MyColors.WHITE, MyColors.BLACK);
                } else {
                    BorderFrame.drawString(model.getScreenHandler(), "FRIEND", loyaltyLocation.x, loyaltyLocation.y,
                            MyColors.BLACK, MyColors.WHITE);
                }
            }
        }

        if (player.hasClaim()) {
            drawClaimAt(model, player, X_OFFSET + 22, Y_MAX - 2);
        }

        if (cheatsOn) {
            BorderFrame.drawString(model.getScreenHandler(), game.getCurrentSuspicionFor(game.getCharacterPlayer()),
                    X_OFFSET + 6, Y_MAX-1,
                    MyColors.WHITE, MyColors.DARK_RED);
        }
    }

    private void drawClaimAt(Model model, CabalCardGamePlayer player, int x, int y) {
        BorderFrame.drawString(model.getScreenHandler(), "" + player.getClaimedStars(),
                x, y,
                MyColors.GREEN, MyColors.WHITE);
        BorderFrame.drawString(model.getScreenHandler(), "" + player.getClaimedDaggers(),
                x + 1, y,
                MyColors.RED, MyColors.WHITE);
    }

    private void coverObolsAt(Model model, int x, int y) {
        BorderFrame.drawString(model.getScreenHandler(), "   ",
                x, y,
                MyColors.WHITE, MyColors.BROWN);
    }

    private void drawClaimAndStone(Model model, CabalCardGamePlayer cabalNPC, Point claimLocation, Point cardPos) {
        if (cabalNPC.hasClaim()) {
            drawClaimAt(model, cabalNPC, claimLocation.x, claimLocation.y);
        } else {
            coverObolsAt(model, claimLocation.x, claimLocation.y);
        }

        if (game.isShowStonesAndCards()) {
            coverObolsAt(model, claimLocation.x, claimLocation.y);
            drawStoneAt(model, cabalNPC, claimLocation);
            drawCardsAt(model, cabalNPC.getCards(), cardPos.x, cardPos.y, 1);
        }
    }

    private void drawStoneAt(Model model, CabalCardGamePlayer cabalNPC, Point claimLocation) {
        Sprite sp = cabalNPC.isTraitor() ? blackStone : whiteStone;
        model.getScreenHandler().register(sp.getName(), claimLocation, sp);
    }

    @Override
    protected void drawNPCTop(Model model, CardGamePlayer npc, int firstNPCX) {
        super.drawNPCTop(model, npc, firstNPCX);
        drawClaimAndStone(model, (CabalCardGamePlayer) npc, new Point(X_OFFSET + firstNPCX + 3, Y_OFFSET),
                new Point(X_OFFSET+13, Y_OFFSET + 2));
        if (cheatsOn) {
            drawCardsAt(model, npc.getCards(), X_OFFSET + firstNPCX+4, Y_OFFSET - 2, 1);
            drawStoneAt(model, (CabalCardGamePlayer) npc, new Point(X_OFFSET + firstNPCX + 1, Y_OFFSET + 1));
            BorderFrame.drawString(model.getScreenHandler(), game.getCurrentSuspicionFor(npc),
                    X_OFFSET + firstNPCX-8, Y_OFFSET,
                    MyColors.WHITE, MyColors.DARK_RED);
        }
    }

    @Override
    protected void drawNPCToLeft(Model model, CardGamePlayer npc, int firstNPCY) {
        super.drawNPCToLeft(model, npc, firstNPCY);
        int y = Y_OFFSET + firstNPCY + Math.max(6, npc.getName().length()) + 1;
        drawClaimAndStone(model, (CabalCardGamePlayer) npc,
                new Point(X_OFFSET, y), new Point(X_OFFSET+2, y));
        if (cheatsOn) {
            drawCardsAt(model, npc.getCards(), X_OFFSET - 6, y, 1);
            drawStoneAt(model, (CabalCardGamePlayer) npc, new Point(X_OFFSET - 3, y+1));
            BorderFrame.drawString(model.getScreenHandler(), game.getCurrentSuspicionFor(npc),
                    X_OFFSET, Y_OFFSET + firstNPCY-1,
                    MyColors.WHITE, MyColors.DARK_RED);
        }
    }

    @Override
    protected void drawNPCToRight(Model model, CardGamePlayer npc, int firstNPCY) {
        super.drawNPCToRight(model, npc, firstNPCY);
        String obolsString = "" + npc.getObols();
        int y = Y_OFFSET + firstNPCY - 3;
        drawClaimAndStone(model, (CabalCardGamePlayer) npc,
                new Point(X_MAX - 3, y), new Point(X_MAX - 7, y));
        if (cheatsOn) {
            drawCardsAt(model, npc.getCards(), X_MAX, y, 1);
            drawStoneAt(model, (CabalCardGamePlayer) npc, new Point(X_MAX, y + 1));
            BorderFrame.drawString(model.getScreenHandler(), game.getCurrentSuspicionFor(npc),
                    X_MAX-4, y + 8,
                    MyColors.WHITE, MyColors.DARK_RED);
        }
    }

    private void drawPickedCards(Model model) {
        int i = 0;
        for (CardGameCard cc : game.getPickedCards()) {
            i++;
            Sprite sp = cc.getSprite();
            model.getScreenHandler().register(sp.getName(), new Point(X_OFFSET + 8 + i * 2, Y_OFFSET + 19),
                    sp);
        }
    }

    private void drawPreviousCards(Model model) {
        if (game.previousStars() > 0) {
            Sprite sp = star.getSprite();
            model.getScreenHandler().register(sp.getName(), new Point(X_OFFSET + 18, Y_OFFSET + 17),
                    sp);
            if (game.previousStars() > 1) {
                BorderFrame.drawString(model.getScreenHandler(), "" + game.previousStars(),
                        X_OFFSET + 20, Y_OFFSET + 18,
                        MyColors.PINK, MyColors.BROWN);
            }
        }

        for (int i = 0; i < game.previousDaggers(); ++i) {
            Sprite sp = dagger.getSprite();
            model.getScreenHandler().register(sp.getName(), new Point(X_OFFSET + 21 + i * 2, Y_OFFSET + 17),
                    sp);
        }
    }

    public void addFallingStoneAnimation(boolean isBlack) {
        this.fallingStoneAnimation = new FallingStoneAnimation(isBlack ? MyColors.BLACK : MyColors.GRAY,
                isBlack ? MyColors.DARK_GRAY : MyColors.WHITE);
        addOngoingEffect(new MyPair<>(new Point(X_OFFSET + 14, Y_OFFSET+9), fallingStoneAnimation));
    }

    public boolean fallingStoneAnimationIsDone() {
        return fallingStoneAnimation == null || fallingStoneAnimation.isDone();
    }

    public void addCardPlayedAnimation(CardGamePlayer target) {
        this.playedCardAnimation = new CardAnimation(getRotationAndOffsetForHandAnimation(target), true);
    }

    public boolean playedCardAnimationIsDone() {
        return playedCardAnimation == null;
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (FatefulEight.inDebugMode() && keyEvent.getKeyCode() == KeyEvent.VK_F8) {
            this.cheatsOn = !this.cheatsOn;
        }
        return super.handleKeyEvent(keyEvent, model);
    }

    private static class FallingStoneAnimation extends RunOnceAnimationSprite {
        private int shift = 1;

        public FallingStoneAnimation(MyColors color1, MyColors color2) {
            super("fallingstone", "lotto.png", 6, 2, 16, 16, 1,
                    color1);
            setColor2(color2);
            setAnimationDelay(15);
        }

        @Override
        public void stepAnimation(long elapsedTimeMs, Model model) {
            super.stepAnimation(elapsedTimeMs, model);
            this.shift++;
        }

        @Override
        public int getYShift() {
            return -4*shift;
        }
    }
}
