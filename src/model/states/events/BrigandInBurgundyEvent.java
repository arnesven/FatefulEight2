package model.states.events;

import model.Model;
import model.Party;
import model.achievements.Achievement;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.items.Equipment;
import model.items.ItemDeck;
import model.items.accessories.LuckyTalisman;
import model.items.clothing.LeatherTunic;
import model.items.weapons.Rapier;
import model.items.weapons.Weapon;
import model.races.Race;
import model.ruins.DungeonMaker;
import model.ruins.DungeonRoom;
import model.ruins.FinalDungeonLevel;
import model.ruins.RuinsDungeon;
import model.ruins.objects.DungeonChest;
import model.states.CombatEvent;
import model.states.DailyEventState;
import model.states.ExploreRuinsState;
import model.states.GameState;
import util.MyRandom;
import view.combat.CombatTheme;
import view.combat.DungeonTheme;
import view.combat.MansionTheme;
import view.subviews.PortraitSubView;

import java.util.List;
import java.util.Random;

public class BrigandInBurgundyEvent extends DailyEventState {
    private static final String DEAD_KEY = "BRIGAND_DEAD";
    private static final String MET_KEY = "MET_BRIGAND";
    private static final String PORTRAIT_KEY = "BrigandInBurgundy";
    private static final String ACHIEVEMENT_1_KEY = MET_KEY + "_MANSION";
    private final GameCharacter brigand;
    private boolean breakInSuccess = false;

    public BrigandInBurgundyEvent(Model model) {
        super(model);
        CharacterAppearance portrait = model.getSavedPortrait(PORTRAIT_KEY);
        if (portrait == null) {
            Race race = Race.randomRace();
            portrait = PortraitSubView.makeRandomPortrait(Classes.BRIGAND, race, MyRandom.flipCoin());
            model.savePortrait(PORTRAIT_KEY, portrait);
        }
        String firstName = "Brigand in";
        String lastName = "Burgundy";
        brigand = new GameCharacter(firstName, lastName, portrait.getRace(), Classes.BRIGAND, portrait,
                Classes.NO_OTHER_CLASSES);
        brigand.setEquipment(new Equipment((Weapon) new Rapier().makeHigherTierCopy(1),
                new LeatherTunic(), new LuckyTalisman()));
        brigand.setLevel((int)GameState.calculateAverageLevel(model));
    }

    @Override
    public boolean exclusiveToOriginalWorld() {
        return true;
    }

    public static DailyEventState generateEvent(Model model) {
        if (model.getSettings().getMiscFlags().containsKey(DEAD_KEY)) {
            return null;
        }
        if (GameState.calculateAverageLevel(model) < 1.8) {
            return null;
        }
        if (!model.getParty().isOnRoad()) {
            return null;
        }
        int roll = MyRandom.rollD6() + MyRandom.rollD6();
        int target = hasMetBrigandBefore(model) ? 12 : 11;
        if (roll >= target) { // TODO: Add other events with the Brigand In Burgundy.
            return new BrigandInBurgundyEvent(model);
            // Horse race with Brigand In Burgundy,
            // Fencing with Brigand In Burgundys nemesis: the Black Viper.
            // The brigand has a forbidden romantic encounter with the Daughter of a notorious crime lord.
            // Finally: the brigand of burgundy retires and passes on his mask to a party member (prestige class?).
        }
        return null;
    }

    public static List<Achievement.Data> getAchievementDatas() {
        return List.of(new Achievement.Data(ACHIEVEMENT_1_KEY, "Brigand in Burgundy I",
                "You helped the Brigand in Burgundy break into a " +
                        "Brotherhood Boss's mansion and steal a valuable piece of art."));
    }

