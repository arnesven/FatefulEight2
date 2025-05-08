package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.races.AllRaces;
import model.states.DailyEventState;
import view.subviews.PortraitSubView;

import java.util.List;

public class SamuraiMonkEvent extends DailyEventState {
    public SamuraiMonkEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to crowd",
                "A crowd of people has gathered over by the stream, don't know what that's all about.");
    }

    @Override
    protected void doEvent(Model model) {
        println("A large crowd of people have gathered down by a stream. They are watching " +
                "a monk shaving an old man's head.");
        leaderSay("What's going on here?");
        showRandomPortrait(model, Classes.None, AllRaces.EASTERN_HUMAN, "Commoner");
        portraitSay("That samurai is disguising himself as a monk.");
        println("Man with the shaven head begins changing clothes with the monk.");
        portraitSay("A thief broke into a house last night, but was caught in the act. He must have panicked, because he " +
                "took a little baby as a hostage. Now he's refusing to give up the baby unless he gets to leave with impunity.");
        leaderSay("What's the samurai going to do about it?");
        portraitSay("I don't know. He just asked to call for a monk, and for some rice. Oh lords... " +
                "the baby has been crying all night.");
        println("Suddenly a woman comes running with a small tray in her hands. She hands it to the Samurai. With only " +
                "the bowl of rice in hand, the Samurai heads to the house where the thief has barricaded himself. He passes you.");
        leaderSay("You, samurai. You should let " + meOrUs() + " handle this.");
        println("The samurai just looks you over. Then silently continues toward the house. " +
                "The worried crowd follows him.");
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);

        println("A few steps away from the house, the Samurai stops, squats down and starts talking calmly with the thief.");
        model.getLog().waitForAnimationToFinish();
        CharacterAppearance samurai = PortraitSubView.makeOldPortrait(Classes.PRI, AllRaces.EASTERN_HUMAN, false);
        showExplicitPortrait(model, samurai, "Disguised Samurai");
        printQuote("Thief", "Stay away! I'll kill the baby!");
        portraitSay("Be calm. I'm a monk. I've brought food for the baby, and for you.");
        printQuote("Thief", "Throw it in!");
        println("Slowly, the samurai tosses the rice balls into the house. Then he rises and dusts himself off.");
        leaderSay("What now?");
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
        println("Then suddenly the samurai lunges into the house. The party hears the panicked screams from the thief, then " +
                "suddenly silence. After a few seconds, a figure stumbles out of the house.");
        leaderSay("The thief?");
        println("The thief stops, then falls to the ground, dead.");
        leaderSay("Incredible!");
        println("The samurai emerges from the house with the baby in his arms. The hysterical parents rush up to him and " +
                "cradles the baby in their arms. The crowd swarms around the samurai to congratulate him.");
        leaderSay("He must have had a concealed weapon in his robes.");
        println("The crowd disperses as the samurai gives the monk his clothes back and changes into his normal attire. " +
                "He passes you on the street.");
        samurai.setClass(Classes.SAMURAI);
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, samurai, "Samurai");
        leaderSay("Samurai, that was truly remarkable.");
        portraitSay("Someone had to do it. Those poor parents.");
        leaderSay("Please teach " + meOrUs() + " your ways!");
        println("The samurai looks at you closely.");
        portraitSay("Perhaps. But the skills of a samurai are not to be misused. What would you use them for?");
        int choice = multipleOptionArrowMenu(model, 24, 24, List.of("Destroy evil",
                "Knowledge and defense", "Rescuing babies"));
        if (choice == 0) {
            leaderSay("With the skills of a samurai, I would vanquish all my foes!");
            portraitSay("You're nothing more than a warlord then. I cannot teach you.");
            println("The samurai wanders off.");
        } else if (choice == 1) {
            leaderSay("I would hone my skills, and focus on knowledge about samurai culture.");
            portraitSay("That's a noble ambition. Perhaps I could teach you some things.");
            println("The samurai is willing to teach you about his profession, ");
            new ChangeClassEvent(model, Classes.SAMURAI).areYouInterested(model);
            println("You part ways with the samurai.");
        } else {
            leaderSay("I would use my skills to rescue babies which have been taken hostage by thieves.");
            portraitSay("Are you mocking me? I cannot teach you.");
            println("The samurai wanders off.");
            randomSayIfPersonality(PersonalityTrait.jovial, List.of(), "That guy just had no sense of humor.");
        }
    }
}
