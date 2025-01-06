package model.states.duel;

import model.Model;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.items.spells.Spell;
import model.races.Race;
import model.states.CombatEvent;
import model.states.DailyEventState;
import model.states.TravelTable;
import model.states.duel.actions.BeamOptions;
import model.states.duel.actions.MagicDuelAction;
import model.states.duel.gauges.*;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyLists;
import util.MyRandom;
import view.MyColors;
import view.subviews.MagicDuelSubView;
import view.subviews.SetupMagicDuelSubView;
import view.subviews.StripedTransition;
import view.subviews.SubView;

import java.util.*;

public class MagicDuelEvent extends DailyEventState {

    private enum OutputType{ normal, console, none;}


    public static final int MAX_HITS = 5;

    private static final int MAX_SHIFT = 3;
    private static final List<CharacterClass> RITUALIST_CLASSES =
            List.of(Classes.SOR, Classes.MAG, Classes.WIZ,
                    Classes.WIT, Classes.PRI, Classes.DRU);
    private static final Map<MyColors, List<MyColors>> ADVANTAGE_MAP =
            Map.of(MyColors.RED, List.of(MyColors.BLUE, MyColors.WHITE),
                    MyColors.BLUE, List.of(MyColors.GREEN),
                    MyColors.GREEN, List.of(MyColors.BLACK),
                    MyColors.BLACK, List.of(MyColors.WHITE));
    private final GameCharacter opponent;
    private final GameCharacter preselectedPartyMember;
    private PowerGauge preselectedOpponentGauge = null;
    protected List<MagicDuelist> duelists;

    protected DuelistController controller1;
    protected DuelistController controller2;
    protected MagicDuelSubView subView;

    private boolean lockedBeamOn = false;
    private int lockedBeamPower = 0;
    private int lockedBeamShift = 0;

    private boolean showOpposColor = false;
    private boolean showOpposGauge = false;

    private final boolean aisOnly;
    private final OutputType outputType;

    public MagicDuelEvent(Model model, boolean aisOnly, GameCharacter opponentChar, GameCharacter preselectedPartyChar) {
        super(model);
        this.opponent = opponentChar;
        if (aisOnly) {
            outputType = OutputType.none;
        } else {
            outputType = OutputType.normal;
        }
        this.aisOnly = aisOnly;
        this.preselectedPartyMember = preselectedPartyChar;
    }

    public MagicDuelEvent(Model model) {
        this(model, false, makeNPCMageOfClassAndLevel(Classes.MAGE, 1), null);
    }

    public static GameCharacter makeNPCMageOfClassAndLevel(CharacterClass cls, int level) {
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
        MyColors opposColor = findBestMagicColor(opponent);
        PowerGauge opposGauge = preselectedOpponentGauge;
        if (opposGauge == null) {
            opposGauge = makeRandomGauge();
        }

        GameCharacter selectedCharacter = model.getParty().getPartyMember(0);
        if (preselectedPartyMember != null) {
            selectedCharacter = preselectedPartyMember;
        } else {
            if (model.getParty().size() > 1) {
                print("Which party member should take part in the duel? ");
                model.getTutorial().magicDuels(model);
                selectedCharacter = model.getParty().partyMemberInput(model,
                        this, model.getParty().getPartyMember(0));
            } else {
                print(selectedCharacter.getName() + " is entering into a magic duel.");
                model.getTutorial().magicDuels(model);
            }
        }

        SetupMagicDuelSubView setupSubview = new SetupMagicDuelSubView(opponent, selectedCharacter,
                showOpposColor ? opposColor : null,
                showOpposGauge ? opposGauge : null);
        model.setSubView(setupSubview);

        do {
            waitForReturnSilently();
        } while (!setupSubview.isOnStart());

        BackgroundMusic previousMusic = ClientSoundManager.getCurrentBackgroundMusic();
        CombatEvent.startMusic();

        this.duelists = new ArrayList<>();

        duelists.add(new MagicDuelist(selectedCharacter,
                setupSubview.getSelectedColor(), setupSubview.getSelectedGauge()));
        this.controller1 = new PlayerDuelistController(duelists.get(0));


        duelists.add(new MagicDuelist(opponent, opposColor, opposGauge));
        this.controller2 = new MatrixDuelistController(duelists.get(1),
                opposGauge.getAIMatrices(opponent));
        this.subView = new MagicDuelSubView(model.getCurrentHex().getCombatTheme(),
                duelists.get(0), duelists.get(1));
        StripedTransition.transition(model, subView);

        runDuel(model);

        MagicDuelist winner = MyLists.find(duelists, d -> !d.isKnockedOut());
        println("The duel is over, the winner is " + winner.getName() + "!");
        drainStamina(model, duelists.get(0));
        drainStamina(model, duelists.get(1));
        print("Press enter to continue.");
        waitForReturn();
        ClientSoundManager.playBackgroundMusic(previousMusic);
    }

