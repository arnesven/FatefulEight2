package view.subviews;

import model.Model;
import model.states.duel.MagicDuelist;
import model.states.duel.PowerGauge;
import sprites.SparklingLockedBeamsSprite;
import util.MyLists;
import util.MyPair;
import view.MyColors;
import view.combat.CombatTheme;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class MagicDuelSubView extends SubView implements Animation {

    private static final double BEAM_SPEED = 7.0;
    private static final Point MID_POINT = new Point(X_OFFSET + 14, Y_OFFSET + 20);
    private static final Point UPPER_DUELIST = new Point(MID_POINT.x, MID_POINT.y - 12);
    private static final Point LOWER_DUELIST = new Point(MID_POINT.x, MID_POINT.y + 12);
    private static final Sprite BLUE_BALL = new DuelistHitBallSprite(MyColors.LIGHT_BLUE, MyColors.WHITE, MyColors.LIGHT_BLUE);
    private static final Sprite RED_BALL = new DuelistHitBallSprite(MyColors.RED, MyColors.RED, MyColors.RED);
    private static final Sprite WHITE_BLOCK = new FilledBlockSprite(MyColors.WHITE);
    private final CombatTheme theme;
    private final Sprite upperAvatar;
    private final Sprite lowerAvatar;
    private final ParticleSprite[] playerBeams;
    private final ParticleSprite[] opposBeams;
    private final List<Beam> temporaryBeams = new ArrayList<>();
    private final LoopingSprite[] gaugeEffects;
    private MyPair<LoopingSprite, Integer> midPointSprite = null;
    private final MyColors playerColor;
    private final MyColors opposColor;
    private final WandSprite wandSprite;
    private final MagicDuelist player;
    private final MagicDuelist opponent;
    private boolean flash = false;

    public MagicDuelSubView(CombatTheme combatTheme, MagicDuelist player,  MagicDuelist opponent) {
        this.theme = combatTheme;
        this.upperAvatar = opponent.getCharacter().getAvatarSprite();
        this.lowerAvatar = player.getCharacter().getAvatarSprite().getAvatarBack();
        ((Animation)upperAvatar).synch();
        ((Animation)lowerAvatar).synch();
        this.player = player;
        this.opponent = opponent;
        this.playerColor = RitualSubView.convertColor(player.getMagicColor());
        this.opposColor = RitualSubView.convertColor(opponent.getMagicColor());
        playerBeams = RitualSubView.makeAngledBeams(this.playerColor);
        opposBeams = RitualSubView.makeAngledBeams(this.opposColor);
        AnimationManager.registerPausable(this);
        this.wandSprite = new WandSprite(player.getCharacter().getRace().getColor(), playerColor);
        this.gaugeEffects = makeGaugeEffects();

    }

    private GaugeEffectStrengthSprite[] makeGaugeEffects() {
        GaugeEffectStrengthSprite[] sprites = new GaugeEffectStrengthSprite[5];
        for (int i = 0; i < sprites.length; ++i) {
            sprites[i] = new GaugeEffectStrengthSprite(i + 1);
        }
        return sprites;
    }

    @Override
    protected void drawArea(Model model) {
        if (flash) {
            model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX, WHITE_BLOCK);
            flash = false;
        } else {
            drawBackground(model);
            drawDuelists(model);
            drawPowerGauge(model);
            drawWandSprite(model);
            drawBeams(model);
            drawMidPointSprite(model);
        }
    }

    private void drawBeams(Model model) {
        for (Beam beam : new ArrayList<>(temporaryBeams)) {
            drawBeam(model, beam, beam.useProgress);
        }
    }

    private void drawBeam(Model model, Beam temporaryBeam, boolean useProgress) {
        Point a = temporaryBeam.from;
        Point b = temporaryBeam.to;
        double length = a.distance(b);
        double dx = (b.x - a.x) / length;
        double dy = (b.y - a.y) / length;
        for (double i = 0; i < length; ++i) {

            int xi = (int)Math.floor(a.x + i * dx);
            int yi = (int)Math.floor(a.y + i * dy);
            int xShift = (int)Math.floor(((a.x + i * dx) - xi) * 8.0);
            int yShift = (int)Math.floor(((a.y + i * dy) - yi) * 8.0);
            Point finalPos = new Point(xi, yi);

            if (useProgress && i / length > temporaryBeam.progress) {
                if (temporaryBeam.headSprite != null) {
                    int xx = temporaryBeam.headSprite.getWidth() / 2;
                    model.getScreenHandler().register(temporaryBeam.headSprite.getName(), finalPos, temporaryBeam.headSprite,
                            2, xShift - xx, yShift);
                }
                return;
            }
            Sprite sprite = getSpriteForAngle(Math.atan(-dy/dx), temporaryBeam.beamSprites);
            model.getScreenHandler().register(sprite.getName(), finalPos , sprite,
                    1, xShift - 4, yShift);
        }
        temporaryBeam.finished = true;
    }

    private ParticleSprite getSpriteForAngle(double angle, ParticleSprite[] spriteMap) {
        double v = Math.PI / 2 - angle;
        int row = (int)Math.floor((v / Math.PI) * (spriteMap.length + 1));
        if (row == spriteMap.length + 1) {
            row = 0;
        }
        return spriteMap[row];
    }

    private void drawPowerGauge(Model model) {
        PowerGauge gauge = player.getGauge();
        gauge.drawYourself(model.getScreenHandler(), X_MAX - 8, Y_OFFSET);

    }

    private void drawWandSprite(Model model) {
        model.getScreenHandler().put(X_MAX - 8, Y_MAX - 8, wandSprite);
    }

    private void drawDuelists(Model model) {
        drawDuelist(model, upperAvatar, UPPER_DUELIST, opponent, -2);
        drawDuelist(model, lowerAvatar, LOWER_DUELIST, player, +4);
    }

    private void drawDuelist(Model model, Sprite avatar, Point point, MagicDuelist duelist, int ballsOffset) {
        Sprite toUse = avatar;
        if (duelist.isKnockedOut()) {
            toUse = duelist.getCharacter().getAvatarSprite().getDead();
        }
        model.getScreenHandler().register(toUse.getName(), point, toUse);
        if (!duelist.isKnockedOut()) {
            drawGaugeEffect(model, point, duelist.getGauge().getCurrentStrength());
        }
        drawHitBalls(model, duelist, point.y + ballsOffset);
        if (duelist.hasAnimation()) {
            Sprite spr = duelist.getAnimation();
            model.getScreenHandler().register(spr.getName(), point, spr, 2);
        }
    }

    private void drawGaugeEffect(Model model, Point point, int currentStrength) {
        if (currentStrength > 0) {
            Point toDraw = new Point(point.x + 1, point.y - 1);
            model.getScreenHandler().register(gaugeEffects[currentStrength-1].getName(), toDraw,
                    gaugeEffects[currentStrength-1], 1);
        }
    }

    private void drawHitBalls(Model model, MagicDuelist duelist, int yStart) {
        for (int i = 0; i < duelist.getMaxHits(); ++i) {
            Sprite  toUse = RED_BALL;
            if (i < duelist.getMaxHits() - duelist.getHitsTaken()) {
                toUse = BLUE_BALL;
            }
            Point p = new Point(MID_POINT.x + i * 2, yStart);
            model.getScreenHandler().register(toUse.getName(), p, toUse);
        }
    }

    private void drawBackground(Model model) {
        theme.drawBackground(model, X_OFFSET, Y_OFFSET);
        model.getScreenHandler().clearSpace(X_MAX - 8, X_MAX, Y_OFFSET, Y_MAX);
        model.getScreenHandler().clearForeground(X_MAX - 8, X_MAX, Y_OFFSET, Y_MAX);
    }

    private void drawMidPointSprite(Model model) {
        if (midPointSprite != null) {
            Point mid = new Point(MID_POINT);
            mid.y += midPointSprite.second * 4;
            model.getScreenHandler().register(midPointSprite.first.getName(), mid, midPointSprite.first, 2);
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return "Magic Duel...";
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENT - MAGIC DUEL";
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return super.handleKeyEvent(keyEvent, model);
    }

    public void animatedBeamAtOpponent(int strength) {
        temporaryBeams.add(
                new Beam(new Point(LOWER_DUELIST.x + 2, LOWER_DUELIST.y - 1),
                new Point(UPPER_DUELIST.x + 2, UPPER_DUELIST.y + 2),
                playerBeams, true, makeBeamHeadSprite(strength, playerColor, 0)));
    }


    public void animatedBeamAtPlayer(int strength) {
        temporaryBeams.add(new Beam(
                new Point(UPPER_DUELIST.x + 2, UPPER_DUELIST.y + 3),
                new Point(LOWER_DUELIST.x + 2, LOWER_DUELIST.y),
                opposBeams, true, makeBeamHeadSprite(strength, opposColor, 180)));
    }


    private void twoBeamsAtMid(int shift, boolean progress, int playerStrength, int opposStrength) {
        Point mid = new Point(MID_POINT);
        mid.y += shift * 4;
        temporaryBeams.add(
                new Beam(new Point(LOWER_DUELIST.x + 2, LOWER_DUELIST.y - 1),
                        new Point(mid.x + 2, mid.y),
                        playerBeams, progress, makeBeamHeadSprite(playerStrength, playerColor, 0)));
        temporaryBeams.add(new Beam(
                new Point(UPPER_DUELIST.x + 2, UPPER_DUELIST.y + 3),
                new Point(mid.x + 2, mid.y + 1),
                opposBeams, progress, makeBeamHeadSprite(opposStrength, opposColor, 180)));

    }

    private Sprite makeBeamHeadSprite(int strength, MyColors color, int rotation) {
        if (strength >= 1) {
            return new BeamHeadSprite(color, rotation, strength);
        }
        return new ParticleSprite(0x00, color);
    }

    public void animatedTwoBeamsAtMidPoint(int playerStrength, int opposStrength) {
        twoBeamsAtMid(0, true, playerStrength, opposStrength);
    }

    public void setTwoBeamsAtMidPoint(int shift) {
        clearTemporaryBeams();
        twoBeamsAtMid(shift, false, 0, 0);
        addMidPointSparkle(shift);
    }

    public void addMidPointSparkle(int shift) {
        this.midPointSprite = new MyPair<>(new SparklingLockedBeamsSprite(playerColor, opposColor), shift);
    }

    public void clearTemporaryBeams() {
        temporaryBeams.clear();
        this.midPointSprite = null;
    }

    public boolean temporaryBeamsDone() {
        return MyLists.all(temporaryBeams, beam -> beam.finished);
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        for (Beam b : temporaryBeams) {
            b.progress += BEAM_SPEED / 100.0;
        }
    }

    @Override
    public void synch() { }

    public void setFlash() {
        this.flash = true;
    }


    private static class Beam {
        Point from;
        Point to;
        double progress;
        ParticleSprite[] beamSprites;
        boolean finished;
        boolean useProgress;
        Sprite headSprite;

        public Beam(Point from, Point to, ParticleSprite[] sprites, boolean useProgress, Sprite headSprite) {
            this.from = from;
            this.to = to;
            this.beamSprites = sprites;
            this.useProgress = useProgress;
            progress = 0.0;
            finished = false;
            this.headSprite = headSprite;
        }
    }
}
