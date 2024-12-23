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

import java.util.ArrayList;
import java.util.List;

public class MagicDuelEvent extends DailyEventState {

    public static final int MAX_HITS = 5;
    private static final int MAX_SHIFT = 3;
    private static final List<CharacterClass> RITUALIST_CLASSES = List.of(Classes.SOR, Classes.MAG, Classes.WIZ,
                                                                          Classes.WIT, Classes.PRI, Classes.DRU);

    private List<MagicDuelist> duelists;
    private DuelistController controller1;
    private DuelistController controller2;
    private MagicDuelSubView subView;
    private LockedBeam lockedBeam = null;

    public MagicDuelEvent(Model model) {
        super(model);
    }

    public GameCharacter makeRandomMageNPC() {
        GameCharacter gc = makeRandomCharacter();
        gc.setClass(MyRandom.sample(RITUALIST_CLASSES));
        gc.addToHP(999);
        return gc;
    }

    @Override
    protected void doEvent(Model model) {
        BackgroundMusic previousMusic = ClientSoundManager.getCurrentBackgroundMusic();
        CombatEvent.startMusic();

        this.duelists = new ArrayList<>();

        MyColors playerMagicColor = MyColors.BLUE;
        duelists.add(new MagicDuelist(model.getParty().getLeader(), playerMagicColor, new PowerGauge()));
        this.controller1 = new PlayerDuelistController(duelists.get(0));

        GameCharacter npcMage = makeRandomMageNPC();
        MyColors opposColor = findBestMagicColor(npcMage);
        duelists.add(new MagicDuelist(npcMage, opposColor, new PowerGauge()));
        this.controller2 = new AIDuelistController(duelists.get(1));
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

    private void runDuel(Model model) {
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
    }

    private boolean duelIsOver() {
        return MyLists.any(duelists, MagicDuelist::isKnockedOut);
    }

    private static MyColors findBestMagicColor(GameCharacter npcMage) {
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
        subView.clearTemporaryBeams();
        selectedAction.prepare(model, this, duelists.get(0));
        opponentAction.prepare(model, this, duelists.get(1));
        selectedAction.resolve(model, this, opponentAction);
        waitUntil(subView, MagicDuelSubView::temporaryBeamsDone);
        selectedAction.wrapUp(model, this, duelists.get(1));
        opponentAction.wrapUp(model, this, duelists.get(0));
        if (!beamsAreLocked()) {
            subView.clearTemporaryBeams();
        } else {
            subView.setTwoBeamsAtMidPoint(lockedBeam.shift);
            subView.setFlash();
        }
    }

    private void beamTurn(Model model) {
        BeamOptions selectedAction = controller1.selectBeamTurnAction(model, this);
        BeamOptions otherAction = controller2.selectBeamTurnAction(model, this);
        resolveBeamOptions(model, selectedAction, otherAction);
    }

    private void resolveBeamOptions(Model model, BeamOptions selectedAction, BeamOptions otherAction) {
        if (selectedAction == BeamOptions.HoldOn && otherAction == BeamOptions.HoldOn) {
            println("Both duelist are holding the locked beams.");
            return;
        }

        if (selectedAction == BeamOptions.Release && otherAction == BeamOptions.Release) {
            println("Both duelists simultaneously released the locked beams.");
            lockedBeam = null;
            subView.clearTemporaryBeams();
            return;
        }

        if (selectedAction == BeamOptions.IncreasePower) {
            println(duelists.get(0).getName() + " increases the power of the beam!");
            lockedBeam.power++;
            lockedBeam.shift--;
        }
        if (otherAction == BeamOptions.IncreasePower) {
            println(duelists.get(1).getName() + " increases the power of the beam!");
            lockedBeam.power++;
            lockedBeam.shift++;
        }
        subView.setTwoBeamsAtMidPoint(lockedBeam.shift);

        if (lockedBeam.shift == MAX_SHIFT) {
            lockedBeamHits(model, duelists.get(0));
            return;
        }
        if (lockedBeam.shift == -MAX_SHIFT) {
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
        print(magicDuelist.getName() + " is trying to release the locked beam...");
        model.getLog().waitForAnimationToFinish();
        if (magicDuelist.testMagicSkill(model, this, MagicDuelAction.BASE_DIFFICULTY + lockedBeam.power)) {
            println("and successfully disentangles the beams!");
            model.getLog().waitForAnimationToFinish();
            subView.clearTemporaryBeams();
            lockedBeam = null;
        } else {
            lockedBeamHits(model, magicDuelist);
        }
    }

    private void lockedBeamHits(Model model, MagicDuelist magicDuelist) {
        println(magicDuelist.getName() + " got hit by the locked beams!");
        magicDuelist.takeDamage(1);
        subView.clearTemporaryBeams();
        lockedBeam = null;
    }

    private void beamUpkeep(Model model) {
        for (MagicDuelist d : duelists) {
            d.addToPower(lockedBeam.power * -3);
        }

        if (duelists.get(0).getPower() == 0 && duelists.get(1).getPower() == 0) {
            println("Both duelists are out of power! The beam dissipates.");
            model.getLog().waitForAnimationToFinish();
            lockedBeam = null;
            subView.clearTemporaryBeams();
            return;
        }

        if (duelists.get(0).getPower() == 0) {
            println(duelists.get(0).getName() + " is out of power!");
            model.getLog().waitForAnimationToFinish();
            lockedBeamHits(model, duelists.get(0));
            return;
        }

        if (duelists.get(1).getPower() == 0) {
            println(duelists.get(1).getName() + " is out of power!");
            model.getLog().waitForAnimationToFinish();
            lockedBeamHits(model, duelists.get(1));
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
            println(duelists.get(0).getName() + "'s attack is blown away by the power of " +
                    duelists.get(1).getName() + "'s attack!");
            duelists.get(0).takeDamage(MAX_SHIFT - shift + 1);
        } else if (-shift >= MAX_SHIFT) {
            println(duelists.get(1).getName() + "'s attack is blown away by the power of " +
                    duelists.get(0).getName() + "'s attack!");
            duelists.get(1).takeDamage(MAX_SHIFT + shift + 1);
        } else {
            lockedBeam = new LockedBeam();
            lockedBeam.power = playerStrength + opposStrength;
            lockedBeam.shift = shift;
            println(duelists.get(0).getName() + "'s and " + duelists.get(1).getName() + "'s beams are locked together!");
            subView.animatedTwoBeamsAtMidPoint(playerStrength, opposStrength);
        }
    }

    public boolean beamsAreLocked() {
        return lockedBeam != null;
    }

    public void fireBeamAtOpponent(MagicDuelist performer, int beamStrength) {
        if (performer == duelists.get(0)) {
            subView.animatedBeamAtOpponent(beamStrength);
        } else {
            subView.animatedBeamAtPlayer(beamStrength);
        }
    }

    private static class LockedBeam {
        private int power = 0;
        private int shift = 0;
    }
}
