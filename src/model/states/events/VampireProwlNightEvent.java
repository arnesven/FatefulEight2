package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.conditions.VampirismCondition;
import model.enemies.VampireEnemy;
import model.items.Equipment;
import model.races.AllRaces;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyRandom;
import view.combat.MansionTheme;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class VampireProwlNightEvent extends NightTimeEvent {
    public static final int DETECT_VAMPIRE_PERCEPTION_DIFFICULTY = 6;
    private final boolean inTavern;
    private final GameCharacter vampireCharacter;

    public VampireProwlNightEvent(Model model, boolean inTavern) {
        super(model);
        vampireCharacter = generateVampireCharacter();
        this.inTavern = inTavern;
    }

    @Override
    protected void doEvent(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.mysticSong);
        GameCharacter victim = MyRandom.sample(model.getParty().getPartyMembers());
        List<GameCharacter> others = new ArrayList<>(model.getParty().getPartyMembers());
        others.remove(victim);
        model.getParty().benchPartyMembers(others);
        model.getParty().forceEyesClosed(victim, true);
        showSilhouettePortrait(model, "Something");
        println("It is night and " + victim.getFirstName() + " is asleep. " +
                "Something approaches " + hisOrHer(victim.getGender()) + " bed...");
        print("Press enter to continue...");
        waitForReturn();
        if (failsToDetectVampire(model, this, victim)) {
            makeVampire(model, victim);
        } else {
            model.getParty().forceEyesClosed(victim, false);
            removePortraitSubView(model);
            showExplicitPortrait(model, vampireCharacter.getAppearance(), "Vampire");
            println(victim.getFirstName() + " wakes up and finds a dark figure standing next to " +
                    hisOrHer(victim.getGender()) + " bed, leaning over " + himOrHer(victim.getGender()) +
                    "! Fearing it may be a vampire, panic starts to set in.");
            print("Do you call out for help (Y) or do you let the vampire feed on you (N)? ");
            if (yesNoInput()) {
                partyMemberSay(victim, "Help! A vampire!!!");
                model.getParty().unbenchAll();
                if (model.getParty().size() > 1) {
                    print("The party members quickly scramble for their gear. ");
                }
                if (MyRandom.flipCoin()) {
                    removePortraitSubView(model);
                    println("But the vampire turns into a bat and flies away.");
                    partyMemberSay(victim, "That was creepy. Don't think I'll sleep more tonight.");
                    leaderSay("I'll keep watch and raise the alarm if the monster returns.");
                } else {
                    println("The vampire attacks you!");
                    if (inTavern) {
                        runCombat(List.of(new VampireEnemy('A')), new MansionTheme(), true);
                    } else {
                        runCombat(List.of(new VampireEnemy('A')));
                    }
                    if (haveFledCombat()) {
                        println("You flee from the fight, out into the night. After waiting for " +
                                "things to cool down a bit you return in hopes " +
                                "to gain a few hours of sleep before dawn.");
                    }
                }
            } else {
                model.getParty().forceEyesClosed(victim, true);
                if (model.getSubView() instanceof PortraitSubView) {
                    ((PortraitSubView) model.getSubView()).forceVampireFeedingLook();
                }
                println(victim.getFirstName() + " closes "+ hisOrHer(victim.getGender()) +
                        " eyes and awaits the vampire's embrace.");
                makeVampire(model, victim);
            }
        }
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
        model.getLog().waitForAnimationToFinish();
        model.getParty().forceEyesClosed(victim, false);
        model.getParty().unbenchAll();
    }

    protected boolean failsToDetectVampire(Model model, GameState state, GameCharacter victim) {
        SkillCheckResult result = victim.testSkill(model, Skill.Perception, DETECT_VAMPIRE_PERCEPTION_DIFFICULTY);
        state.println("Perception " + result.asString() + ".");
        return result.isFailure();
    }

    protected void makeVampire(Model model, GameCharacter victim) {
        makeCharacterIntoVampire(model, this, victim, true);
    }

    public static void makeCharacterIntoVampire(Model model, GameState state, GameCharacter victim, boolean random) {
        state.println("A sharp pain in the neck! Then, just as suddenly, it is gone and a strange, " +
                "but pleasant sensation falls upon " + victim.getFirstName() + ". " + heOrSheCap(victim.getGender()) +
                " falls into a deeper sleep and have odd dreams filled with violence, lust and the sense of immortality.");
        int hpLoss = victim.getHP()-1;
        if (hpLoss > 0) {
            state.println(victim.getName() + " lost "+ hpLoss + " HP and all stamina.");
            victim.addToHP(-hpLoss);
            victim.addToSP(-victim.getSP());
        }
        if (MyRandom.rollD10() < 7 || !random) {
            VampirismCondition.makeVampire(model, state, victim);
        } else {
            state.println("Apart from feeling incredibly weak, " + victim.getFirstName() +
                    " seems to be otherwise unaffected by the ordeal.");
        }
    }

    public static GameCharacter generateVampireCharacter() {
        Race race = MyRandom.sample(AllRaces.getAllRaces());
        CharacterAppearance vampirePortrait = PortraitSubView.makeOldPortrait(Classes.VAMPIRE, race, MyRandom.flipCoin());
        GameCharacter gc = new GameCharacter("Vampire", "", race, Classes.VAMPIRE,
                vampirePortrait, Classes.NO_OTHER_CLASSES, new Equipment());
        VampirismCondition cond = new VampirismCondition(5, 0);
        gc.addCondition(cond);
        cond.updateAppearance(gc);
        return gc;
    }
}
