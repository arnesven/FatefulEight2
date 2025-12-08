package model.states.dailyaction;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.classes.SpecificArtisanClass;
import model.combat.conditions.IntoxicatedCondition;
import model.items.Item;
import model.items.potions.BeerPotion;
import model.items.potions.IntoxicatingPotion;
import model.items.potions.RumPotion;
import model.items.potions.WinePotion;
import model.journal.MainStorySpawnWest;
import model.journal.PartSixStoryPart;
import model.journal.StoryPart;
import model.mainstory.GainSupportOfPiratesTask;
import model.map.CastleLocation;
import model.map.WorldBuilder;
import model.map.locations.SunblazeCastle;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.GameState;
import model.states.NoLodgingState;
import model.states.cardgames.CardGameState;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.subviews.CollapsingTransition;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.util.List;

public class VisitSunkenWorldsEvent extends DailyEventState {
    private static final List<String> CAPTAIN_NAMES = List.of("Bluebeard", "Long Joan Timber",
            "Millie the Kidd", "Descarte the Grim", "Jauque Eagle");
    private boolean allPassedOut;

    public VisitSunkenWorldsEvent(Model model) {
        super(model);
        this.allPassedOut = false;
    }

    @Override
    protected void doEvent(Model model) {
        SubView alternativeSubView = new ImageSubView("theinnalt","EVENT", "The Sunken Worlds");
        CollapsingTransition.transition(model, alternativeSubView);
        println("You step into the pirate bar, the Sunken Worlds. This is, by all accounts, a complete dive, and yet " +
                "there is a mysterious ambiance that draws you in. There are all kinds of pirates and sailors here, " +
                "some talking, some singing, some gambling, all of them drinking.");
        showRandomPortrait(model, new SpecificArtisanClass(MyColors.BEIGE, MyColors.BLACK), "Bartender");

        String randomCaptain = MyRandom.sample(CAPTAIN_NAMES);
        if (isMainStoryTriggered(model)) {
            randomCaptain = GainSupportOfPiratesTask.CAPTAIN_NAME;
        }

        portraitSay("You're in luck newcomer. Tonight drinks are on the generous, " +
                "notorious and glorious, Captain " + randomCaptain + "!");
        leaderSay("Seriously? It's an open bar?");
        portraitSay("Yes. The captain has had a very good haul and is celebrating with anyone who call themselves pirates.");
        leaderSay("Three cheers for Captain " + randomCaptain + "!");
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);

