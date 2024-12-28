package model.states.duel;

import model.Model;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.items.spells.Spell;
import model.states.CombatEvent;
import model.states.DailyEventState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyLists;
import util.MyRandom;
import view.MyColors;
import view.subviews.MagicDuelSubView;
import view.subviews.StripedTransition;

import java.util.*;

public class MagicDuelEvent extends DailyEventState {

    private enum OutputType{ normal, console, none;}

    public static final int MAX_HITS = 5;

    private static final int MAX_SHIFT = 3;
    private static final List<CharacterClass> RITUALIST_CLASSES = List.of(Classes.SOR, Classes.MAG, Classes.WIZ,
                                                                          Classes.WIT, Classes.PRI, Classes.DRU);
    protected List<MagicDuelist> duelists;

    protected DuelistController controller1;
    protected DuelistController controller2;
    private MagicDuelSubView subView;

    private boolean lockedBeamOn = false;
    private int lockedBeamPower = 0;
    private int lockedBeamShift = 0;

    private final boolean aisOnly;
    private final OutputType outputType;

    public MagicDuelEvent(Model model, boolean aisOnly) {
        super(model);
        if (aisOnly) {
            outputType = OutputType.none;
        } else {
            outputType = OutputType.normal;
        }
        this.aisOnly = aisOnly;
    }

    public MagicDuelEvent(Model model) {
        this(model, false);
    }

    public GameCharacter makeNPCMageOfClassAndLevel(CharacterClass cls, int level) {
        GameCharacter gc = makeRandomCharacter(level);
        gc.setClass(cls);
        gc.addToHP(999);
        return gc;
    }

    public GameCharacter makeRandomMageNPC() {
        return makeNPCMageOfClassAndLevel(MyRandom.sample(RITUALIST_CLASSES), 6);
    }

    @Override
    protected void doEvent(Model model) {
        BackgroundMusic previousMusic = ClientSoundManager.getCurrentBackgroundMusic();
        CombatEvent.startMusic();

        this.duelists = new ArrayList<>();

        MyColors playerMagicColor = MyColors.BLUE;
        duelists.add(new MagicDuelist(model.getParty().getLeader(), playerMagicColor, new ATypePowerGauge(true)));
        this.controller1 = new PlayerDuelistController(duelists.get(0));

        GameCharacter npcMage = makeRandomMageNPC();
        MyColors opposColor = findBestMagicColor(npcMage);
        duelists.add(new MagicDuelist(npcMage, opposColor, new BTypePowerGauge()));
        this.controller2 = new MatrixDuelistController(duelists.get(1), AIMatrices.level6WizardWithBGauge());
        this.subView = new MagicDuelSubView(model.getCurrentHex().getCombatTheme(),
                duelists.get(0), duelists.get(1));
        StripedTransition.transition(model, subView);

        println("Press enter to start duel");
        waitForReturn();

        runDuel(model);

        MagicDuelist winner = MyLists.find(duelists, d -> !d.isKnockedOut());
        println("The duel is over, the winner is " + winner.getName() + "!");
        print("Press enter to continue.");
        waitForReturn();
        ClientSoundManager.playBackgroundMusic(previousMusic);
    }

    protected int runDuel(Model model) {
        int roundCounter = 1;
        do {
            if (beamsAreLocked()) {
                beamUpkeep(model);
            }

            if (beamsAreLocked()) {
                beamTurn(model);
            } else {
                generatePower(duelists);
                normalTurn(model);
            }
        } while (!duelIsOver());
        return roundCounter;
    }

    private boolean duelIsOver() {
        return MyLists.any(duelists, MagicDuelist::isKnockedOut);
    }

    protected static MyColors findBestMagicColor(GameCharacter npcMage) {
        int maxRank = -1;
        Skill bestSkill = null;
        for (Skill s : Skill.values()) {
            if (s.isMagic()) {
                int rankForSkill = npcMage.getRankForSkill(s);
                if (rankForSkill > maxRank) {
                    bestSkill = s;
                    maxRank = rankForSkill;
                }
            }
        }
        return Spell.getColorForSkill(bestSkill);
    }

    private void normalTurn(Model model) {
        MagicDuelAction selectedAction = controller1.selectNormalTurnAction(model, this);
        MagicDuelAction otherAction = controller2.selectNormalTurnAction(model, this);
        resolveActions(model, selectedAction, otherAction, subView);
    }

    private void resolveActions(Model model, MagicDuelAction selectedAction,
                                MagicDuelAction opponentAction,
                                MagicDuelSubView subView) {
        clearTemporaryBeams();
        selectedAction.prepare(model, this, duelists.get(0));
        opponentAction.prepare(model, this, duelists.get(1));
        selectedAction.resolve(model, this, opponentAction);
        if (!aisOnly) {
            waitUntil(subView, MagicDuelSubView::temporaryBeamsDone);
        }
        selectedAction.wrapUp(model, this, duelists.get(1));
        opponentAction.wrapUp(model, this, duelists.get(0));
        if (!aisOnly) {
            if (!beamsAreLocked()) {
                clearTemporaryBeams();
            } else {
                subView.setTwoBeamsAtMidPoint(lockedBeamShift);
                subView.setFlash();
            }
        }
    }

    private void beamTurn(Model model) {
        BeamOptions selectedAction = controller1.selectBeamTurnAction(model, this);
        BeamOptions otherAction = controller2.selectBeamTurnAction(model, this);
        resolveBeamOptions(model, selectedAction, otherAction);
    }

