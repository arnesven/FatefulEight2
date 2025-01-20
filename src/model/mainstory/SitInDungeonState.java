package model.mainstory;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.SilhouetteAppearance;
import model.classes.Classes;
import model.map.CastleLocation;
import model.quests.Quest;
import model.quests.RescueMissionQuest;
import model.races.Race;
import model.states.EveningState;
import model.states.GameState;
import model.states.QuestState;
import view.MyColors;
import view.subviews.CollapsingTransition;
import view.subviews.ImageSubView;
import view.subviews.PortraitSubView;
import view.subviews.SubView;

import java.util.List;

public class SitInDungeonState extends GameState {
    private static final SubView JAIL_SUBVIEW =
            new ImageSubView("jail", "DUNGEON", "You are in the castle dungeons.");;
    private final CastleLocation castle;
    private final Quest quest;
    private final int daysToWait;
    private final boolean withFlavor;

    public SitInDungeonState(Model model, CastleLocation castle, Quest quest, int daysToWait, boolean withFlavor) {
        super(model);
        this.castle = castle;
        this.quest = quest;
        this.daysToWait = daysToWait;
        this.withFlavor = withFlavor;
    }

    @Override
    public GameState run(Model model) {
        CollapsingTransition.transition(model, JAIL_SUBVIEW);
        for (int day = 1; ; ++day) {
            println("You are in the dungeon cell of " + castle.getPlaceName() + ".");
            model.getLog().waitForAnimationToFinish();
            multipleOptionArrowMenu(model, 24, 4, List.of("Do nothing"));
            println("You sit in the cell.");
            if (withFlavor) {
                runFlavor(model, day);
            }

            if (day >= daysToWait) {
                model.getLog().waitForAnimationToFinish();
                println("An opportunity to escape from the dungeon may have arisen.");
                Quest q2 = EveningState.offerQuests(model, this,
                        List.of(quest));
                if (q2 != null) {
                    stepToNextDay(model);
                    return new QuestState(model, q2, model.getParty().getPosition());
                }
            }
            stepToNextDay(model);
        }
    }

    private void runFlavor(Model model, int day) {
        GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
        if (day == 1 && other != null) {
            partyMemberSay(other, "What in the world just happened? Has " + castle.getLordName() + " lost " +
                    hisOrHer(castle.getLordGender()) + " mind?");
            leaderSay("Maybe. " + heOrShe(castle.getLordGender()) + " seemed completely different...");
            partyMemberSay(other, "In chance we can get out of here?");
            leaderSay("I saw a lot of other prisoners in the cells on our way in here.");
            partyMemberSay(other, "What do you mean by that?");
            leaderSay("I mean, either the " + castle.getLordTitle() + " has been very busy imprisoning people, " +
                    "or this place is not so easily escapable.");
            partyMemberSay(other, "Let's hope it's the former and not the latter.");
            leaderSay("Indeed.");
        } else if (day == 2) {
            println("The guards open the cell doors and violently throw a man inside. He stumbles to the floor.");
            AdvancedAppearance advisor = PortraitSubView.makeRandomPortrait(Classes.ARISTOCRAT, Race.ALL, false);
            advisor.setMascaraColor(MyColors.BLACK);
            advisor.setMouth(0); // To prevent "Smiley mouth"
            advisor.setClass(Classes.ARISTOCRAT);
            leaderSay("He's been beaten badly.");
            model.getLog().waitForAnimationToFinish();
            PortraitSubView subView = new PortraitSubView(model.getSubView(), advisor, "Advisor");
            model.setSubView(subView);
            println(model.getParty().getLeader().getFirstName() + " helps the man sit up.");
            leaderSay("Are you all right?");
            println("The man coughs and spits up blood.");
            subView.portraitSay(model, this, "I've been better. Thank you.");
            leaderSay("I'd offer you some water... but, well I don't have any.");
            subView.portraitSay(model, this, "Yes, it seems my " + castle.getLordTitle() +
                    "'s hospitality has taken a bit of a downward turn. It saddens me greatly.");
            leaderSay("Who are you?");
            subView.portraitSay(model, this, "My name is Damal. I've been an advisor to the " +
                    castle.getLordTitle() + " for a good fifteen years. Up until a few days ago that is.");
            leaderSay("You fell out of the lord's favor?");
            subView.portraitSay(model, this, "I'll say. We were good friends too. But then, all of a " +
                    "sudden I was arrested, for seemingly no reason at all.");
            leaderSay("Sounds like what happened to us. You didn't do anything to upset " + himOrHer(castle.getLordGender()) + "?");
            subView.portraitSay(model, this, "I may have opposed a few new policies, nothing out of the ordinary.");
            leaderSay("Has anything strange or suspicious been going on here at the castle lately? I mean apart from " +
                    "the recent orcish incursions?");
            String direction = model.getMainStory().getExpandDirectionName().toLowerCase();
            subView.portraitSay(model, this, "Now that you mention it, we had an envoy arrive from the " +
                    direction + " a few weeks past. She came " +
                    "baring many pretty and curious gifts but was not very specific about who they were from. All she would " +
                    "say was 'a great and generous lord in the " + direction + "'. I remember thinking it odd at the time, " +
                    "but I quickly put it out of my mind, I had many other duties to attend to.");
            leaderSay("An envoy you say. What did she look like?");
            subView.portraitSay(model, this, "She was fair, dark, tall. Now that I think about it I think I've seen " +
                    "her several times at the castle since then.");
            leaderSay("What was she doing?");
            subView.portraitSay(model, this, "She, was... aaagh!");
            leaderSay("You're bleeding!");
            subView.portraitSay(model, this, "Yes... I don't feel so good. I...");
            println("Damal coughs up a lot of blood.");
            leaderSay("Damal!");
            if (other != null) {
                partyMemberSay(other, "He must be hemorrhaging internally. Dammit, if only we had our gear, we could help him!");
            }
            leaderSay("Try not to speak any more Damal. Guards! Guards! This prisoner is dying!");
            if (other != null) {
                partyMemberSay(other, "Nobody's coming. They're just gonna let him die! Those bastards!");
            }
            model.getLog().waitForAnimationToFinish();
            subView.forceEyesClosed(true);
            leaderSay("He's... dead.");
            if (other != null) {
                partyMemberSay(other, "Murdered in cold blood. We must avenge him!");
                leaderSay("Yes. But first we need to figure out a way out of here.");
                partyMemberSay(other, "That envoy he was talking about. Do you think it could have been a servant of the Quad?");
                leaderSay("Possibly. But if she somehow managed to get " + castle.getLordName() + " to swallow one of " +
                        "crimson pearls, who's controlling him? Surely not the envoy, the Quad?");
                partyMemberSay(other, "But we destroyed the Quad!");
                leaderSay("Maybe there are more of them.");
            }
            model.getLog().waitForAnimationToFinish();
            model.setSubView(subView.getPreviousSubView());
        } else if (day == 3 && other != null) {
            leaderSay("Poor Damal...");
            partyMemberSay(other, "I think I've figured out a way out of here.");
            leaderSay("Oh yeah? Let's hear it.");
       }
    }
}