    @Override
    protected void doEvent(Model model) {
        println("There's a stone wall on one side of the road. It looks like the road is passing by some property. " +
                "Up on a hill, you can see a large mansion.");
        leaderSay("Wow... one must be rather wealthy to live here.");
        println("After walking a little further you spot a figure trying to climb over a wall. It looks rather suspicious.");
        if (hasMetBrigandBefore(model)) {
            leaderSay("Why, if it isn't the Brigand in Burgundy.");
            showExplicitPortrait(model, brigand.getAppearance(), brigand.getName());
            leaderSay("Oh, it's you!");
            leaderSay("What are you up to now?");
            portraitSay("This is the residence of a high-ranking individual in the Brotherhood. You know about the Brotherhood?");
            leaderSay("Yeah, we know about them. Are you robbing his house?");
            tellThePlan(model);
        } else {
            leaderSay("Hey there. What are you doing?");
            setMetBefore(model);
            showExplicitPortrait(model, brigand.getAppearance(), "Stranger");
            portraitSay("Oh, ha ha! You seem to have caught me red-handed!");
            leaderSay("I beg your pardon?");
            portraitSay("Ho ho. Just a little pun there. A little joke, with my name in mind.");
            leaderSay("Your name? What do you mean?");
            portraitSay("Haha, what luck! A fellow jester! Nice one! I applaud you!");
            leaderSay("Uhm... I'm afraid I really don't know who you are.");
            portraitSay("I thought it obvious from my garb! You've finally met " + himOrHer(brigand.getGender()) + ". The one and only. The...");
            println("The " + brigand.getRace().getName().toLowerCase() + " makes a theatrical pause, as if to increase the suspense.");
            portraitSay("Brigand in Burgundy!");
            removePortraitSubView(model);
            showExplicitPortrait(model, brigand.getAppearance(), brigand.getName());
            GameCharacter other;
            if (model.getParty().size() == 1) {
                other = model.getParty().getLeader();
            } else {
                other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            }
            partyMemberSay(other, "The Brigand of what now?");
            println("The Brigand seems slightly annoyed.");
            portraitSay("The Brigand in Burgundy! You haven't heard of me?");
            partyMemberSay(other, "No. Should " + iOrWe() + "?");
            portraitSay("Perhaps you know me by one of my aliases. For I am the...");
            println("Once again, the Brigand sings out the 'the', as if somebody else is announcing " +
                    himOrHer(brigand.getGender())+ " dramatically.");
            portraitSay("Crimson Lord of Crime!");
            partyMemberSay(other, "Sorry, haven't heard of " + himOrHer(brigand.getGender()) + " either.");
            portraitSay("What about the Maroon Master of Mischief?");
            partyMemberSay(other, "No.");
            portraitSay("The Rosen Robber?");
            leaderSay("No");
            portraitSay("Sanguine Sneak Thief?");
            leaderSay("Sorry, not that one either.");
            portraitSay("Well maybe you've not heard of me by name, only by my many heroic deeds!");
            partyMemberSay(other, "Perhaps. What are they?");
            println("The Brigand clears his throat, as if he's about to recite a poem.");
            portraitSay("I expose the corrupt. I protect the weak. I punish the cruel and reward the meek. " +
                    "I steal from the rich and give to the poor. " +
                    "For all those unable, I settle the score.");
            partyMemberSay(other, "Nice, it rhymes.");
            leaderSay("So you fight for the little guy. Good for you. Why are you breaking into this mansion?");
            portraitSay("This is the residence of a high-ranking individual in the Brotherhood. You know about the Brotherhood?");
            partyMemberSay(other, "Yeah, we know about them. Are you robbing his house?");
            println("The Brigand looks with suspicion on the party.");
            portraitSay("Hey! I'm no common cat burglar. I... I'm not sure I can trust you actually.");
            boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Persuade, 7);
            if (success) {
                portraitSay("Alright, I'll tell you the plan.");
                tellThePlan(model);
            } else {
                portraitSay("Actually. I was just trying to reach an apple in that tree. I think I'll be on my way now.");
                leaderSay("See you around Brigand.");
            }
        }