    private void resolveBeamOptions(Model model, BeamOptions selectedAction, BeamOptions otherAction) {
        if (selectedAction == BeamOptions.HoldOn && otherAction == BeamOptions.HoldOn) {
            textOutput("Both duelist are holding the locked beams.");
            return;
        }

        if (selectedAction == BeamOptions.Release && otherAction == BeamOptions.Release) {
            textOutput("Both duelists simultaneously released the locked beams.");
            lockedBeamOn = false;
            clearTemporaryBeams();
            return;
        }

        if (selectedAction == BeamOptions.IncreasePower) {
            textOutput(duelists.get(0).getName() + " increases the power of the beam!");
            lockedBeamPower++;
            lockedBeamShift--;
        }
        if (otherAction == BeamOptions.IncreasePower) {
            textOutput(duelists.get(1).getName() + " increases the power of the beam!");
            lockedBeamPower++;
            lockedBeamShift++;
        }
        if (!aisOnly) {
            subView.setTwoBeamsAtMidPoint(lockedBeamShift);
        }

        if (lockedBeamShift == MAX_SHIFT) {
            lockedBeamHits(model, duelists.get(0));
            return;
        }
        if (lockedBeamShift == -MAX_SHIFT) {
            lockedBeamHits(model, duelists.get(1));
            return;
        }

        if (selectedAction == BeamOptions.Release) {
            attemptReleaseBeam(model, duelists.get(0));
            return;
        }

        if (otherAction == BeamOptions.Release) {
            attemptReleaseBeam(model, duelists.get(1));
        }
    }

    private void attemptReleaseBeam(Model model, MagicDuelist magicDuelist) {
        textOutput(magicDuelist.getName() + " is trying to release the locked beam...", false);
        waitForLog(model);
        if (magicDuelist.testMagicSkill(model, this, MagicDuelAction.BASE_DIFFICULTY + lockedBeamPower)) {
            textOutput("and successfully disentangles the beams!");
            waitForLog(model);
            clearTemporaryBeams();
            lockedBeamOn = false;
        } else {
            if (MyRandom.rollD6() < 4) {
                lockedBeamHits(model, magicDuelist);
            } else {
                textOutput("but the beams are still locked.");
            }
        }
    }

    private void waitForLog(Model model) {
        if (!aisOnly) {
            model.getLog().waitForAnimationToFinish();
        }
    }

    private void lockedBeamHits(Model model, MagicDuelist magicDuelist) {
        textOutput(magicDuelist.getName() + " got hit by the locked beams!");
        magicDuelist.takeDamage(1);
        clearTemporaryBeams();
        lockedBeamOn = false;
    }

    private void beamUpkeep(Model model) {
        for (MagicDuelist d : duelists) {
            d.addToPower(lockedBeamPower * -3);
        }

        if (duelists.get(0).getPower() == 0 && duelists.get(1).getPower() == 0) {
            textOutput("Both duelists are out of power! The beam dissipates.");
            waitForLog(model);
            lockedBeamOn = false;
            clearTemporaryBeams();
            return;
        }

        if (duelists.get(0).getPower() == 0) {
            textOutput(duelists.get(0).getName() + " is out of power!");
            waitForLog(model);
            lockedBeamHits(model, duelists.get(0));
            return;
        }

        if (duelists.get(1).getPower() == 0) {
            textOutput(duelists.get(1).getName() + " is out of power!");
            waitForLog(model);
            lockedBeamHits(model, duelists.get(1));
        }
    }

    private void clearTemporaryBeams() {
        if (!aisOnly) {
            subView.clearTemporaryBeams();
        }
    }

    private void generatePower(List<MagicDuelist> duelists) {
        for (MagicDuelist d : duelists) {
            d.generatePower();
        }
    }

    public void lockBeams(int playerStrength, int opposStrength) {
        int shift = opposStrength - playerStrength;
        if (shift >= MAX_SHIFT) {
            textOutput(duelists.get(0).getName() + "'s attack is blown away by the power of " +
                    duelists.get(1).getName() + "'s attack!");
            duelists.get(0).takeDamage(MAX_SHIFT - shift + 1);
        } else if (-shift >= MAX_SHIFT) {
            textOutput(duelists.get(1).getName() + "'s attack is blown away by the power of " +
                    duelists.get(0).getName() + "'s attack!");
            duelists.get(1).takeDamage(MAX_SHIFT + shift + 1);
        } else {
            lockedBeamOn = true;
            lockedBeamPower = playerStrength + opposStrength;
            lockedBeamShift = shift;
            textOutput(duelists.get(0).getName() + "'s and " + duelists.get(1).getName() + "'s beams are locked together!");
            if (!aisOnly) {
                subView.animatedTwoBeamsAtMidPoint(playerStrength, opposStrength);
            }
        }
    }

    protected void textOutput(String s) {
        textOutput(s,true);
    }

    protected void textOutput(String s, boolean newLine) {
        switch (outputType) {
            case normal:
                if (newLine) {
                    println(s);
                } else {
                    print(s);
                }
                break;

            case console:
                if (newLine) {
                    System.out.println(s);
                } else {
                    System.out.print(s);
                }
        }
    }

    public boolean beamsAreLocked() {
        return lockedBeamOn;
    }

    public void fireBeamAtOpponent(MagicDuelist performer, int beamStrength) {
        if (!aisOnly) {
            if (performer == duelists.get(0)) {
                subView.animatedBeamAtOpponent(beamStrength);
            } else {
                subView.animatedBeamAtPlayer(beamStrength);
            }
        }
    }

    public int getOpponentPowerStrength(MagicDuelist whosAsking) {
        if (whosAsking == duelists.get(0)) {
            return duelists.get(1).getGauge().getCurrentStrength();
        }
        return duelists.get(0).getGauge().getCurrentStrength();
    }

    public int getLockedBeamShift() {
        return lockedBeamShift;
    }

    private static class LockedBeam {


    }
}
