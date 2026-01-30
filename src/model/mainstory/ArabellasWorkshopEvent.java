package model.mainstory;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.map.CastleLocation;
import model.states.DailyEventState;
import util.MyLists;
import view.subviews.SnakeTransition;
import view.subviews.SubView;

public class ArabellasWorkshopEvent extends DailyEventState {
    public ArabellasWorkshopEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You rush into the workshop, but immediately realize your mistake.");
        leaderSay("I must have stepped on something...");
        println("A thick smoke billows out of a pipe in the wall, promptly putting " +
                (model.getParty().size() == 1 ? "you" : "the whole party")  + " to sleep.");
        model.getLog().waitForAnimationToFinish();
        MyLists.forEach(model.getParty().getPartyMembers(),
                gc -> model.getParty().forceEyesClosed(gc, true));
        SubView oldSubView = model.getSubView();
        SnakeTransition.transition(model, new ArabellasWorkshopSuView());
        println("A strange rattling sound wakes you...");
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, model.getMainStory().getArabellaAppearance(), "Arabella");
        MyLists.forEach(model.getParty().getPartyMembers(),
                gc -> model.getParty().forceEyesClosed(gc, false));
        println("A woman stands before you. You try to move but your hands and feet have been bound.");
        portraitSay("When I installed my little gas trap I never thought you would be the one" +
                (model.getParty().size() > 1 ? "s" : "") + " to fall into it.");
        if (model.getParty().size() > 1) {
            println("You can see the rest of the party members have also been " +
                    "tied to rings in the workshop walls.");
        }
        leaderSay("You must be Arabella.");
        portraitSay("Bravo, at least you've caught on there.");
        leaderSay("Release " + meOrUs() + ". Whatever you're doing here, it's not going to work.");
        CastleLocation castle = model.getWorld().getCastleByName(model.getMainStory().getCastleName());
        portraitSay("Hehe, actually I think you might be wrong there. But I should give you some credit. I " +
                "must admit I was actually surprised when you showed up in " + castle.getLordName() + "'s throne room " +
                "after your trip to the ancient stronghold. I did not expect anybody to survive that place " +
                "after what I did to it.");
        leaderSay("So it was you who enchanted it! " + iOrWeCap() + " should have known.");
        portraitSay("Enchanted? I guess that's one way of describing it...");
        GameCharacter wiggler = model.getParty().size() > 1 ? model.getParty().getRandomPartyMember(model.getParty().getLeader()) :
                model.getParty().getLeader();

        println(wiggler.getFirstName() + " can feel " + hisOrHer(wiggler.getGender()) +
                " bonds have come loose a little. " +
                "Perhaps with a little more wiggling you can loosen them further.");
        model.getLog().waitForAnimationToFinish();
        SkillCheckResult result1 = wiggler.testSkill(model, Skill.Security);
        println(wiggler.getFirstName() + " tries to get loose, Security " + result1.asString() + ".");

        portraitSay("The place was deserted when I found it. I couldn't believe I'd found the legendary stronghold " +
                "of the Quad! I spent weeks researching those dusty old tomes and scrolls. Finally I managed to get their " +
                "equipment running again. Ahh, that splendid machinery, the alchemical apparatuses...");
        leaderSay("You managed to synthesize a crimson pearl. Which could control the minds of others.");
        portraitSay("Yes. And by then I had plenty of test subjects...");

        println(wiggler.getFirstName() + "'s bonds are getting looser.");
        model.getLog().waitForAnimationToFinish();
        SkillCheckResult result2 = wiggler.testSkill(model, Skill.Security);
        println(wiggler.getFirstName() + " again tries to get loose, Security " + result2.asString() + ".");

        portraitSay("The arcane magic of the Quad radiates some strange aura. Natives from the region were " +
                "drawn to the stronghold, orcs, lizardmen, frogmen, trolls, even humans, elves and dwarves.");
        leaderSay("You brainwashed them all? Now I see why you were banished from your kingdom.");
        GameCharacter caid = MyLists.find(model.getParty().getPartyMembers(), gc -> gc == model.getMainStory().getCaidCharacter());
        if (caid != null) {
            partyMemberSay(caid, "We should have locked you up and thrown away the key when we had the chance!");
        }
        portraitSay("The stronger-willed creatures required crimson pearls for me to control them. The lesser ones, " +
                "and the wild beasts could simply be dominated with spells.");
        leaderSay("And then you infiltrated " + castle.getLordTitle() + "'s court, posing as a noble adviser.");
        portraitSay("That part was easier than expected. Just a simple illusion, and nobody recognized me.");
        leaderSay( "But your plan was simply to take control of " + hisOrHer(castle.getLordGender()) + " mind. " +
                        "What is it you want Arabella, revenge?");
        portraitSay("Revenge? I'm not that petty. I just want what I feel I deserve for my toils. " +
                "I'll settle for the " + castle.getLordTitle() + "'s kingdom.");

        println(wiggler.getFirstName() + "'s bonds are getting very loose. A little more wiggling and " +
                heOrShe(wiggler.getGender()) + " will be free.");
        model.getLog().waitForAnimationToFinish();
        SkillCheckResult result3 = wiggler.testSkill(model, Skill.Security);
        println(wiggler.getFirstName() + " again tries to get loose, Security " + result3.asString() + ".");

        leaderSay("You'll never have it. You'll be stopped and brought to justice!");
        portraitSay("Ha! Whatever makes you think so?");
        leaderSay("You've confessed all your crimes! As soon as " + iOrWe() + " are freed, " + iOrWe() + "'ll arrest you!");
        portraitSay("Oh you fool" + (model.getParty().size() > 1 ? "s" : "") + "! In a minute it won't matter. You " +
                "won't remember a thing!");
        leaderSay("What are you talking about?");
        portraitSay("You see this machine behind me? I am moments away from completing it. When it is running my mind-control " +
                "power will extend to the borders of the " + CastleLocation.placeNameToKingdom(castle.getPlaceName()) + " and perhaps beyond. " +
                "Every single sentient being will be under my control, doing my bidding!");
        leaderSay("You're crazy!");
        portraitSay("No, just ambitious. If only I could have learned from the Quad themselves. The power I could have attained. " +
                "It would have let me control all four kingdoms, perhaps even the entire world!");
        GameCharacter willis = MyLists.find(model.getParty().getPartyMembers(), gc -> gc == model.getMainStory().getWillisCharacter());
        if (willis != null) {
            partyMemberSay(willis, "You don't know what kind of power you're dealing with Arabella. " +
                    "That machine looks highly volatile!");
        } else {
            leaderSay("Spoken like a true megalomaniac.");
        }
        portraitSay("I don't expect you to understand. But again, it matters not. The time has come!");
        println("Arabella reaches into her pocket and brings out a crimson pearl.");
        portraitSay("As soon as I insert this into the device... Well, let's just say you won't be so recalcitrant anymore. " +
                "You'll be my loyal subject!");

        println(wiggler.getFirstName() + " breaks free from " + hisOrHer(wiggler.getGender()) + " bonds!");
        partyMemberSay(wiggler, "I'm free!");
        if (wiggler != model.getParty().getLeader()) {
            portraitSay("What!?");
            println("Arabella is surprised and drops the crimson pearl on the floor.");
            leaderSay("Quickly " + wiggler.getFirstName() + ", free the rest of us!");
            println("While Arabella scrambles about on the floor, " + wiggler.getFirstName() +
                    " quickly cuts the rest of the party loose from their bonds.");
            portraitSay("Gotcha!");
            println("Then, just as the party members are about to pounce on her, she picks up the pearl and dashes towards the machine.");
        } else {
            println("Arabella dashes towards the machine.");
        }
        println("She holds out her hand and drops the pearl into the machine...");
        model.getLog().waitForAnimationToFinish();
        model.setSubView(oldSubView);
    }
}