        do {
            println("What would you like to do?");
            int choice = multipleOptionArrowMenu(model, 24, 24, List.of("Have a drink", "Do some gambling", "Join in the singing",
                    "Look for Captain", "Leave"));
            if (choice == 0) {
                println("Who should get a drink from the bar?");
                GameCharacter gc = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
                getDrinkFromBar(model, gc);
                if (gc.hasCondition(IntoxicatedCondition.class)) {
                    println(gc.getName() + " is getting quite drunk.");
                    SkillCheckResult result = gc.testSkill(model, Skill.Endurance, 8);
                    if (result.isFailure()) {
                        println(gc.getName() + " passes out.");
                        if (model.getParty().getBench().size() == model.getParty().size() - 1) {
                            allPassedOut = true;
                            break;
                        } else {
                            model.getParty().benchPartyMembers(List.of(gc));
                            if (model.getParty().getLeader() == gc) {
                                GameCharacter nonBench = MyLists.filter(model.getParty().getPartyMembers(),
                                        p -> !model.getParty().getBench().contains(p)).get(0);
                                model.getParty().setLeader(nonBench);
                                println(nonBench.getFirstName() + " is now the leader of the party.");
                            }
                        }
                    }
                }
            } else if (choice == 1) {
                new CardGameState(model).run(model);
                CollapsingTransition.transition(model, alternativeSubView);
            } else if (choice == 2) {
                MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Entertain, 7);
                if (pair.first) {
                    println(pair.second.getFirstName() + " sings loudly and well, and is cheered on by the patrons of the Sunken World. Afterwards " +
                            "applause and some coins come " + hisOrHer(pair.second.getGender()) + " way.");
                    int obols = MyRandom.randInt(3, 20);
                    println("You gain " + obols + " obols.");
                    model.getParty().addToObols(obols);
                    partyMemberSay(pair.second, "Phew, that got me thirsty. I need a drink from the bar.");
                    getDrinkFromBar(model, pair.second);
                } else {
                    println(pair.second.getFirstName() + " sings terribly and is booed and shushed.");
                }
            } else if (choice == 3) {
                // TODO: If blackbone already met.
                if (isMainStoryTriggered(model)) {
                    CharacterAppearance app = getGainSupportTask(model).getCaptainAppearance();
                    println("You look around for Captain " + randomCaptain + ". Finally you get an opportunity to sit down with " + himOrHer(app.getGender()) + ".");
                    showExplicitPortrait(model, app, "Captain " + GainSupportOfPiratesTask.CAPTAIN_NAME);
                    portraitSay("I know you! You're the leader of that wanted gang.");
                    leaderSay("What?");
                    SunblazeCastle sunblaze = new SunblazeCastle();
                    portraitSay("Yes. I've heard all of " + CastleLocation.placeNameShort(sunblaze.getPlaceName()) +
                            " is looking for you. What did you do to piss off " + sunblaze.getLordName() + "?");
                    leaderSay("Oh, nothing really. " + sunblaze.getLordName() + " isn't quite " + himOrHer(sunblaze.getLordGender()) + "self these days.");
                    portraitSay("I'll say. That whole kingdom is coming to pieces. But the world has gone pretty crazy in these parts too.");
                    leaderSay("Crazy? What do you mean?");
                    portraitSay("There's been a huge surge in orcish activity. We used to encounter orcish pirates every once in a while. But now, " +
                            "you can barely leave port without bumping into an orc ship. And they're way better equipped than they used to be. They're more " +
                            "like war ships than pirate raiding parties.");
                    leaderSay("They are war ships. Orc armies have been deployed to the western shores of " + CastleLocation.placeNameShort(sunblaze.getPlaceName()) +
                            ", and more recently I've heard orc attacks have spread to other kingdoms as well.");
                    portraitSay("Blasted greenskins. It's not good for business you know. I was lucky I got a good haul this time. " +
                            "But what have got the orcs so riled up?");
                    leaderSay("Actually, " + iOrWe() + " have been investigating the matter. Or " + iWasOrWeWere() + ", until " +
                            sunblaze.getLordName() + " turned on us. " + iOrWeCap() + " think there is " +
                            "a connection between " + hisOrHer(sunblaze.getLordGender()) + " sudden despotic behavior and the orcish uprising.");
                    portraitSay("How intriguing!");
                    leaderSay("Yes. The theory is that " + sunblaze.getLordName() + " has been put under some kind " +
                            "of spell by a cabal of ancient evil sorcerers, called the Quad.");
                    portraitSay("The Quad? I think I've heard of that. Thought it just an old wives tale. So... what do you intend to do?");
                    leaderSay(iOrWeCap() + " have to get back to " + sunblaze.getPlaceName() + ". It's our only lead. And in any case, we can't allow " +
                            sunblaze.getLordName() + " to continue his mad rule. In fact, " + iOrWe() + "'ve been looking for an influential pirate lord like yourself to " +
                            "rally the pirate captains in support of this cause.");
                    portraitSay("Now wait just a minute...");
                    leaderSay("Captain " + GainSupportOfPiratesTask.CAPTAIN_NAME + ", we need your help!");
                    portraitSay("And you thought you could convince a notorious pirate to aid you for the greater good? What's in it for me?");
                    leaderSay("You said yourself that the current state of things was bad for business. If you help us overthrow " + sunblaze.getLordName() +
                            ", the orcish activity will stop.");
                    portraitSay("You don't really know that for sure.");
                    leaderSay("No, I don't. But the way I see it, you have two choices. Either, you can sit here and enjoy your last big score, " +
                            "knowing that these lands will soon be overrun with " + sunblaze.getLordName() + "'s forces, rampaging orcs, or something worse. " +
                            "Or you can join me, and go down in history as the Pirate Lord who stepped up to the challenge and fought for freedom and justice!");
                    portraitSay("That does sound pretty good.... Okay you've convinced me, but there's a problem.");
                    leaderSay("What is it?");
                    portraitSay("I've had a good run. But after a few close calls with those orcish sea scum, I've had to hire many new hands. " +
                            "Most are good lads and lasses, but the other night, my first mate overheard a worrying conversation " +
                            "here at the Sunken Worlds. There's a mutineer on my crew! I've narrowed it down to eight, but I need help figuring out who it is.");
                    leaderSay("Seems easy enough.");
                    portraitSay("It would be, for somebody acting like their part of the crew. Join my crew for a while. Find out who the mutineer is! " +
                            "Then I promise I'll pull strings, gather every captain I know, and put together a force to be reckoned with!");
                    leaderSay("Sounds fair! When do I start?");
                    portraitSay("As soon as you like. But tonight, let's celebrate!");
                    leaderSay("Can't argue with that. My thanks " + GainSupportOfPiratesTask.CAPTAIN_NAME + ".");
                    getGainSupportTask(model).setBlackboneMet(true);
                    removePortraitSubView(model);
                } else {
                    println("You look around for Captain " + randomCaptain + " but can only catch a glimpse, as the notorious pirate is " +
                            "a very popular person tonight.");
                }
            } else {
                break;
            }
        } while (true);
        model.getParty().unbenchAll();
        if (allPassedOut) {
            println("Your entire party has passed out at the Sunken World. You spend the night on the floor, sleeping off your stupor.");
        } else {
            println("You leave the Sunken Worlds.");
        }
    }

    private GainSupportOfPiratesTask getGainSupportTask(Model model) {
        return ((GainSupportOfPiratesTask)getStoryPartSix(model).getRemotePeopleTask());
    }

    private PartSixStoryPart getStoryPartSix(Model model) {
        List<StoryPart> storyParts = model.getMainStory().getStoryParts();
        StoryPart lastStoryPart = storyParts.get(storyParts.size() - 1);
        return (PartSixStoryPart) lastStoryPart;
    }

    private void getDrinkFromBar(Model model, GameCharacter gc) {
        List<IntoxicatingPotion> drinks = List.of(new BeerPotion(), new WinePotion(), new RumPotion());
        int drinkType = multipleOptionArrowMenu(model, 24, 24, MyLists.transform(drinks, Item::getName));
        println(gc.getFirstName() + " is served a " + drinks.get(drinkType).getTypicalGlass() +
                " of " + drinks.get(drinkType).getName() + ".");
        String result = drinks.get(drinkType).useYourself(model, gc);
        println(result);
    }

    @Override
    protected GameState getEveningState(Model model) {
        if (allPassedOut) {
            return new LodgingState(model, true); // FEATURE: Special evening state where you sleep on the floor, then wake up and can recruit a special character.
        }
        return new NoLodgingState(model, false);
    }

    private boolean isMainStoryTriggered(Model model) {
        if (model.getMainStory().getExpandDirection() == WorldBuilder.EXPAND_WEST) {
            List<StoryPart> storyParts = model.getMainStory().getStoryParts();
            StoryPart lastStoryPart = storyParts.get(storyParts.size() - 1);
            if (lastStoryPart instanceof PartSixStoryPart) {
                PartSixStoryPart partSix = (PartSixStoryPart) lastStoryPart;
                if (partSix.witchTalkedTo()) {
                    return !partSix.getRemotePeopleTask().isCompleted();
                }
            }
        }
        return false;
    }
}
