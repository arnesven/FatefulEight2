package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.states.events.RitualEvent;
import util.Arithmetics;
import util.MyPair;
import view.MyColors;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static view.subviews.CombatSubView.CURRENT_MARKER;
import static view.subviews.CombatSubView.INITIATIVE_MARKER;

public class RitualSubView extends SubView implements Animation {
    private final CombatTheme theme;
    private final RitualEvent ritual;
    private int selected = 0;
    private static final ParticleSprite BEAM_PARTICLE = new ParticleSprite(0x00, MyColors.LIGHT_GREEN);
    private static final ParticleSprite SMALL_BEAM_PARTICLE = new ParticleSprite(0x10, MyColors.LIGHT_GREEN);
    private static final ParticleSprite[] ANGLED_BEAMS = makeAngledBeams();

    private MyPair<GameCharacter, GameCharacter> temporaryBeam;
    private double tempBeamProgress = 0.0;
    private boolean ritualSuccess = false;

    public RitualSubView(CombatTheme theme, RitualEvent ritual) {
        this.theme = theme;
        this.ritual = ritual;
        selected = 0;
        AnimationManager.register(this); // TODO: Dont forget to unregister this
    }

    @Override
    protected void drawArea(Model model) {
        theme.drawBackground(model, X_OFFSET, Y_OFFSET);

        drawRitualists(model);
        drawBeams(model);
        drawBystanders(model);
        drawInitiativeOrder(model);
        drawCursor(model);

    }

    private void drawBeams(Model model) {
        for (MyPair<GameCharacter, GameCharacter> beam : ritual.getBeams()) {
            drawBeam(model, beam, BEAM_PARTICLE, false);
        }
        if (temporaryBeam != null) {
            drawBeam(model, temporaryBeam, SMALL_BEAM_PARTICLE, true);
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
        int row = (int)Math.floor((v / Math.PI) * (ANGLED_BEAMS.length + 1));
        if (row == ANGLED_BEAMS.length + 1) {
            row = 0;
        }
        return ANGLED_BEAMS[row];
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
        int width = X_MAX - X_OFFSET;
        int yOffset = 4;
        int radius = 10;
        Point origin = new Point(X_OFFSET + width/2 - 2, Y_OFFSET + radius + yOffset);
        int index = list.indexOf(gc);
        int starPoints = list.size();

        double angle = (Math.PI * 2.0 / starPoints) * index + Math.PI / 2.0;
        double x = Math.round(origin.x + radius * Math.cos(angle));
        double y = Math.round(origin.y - radius * Math.sin(angle));
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
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            selected = Arithmetics.decrementWithWrap(selected, ritual.getRitualists().size());
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            selected = Arithmetics.incrementWithWrap(selected, ritual.getRitualists().size());
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            System.out.println("up");
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            System.out.println("down");
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

    private static ParticleSprite[] makeAngledBeams() {
        ParticleSprite[] sprites = new ParticleSprite[14];
        for (int i = 0; i < sprites.length; ++i) {
            sprites[i] = new ParticleSprite(0x20 + i*0x10, MyColors.LIGHT_GREEN);
        }
        return sprites;
    }
}
