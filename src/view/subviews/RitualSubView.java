package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.events.RitualEvent;
import util.Arithmetics;
import util.MyPair;
import view.MyColors;
import view.combat.CombatTheme;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.InputMismatchException;
import java.util.List;

import static view.subviews.CombatSubView.CURRENT_MARKER;
import static view.subviews.CombatSubView.INITIATIVE_MARKER;

public class RitualSubView extends SubView implements Animation {
    private static final int WINDOW_WIDTH = X_MAX - X_OFFSET;
    private static final int EXTRA_Y_OFFSET = 4;
    private static final int RADIUS = 10;
    private static final Point ORIGIN = new Point(X_OFFSET + WINDOW_WIDTH/2 - 2, Y_OFFSET + RADIUS + EXTRA_Y_OFFSET);

    private final CombatTheme theme;
    private final RitualEvent ritual;
    private int selected = 0;
    private ParticleSprite beamParticle;
    private ParticleSprite smallBeamParticle;
    private ParticleSprite[] angledBeams;

    private MyPair<GameCharacter, GameCharacter> temporaryBeam;
    private double tempBeamProgress = 0.0;
    private boolean ritualSuccess = false;
    private boolean cursorEnabled;

    public RitualSubView(CombatTheme theme, RitualEvent ritual, MyColors magicColor) {
        this.theme = theme;
        this.ritual = ritual;
        selected = 0;
        AnimationManager.register(this);

        MyColors color = convertColor(magicColor);
        beamParticle = new ParticleSprite(0x00, color);
        smallBeamParticle = new ParticleSprite(0x10, color);
        angledBeams = makeAngledBeams(color);
        cursorEnabled = true;
    }

    @Override
    protected void drawArea(Model model) {
        theme.drawBackground(model, X_OFFSET, Y_OFFSET);
        Sprite centerSprite = ritual.getCenterSprite();
        if (ritualSuccess) {
            centerSprite = ritual.getCenterSpriteSuccess();
        }
        if (centerSprite != null) {
            model.getScreenHandler().register(centerSprite.getName(), ORIGIN, centerSprite);
        }
        drawRitualists(model);
        drawBeams(model);
        drawBystanders(model);
        drawInitiativeOrder(model);
        if (cursorEnabled) {
            drawCursor(model);
        }

    }

    private void drawBeams(Model model) {
        for (MyPair<GameCharacter, GameCharacter> beam : ritual.getBeams()) {
            drawBeam(model, beam, beamParticle, false);
        }
        if (temporaryBeam != null) {
            drawBeam(model, temporaryBeam, smallBeamParticle, true);
        }
    }

    private void drawBeam(Model model, MyPair<GameCharacter, GameCharacter> beam, ParticleSprite sprite,
                          boolean useProgress) {
        Point a = getPositionFor(beam.first, ritual.getRitualists());
        a.x += 2;
        a.y += 2;
        Point b = getPositionFor(beam.second, ritual.getRitualists());
        b.x += 2;
        b.y += 2;

        double length = a.distance(b);
        double dx = (b.x - a.x) / length;
        double dy = (b.y - a.y) / length;
        for (double i = 0; i < length; ++i) {
            if (useProgress && i / length > tempBeamProgress) {
                break;
            }
            int xi = (int)Math.floor(a.x + i * dx);
            int yi = (int)Math.floor(a.y + i * dy);
            int xShift = (int)Math.floor(((a.x + i * dx) - xi) * 8.0);
            int yShift = (int)Math.floor(((a.y + i * dy) - yi) * 8.0);
            if (!ritualSuccess && !useProgress) {
                sprite = getSpriteForAngle(Math.atan(-dy/dx));
            }
            model.getScreenHandler().register(sprite.getName(), new Point(xi, yi), sprite,
                    2, xShift - 4, yShift);
        }
    }

    private ParticleSprite getSpriteForAngle(double angle) {
        double v = Math.PI / 2 - angle;
        int row = (int)Math.floor((v / Math.PI) * (angledBeams.length + 1));
        if (row == angledBeams.length + 1) {
            row = 0;
        }
        return angledBeams[row];
    }

    private void drawRitualists(Model model) {
        for (GameCharacter gc : ritual.getRitualists()) {
            Point p = getPositionFor(gc, ritual.getRitualists());
            AvatarSprite avatar = gc.getAvatarSprite();
            avatar.synch();
            model.getScreenHandler().register(avatar.getName(), p, avatar);
            if (model.getParty().getPartyMembers().contains(gc)) {
                Sprite initiativeSymbol = CombatSubView.getInitiativeSymbol(gc, model);
                model.getScreenHandler().register(initiativeSymbol.getName(),
                        new Point(p.x+3, p.y+3), initiativeSymbol);
            }
            if (gc == ritual.getCurrentTurnTaker()) {
                model.getScreenHandler().register(CombatSubView.CURRENT_MARKER.getName(),
                        p, CURRENT_MARKER);
            }
        }
    }

