package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.spells.Spell;
import model.states.DailyEventState;
import model.states.GameState;
import sound.SoundEffects;
import util.Arithmetics;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.AnimationManager;
import view.sprites.CastingEffectSprite;
import view.sprites.MiscastEffectSprite;
import view.sprites.Sprite;
import view.subviews.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class RitualEvent extends DailyEventState {
    private static final int DEFAULT_MAX_NPC_MAGES = 4;
    public static final int CAST_BEAM_DIFFICULTY = 8;
    public static final int RECEIVE_BEAM_DIFFICULTY = 5;
    private static final int COMMAND_NPC_CHANCE = 8;
    public static final int DROP_OUT_HP_THRESHOLD = 3;
    private final Skill primeMagicSkill;
    private final MyColors magicColor;
    private List<GameCharacter> benched;
    private List<GameCharacter> ritualists;
    private GameCharacter turnTaker;
    private List<GameCharacter> turnOrder;
    private final List<MyPair<GameCharacter, GameCharacter>> beams;

    public RitualEvent(Model model, MyColors magicColor) {
        super(model);
        this.magicColor = magicColor;
        this.primeMagicSkill = Spell.getSkillForColor(magicColor);
        beams = new ArrayList<>();
    }

    protected abstract CombatTheme getTheme();
    protected abstract boolean runEventIntro(Model model, List<GameCharacter> ritualists);
    protected abstract void runEventOutro(Model model, boolean success, int power);
    public abstract Sprite getCenterSprite();

    @Override
    protected final void doEvent(Model model) {
        ritualists = makeNPCMages(model);
        if (!runEventIntro(model, ritualists)) {
            return;
        }
        SubView prevSubView = model.getSubView();

        println("Please select which party members to participate in the ritual (B). " +
                "The remaining group (A), will stand back and watch the ritual unfold.");
        model.getLog().waitForAnimationToFinish();
        List<GameCharacter> partOfParty = divideParty(model);
        if (partOfParty.isEmpty()) {
            model.getParty().unbenchAll();
            println("You decline to participated in the ritual and continue on your journey.");
            return;
        }
        ritualists.addAll(partOfParty);
        setupTurnOrder();
        Collections.shuffle(ritualists);
        this.benched = new ArrayList<>(model.getParty().getBench());

        RitualSubView subView = new RitualSubView(getTheme(), this, magicColor);
        CollapsingTransition.transition(model, subView);

        print("Press enter to start the ritual.");
        model.getTutorial().rituals(model);
        waitForReturn();
        while (!ritualFailed() && !ritualSucceeded()) {
            print(turnTaker.getName() + "'s turn. ");
            if (getNumberOfBeams(turnTaker) > 0) {
                println(turnTaker.getFirstName() + " takes damage from maintaining the beam.");
                takeBeamDamage(turnTaker, subView);
            }
            if (turnTaker.getHP() < DROP_OUT_HP_THRESHOLD) {
                dropOut(turnTaker, subView);
            } else {
                subView.setCursor(turnTaker);
                if (!model.getParty().getPartyMembers().contains(turnTaker)) {
                    if (MyRandom.rollD10() > COMMAND_NPC_CHANCE){
                        print(heOrSheCap(turnTaker.getGender()) + " acts of " + hisOrHer(turnTaker.getGender()) +
                                " own volition! ");
                        model.getLog().waitForAnimationToFinish();
                        takeNPCTurn(model, subView);
                    } else {
                        print(heOrSheCap(turnTaker.getGender()) + " awaits your instruction. ");
                        takeOneTurn(model, subView);
                    }
                } else {
                    takeOneTurn(model, subView);
                }
                if (ritualists.contains(turnTaker)) {
                    stepNextTurnTaker();
                }
            }
        }

        if (ritualFailed()) {
            beams.clear();
            println("The ritual has failed");
        } else if (ritualSucceeded()) {
            subView.setRitualSuccess(true);
            println("The ritual has succeeded!");
        }
        print("Press enter to continue.");
        waitForReturn();
        AnimationManager.unregister(subView);
        model.getParty().unbenchAll();

        CollapsingTransition.transition(model, prevSubView);
        runEventOutro(model, !ritualFailed(), ritualists.size() - 4);
    }

    private void stepNextTurnTaker() {
        int index = Arithmetics.incrementWithWrap(turnOrder.indexOf(turnTaker), turnOrder.size());
        turnTaker = turnOrder.get(index);
    }

    public boolean ritualSucceeded() {
        if (beams.size() != ritualists.size()) {
            return false;
        }
        for (MyPair<GameCharacter, GameCharacter> beam : beams) {
            int index = ritualists.indexOf(beam.first);
            int otherIndex = ritualists.indexOf(beam.second);
            int forward = Arithmetics.incrementWithWrap(index, ritualists.size());
            forward = Arithmetics.incrementWithWrap(forward, ritualists.size());
            int backward = Arithmetics.decrementWithWrap(index, ritualists.size());
            backward = Arithmetics.decrementWithWrap(backward, ritualists.size());
            if (otherIndex != forward && otherIndex != backward) {
                return false;
            }
        }
        return true;
    }

    private boolean ritualFailed() {
        return ritualists.size() < 5;
    }

    private void takeNPCTurn(Model model, RitualSubView subView) {
        int dieRoll = MyRandom.rollD10();
        if (dieRoll < 3) {
            swapWithNeighborInDirection(MyRandom.randInt(2));
        } else if (dieRoll < 9 && getNumberOfBeams(turnTaker) < 2) {
            if (!npcTryCastBeam(model, subView)) {
                pass(turnTaker);
            }
        } else {
            pass(turnTaker);
        }
    }

    private void takeOneTurn(Model model, RitualSubView subView) {
        boolean done = false;
        while (!done) {
            waitForReturnSilently();
            Point p = subView.getPositionForSelected();
            int selected = multipleOptionArrowMenu(model, p.x + 3, p.y + 3,
                    List.of("Cast Beam", "Move", "Drop Out", "Release", "Pass", "Cancel"));
            if (selected == 0) {
                done = tryToCastBeam(model, subView, subView.getSelected());
            } else if (selected == 1) {
                done = swapWithNeighbor(model, p);
            } else if (selected == 2) {
                dropOut(turnTaker, subView);
                done = true;
            } else if (selected == 3) {
                done = releaseBeams();
            } else if (selected == 4) {
                pass(turnTaker);
                done = true;
            } else {
                done = false;
            }
        }
    }

    private boolean releaseBeams() {
        if (getNumberOfBeams(turnTaker) > 0) {
            releaseBeamsFor(turnTaker);
            return true;
        }
        println(turnTaker.getName() + " isn't holding any beams.");
        return false;
    }

    private void pass(GameCharacter turnTaker) {
        println(turnTaker.getFirstName() + " does nothing.");
    }

    private void dropOut(GameCharacter dropOut, RitualSubView subView) {
        println(dropOut.getName() + " drops out of the ritual.");
        if (dropOut == subView.getSelected()) {
            subView.resetSelected();
        }
        stepNextTurnTaker();
        ritualists.remove(dropOut);
        turnOrder.remove(dropOut);
        benched.add(dropOut);
        releaseBeamsFor(dropOut);
        getModel().getLog().waitForAnimationToFinish();
    }

    private void releaseBeamsFor(GameCharacter beamReleaser) {
        int size = beams.size();
        beams.removeIf((MyPair<GameCharacter, GameCharacter> pair) ->
                pair.first == beamReleaser || pair.second == beamReleaser);
        if (size > beams.size()) {
            println(beamReleaser.getFirstName() + "'s beams dissipate.");
        }
    }


    private boolean npcTryCastBeam(Model model, RitualSubView subView) {
        List<GameCharacter> candidates = new ArrayList<>(ritualists);
        candidates.remove(turnTaker);
        candidates.removeIf((GameCharacter gc) -> getNumberOfBeams(gc) == 2);
        candidates.removeIf((GameCharacter gc) -> hasABeam(turnTaker, gc));
        if (candidates.isEmpty()) {
            return false;
        }
        Collections.shuffle(candidates);
        tryToCastBeam(model, subView, candidates.get(0));
        return true;
    }


    private boolean tryToCastBeam(Model model, RitualSubView subView, GameCharacter receiver) {
        model.getTutorial().ritualBeams(model);
        if (receiver == turnTaker) {
            println(turnTaker.getName() + " cannot cast a beam onto " + himOrHer(turnTaker.getGender()) + "self.");
            return false;
        }
        if (getNumberOfBeams(turnTaker) == 2) {
            println(turnTaker.getName() + " is already holding two beams, " + heOrShe(turnTaker.getGender()) +
                    " can't hold any more!");
            return false;
        }
        if (getNumberOfBeams(receiver) == 2) {
            println(receiver.getName() + " is already holding two beams, " + heOrShe(receiver.getGender()) +
                    " can't hold any more!");
            return false;
        }
        if (hasABeam(turnTaker, receiver)) {
            println(turnTaker.getName() + " already has a been with " + receiver.getName() + ".");
        }

        println(turnTaker.getName() + " attempts to cast a beam onto " + receiver.getName() + ".");

        SkillCheckResult result;
        if (model.getParty().getPartyMembers().contains(turnTaker)) {
            result = model.getParty().doSkillCheckWithReRoll(model, this, turnTaker, primeMagicSkill,
                    CAST_BEAM_DIFFICULTY, 10, turnTaker.getRankForSkill(Skill.SpellCasting));
        } else {
            result = turnTaker.testSkill(primeMagicSkill, CAST_BEAM_DIFFICULTY, turnTaker.getRankForSkill(Skill.SpellCasting));
            println(primeMagicSkill.getName() + " " + result.asString() + ".");
        }
        model.getLog().waitForAnimationToFinish();
        if (result.isSuccessful()) {
            takeBeamDamage(turnTaker, subView);
            subView.addSpecialEffect(turnTaker, new CastingEffectSprite());
            SoundEffects.playSpellSuccess();
            subView.addTemporaryBeam(turnTaker, receiver);
            tryReceiveBeam(model, subView, turnTaker, receiver);
        } else {
            subView.addSpecialEffect(turnTaker, new MiscastEffectSprite());
            SoundEffects.playSpellFail();
        }
        return true;
    }

    private boolean hasABeam(GameCharacter turnTaker, GameCharacter receiver) {
        for (MyPair<GameCharacter, GameCharacter> pair : beams) {
            if ((pair.first == turnTaker && pair.second == receiver) ||
                    (pair.second == turnTaker && pair.first == receiver)) {
                return true;
            }
        }
        return false;
    }

    private void takeBeamDamage(GameCharacter turnTaker, RitualSubView subView) {
        turnTaker.addToHP(-1);
        subView.addFloatyDamage(turnTaker, 1);
    }

    private void tryReceiveBeam(Model model, RitualSubView subView, GameCharacter sender, GameCharacter receiver) {
        println(receiver.getFirstName() + " tries to catch the beam.");
        SkillCheckResult result2;
        if (model.getParty().getPartyMembers().contains(receiver)) {
            result2 = model.getParty().doSkillCheckWithReRoll(model, this, receiver, Skill.MagicAny,
                    RECEIVE_BEAM_DIFFICULTY, 0, 0);
        } else {
            result2 = receiver.testSkill(Skill.MagicAny, RECEIVE_BEAM_DIFFICULTY);
            println(Skill.MagicAny.getName() + " " + result2.asString() + ".");
        }
        model.getLog().waitForAnimationToFinish();
        subView.removeTemporaryBeam();
        if (result2.isSuccessful()) {
            subView.addSpecialEffect(receiver, new CastingEffectSprite());
            SoundEffects.playSpellSuccess();
            addBeam(sender, receiver);
            println("A ritual beam was successfully established between " + sender.getFirstName() +
                    " and " + receiver.getFirstName());
        } else {
            println(receiver.getName() + " couldn't hold the beam, and it dissipates.");
            subView.addSpecialEffect(receiver, new MiscastEffectSprite());
            SoundEffects.playSpellFail();
        }
    }

    private int getNumberOfBeams(GameCharacter target) {
        int result = 0;
        for (MyPair<GameCharacter, GameCharacter> pair : beams) {
            if (pair.first == target || pair.second == target) {
                result++;
            }
        }
        return result;
    }

    private void addBeam(GameCharacter sender, GameCharacter receiver) {
        this.beams.add(new MyPair<>(sender, receiver));
    }

    private boolean swapWithNeighbor(Model model, Point p) {
        List<String> directionOptions = List.of("Clockwise", "Counter-Clockwise", "Back");
        int direction = multipleOptionArrowMenu(model, p.x+3, p.y+3,
                directionOptions);
        if (direction == 2) {
            return false;
        }
        swapWithNeighborInDirection(direction);
        return true;
    }

    private void swapWithNeighborInDirection(int direction) {
        int index = ritualists.indexOf(turnTaker);
        int newIndex = 0;
        if (direction == 0) {
            newIndex = Arithmetics.decrementWithWrap(index, ritualists.size());
        } else if (direction == 1) {
            newIndex = Arithmetics.incrementWithWrap(index, ritualists.size());
        }
        GameCharacter swapper = ritualists.get(newIndex);
        ritualists.set(newIndex, turnTaker);
        ritualists.set(index, swapper);
        println(turnTaker.getName() + " swaps places with " + swapper.getName() + ".");
    }

    public void setupTurnOrder() {
        turnOrder = new ArrayList<>(ritualists);
        Collections.sort(turnOrder, Comparator.comparingInt(GameCharacter::getSpeed));
        Collections.reverse(turnOrder);
        this.turnTaker = turnOrder.get(0);
    }

    protected List<GameCharacter> makeNPCMages(Model model) {
        List<CharacterClass> classes = List.of(Classes.WIT, Classes.WIZ, Classes.MAG,
                                               Classes.PRI, Classes.DRU, Classes.SOR);
        List<GameCharacter> result = new ArrayList<>();
        for (int i = MyRandom.randInt(2, DEFAULT_MAX_NPC_MAGES); i > 0; --i) {
            GameCharacter gc = makeRandomCharacter();
            gc.setClass(MyRandom.sample(classes));
            gc.addToHP(999);
            result.add(gc);
        }
        return result;
    }

    private List<GameCharacter> divideParty(Model model) {
        SubView oldSub = model.getSubView();
        List<GameCharacter> groupA = new ArrayList<>(model.getParty().getPartyMembers());
        List<GameCharacter> groupB = new ArrayList<>();
        model.setSubView(new SplitPartySubView(oldSub, groupA, groupB));
        waitForReturnSilently();
        model.getParty().benchPartyMembers(groupA);
        model.setSubView(oldSub);
        return groupB;
    }

    public List<GameCharacter> getRitualists() {
        return ritualists;
    }

    public List<GameCharacter> getBench() {
        return benched;
    }

    public GameCharacter getCurrentTurnTaker() {
        return turnTaker;
    }

    public List<GameCharacter> getRitualistsInTurnOrder() {
        return turnOrder;
    }

    public List<MyPair<GameCharacter, GameCharacter>> getBeams() {
        return beams;
    }

    protected void healParty(Model model) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            println(gc.getName() + " is healed.");
            model.getLog().waitForAnimationToFinish();
            gc.addToHP(5);
        }
    }
}
