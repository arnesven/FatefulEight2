package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.states.DailyEventState;
import model.states.GameState;
import util.Arithmetics;
import util.MyRandom;
import view.sprites.Animation;
import view.sprites.AnimationManager;
import view.sprites.CastingEffectSprite;
import view.sprites.MiscastEffectSprite;
import view.subviews.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class RitualEvent extends DailyEventState {
    private static final int DEFAULT_NPC_MAGES = 10; // TODO: 4
    private static final int CAST_BEAM_DIFFICULTY = 1; // TODO: 8
    private static final int RECEIVE_BEAM_DIFFICULTY = 1; // TODO: 5
    private final Skill primeMagicSkill;
    private List<GameCharacter> benched;
    private List<GameCharacter> ritualists;
    private GameCharacter turnTaker;
    private List<GameCharacter> turnOrder;

    public RitualEvent(Model model, Skill primaryMagicSkill) {
        super(model);
        this.primeMagicSkill = primaryMagicSkill;
    }

    protected abstract CombatTheme getTheme();
    protected abstract void runEventIntro(Model model, List<GameCharacter> ritualists);
    protected abstract void runEventOutro(Model model);

    @Override
    protected void doEvent(Model model) {
        ritualists = makeNPCMages(model);
        runEventIntro(model, ritualists);
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

        RitualSubView subView = new RitualSubView(getTheme(), this);
        CollapsingTransition.transition(model, subView);

        while (!ritualFailed() && !ritualSucceeded()) {
            println(turnTaker.getName() + "'s turn.");
            subView.setCursor(turnTaker);
            boolean done = true;
            if (model.getParty().getPartyMembers().contains(turnTaker)) {
                done = takeOneTurn(model, subView);
            } else {
                takeNPCTurn(model, subView);
            }
            if (done) {
                int index = Arithmetics.incrementWithWrap(turnOrder.indexOf(turnTaker), turnOrder.size());
                turnTaker = turnOrder.get(index);
            }
        }

        if (ritualFailed()) {
            println("The ritual has failed");
        } else if (ritualSucceeded()) {
            println("The ritual has succeeded!");
        }

        // Logic for ritual

        //CollapsingTransition.transition(model, prevSubView);
        //runEventOutro(model);
    }

    private boolean ritualSucceeded() {
        return false;
    }

    private boolean ritualFailed() {
        return ritualists.size() < 5;
    }


    private void takeNPCTurn(Model model, RitualSubView subView) {
        println(turnTaker.getName() + " passes.");
    }

    private boolean takeOneTurn(Model model, RitualSubView subView) {
        waitForReturnSilently();
        Point p = subView.getPositionForSelected();
        int selected = multipleOptionArrowMenu(model, p.x+3, p.y+3,
                List.of("Cast Beam", "Move", "Drop Out", "Pass", "Cancel"));
        if (selected == 0) {
            return tryToCastBeam(model, subView);
        } else if (selected == 1) {
            return swapWithNeighbor(model, p);
        } else if (selected == 2) {
            println(turnTaker.getName() + " drops out of the ritual.");
            ritualists.remove(turnTaker);
            turnOrder.remove(turnTaker);
            benched.add(turnTaker);
        }
        return selected < 4;
    }

    private boolean tryToCastBeam(Model model, RitualSubView subView) {
        if (subView.getSelected() == turnTaker) {
            println("You cannot cast a beam onto yourself!");
            return false;
        }
        GameCharacter receiver = subView.getSelected();
        println(turnTaker.getName() + " attempts to cast a beam onto " + receiver.getName() + ".");
        SkillCheckResult result = model.getParty().doSkillCheckWithReRoll(model, this, turnTaker, primeMagicSkill,
                CAST_BEAM_DIFFICULTY, 10, turnTaker.getRankForSkill(Skill.SpellCasting));
        if (result.isSuccessful()) {
            subView.addSpecialEffect(turnTaker, new CastingEffectSprite());
            subView.addTemporaryBeam(turnTaker, receiver);
            println(receiver.getFirstName() + " tries to catch the beam.");
            SkillCheckResult result2;
            if (model.getParty().getPartyMembers().contains(receiver)) {
                result2 = model.getParty().doSkillCheckWithReRoll(model, this, receiver, Skill.MagicAny,
                        RECEIVE_BEAM_DIFFICULTY, 0, 0);
            } else {
                result2 = receiver.testSkill(Skill.MagicAny, RECEIVE_BEAM_DIFFICULTY);
                println(Skill.MagicAny.getName() + " " + result2.asString() + ".");
                model.getLog().waitForAnimationToFinish();
            }
            subView.removeTemporaryBeam();
            if (result2.isSuccessful()) {
                subView.addSpecialEffect(receiver, new CastingEffectSprite());
                subView.addBeam(turnTaker, receiver);
                println("A ritual beam was successfully established between " + turnTaker.getFirstName() +
                        " and " + receiver.getFirstName());
            } else {
                println(receiver.getName() + " couldn't hold the beam, and it dissipates.");
                subView.addSpecialEffect(receiver, new MiscastEffectSprite());
            }
        } else {
            subView.addSpecialEffect(turnTaker, new MiscastEffectSprite());
        }
        return true;
    }

    private boolean swapWithNeighbor(Model model, Point p) {
        List<String> directionOptions = List.of("Clockwise", "Counter-Clockwise", "Back");
        int direction = multipleOptionArrowMenu(model, p.x+3, p.y+3,
                directionOptions);
        if (direction == 2) {
            return false;
        }
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
        return true;
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
        int level = (int)Math.ceil(GameState.calculateAverageLevel(getModel()));
        List<GameCharacter> result = new ArrayList<>();
        for (int i = DEFAULT_NPC_MAGES; i > 0; --i) {
            GameCharacter gc = makeRandomCharacter(level);
            gc.setClass(MyRandom.sample(classes));
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
}
