package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.horses.Horse;
import model.states.horserace.HorseRaceTrack;
import model.states.horserace.HorseRacer;
import model.states.horserace.NPCHorseRacer;
import view.MyColors;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class HorseRacingSubView extends SubView implements Animation {

    private static final Sprite BANNER_ICON = new BannerSprite();
    private static final Sprite PATH_ICON = new Sprite32x32("path", "riding.png",
            0x61, MyColors.BROWN, MyColors.DARK_GREEN, MyColors.BROWN);
    private static final long ANIMATION_DELAY = 3;
    public static final int HORSE_VERTICAL_POSITION = 2;
    private HorseRaceTrack horseRaceTrack = new HorseRaceTrack(HorseRaceTrack.RANDOM_TRACK);
    private long internalStep = 0;

    private HorseRacer player;
    private List<NPCHorseRacer> npcs = new ArrayList<>();
    private List<HorseRacer> allRacers = new ArrayList<>();
    private boolean animationStarted = false;
    private boolean timeMode;
    private long racingTime = 0;

    public HorseRacingSubView(GameCharacter rider, Horse horse) {
        this.player = new HorseRacer(3, rider, horse, this);
        allRacers.add(player);
    }

    @Override
    protected void drawArea(Model model) {
        horseRaceTrack.drawYourself(model, this, player);
        player.drawHorse(model, HORSE_VERTICAL_POSITION, 0);
        player.updateStrafeAnimation();
        for (HorseRacer npc : npcs) {
            int posDiff = calcPosDiff(npc);
            int yPos = HORSE_VERTICAL_POSITION + posDiff;
            int shiftDiff = npc.getYShift() - player.getYShift();
            if (0 < yPos && yPos < 8) {
                npc.drawHorse(model, yPos, shiftDiff);
            } else if (yPos == 0 && shiftDiff > 0) {
                npc.drawHorse(model, yPos, shiftDiff);
            } else if (yPos == 8 && shiftDiff < 0) {
                npc.drawHorse(model, yPos, shiftDiff);
            }
            npc.updateStrafeAnimation();
        }
        model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_OFFSET, Y_OFFSET+4, blackBlock, 20);
        model.getScreenHandler().fillForeground(X_OFFSET, X_MAX, Y_MAX-4, Y_MAX, blackBlock, 20);
        drawBanner(model);
    }

    private int calcPosDiff(HorseRacer npc) {
        int totalDiff = (npc.getPosition().y + npc.getLap() * HorseRaceTrack.TRACK_LENGTH) -
                (player.getPosition().y + player.getLap() * HorseRaceTrack.TRACK_LENGTH);
        while (totalDiff > HorseRaceTrack.TRACK_LENGTH / 2) {
            totalDiff -= HorseRaceTrack.TRACK_LENGTH;
        }
        while (totalDiff < -HorseRaceTrack.TRACK_LENGTH / 2) {
            totalDiff += HorseRaceTrack.TRACK_LENGTH;
        }
        return totalDiff;
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
            extra = ", Place: " + getPlayerPlacement();
        }
        if (timeMode) {
            extra += ", " + getTimeString();
        }
        return "Speed: " + player.getCurrentSpeed() + ", Lap: " + player.getLap() + extra;
    }

    public String getTimeString() {
        int time = (int)(racingTime);
        int min = time / (60 * 1000);
        int sec = (time - min*60*1000) / 1000;
        int hund = (time - min*60*1000 - sec*1000) / 10;
        return String.format("Time: %02d:%02d:%02d", min, sec, hund);
    }

    private int findPlace(HorseRacer forWhom) {
        List<HorseRacer> racers = getPlacements();
        return racers.indexOf(forWhom) + 1;
    }

    public List<HorseRacer> getPlacements() {
        List<HorseRacer> racers = new ArrayList<>(npcs);
        racers.add(player);
        racers.sort((r1, r2) -> {
            int left = r1.getLap() * 10000 + r1.getPosition().y * 100 + r1.getYShift();
            int right = r2.getLap() * 10000 + r2.getPosition().y * 100 + r2.getYShift();
            return right - left;
        });
        return racers;
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
        racingTime += elapsedTimeMs;
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
        AnimationManager.registerPausable(this);
        animationStarted = true;
        for (HorseRacer racer : allRacers) {
            SkillCheckResult result = racer.getCharacter().testSkillHidden(Skill.Survival, SkillCheckResult.NO_DIFFICULTY, 0);
            racer.setSpeed(getStartSpeedFromSkillResult(result.getModifiedRoll()));
        }
    }

    private int getStartSpeedFromSkillResult(int modifiedRoll) {
        if (modifiedRoll < 6) {
            return 0;
        }
        if (modifiedRoll < 10) {
            return 1;
        }
        return 2;
    }

    public void stopRace() {
        AnimationManager.unregister(this);
        animationStarted = false;
        player.setSpeed(0);
        for (HorseRacer npc : npcs){
            npc.setSpeed(0);
        }
    }

    public boolean raceIsOver(int targetLaps) {
        return player.getLap() > targetLaps;
    }

    public void addNPC(GameCharacter chara, Horse horse) {
        int[] positionsForNpcSize = new int[]{2, 4, 1, 5, 0, 6};
        NPCHorseRacer npc = new NPCHorseRacer(positionsForNpcSize[npcs.size()], chara, horse, this);
        this.npcs.add(npc);
        allRacers.add(0, npc);
    }

    public int getPlayerPlacement() {
        return findPlace(player);
    }

    public void setTimeModeEnabled(boolean b) {
        timeMode = b;
    }

    public int getTimeResult() {
        return (int)(Math.ceil(racingTime / 1000.0));
    }

    public void setTrack(int track) {
        horseRaceTrack = new HorseRaceTrack(track);
    }

    public HorseRaceTrack getTrack() {
        return horseRaceTrack;
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
