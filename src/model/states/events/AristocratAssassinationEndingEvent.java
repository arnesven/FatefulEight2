package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.tasks.AssassinationDestinationTask;

import java.util.List;

public class AristocratAssassinationEndingEvent extends AssassinationEndingEvent {
    public AristocratAssassinationEndingEvent(Model m) {
        super(m, true, 6);
    }

    @Override
    protected void victimReactToAttack(String name) {
        printQuote(name, "No, wait wait wait, don't kill me! I'll give you gold!");
    }

    @Override
    protected Ending runCombatWithVictimInside(Model model, AssassinationDestinationTask task, GameCharacter victim, boolean ambush) {
        showExplicitPortrait(model, victim.getAppearance(), victim.getName());
        leaderSay("Gold? What do you mean?");
        portraitSay("I-I-I'm very wealthy... I have a lot of money, I'll give it to you. Just spare my life!");
        leaderSay("Thanks for telling " + meOrUs() + ". " + iOrWeCap() + "'ll be sure to empty your pockets after " + iOrWe() + " kill you.");
        println("You move closer with your weapon poised to strike.");
        portraitSay("No no! Wait! It's not on my person, it's hidden!");
        leaderSay("Of course. Okay, tell " + meOrUs() + " where it is, and " + iOrWe() + " won't kill you.");
        portraitSay("Seriously? I don't know if I trust you, but I guess I have no choice but take your word for it.");
        leaderSay("Don't " + iOrWe() + " look trustworthy?");
        portraitSay("Uh... I can't really tell if you're joking. But in any case, it's hidden in a " +
                "secret compartment under the stairs.");
        leaderSay("Noted. Now...");
        print("Kill " + victim.getName() + " anyway? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("Please die for " + myOrOur() + " happiness!");
            portraitSay("What! But but but, we had a deal!");
            leaderSay("I don't care.");
            someMayGetAngry(model);
            Ending result = super.runCombatWithVictimInside(model, task, victim, ambush);
            if (!haveFledCombat()) {
                findCache(model);
            }
            println("You leave the " + task.getWrit().getDestinationShortDescription() + ".");
            return result;
        }

        leaderSay(iOrWeCap() + "'ll just get our gold and then be on our way.");
        portraitSay("Ah... yes. And I think I'll be packing my bags. I should leave town before another assassin shows up.");
        findCache(model);
        println("You leave the " + task.getWrit().getDestinationShortDescription() + ".");
        return Ending.failure;
    }

    private void someMayGetAngry(Model model) {
        boolean said = randomSayIfPersonality(PersonalityTrait.diplomatic, List.of(model.getParty().getLeader()),
                "This was a new low, even for us.");
        if (!said) {
            said = randomSayIfPersonality(PersonalityTrait.benevolent, List.of(model.getParty().getLeader()),
                    "This was a new low, even for us.");
        }
        if (!said) {
            randomSayIfPersonality(PersonalityTrait.lawful, List.of(model.getParty().getLeader()),
                    "This was a new low, even for us.");
        }

        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.hasPersonality(PersonalityTrait.lawful) || gc.hasPersonality(PersonalityTrait.diplomatic) ||
                    gc.hasPersonality(PersonalityTrait.benevolent)) {
                gc.addToAttitude(model.getParty().getLeader(), -50);
            }
        }
        randomSayIfPersonality(PersonalityTrait.cold, List.of(model.getParty().getLeader()),
                "No witnesses. Smart choice.");
    }

    private void findCache(Model model) {
        println("You find the cache under the stairs (150 gold) and put it in your pocket.");
        model.getParty().earnGold(150);
    }
}
