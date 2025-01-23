package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.spells.LevitateSpell;
import model.items.spells.Spell;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;
import view.subviews.CollapsingTransition;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.List;

public abstract class RiverEvent extends DailyEventState {

    public static SubView subView = new ImageSubView("river", "RIVER", "You are attempting to cross the river.", true);
    private final boolean levitate;

    public RiverEvent(Model model, boolean levitateAcross) {
        super(model);
        this.levitate = levitateAcross;
    }

    public abstract boolean eventPreventsCrossing(Model model);

    @Override
    protected final void doEvent(Model model) {
        Spell levitateSpell = getLevitateSpell(model);
        if (levitate && levitateSpell != null) {
            print("Would you like to attempt to levitate the party across the river? (Y/N) ");
            if (yesNoInput()) {
                levitateAcross(model, levitateSpell);
                return;
            }
        }

        doRiverEvent(model);
    }

    private void levitateAcross(Model model, Spell levitateSpell) {
        List<GameCharacter> charsToLevitate = new ArrayList<>(model.getParty().getPartyMembers());

        GameCharacter caster = model.getParty().getPartyMember(0);
        while (!charsToLevitate.isEmpty()) {
            println("There are still " + charsToLevitate.size() + " party member(s) to levitate across the river.");
            print("Who do you want to levitate across the river? ");
            GameCharacter target = model.getParty().partyMemberInput(model, this, charsToLevitate.get(0));
            print("Who do you want to cast the levitate spell? ");
            caster = model.getParty().partyMemberInput(model, this, caster);
            boolean success = levitateSpell.castYourself(model, this, caster);
            String targetName = target.getFirstName();
            if (!success) {
                int roll = MyRandom.rollD10();
                partyMemberSay(caster, "Oops!");
                if (target == caster) {
                    targetName = himOrHer(target.getGender()) + "self";
                }
                if (roll < 5) {
                    fallIntoRiver(model, target, caster.getFirstName() + " drops " + target.getFirstName() +
                            " into the river! " + target.getFirstName() + " attempts to swim across!");
                    charsToLevitate.remove(target);
                } else if (roll > 8) {
                    println(caster.getFirstName() + " drops " + targetName +
                            " in the shallows on the other side of the river. " +
                            target.getFirstName() + " wades ashore.");
                    charsToLevitate.remove(target);
                } else {
                    println(caster.getFirstName() + " drops " + targetName +
                            " in the shallows on this side of the river.");
                    partyMemberSay(target, "Well, I'm completely soaked and I still haven't crossed the river.");
                }
            } else {
                println(caster.getFirstName() + " successfully moved " + targetName + " through the air and " +
                        heOrShe(target.getGender()) + " is gently set down on the other side of the river.");
                charsToLevitate.remove(target);
                model.getParty().partyMemberSay(model, target, List.of("Whoa!", "Oh my goodness.",
                        "Did you see that? I was flying!", "I'm a flying " + target.getCharClass().getFullName().toLowerCase() + "!",
                        "I'm a flying " + target.getRace().getName().toLowerCase() + "!",
                        "That was fantastic.", "Magical...", "Light as a feather..."));
                partyMemberSay(target, "Whoa!");
            }
            if (caster.isDead()) {
                charsToLevitate.remove(caster);
            }
            if (target.isDead()) {
                charsToLevitate.remove(target);
            }
        }
        println("The entire party has been levitated across!");
        leaderSay("Good job team!");
    }

    private Spell getLevitateSpell(Model model) {
        return MyLists.find(model.getParty().getSpells(), sp -> sp instanceof LevitateSpell);
    }

    protected abstract void doRiverEvent(Model model);

    public void fallIntoRiver(Model model, GameCharacter gc, String text) {
        SkillCheckResult result;
        do {
            result = gc.testSkill(model, Skill.Endurance, 8);
            println(gc.getName() + " " + text + " " + result.asString() + ".");
            if (gc.getSP() > 0) {
                println("Using stamina to re-roll.");
                gc.addToSP(-1);
            } else {
                break;
            }
        } while (!result.isSuccessful());

        if (result.isSuccessful()) {
            model.getParty().partyMemberSay(model, gc, List.of("I'm okay!", "Gaah, that was tough!#", "Brrr, it was cold!",
                    "I just felt like having a dip."));
            if (model.getParty().size() > 1) {
                GameCharacter other = model.getParty().getRandomPartyMember(gc);
                partyMemberSay(other, "You should really get out of those wet clothes!");
                if (gc.hasPersonality(PersonalityTrait.prudish)) {
                    partyMemberSay(gc, "No way... I'm not taking anything off.");
                } else if (gc.hasPersonality(PersonalityTrait.rude)) {
                    partyMemberSay(gc, "Just mind your own business.");
                }
            }

        } else {
            model.getLog().waitForAnimationToFinish();
            characterDies(model, this, gc," has been swept away by the current and drowns!", false);
        }
    }

    protected void showRiverSubView(Model model) {
        CollapsingTransition.transition(model, RiverEvent.subView);
    }
}
