package model.states.events;

import model.Model;
import model.characters.appearance.ChildAppearance;
import model.combat.abilities.InvisibilityCombatAction;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.Combatant;
import model.combat.conditions.InvisibilityCondition;
import model.enemies.TrainingDummyEnemy;
import model.items.spells.FireworksSpell;
import model.races.Race;
import model.states.CombatEvent;
import model.states.DailyEventState;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class KidsWantFireworksEvent extends DailyEventState {
    public KidsWantFireworksEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Find kids", "There are some kids who are always begging for magic tricks");
    }

    @Override
    protected void doEvent(Model model) {
        println("As you walk down the road a group of little kids suddenly ambush you.");
        ChildAppearance kid1 = PortraitSubView.makeChildAppearance(Race.randomRace(), MyRandom.flipCoin());
        kid1.setBigMouth();
        showExplicitPortrait(model, kid1, "Kid 1");
        portraitSay("Hey... you know magic right? Do some magic!");
        leaderSay("Eh, what?");
        CharacterAppearance kid2 = PortraitSubView.makeChildAppearance(Race.randomRace(), MyRandom.flipCoin());
        showExplicitPortrait(model, kid2, "Kid 2");
        portraitSay("Yeah, show us some magic tricks! Pull a rabbit out of your hat!");
        ChildAppearance kid3 = PortraitSubView.makeChildAppearance(Race.randomRace(), MyRandom.flipCoin());
        showExplicitPortrait(model, kid3, "Kid 3");
        portraitSay("Turn this stick into a flower!");
        ChildAppearance kid4 = PortraitSubView.makeChildAppearance(Race.randomRace(), MyRandom.flipCoin());
        kid4.setEyebrowsDown();
        showExplicitPortrait(model, kid4,"Kid 4");
        portraitSay("Make my mommy disappear!");
        showExplicitPortrait(model, kid1, "Kid 1");
        portraitSay("Do some fireworks!");
        showExplicitPortrait(model, kid3, "Kid 3");
        kid3.setBigMouth();
        portraitSay("Yeah fireworks!");
        println("All of the kids are now chanting for fireworks.");
        leaderSay("Oh come on kids...");
        List<String> options = new ArrayList<>();
        options.add("Do cheap trick");
        SpecialInvisibilityAbility invisibilityAbility = new SpecialInvisibilityAbility();
        if (MyLists.any(model.getParty().getPartyMembers(), gc -> invisibilityAbility.possessesAbility(model, gc))) {
            options.add("Go invisible");
        }
        if (MyLists.any(model.getParty().getSpells(), sp -> sp instanceof FireworksSpell)) {
            options.add("Do fireworks");
        }
        options.add("Drive kids off");
        int count = multipleOptionArrowMenu(model, 24, 24, options);
        if (options.get(count).contains("fireworks")) {
            GameCharacter caster = model.getParty().getPartyMember(0);
            if (model.getParty().size() > 1) {
                print("Who should cast the fireworks spell? ");
                caster = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            }
            boolean success = new FireworksSpell().castYourself(model, this, caster);
            if (success) {
                showExplicitPortrait(model, kid1, "Kid 1");
                portraitSay("Awesome! Fireworks!");
                showExplicitPortrait(model, kid2, "Kid 2");
                portraitSay("That was the coolest thing ever!");
                rewardByParent(model, 20);
            } else {
                portraitSay("Lame!");
                println("The kids stick their tongues out at you, then run away.");
            }
        } else if (options.get(count).contains("invisible")) {
            GameCharacter caster = model.getParty().getPartyMember(0);
            if (model.getParty().size() > 1) {
                print("Who should cast the invisibility? ");
                caster = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            }
            invisibilityAbility.castYourself(model,
                    new CombatEvent(model, List.of(new TrainingDummyEnemy('A'))), caster, caster);
            if (caster.hasCondition(InvisibilityCondition.class)) {
                println("The kids are visibly stunned by the disappearance of " + caster.getFirstName() + ".");
                showExplicitPortrait(model, kid1, "Kid 1");
                portraitSay("Whoa! Where did " + heOrShe(caster.getGender()) + " go?");
                println("After a little while, " + caster.getFirstName() + " becomes visible again, right behind the kids.");
                caster.removeCondition(InvisibilityCondition.class);
                partyMemberSay(caster, "Booo!");
                showExplicitPortrait(model, kid4, "Kid 4");
                portraitSay("Eeeeh!");
                println("The kids shout with glee and jump around " + caster.getFirstName() + ".");
                rewardByParent(model, 15);
            } else {
                portraitSay("Lame!");
                println("The kids stick their tongues out at you, then run away.");
            }
        } else if (options.get(count).contains("cheap trick")) {
            leaderSay("Okay kids, check this out...");
            MyPair<Boolean, GameCharacter> result = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.MagicAny, 6);
            if (result.first) {
                println(result.second.getFirstName() + " make some sparks glitter in the air. " +
                        "They crackle faintly, then disappear with little pops.");
                println("The kids seem more curious than impressed.");
                showExplicitPortrait(model, kid1, "Kid 1");
                portraitSay("Do more do more!");
                leaderSay("I think that's enough for now. We got places to be you know.");
                portraitSay("Awww... okay...");
                leaderSay("Bye kids.");
                println("Each party member gains 5 experience points.");
                MyLists.forEach(model.getParty().getPartyMembers(), gc -> model.getParty().giveXP(model, gc, 5));
            } else {
                println(result.second.getFirstName() + " waves " + hisOrHer(result.second.getGender()) + " hands in the air " +
                        "but nothing happens.");
                showExplicitPortrait(model, kid1, "Kid 1");
                portraitSay("What's that supposed to be?");
                showExplicitPortrait(model, kid2, "Kid 2");
                portraitSay("Lame!");
                println("The kids stick their tongues out at you, then run away.");
            }
        } else {
            leaderSay("Beat it kids! We don't have time to waste on you urchins.");
            showExplicitPortrait(model, kid2, "Kid 2");
            portraitSay("Awww... no fun. Bluuh!");
            println("The kids stick their tongues out at you, then run away.");
        }
    }

    private void rewardByParent(Model model, int gold) {
        leaderSay("Hehe... Okay kids, we've had some fun, but we'd better be on our way now.");
        portraitSay("Okay. Come back again sometime!");
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
        println("You leave the elated group of kids and continue down the road. After just a few yards a man stops you.");
        showRandomPortrait(model, Classes.None, "Parent");
        portraitSay("Hey there. Saw what you did for the kids. They don't get a show like that very often. Most people " +
                "just ignore them or yell at them to go away.");
        leaderSay("It's rough being a kid...");
        portraitSay("Yeah... why don't you take this. It's not much, but you've earned it.");
        println("The man hands you a small bag of coins.");
        println("The party receives " + gold + " gold.");
        model.getParty().earnGold(gold);
        leaderSay("Thanks.");
    }

    private static class SpecialInvisibilityAbility extends InvisibilityCombatAction {
        public void castYourself(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
            doAction(model, combat, performer, target);
        }
    }
}
