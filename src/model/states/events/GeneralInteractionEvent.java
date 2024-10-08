package model.states.events;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.conditions.CowardlyCondition;
import model.combat.conditions.RoutedCondition;
import model.enemies.BodyGuardEnemy;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.journal.JournalEntry;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.map.WorldHex;
import model.map.wars.KingdomWar;
import model.states.DailyEventState;
import model.states.GameState;
import model.tasks.BountyDestinationTask;
import model.tasks.DestinationTask;
import model.tasks.FatueDestinationTask;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import util.MyStrings;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class GeneralInteractionEvent extends DailyEventState {
    public static final int PICK_POCKETING_NOTORIETY = 5;
    public static final int MURDER_NOTORIETY = 50;
    public static final int PICK_POCKETING_BASE_SNEAK_DIFFICULTY = 7;
    public static final int PICK_POCKETING_BASE_SECURITY_DIFFICULTY = 6;
    public static final int ASSAULT_NOTORIETY = 10;
    private final int stealMoney;
    private final String interactText;

    public enum ProvokedStrategy {
        ALWAYS_ESCAPE,
        FIGHT_IF_ADVANTAGE,
        FIGHT_TO_DEATH
    }

    public GeneralInteractionEvent(Model model, String interactText, int stealMoney) {
        super(model);
        this.interactText = interactText;
        this.stealMoney = stealMoney;
    }

    @Override
    protected final void doEvent(Model model) {
        if (!doIntroAndContinueWithEvent(model)) {
            return;
        }
        if (darkDeedsMenu(getVictimCharacter(model),
                getVictimCompanions(model), getProvokedStrategy(), true)) {
            return;
        }
        if (partyIsCreepy(model)) {
            println("The " + getVictimCharacter(model).getName() + " refuses to deal with you!");
        } else {
            if (!doMainEventAndShowDarkDeeds(model)) {
                return;
            }
        }
        if (darkDeedsMenu(getVictimCharacter(model), getVictimCompanions(model),
                getProvokedStrategy(), false)) {
            return;
        }
        doEnding(model);
    }

    protected abstract boolean doIntroAndContinueWithEvent(Model model);
    protected abstract boolean doMainEventAndShowDarkDeeds(Model model);
    protected abstract String getVictimSelfTalk();

    protected void doEnding(Model model) {
        println("You part ways with the " + getVictimCharacter(model).getName().toLowerCase() + ".");
    }

    protected abstract GameCharacter getVictimCharacter(Model model);
    protected abstract List<Enemy> getVictimCompanions(Model model);
    protected abstract ProvokedStrategy getProvokedStrategy();

    private boolean darkDeedsMenu(GameCharacter victimChar, List<Enemy> companions,
                                  ProvokedStrategy strat, boolean withInteract) {
        String victim = getVictimCharacter(getModel()).getName().toLowerCase();
        if (!companions.isEmpty()) {
            print("The " + victim + " is traveling with ");
            println(makeCompanionString(companions) + ".");

        }
        while (true) {
            println("How would you like to interact with the " + victim + "?");
            List<String> options = new ArrayList<>(List.of("Attack " + victim));
            if (withInteract) {
                options.add(0, this.interactText + " " + victim);
            } else {
                options.add(0, "Leave " + victim);
            }
            if (getModel().getParty().size() > 1) {
                options.add("Steal from " + victim);
            }
            options.add("Make Inquiry");
            int chosen = multipleOptionArrowMenu(getModel(), 24, 25, options);
            if (chosen == 0) {
                return false;
            }
            if (chosen == 1) {
                attack(victimChar, companions, strat, true);
                return true;
            }
            if (options.get(chosen).contains("Steal")) {
                attemptPickPocket(victim, victimChar, stealMoney, companions, strat);
                return true;
            }
            if (options.get(chosen).contains("Make Inquiry")) {
                makeInquiry(getModel(), victim, victimChar);
            }
        }
    }

    private void attemptPickPocket(String victim, GameCharacter victimChar, int stealMoney,
                                   List<Enemy> companions, ProvokedStrategy strat) {
        print("Who should attempt to pick-pocket the " + victim + "?");
        getModel().getTutorial().pickPocketing(getModel());
        GameCharacter thief = getModel().getParty().partyMemberInput(getModel(),
                this, getModel().getParty().getPartyMember(0));
        SkillCheckResult result = thief.testSkill(getModel(), Skill.Sneak,
                PICK_POCKETING_BASE_SNEAK_DIFFICULTY + victimChar.getRankForSkill(Skill.Perception));
        GameCharacter decoy = getModel().getParty().getRandomPartyMember(thief);
        println("While " + decoy.getFirstName() + " distracts " + victim + ", " + thief.getFirstName() +
                " sneaks around from the back (Sneak " + result.asString() + ").");
        if (result.isSuccessful()) {
            result = thief.testSkill(getModel(), Skill.Security,
                    PICK_POCKETING_BASE_SECURITY_DIFFICULTY + victimChar.getLevel());
            println(thief.getFirstName() + " attempts to grab the " + victim +
                    "'s purse (Security " + result.asString() + ").");
            if (result.isSuccessful()) {
                GameStatistics.incrementGoldPickpocketed(stealMoney);
                println(thief.getFirstName() + " successfully pick-pocketed " + stealMoney + " gold from the " + victim + ".");
                getModel().getParty().addToGold(stealMoney);
                leaderSay("Please excuse us " + victim + ", we have pressing matters to attend.");
                portraitSay(MyRandom.sample(List.of("Oh... alright.", "I see, goodbye.",
                        "Safe travels.", "I understand, farewell")));
                println("You take leave of the " + victim + ".");
                partyMemberSay(decoy, "Nice work " + thief.getName() + ".");
                partyMemberSay(thief, MyRandom.sample(List.of("Child's play", "Easy when you know how.",
                        "Like taking candy from a baby.", heOrSheCap(victimChar.getGender()) + " never suspected a thing.",
                        "What can I say? I'm good.", "Easy money")));
                randomSayIfPersonality(PersonalityTrait.lawful, List.of(getModel().getParty().getLeader()),
                        "I feel dirty.");
                return;
            }
        }
        addToNotoriety(getModel(), this, PICK_POCKETING_NOTORIETY);
        portraitSay(MyRandom.sample(List.of("Hey, there! Keep your fingers to yourself!",
                "Stop! Thief!", "Huh? A pick-pocket? Get away from me!#")));
        if (strat == ProvokedStrategy.ALWAYS_ESCAPE) {
            print("Do you want to attack the " + victim + "? (Y/N) ");
            if (yesNoInput()) {
                attack(victimChar, companions, strat, true);
            } else {
                println("The " + victim + " is enraged and stomps off.");
            }
        } else if (strat == ProvokedStrategy.FIGHT_IF_ADVANTAGE &&
                CowardlyCondition.goodGuysHasTheAdvantage(getModel(), makeEnemyTeam(victimChar, companions),
                        getModel().getParty().getPartyMembers())) {
            portraitSay("I would fight you... but uh... Well, I just don't want to.");
            print("Do you want to attack the " + victim + "? (Y/N) ");
            if (yesNoInput()) {
                attack(victimChar, companions, strat, true);
            } else {
                println("The " + victim + " flees.");
            }
        } else {
            attack(victimChar, companions, strat, false);
        }
    }

    public static void addToNotoriety(Model model, GameState state, int notoriety) {
        state.printAlert("Your notoriety has increased by " + notoriety + "!");
        if (model.getParty().getNotoriety() == 0) {
            model.getTutorial().notoriety(model);
        }
        model.getParty().addToNotoriety(notoriety);
    }

    public static void addMurdersToNotoriety(Model model, GameState state, int numberOfMurders) {
        GameStatistics.incrementMurders(numberOfMurders);
        addToNotoriety(model, state, numberOfMurders * MURDER_NOTORIETY);
    }

    private void attack(GameCharacter victimChar, List<Enemy> companions,
                        ProvokedStrategy combStrat, boolean isAggressor) {
        if (combStrat == ProvokedStrategy.ALWAYS_ESCAPE) {
            portraitSay(MyRandom.sample(List.of("Help! I'm being attacked!",
                    "What are you doing? Please don't hurt me!",
                    "Help! Bandits are attacking me!",
                    "Stay away!")));
        } else if (combStrat == ProvokedStrategy.FIGHT_IF_ADVANTAGE) {
            portraitSay(MyRandom.sample(List.of("You want to fight? Fine, I'll teach you a lesson!",
                    "Come on then. I'll give you a proper thrashing!")));
        } else {
            portraitSay(MyRandom.sample(List.of("Now you die!",
                    "You messed with the wrong " + victimChar.getRace().getName().toLowerCase() + "!")));
        }
        List<Enemy> enemies = makeEnemyTeam(victimChar, companions);
        for (Enemy e : enemies) {
            if (combStrat == ProvokedStrategy.ALWAYS_ESCAPE) {
                e.addCondition(new RoutedCondition());
            } else if (combStrat == ProvokedStrategy.FIGHT_IF_ADVANTAGE) {
                e.addCondition(new CowardlyCondition(enemies));
            }
        }
        runCombat(enemies);
        if (getModel().getParty().isWipedOut()) {
            return;
        }
        possiblyGetHorsesAfterCombat(victimChar.getFirstName().toLowerCase(),
                getVictimCompanions(getModel()).size() - 2);
        int numberOfDead = 0;
        for (Enemy e : enemies) {
            if (e.isDead()) {
                numberOfDead++;
            }
        }
        if (numberOfDead < enemies.size()) {
            println("Some of your enemies have escaped and reported your crime to the local authorities.");
            if (numberOfDead > 0) {
                addMurdersToNotoriety(getModel(), this, numberOfDead);
            }
            if (isAggressor) {
                addToNotoriety(getModel(), this, enemies.size() - numberOfDead * ASSAULT_NOTORIETY);
            }
        } else if (getModel().getCurrentHex().getLocation() instanceof UrbanLocation) {
            println("Your crime has been witnessed and reported to the local authorities.");
            addMurdersToNotoriety(getModel(), this, numberOfDead );
        }
        randomSayIfPersonality(PersonalityTrait.lawful, List.of(getModel().getParty().getLeader()),
                "Is this what we are? Thugs who attack innocent people?");
        println("You continue on your journey.");
    }

    private List<Enemy> makeEnemyTeam(GameCharacter victimChar, List<Enemy> companions) {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new FormerPartyMemberEnemy(victimChar));
        enemies.addAll(companions);
        Collections.shuffle(enemies);
        return enemies;
    }

    protected static List<Enemy> makeBodyGuards(int number, char group) {
        List<Enemy> list = new ArrayList<>();
        for (int i = number; i > 0; --i) {
            list.add(new BodyGuardEnemy(group));
        }
        return list;
    }

    private String makeCompanionString(List<Enemy> companions) {
        Map<String, Integer> countMap = new HashMap<>();
        for (Enemy e : companions) {
            if (countMap.containsKey(e.getName())) {
                countMap.put(e.getName(), countMap.get(e.getName()) + 1);
            } else {
                countMap.put(e.getName(), 1);
            }
        }

        StringBuilder bldr = new StringBuilder();
        for (String s : countMap.keySet()) {
            bldr.append(MyStrings.numberWord(countMap.get(s)));
            bldr.append(" ");
            bldr.append(s.toLowerCase());
            if (countMap.get(s) > 1) {
                bldr.append("s");
            }
            bldr.append(", ");
        }

        String result =  bldr.substring(0, bldr.length()-2);
        int index = result.lastIndexOf(", ");
        if (index != -1) {
            result = result.substring(0, index) + " and" + result.substring(index + 1);
        }
        return result;
    }

    public static DailyEventState generateEvent(Model model, WorldHex worldHex) {
        if (model.getParty().getNotoriety() > 0) {
            if (worldHex.getLocation() != null && worldHex.getLocation() instanceof UrbanLocation) {
                if (MyRandom.rollD10() < Math.min(5, model.getParty().getNotoriety() / 10)) {
                    return new ConstableEvent(model);
                }
            }
        }
        return null;
    }

    private void askAboutBounties(Model model, GameCharacter victimChar) {
        List<BountyDestinationTask> tasks = MyLists.transform(
                MyLists.filter(model.getParty().getDestinationTasks(),
                destinationTask -> destinationTask instanceof BountyDestinationTask &&
                        ((BountyDestinationTask) destinationTask).canGetClue()),
                destinationTask -> (BountyDestinationTask) destinationTask);
        if (tasks.isEmpty()) {
            return;
        }
        for (BountyDestinationTask task : tasks) {
            MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(
                    model, this, Skill.SeekInfo, BountyDestinationTask.SEEK_INFO_DIFFICULTY);
            if (!pair.first) {
                partyMemberSay(pair.second, "Uh... do you know " + task.getBountyName() + "?");
                portraitSay("I'm afraid I don't.");
                continue;
            }
            boolean bountyGender = task.getBountyPortrait().getGender();
            String wanted = heOrSheCap(bountyGender) + " is wanted in " + task.getTurnInTown() + ".";
            boolean shown = false;
            if (MyRandom.flipCoin()) {
                println("You show the wanted poster of " + task.getBountyName() + " to " + victimChar.getFirstName() + ".");
                partyMemberSay(pair.second, "Have you ever seen this person? " + wanted);
                shown = true;
            } else {
                partyMemberSay(pair.second,
                        "Have you heard of a bandit called '" + task.getBountyName() + "'? " + wanted);
            }
            model.getWorld().dijkstrasByLand(model.getParty().getPosition(), true);
            List<Point> path = model.getWorld().shortestPathToPoint(task.getPosition());
            if (path.size() > BountyDestinationTask.LONG_RANGE) {
                portraitSay("I'm sorry but I don't know at all who you are talking about.");
                leaderSay("Darn. I guess " + heOrShe(bountyGender) + " could be hiding anywhere. " +
                        "Maybe even in different kingdom.");
            } else if (path.size() >= BountyDestinationTask.SHORT_RANGE) {
                String reply = shown ? heOrShe(bountyGender) + " looks familiar, but I can't " +
                        "tell you where I've seen " + himOrHer(bountyGender) :
                        "The name sounds familiar, but I can't tell you where I've heard it";
                portraitSay("Hmm... " + reply + ".");
                leaderSay("Perhaps we're getting close?");
            } else {
                portraitSay("Yes, I know " + himOrHer(bountyGender) + "! " +
                        heOrSheCap(bountyGender) + " is hiding out in " + task.getClue() + ".");
                task.askForClue();
                leaderSay("Thank you. You have been most helpful.");
            }
        }
    }

    private boolean hasBountyTasks(Model model) {
        for (DestinationTask dt : model.getParty().getDestinationTasks()) {
            if (dt instanceof BountyDestinationTask) {
                if (((BountyDestinationTask)dt).canGetClue()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void makeInquiry(Model model, String victim, GameCharacter victimChar) {
        CastleLocation kingdomCastle = model.getWorld().getKingdomForPosition(model.getParty().getPosition());

        while (true) {
            List<String> options = new ArrayList<>(List.of(
                    "Ask about " + himOrHer(victimChar.getGender()),
                    "Ask about region",
                    "Ask about news"));
            Map<String, MyPair<String, String>> specificTopics = makeSpecificTopics(model); // Ask about curing vampirism
            for (String s : specificTopics.keySet()) {
                options.add("Ask about " + s);
            }
            if (hasBountyTasks(getModel())) {
                options.add("Ask about bounties");
            }
            options.add("Cancel");
            int chosen = multipleOptionArrowMenu(getModel(), 24, 25, options);
            if (chosen == 0) {
                leaderSay(MyRandom.sample(List.of("Tell me about yourself.", "Who are you?",
                        "What do you do?", "I'm wondering about you. What's your story?")));
                portraitSay(getVictimSelfTalk());
            } else if (options.get(chosen).contains("bounties")) {
                askAboutBounties(getModel(), victimChar);
            } else if (options.get(chosen).contains("Cancel")) {
                break;
            } else if (options.get(chosen).contains("news")) {
                askAboutNews(getModel(), kingdomCastle);
            } else if (options.get(chosen).contains("region")) {
                leaderSay(MyRandom.sample(List.of("Uhm, where are we?", "Tell me about this region.",
                        "What kingdom is this?", "What can you tell me about these lands?")));
                if (kingdomCastle == null) {
                    portraitSay("This is the wilderness, far from the kingdoms of the world.");
                } else {
                    String kingdom = CastleLocation.placeNameToKingdom(kingdomCastle.getPlaceName());
                    portraitSay(MyRandom.sample(List.of("This is", "You are in")) + " the " + kingdom + ".");
                    if (!talkAboutFatue(model, kingdomCastle)) {
                        portraitSay(kingdomCastle.getLordName() + " rules these lands.");
                    }
                }
            } else {
                String key = options.get(chosen).replace("Ask about ", "");
                MyPair<String, String> queryAndResponse = specificTopics.get(key);
                leaderSay(queryAndResponse.first);
                portraitSay(queryAndResponse.second);
            }
        }
    }

    private void askAboutNews(Model model, CastleLocation kingdomCastle) {
        leaderSay("Got any news to share?");
        if (kingdomCastle == null) {
            portraitSay("Not much news out here. Haven't heard much in a while.");
            return;
        }
        List<KingdomWar> warsForThisKingdom = getModel().getWarHandler().getWarsForKingdom(kingdomCastle);
        if (!warsForThisKingdom.isEmpty()) {
            KingdomWar warToTalkAbout = MyRandom.sample(warsForThisKingdom);
            if (warToTalkAbout.isAggressor(kingdomCastle)) {
                String extra = MyRandom.sample(List.of("Best stay clear of any fighting, friend.",
                        "Everybody must do their part I suppose.", "Glory to our homeland!",
                        "I hope nobody expects me to fight."));
                portraitSay("The " + kingdomCastle.getLordTitle() + " of " + kingdomCastle.getPlaceName() +
                        " has declared war on " + CastleLocation.placeNameToKingdom(warToTalkAbout.getDefender()) +
                        ". " + extra);
            } else {
                portraitSay(warToTalkAbout.getAggressor() + " has declared war on this kingdom. Our " +
                        kingdomCastle.getLordTitle() + " has been mustering troops to defend our land!");
            }
            model.getTutorial().kingdomWars(model);
        } else if (!getModel().getWarHandler().getWars().isEmpty() && MyRandom.flipCoin()) {
            KingdomWar warToTalkAbout = MyRandom.sample(model.getWarHandler().getWars());
            String extra = MyRandom.sample(List.of("So much for diplomacy...", "It's apparently a very old feud.",
                    "Who do you think will prevail?", "Such meaningless suffering.",
                    "I try not to think of the horrific battles.", "At least the vultures will be pleased."));
            portraitSay("I've heard " + CastleLocation.placeNameToKingdom(warToTalkAbout.getAggressor()) +
                    " has declared war on " +
                    CastleLocation.placeNameToKingdom(warToTalkAbout.getDefender()) + ". " + extra);
            model.getTutorial().kingdomWars(model);
        } else {
            portraitSay(MyRandom.sample(List.of(
                    "I've heard " + kingdomCastle.getLordName() + " is planning to host an archery contest soon.",
                    "I've heard " + kingdomCastle.getLordName() + " is planning to host a melee tournament soon.",
                    "I've heard " + kingdomCastle.getLordName() + " is planning a horse racing cup soon.",
                    "Orcish raids seem to have become more common lately. Watch yourself friend.",
                    "Ever been to the Isle of Faith? The monks are restoring the monastery there.",
                    "I've heard there are communities of dwarves that live down in caves. I wonder what that's like.",
                    "Ships travel regularly between most coastal towns. Why walk when you can sail?")));
        }
    }

    protected boolean talkAboutFatue(Model model, CastleLocation kingdomCastle) {
        Point fatuePos = model.getCaveSystem().getFatuePosition();
        CastleLocation fatueKingdom = model.getWorld().getKingdomForPosition(fatuePos);
        if (fatueKingdom == kingdomCastle) {
            Point townOrCastlePosition = model.getWorld().getPositionForLocation(fatueKingdom);
            UrbanLocation townOrCastle = (UrbanLocation) model.getWorld().getHex(townOrCastlePosition).getLocation();
            portraitSay("I've heard there's a strange place in the caves around " + townOrCastle.getPlaceName() + ". " +
                    "From what I've heard it's some kind of ruined fortress.");
            if (!MyLists.any(model.getParty().getDestinationTasks(), (DestinationTask dt) -> dt instanceof FatueDestinationTask)) {
                model.getParty().addDestinationTask(new FatueDestinationTask(townOrCastlePosition, townOrCastle));
                JournalEntry.printJournalUpdateMessage(model);
            }
            return true;
        }
        return false;
    }

    protected Map<String, MyPair<String, String>> makeSpecificTopics(Model model) {
        return new HashMap<>();
    }

}