    private void drainStamina(Model model, MagicDuelist duelist) {
        int SPLoss = Math.min(duelist.getCharacter().getSP(), duelist.getHitsTaken()/2);
        String word = "tired";
        if (SPLoss > 1) {
            word = "exhausted";
        }
        if (SPLoss > 0) {
            duelist.getCharacter().addToSP(-SPLoss);
            if (model.getParty().getPartyMembers().contains(duelist.getCharacter())) {
                println(duelist.getCharacter().getName() + " is " + word + " from the duel and loses " + SPLoss + " stamina.");
            }
        }
    }

    protected int runDuel(Model model) {
        int roundCounter = 1;
        do {
            if (beamsAreLocked()) {
                beamUpkeep(model);
            }
            if (duelIsOver()) { // Knocked out from beam upkeep.
                break;
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

    public static MyColors findBestMagicColor(GameCharacter npcMage) {
        List<Skill> magicSkills = List.of(Skill.MagicRed, Skill.MagicBlue, Skill.MagicGreen,
                Skill.MagicBlack, Skill.MagicWhite);
        int maxRank = -1;
        Skill bestSkill = null;
        for (Skill s : magicSkills) {
            int rankForSkill = npcMage.getRankForSkill(s);
            if (rankForSkill > maxRank) {
                bestSkill = s;
                maxRank = rankForSkill;
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
        if (magicDuelist.testMagicSkill(model, this,
                calcDifficultyFor(magicDuelist) + lockedBeamPower)) {
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

    public int calcDifficultyFor(MagicDuelist magicDuelist) {
        MagicDuelist opponent = duelists.get(0);
        if (opponent == magicDuelist) {
            opponent = duelists.get(1);
        }
        if (hasAdvantageOver(magicDuelist.getMagicColor(), opponent.getMagicColor())) {
            return MagicDuelAction.BASE_DIFFICULTY - 1;
        }
        return MagicDuelAction.BASE_DIFFICULTY;
    }

    public static boolean hasAdvantageOver(MyColors left, MyColors right) {
        if (!ADVANTAGE_MAP.containsKey(left)) {
            return false;
        }
        return ADVANTAGE_MAP.get(left).contains(right);
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
            addToPower(d, lockedBeamPower * -3);
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

    public void addToPower(MagicDuelist d, int amount) {
        if (amount > 0 && controller1 instanceof PlayerDuelistController && controller1.getDuelist() == d) {
            for (int x = 0; x < amount; ++x) {
                d.addToPower(1);
                delay(Math.max(50, 400 / (amount - x)));
            }
        } else {
            d.addToPower(amount);
        }
    }

    public void clearTemporaryBeams() {
        if (!aisOnly) {
            subView.clearTemporaryBeams();
        }
    }

    private void generatePower(List<MagicDuelist> duelists) {
        for (MagicDuelist d : duelists) {
            int amount = MyRandom.rollD6() + MyRandom.rollD6() + MyRandom.rollD6();
            addToPower(d, amount);
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

    public void textOutput(String s) {
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

    public GameCharacter getWinner() {
        if (duelists.get(0).getHitsTaken() >= 5) {
            return duelists.get(1).getCharacter();
        }
        if (duelists.get(1).getHitsTaken() >= 5) {
            return duelists.get(0).getCharacter();
        }
        return null;
    }

    public void setShowOpponentColor(boolean b) {
        this.showOpposColor = b;
    }

    public void setShowOpponentGauge(boolean b) {
        this.showOpposGauge = b;
    }

    public MagicDuelist getDuelist(int i) {
        return duelists.get(i);
    }

    public void setPreselectedOpponentGauge(PowerGauge powerGauge) {
        this.preselectedOpponentGauge = powerGauge;
    }

    public static PowerGauge makeRandomGauge() {
        int i = MyRandom.randInt(7);
        switch (i) {
            case 0:
                return new BTypePowerGauge(true);
            case 1:
                return new ATypePowerGauge(true);
            case 2:
                return new CTypePowerGauge(true);
            case 3:
                return new KTypePowerGauge(true);
            case 4:
                return new TTypePowerGauge(true);
            case 5:
                return new STypePowerGauge(true);
            default:
                return new VTypePowerGauge(true);
        }
    }
}
