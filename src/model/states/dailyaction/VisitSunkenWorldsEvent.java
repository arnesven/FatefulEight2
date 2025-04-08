package model.states.dailyaction;

import model.Model;
import model.characters.GameCharacter;
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
            randomCaptain = MainStorySpawnWest.CAPTAIN_NAME;
        }

        portraitSay("Your in luck newcomer. Tonight drinks are on the generous, " +
                "notorious and glorious, Captain " + randomCaptain + "!");
        leaderSay("Seriously? It's an open bar?");
        portraitSay("Yes. The captain has had a very good haul and is celebrating with anyone who call themselves pirates.");
        leaderSay("Three cheers for Captain " + randomCaptain);
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
                if (isMainStoryTriggered(model)) {
                    println("You look around for Captain " + randomCaptain + ". Finally you get an opportunity to sit down with TODO");
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
            return new LodgingState(model, true); // TODO: Special evening state where you sleep on the floor, then wake up and can recruit a special character.
        }
        return new NoLodgingState(model, false);
    }

    private boolean isMainStoryTriggered(Model model) {
        return false;
    }
}