        if (breakInSuccess) {
            showExplicitPortrait(model, brigand.getAppearance(), brigand.getName());
            portraitSay("Haha! What chumps! They never knew what hit them.");
            leaderSay("You don't seem to worried about how the Brotherhood will react.");
            portraitSay("Those scoundrels! They'll never catch me, I'm the...");
            leaderSay("Brigand in Burgundy, yes " + iOrWe() + " know.");
            portraitSay("Uh, yes.");
            leaderSay("Well, good luck in your travels Brigand. " + iOrWeCap() + "'ll be on " + myOrOur() + " way now.");
            portraitSay("Farewell noble adventurer! Until we meet again.");
        }
    }

    private static boolean hasMetBrigandBefore(Model model) {
        return model.getSettings().getMiscFlags().containsKey(MET_KEY);
    }

    private void setMetBefore(Model model) {
        model.getSettings().getMiscFlags().put(MET_KEY, true);
    }

    private void tellThePlan(Model model) {
        portraitSay("This individual has a particularly rare and expensive piece of art " +
                "stored away in the basement. I'm going to steal it. You look like the capable sort. Do you want to help me do it?");
        print("Do you want to accompany the Brigand into the mansion (Y/N) ");
        if (yesNoInput()) {
            breakIntoMansion(model);
        } else {
            leaderSay("Sounds too risky. We'll leave you to it.");
            portraitSay("Maybe too risky for you. Not for the Brigand in Burgundy!");
            leaderSay("See you around Brigand.");
        }
    }

    private void breakIntoMansion(Model model) {
        RuinsDungeon dungeon = new RuinsDungeon(DungeonMaker.makeMansionDungeon(model, 7));

        FinalDungeonLevel finalLevel = (FinalDungeonLevel) dungeon.getLevel(dungeon.getNumberOfLevels()-1);
        finalLevel.setFinalRoom(new FinalMansionRoom());

        ExploreRuinsState explore = new ExploreMansionDungeonState(model, dungeon, "Brotherhood Mansion");
        explore.run(model);
    }

    private class ExploreMansionDungeonState extends ExploreRuinsState {
        public ExploreMansionDungeonState(Model model, RuinsDungeon dungeon, String name) {
            super(model, dungeon, name);
        }

        @Override
        public CombatTheme getCombatTheme() {
            if (getCurrentLevel() < 3) {
                return new MansionTheme();
            }
            return new DungeonTheme();
        }

        @Override
        public void modifyCombatWithMonsters(Model model, CombatEvent combat, boolean surprise) {
            if (!brigand.isDead()) {
                combat.addAllies(List.of(brigand));
            }
        }

        @Override
        public void combatPostHook(Model model, CombatEvent combat) {
            if (brigand.isDead()) {
                model.getSettings().getMiscFlags().put(DEAD_KEY, true);
                leaderSay("That was the end of the Brigand in Burgundy.");
                boolean said = randomSayIfPersonality(PersonalityTrait.cold, List.of(), "Good riddance.");
                said = said || randomSayIfPersonality(PersonalityTrait.encouraging, List.of(), "The world will mourn...");
                said = said || randomSayIfPersonality(PersonalityTrait.romantic, List.of(), "What a terrible loss!");
                leaderSay("I guess we can still keep going though.");
            } else {
                if (combat.fled()) {
                    printQuote(brigand.getName(), "What, we're giving up already?");
                    leaderSay(iOrWeCap() + " can't take any more. You can keep going if you wish.");
                    printQuote(brigand.getName(), "Aahh... better flee and fight another day perhaps.");
                } else {
                    printQuote(brigand.getName(), MyRandom.sample(List.of("Vanquished!", "Low lives!",
                            "They were no match for my skill!", "Let's push on!")));
                }
            }
        }
    }

    private class FinalMansionRoom extends DungeonRoom {

        public FinalMansionRoom() {
            super(3, 3);
            addObject(new ChestWithPieceOfArt());
        }

        @Override
        public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
            exploreRuinsState.leaderSay("We made it. The piece of art must be in there.");
            super.entryTrigger(model, exploreRuinsState);
            exploreRuinsState.print("Press enter to continue.");
            exploreRuinsState.waitForReturn();
            exploreRuinsState.setDungeonExited(true);
            exploreRuinsState.getDungeon().setCompleted(true);
        }
    }

    private class ChestWithPieceOfArt extends DungeonChest {
        public ChestWithPieceOfArt() {
            super(new Random());
        }

        @Override
        public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
            exploreRuinsState.println("You found the rare piece of art!");
            exploreRuinsState.completeAchievement(ACHIEVEMENT_1_KEY);
            if (brigand.isDead()) {
                exploreRuinsState.leaderSay("Unfortunately. We'll probably never be able to fence this. So there's no point in stealing now.");
                exploreRuinsState.println("You leave the rare piece of art where it is.");
            } else {
                exploreRuinsState.printQuote(brigand.getName(), "I'll be able to fence this once I get back to town. " +
                        "Here's your share.");
                exploreRuinsState.println("You got 150 gold!");
                model.getParty().earnGold(150);
                breakInSuccess = true;
            }
        }
    }
}
