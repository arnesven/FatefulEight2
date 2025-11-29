package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.states.RecruitState;
import model.tasks.BoyfriendGirlfriendDestinationTask;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyLists;
import util.MyRandom;
import util.MyStrings;

import java.util.Arrays;
import java.util.List;

public class RevisitBoyfriendGirlfriendEvent extends AbstractBoyfriendGirlfriendEvent {
    private final GameCharacter friend;
    private final int prevEventChoice;
    private final int step;

    public RevisitBoyfriendGirlfriendEvent(Model model, GameCharacter main,
                                           GameCharacter friend, int previousEventChoice,
                                           int step) {
        super(model, main);
        this.friend = friend;
        this.prevEventChoice = previousEventChoice;
        this.step = step;
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.gentleMemory);
        benchAllButMain(model);
        GameCharacter main = getMainCharacter();
        if (step == 0) {
            daytimeDate(model, main);
        } else if (step == 1) {
            nightTimeDate(model, main);
        } else {
            justRecruit(model, main);
        }

        removePortraitSubView(model);
        model.getParty().unbenchAll();
    }

    private void daytimeDate(Model model, GameCharacter main) {
        println(main.getName() + " sets off in search of " + friend.getFirstName() + ". It does not take long before " +
                heOrShe(main.getGender()) + " finds the shop where " + friend.getFirstName() + " works.");
        partyMemberSay(main, "Hello there " + friend.getFirstName() + ".");
        friend.setClass(Classes.ART);
        showExplicitPortrait(model, friend.getAppearance(), friend.getFirstName());
        portraitSay(main.getFirstName() + ", you're back! How have you been?");
        partyMemberSay(main, "Good. Do you want to hang out today?");
        portraitSay("Yes, of course. I'm just about to finish up my shift. Why don't you wait outside for a little bit.");
        partyMemberSay(main, "Alright.");
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
        println(main.getFirstName() + " exists the shop and thinks about what might be a fun activity to share with " + friend.getFirstName() + " today.");
        int chosenActivity = multipleOptionArrowMenu(model, 24, 24,
                List.of("Visit a cafe",
                        "stroll in the park",
                        "Row a boat on the lake"));

        println("After a little while " + friend.getFirstName() + " comes out of the shop.");
        friend.setClass(Classes.None);
        showExplicitPortrait(model, friend.getAppearance(), friend.getFirstName());
        portraitSay("It's a pleasure to see you again, thanks for seeking me out. So what do you want to do?");
        if (chosenActivity == 0) {
            partyMemberSay(main, "I was thinking we could go to a cafe.");
        } else if (chosenActivity == 1) {
            partyMemberSay(main, "I was thinking we could go for a stroll through the park in town.");
        } else {
            partyMemberSay(main, "I was thinking we could rent a row boat and take a short trip on the water.");
        }
        friendReactToActivity(chosenActivity);

        if (chosenActivity == 0) {
            println(main.getFirstName() + " and " + friend.getFirstName() + " find a cozy little cafe. " +
                    "They sit down and order coffee and pastries.");
        } else if (chosenActivity == 1) {
            println(main.getFirstName() + " and " + friend.getFirstName() + " take a relaxing stroll through " +
                    "the park in town. There's a gentle breeze through the trees and there's a pleasant scent of blooming flowers.");
        } else {
            println(main.getFirstName() + " and " + friend.getFirstName() + " find their way down to the water. " +
                    "There they rent a row boat and head out on the water. " + main.getFirstName() + " takes the oars while " +
                    friend.getFirstName() + " relaxes.");
        }
        int chosenTopic = topicChoice(model, main, friend);
        if (chosenActivity == 0) {
            println(main.getFirstName() + " and " + friend.getFirstName() + " have finished their coffee and pastries and " +
                    "it's time to leave the cafe.");
        } else if (chosenActivity == 1) {
            println(main.getFirstName() + " and " + friend.getFirstName() + " come back to where they started their walk.");
        } else {
            println(main.getFirstName() + " and " + friend.getFirstName() + " take the boat back to shore.");
        }
        portraitSay("I live not far from here. Care to walk me home?");
        partyMemberSay(main, "Certainly. Lead the way.");
        println("After a short walk the two end up in front of a small house.");
        portraitSay("Well this is it.");
        partyMemberSay(main, "Yep...");
        if (friend.getAttitude(main) >= 20) {
            println(friend.getFirstName() + " leans in and gives " + main.getFirstName() + " a gentle kiss on the lips.");
        } else {
            println(friend.getFirstName() + " embraces " + main.getFirstName() + " warmly.");
        }
        portraitSay("This was fun. You'll visit me again, right?");
        partyMemberSay(main, "Of course. See ya " + friend.getFirstName() + ".");
        portraitSay("Goodbye for now " + main.getFirstName());
        println(main.getFirstName() + " returns to the party");

        BoyfriendGirlfriendDestinationTask dt = findTask(model);
        assert dt != null;
        dt.progress(chosenTopic, model.getDay());
    }

    private BoyfriendGirlfriendDestinationTask findTask(Model model) {
        return (BoyfriendGirlfriendDestinationTask)
                MyLists.find(model.getParty().getDestinationTasks(),
                        task -> task instanceof BoyfriendGirlfriendDestinationTask &&
                        !task.isCompleted() && !task.isFailed(model));
    }

    private void nightTimeDate(Model model, GameCharacter main) {
        println(main.getName() + " is visiting " + friend.getFirstName() + "'s home. " +
                heOrSheCap(main.getGender()) + " finds the little cottage and knocks on the door. " +
                friend.getFirstName() + " opens it and smiles.");
        showExplicitPortrait(model, friend.getAppearance(), friend.getFirstName());
        portraitSay(main.getFirstName() + ", I've missed you.");
        partyMemberSay(main, "Here I am again. What are you up to today?");
        portraitSay("Actually, I'm off to work now. Can we meet this evening?");
        partyMemberSay(main, "Sure. Should I come back around sunset?");
        portraitSay("Yes, that's great.");
        partyMemberSay(main, "Goodbye for now then.");
        model.getLog().waitForReturn();
        removePortraitSubView(model);
        println(main.getFirstName() + " heads to the tavern. Once there, " +
                heOrShe(main.getGender()) + " tries to think of a romantic evening activity for " +
                friend.getFirstName() + " and " + himOrHer(main.getGender()) + "self.");
        int chosenActivity = multipleOptionArrowMenu(model, 24, 24,
                List.of("Have dinner at restaurant",
                        "Go camping just outside of town",
                        "Go swimming in the moonlight"));
        println("Just around sunset " + main.getFirstName() + " heads over to " +
                friend.getFirstName() + "'s house.");
        friend.setClass(Classes.BEAUTY);
        showExplicitPortrait(model, friend.getAppearance(), friend.getFirstName());
        partyMemberSay(main, "Hi there " + friend.getFirstName() + ". You look nice.");
        portraitSay("Thank you. I wasn't sure what you had planned, so I took something pretty but comfortable.");
        if (chosenActivity == 0) {
            partyMemberSay(main, "Pretty is good, I was thinking we could have dinner at a nice restaurant.");
        } else if (chosenActivity == 1) {
            partyMemberSay(main, "Comfortable is good, I was thinking we could go camping, and sleep under the stars.");
        } else {
            partyMemberSay(main, "Actually you may want to bring a swimsuit. I was thinking we could go swimming in the moonlight.");
        }
        friendReactToActivity(chosenActivity);

        if (chosenActivity == 0) {
            println(main.getFirstName() + " and " + friend.getFirstName() + " find a fancy restaurant. " +
                    "They sit down and have a nice meal, which " + friend.getFirstName() + " generously pays for.");
            println("The two stay at the restaurant and talk for a long time.");
        } else if (chosenActivity == 1) {
            println(main.getFirstName() + " and " + friend.getFirstName() + " head out toward the outskirts of town. " +
                    "They set up their tent and roast some food over an open fire.");
            println("The two then crawl into the tent and look up at the starry night sky and talk for a long time.");
        } else {
            println(main.getFirstName() + " and " + friend.getFirstName() + " head down toward the water. " +
                    "They eat roasted snacks and when the moon comes out they go for a swim under the stars.");
            println("Afterward, the two stay on the beach and talk for a long while.");
        }

        portraitSay("This has been a really nice date " + main.getFirstName() + ". But I have to ask you, where do " +
                "you think this relationship is going?");
        partyMemberSay(main, "Oh... I've just been enjoying our time together so much, I haven't really " +
                "been thinking about what the next step would be.");
        portraitSay("What would you want it to be?");

        int choice = multipleOptionArrowMenu(model, 24, 24, List.of("Ask to settle down", "Ask to join party"));
        if (choice == 0) {
            askSettleDown(model, main);
        } else {
            askToJoinParty(model, main);
        }
    }

    private void askToJoinParty(Model model, GameCharacter main) {
        partyMemberSay(main, "I was thinking... Maybe you want to join the company?");
        BoyfriendGirlfriendDestinationTask task = findTask(model);
        if (MyRandom.randInt(30) < friend.getAttitude(main) / 2 + 10 * task.getPartyTalkCount()) {
            portraitSay("Oh... that's not what I thought you were going to say, but it does sound interesting!");
            partyMemberSay(main, "Why don't we go find the leader of the company and see if we can work out a deal?");
            portraitSay("Sure. Lead the way.");
            tryRecruit(model, main, friend);
            task.progress(-1, model.getDay());
        } else {
            friendNotReadyForThat(main, friend);
        }
    }

    private void justRecruit(Model model, GameCharacter main) {
        println(main.getName() + " sets off in search of " + friend.getFirstName() + ". It does not take long before " +
                heOrShe(main.getGender()) + " finds the shop where " + friend.getFirstName() + " works.");
        partyMemberSay(main, "Hello there " + friend.getFirstName() + ".");
        friend.setClass(Classes.ART);
        showExplicitPortrait(model, friend.getAppearance(), friend.getFirstName());
        portraitSay(main.getFirstName() + ", you're back!");
        partyMemberSay(main, "Yes. I think we want you in our party now.");
        portraitSay("Alright. I'll come along right now.");
        tryRecruit(model, main, friend);
    }

    private void tryRecruit(Model model, GameCharacter main, GameCharacter friend) {
        println("The two head to the tavern where they find the rest of the party.");
        model.getLog().waitForAnimationToFinish();
        model.getParty().unbenchAll();
        leaderSay("Hello there " + main.getFirstName() + ", who's your friend?");
        partyMemberSay(main, "Uhm, this is my " + (friend.getGender() ? "girl":"boy") + "friend, " +
                friend.getName() + ". " + heOrSheCap(friend.getGender()) + " has expressed an interest in joining our party.");
        leaderSay("Oh, really. What are your qualifications?");
        portraitSay("Well, currently I'm working in a shop, you could perhaps call me an Artisan.");
        leaderSay("Uh-huh, what else? How are you with a weapon?");
        portraitSay( "I've worked as a guard and could function well as a Captain. I'm also " +
                "a decent hunter, and I've dabbled in some white and blue magic. What class would you like me to assume if " +
                "I would to join your party?");
        CharacterClass[] classes = friend.getClasses();
        List<String> choices = MyLists.transform(Arrays.asList(classes), CharacterClass::getFullName);
        int choice = multipleOptionArrowMenu(model, 24, 24, choices);
        leaderSay("I think we could do well with a " + choices.get(choice) + ".");
        model.getLog().waitForAnimationToFinish();
        friend.setClass(classes[choice]);
        friend.setLevel((int)Math.ceil(calculateAverageLevel(model)) + 1);
        RecruitState recruit = new RecruitState(model, List.of(friend));
        recruit.setStartingGold(friend, 50);
        recruit.run(model);
        setCurrentTerrainSubview(model);
        if (model.getParty().getPartyMembers().contains(friend)) {
            partyMemberSay(main, "This is great " + friend.getFirstName() + "! Now we can be together all the time.");
            partyMemberSay(friend, "I know. I'm so excited!");
            leaderSay("Okay... Settle down you two. Can you try to be a little discreet? Just to spare the rest of us.");
            partyMemberSay(main, "Sure. We can get our own tent.");
            partyMemberSay(friend, "Hehe...");
            findTask(model).setCompleted(true);
            friend.addToAttitude(main, 10);
            main.addToAttitude(friend, 10);
        } else {
            leaderSay("Sorry " + friend.getFirstName() + " we can't take you on at the moment.");
            println(friend.getFirstName() + " leaves, visibly annoyed.");
            partyMemberSay(main, "Maybe we can recruit " + friend.getFirstName() + " later...");
            leaderSay("Yeah maybe, we'll see.");
        }
    }

    private void askSettleDown(Model model, GameCharacter main) {
        partyMemberSay(main, "I was thinking... Maybe we could be together... Like permanently?");
        if (MyRandom.randInt(30) < friend.getAttitude(main)) {
            portraitSay("Oh yes, I would like that very much! This makes me so happy. But, are you okay with " +
                    "giving up your adventuring career?");
            print("Are you willing to lose " + main.getName() + " from the party? (Y/N) ");
            if (yesNoInput()) {
                partyMemberSay(main, "Yes. I think my adventuring days are done. I want to stay right here with you.");
                portraitSay("Oh " + main.getFirstName() + "!");
                partyMemberSay(main, "But I've got to square this with the company's leader. " +
                        "Can you wait at your cottage for me?");
                portraitSay("Of course.");
                println(main.getFirstName() + " and " + friend.getFirstName() + " part ways for now.");
                model.getLog().waitForAnimationToFinish();
                removePortraitSubView(model);
                println(main.getFirstName() + " returns to the party.");
                model.getParty().unbenchAll();
                leaderSay("Hello there " + main.getFirstName() + " where have you been? It seems like you always " +
                        "disappear on us when we're in this town.");
                partyMemberSay(main, "Yes. Actually I've met someone.");
                leaderSay("That's nice. Get some while you can I guess.");
                partyMemberSay(main, "No, it's not like that. It's serious.");
                leaderSay("Serious? You're not thinking about to settling down are you?");
                partyMemberSay(main, "Yes, actually I am.");
                leaderSay("Oh...");
                boolean wasCombat = new PartyMemberWantsToLeaveEvent(model).wantsToLeave(model, main, false);
                if (wasCombat && main.isDead()) {
                    findTask(model).setFailed(true);
                } else {
                    showExplicitPortrait(model, main.getAppearance(), main.getFirstName());
                    leaderSay("I hope " + heOrShe(friend.getGender()) + " is worth it.");
                    portraitSay(heOrSheCap(friend.getGender()) + " is. Goodbye " + model.getParty().getLeader().getFirstName() + ", thanks for everything.");
                    leaderSay("Bye " + main.getName() + ".");
                    model.getLog().waitForAnimationToFinish();
                    removePortraitSubView(model);
                    findTask(model).setCompleted(true);
                    println(main.getName() + " has settled down with " + hisOrHer(main.getGender()) + " love.");
                    completeAchievement(RevisitBoyfriendGirlfriendEvent.class.getCanonicalName());
                }
            } else {
                partyMemberSay(main, "Actually, no... I was thinking you could come with me?");
                portraitSay("Oh " + main.getFirstName() + ", I don't think I can do that. That life is just not for me.");
                partyMemberSay(main, "That makes me sad. Maybe we just aren't right for each other.");
                portraitSay("Maybe not. But I'm glad we got to meet again. Is this goodbye then?");
                partyMemberSay(main, "It seems like it.");
                failEnding(main);
            }
        } else {
            friendNotReadyForThat(main, friend);
        }
    }

    private void friendNotReadyForThat(GameCharacter main, GameCharacter friend) {
        portraitSay("Oh... Uhm, I guess I'm not really ready for that. I think we may not be " +
                "right for each other after all.");
        partyMemberSay(main, "That makes me sad. But if you don't want to, then I guess that's just how it is.");
        portraitSay("I'm sorry. I apologize if I let you think there was something more serious here than " +
                "there actually was.");
        partyMemberSay(main, "It's okay " + friend.getFirstName() + ". I'm glad we got to meet again. " +
                "Is this goodbye then?");
        portraitSay("It seems like it.");
        failEnding(main);
    }

    private void failEnding(GameCharacter main) {
        println("After the date, " + main.getFirstName() + " returns to the party quite depressed. It will take " +
                "some time before " + heOrShe(main.getGender()) + " gets over " + friend.getFirstName() + ".");
        BoyfriendGirlfriendDestinationTask dt = findTask(getModel());
        dt.setFailed(true);
    }

    private void friendReactToActivity(int chosenActivity) {
        if (chosenActivity + 1 == prevEventChoice || (prevEventChoice == 0 && MyRandom.randInt(3) == 0)) {
            portraitSay("That sounds really fun! Let's go.");
            friend.addToAttitude(getMainCharacter(), 10);
            getMainCharacter().addToAttitude(friend, 10);
        } else {
            portraitSay("Okay, that sounds good. Let's go.");
        }
    }
}