    private void drawBystanders(Model model) {
        for (int i = 0; i < ritual.getBench().size(); ++i) {
            Point p = new Point(X_OFFSET + i*4, Y_MAX - 6 - (i/8) * 4);
            AvatarSprite avatar = ritual.getBench().get(i).getAvatarSprite();
            avatar.synch();
            model.getScreenHandler().register(avatar.getName(), p, avatar);
        }
    }

    private void drawCursor(Model model) {
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        Point p = getPositionFor(getSelected(), ritual.getRitualists());
        p.y -= 4;
        model.getScreenHandler().register("combatcursor", p, cursor, 2);
    }

    private void drawInitiativeOrder(Model model) {
        int col = 0;
        int xPosStart = X_OFFSET + (X_MAX - X_OFFSET) / 2 - ritual.getRitualists().size();
        for (Combatant combatant : ritual.getRitualistsInTurnOrder()) {
            if (isCurrentTurn(combatant)) {
                model.getScreenHandler().put(xPosStart + col - 1, Y_MAX - 1 ,
                        INITIATIVE_MARKER);
            }
            model.getScreenHandler().put(xPosStart + col, Y_MAX - 1 ,
                    CombatSubView.getInitiativeSymbol(combatant, model));
            col += 2;
        }
    }

    private boolean isCurrentTurn(Combatant combatant) {
        return combatant == ritual.getCurrentTurnTaker();
    }

    private Point getPositionFor(GameCharacter gc, List<GameCharacter> list) {
        int index = list.indexOf(gc);
        int starPoints = list.size();

        double angle = (Math.PI * 2.0 / starPoints) * index + Math.PI / 2.0;
        double x = Math.round(ORIGIN.x + RADIUS * Math.cos(angle));
        double y = Math.round(ORIGIN.y - RADIUS * Math.sin(angle));
        return new Point((int)x, (int)y);
    }

    @Override
    protected String getUnderText(Model model) {
        GameCharacter selectedChar = ritual.getRitualists().get(selected);
        return selectedChar.getName() + ", " + selectedChar.getCharClass().getShortName() +
                " Lvl " + selectedChar.getLevel() + ", " +
                String.format("%d/%d HP", selectedChar.getHP(), selectedChar.getMaxHP());
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENT - RITUAL";
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT && cursorEnabled) {
            selected = Arithmetics.decrementWithWrap(selected, ritual.getRitualists().size());
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT && cursorEnabled) {
            selected = Arithmetics.incrementWithWrap(selected, ritual.getRitualists().size());
            return true;
        }
        return false;
    }

    public Point getPositionForSelected() {
        return getPositionFor(getSelected(), ritual.getRitualists());
    }

    public void setCursor(GameCharacter turnTaker) {
        selected = ritual.getRitualists().indexOf(turnTaker);
    }

    public GameCharacter getSelected() {
        return ritual.getRitualists().get(selected);
    }

    public void addTemporaryBeam(GameCharacter turnTaker, GameCharacter receiver) {
        tempBeamProgress = 0.0;
        temporaryBeam = new MyPair<>(turnTaker, receiver);
    }

    public void removeTemporaryBeam() {
        temporaryBeam = null;
    }

    public void addSpecialEffect(GameCharacter target, RunOnceAnimationSprite effect) {
        addOngoingEffect(new MyPair<>(getPositionFor(target, ritual.getRitualists()), effect));
    }

    @Override
    public void stepAnimation(long elapsedTimeMs, Model model) {
        tempBeamProgress += 0.05;
    }

    @Override
    public void synch() {

    }

    public void addFloatyDamage(GameCharacter turnTaker, int damage) {
        super.addFloatyDamage(getPositionFor(turnTaker, ritual.getRitualists()),
                damage, DamageValueEffect.MAGICAL_DAMAGE);
    }

    public void setRitualSuccess(boolean b) {
        ritualSuccess = b;
    }

    private static ParticleSprite[] makeAngledBeams(MyColors color) {
        ParticleSprite[] sprites = new ParticleSprite[14];
        for (int i = 0; i < sprites.length; ++i) {
            sprites[i] = new ParticleSprite(0x20 + i*0x10, color);
        }
        return sprites;
    }

    private static MyColors convertColor(MyColors magicColor) {
        switch (magicColor) {
            case GREEN:
                return MyColors.LIGHT_GREEN;
            case BLUE:
                return MyColors.CYAN;
            case BLACK:
                return MyColors.GRAY;
            case RED:
                return MyColors.LIGHT_RED;
            case WHITE:
                return MyColors.LIGHT_YELLOW;
            case PURPLE:
                return MyColors.PINK;
            default:
                throw new InputMismatchException("Bad input for color conversion: " + magicColor.name());
        }
    }

    public void resetSelected() {
        selected = 0;
    }

    public void setCursorEnabled(boolean b) {
        this.cursorEnabled = b;
    }
}
