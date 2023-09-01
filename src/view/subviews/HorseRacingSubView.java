package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.horses.Horse;
import model.states.horserace.HorseRaceTrack;
import model.states.horserace.HorseRacer;
import model.states.horserace.NPCHorseRacer;
import view.MyColors;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HorseRacingSubView extends SubView implements Animation {

    private Sprite BANNER_ICON = new BannerSprite();
    private Sprite PATH_ICON = new Sprite32x32("path", "riding.png",
            0x61, MyColors.DARK_GREEN, MyColors.GREEN, MyColors.BROWN);
    private static final long ANIMATION_DELAY = 3;
    public static final int HORSE_VERTICAL_POSITION = 2;
    private final Horse horse;
    private HorseRaceTrack horseRaceTrack = new HorseRaceTrack();
    private long internalStep = 0;

    private HorseRacer player;
    private List<NPCHorseRacer> npcs = new ArrayList<>();
    private List<HorseRacer> allRacers = new ArrayList<>();
    private boolean animationStarted = false;

    public HorseRacingSubView(GameCharacter rider, Horse horse) {
        this.horse = horse;
        this.player = new HorseRacer(3, rider, horse, horseRaceTrack);
        allRacers.add(player);
        AnimationManager.registerPausable(this);
    }

    @Override
    protected void drawArea(Model model) {
        horseRaceTrack.drawYourself(model, this, player);
        player.drawHorse(model, HORSE_VERTICAL_POSITION, 0);
        for (HorseRacer npc : npcs) {
            int yPos = HORSE_VERTICAL_POSITION +
                    (npc.getPosition().y + npc.getLap()*HorseRaceTrack.TRACK_LENGTH) -
                    (player.getPosition().y + player.getLap()*HorseRaceTrack.TRACK_LENGTH);
            int shiftDiff = npc.getYShift() - player.getYShift();
            if (0 < yPos && yPos < 8) {
                npc.drawHorse(model, yPos, shiftDiff);
            } else if (yPos == 0 && shiftDiff > 0) {
                npc.drawHorse(model, yPos, shiftDiff);
            } else if (yPos == 8 && shiftDiff < 0) {
                npc.drawHorse(model, yPos, shiftDiff);
            }
        }
        model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_OFFSET, Y_OFFSET+4, blackBlock, 20);
        model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_MAX-4, Y_MAX, blackBlock, 20);
        drawBanner(model);
    }

    private void drawBanner(Model model) {
        for (int i = 0; i < 8; ++i) {
            model.getScreenHandler().register(PATH_ICON.getName(),
                    new Point(X_OFFSET + i*4, Y_OFFSET), PATH_ICON, 21, 0, 5);
        }
        model.getScreenHandler().register(BANNER_ICON.getName(),
                new Point(X_OFFSET + (X_MAX - X_OFFSET)/2, Y_OFFSET), BANNER_ICON, 23);
        for (HorseRacer racer : allRacers) {
            Sprite symbol = CombatSubView.getInitiativeSymbol(racer.getCharacter(), model);
            int halfPos = (racer.getPosition().y + HorseRaceTrack.TRACK_LENGTH / 2) % HorseRaceTrack.TRACK_LENGTH;
            int xPos = (halfPos * (X_MAX - X_OFFSET)) / HorseRaceTrack.TRACK_LENGTH;
            model.getScreenHandler().register(symbol.getName(), new Point(X_OFFSET + xPos, Y_OFFSET + 2), symbol, 22);
        }
    }


    public static Point convertToScreen(int col, int row) {
        return new Point(X_OFFSET + col*4 + 2,
                Y_OFFSET + row * 4);
    }

    @Override
    protected String getUnderText(Model model) {
        String extra = "";
        if (!npcs.isEmpty()) {
            extra = ", Place: " + findPlace();
        }
        return "Speed: " + player.getCurrentSpeed() + ", Lap: " + player.getLap() + extra;
    }

    private int findPlace() {
        List<HorseRacer> racers = new ArrayList<>(npcs);
        racers.add(player);
        racers.sort((r1, r2) -> {
            int left = r1.getLap() * 10000 + r1.getPosition().y * 100 + r1.getYShift();
            int right = r2.getLap() * 10000 + r2.getPosition().y * 100 + r2.getYShift();
            return right - left;
        });
        return racers.indexOf(player) + 1;
    }

    @Override
    protected String getTitleText(Model model) {
        return "HORSE RACE";
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        if (!animationStarted) {
            return;
        }
        internalStep++;
        if (internalStep % ANIMATION_DELAY == 0) {
            player.updateYourself(allRacers);
            for (HorseRacer npc : npcs) {
                npc.updateYourself(allRacers);
            }
        }

        for (NPCHorseRacer npc : npcs) {
            npc.steer(allRacers);
        }
    }

    @Override
    public void synch() {   }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            player.possiblyMoveLeft(allRacers);
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.possiblyMoveRight(allRacers);
            return true;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            player.possiblyJump();
            return true;
        }
        return super.handleKeyEvent(keyEvent, model);
    }

    public void startRace() {
        animationStarted = true;
        player.setSpeed(1);
        for (HorseRacer npc : npcs){
            npc.setSpeed(1);
        }
    }

    public boolean raceIsOver() {
        return false;
    }

    public void addNPC(GameCharacter chara, Horse horse) {
        int[] positionsForNpcSize = new int[]{2, 4, 1, 5, 0, 6};
        NPCHorseRacer npc = new NPCHorseRacer(positionsForNpcSize[npcs.size()], chara, horse, horseRaceTrack);
        this.npcs.add(npc);
        allRacers.add(0, npc);
    }

    private static class BannerSprite extends Sprite {
        public BannerSprite() {
            super("racebanner", "riding.png", 7, 5, 16, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BROWN);
            setColor3(MyColors.PINK);
        }
    }
}
