package model.states.events;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.conditions.CowardlyCondition;
import model.combat.conditions.RoutedCondition;
import model.enemies.BodyGuardEnemy;
import model.enemies.Enemy;
import model.enemies.FormerPartyMemberEnemy;
import model.items.puzzletube.DwarvenPuzzleConstants;
import model.items.puzzletube.DwarvenPuzzleTube;
import model.items.puzzletube.FindPuzzleDestinationTask;
import model.items.puzzletube.MysteryOfTheTubesDestinationTask;
import model.items.spells.Spell;
import model.items.spells.TelekinesisSpell;
import model.journal.JournalEntry;
import model.journal.PartSevenStoryPart;
import model.map.*;
import model.map.wars.KingdomWar;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import model.tasks.BountyDestinationTask;
import model.tasks.Destination;
import model.tasks.DestinationTask;
import model.tasks.FatueDestinationTask;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import util.MyStrings;
import view.subviews.SimpleInputDialogSubView;
import view.subviews.SubView;

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
    private final List<Enemy> companions;
    private final boolean canAttack;

    public enum ProvokedStrategy {
        ALWAYS_ESCAPE,
        FIGHT_IF_ADVANTAGE,
        FIGHT_TO_DEATH
    }

    public GeneralInteractionEvent(Model model, String interactText, int stealMoney, boolean canAttack) {
        super(model);
        this.interactText = interactText;
        this.stealMoney = stealMoney;
        this.companions = getVictimCompanions(model);
        this.canAttack = canAttack;
    }

    public GeneralInteractionEvent(Model model, String interactText, int stealMoney) {
        this(model, interactText, stealMoney, true);
    }

    @Override
    public String getDistantDescription() {
        int groupSize = 1 + companions.size();
        if (groupSize > 7) {
            return "a large group of people";
        }
        if (groupSize == 1) {
            return "a person traveling alone";
        }
        return MyStrings.numberWord(groupSize) + " people";
    }

    @Override
    protected final void doEvent(Model model) {
        if (!doIntroAndContinueWithEvent(model)) {
            return;
        }
        model.getLog().waitForAnimationToFinish();
        if (darkDeedsMenu(getVictimCharacter(model),
                companions, getProvokedStrategy(), true)) {
            return;
        }
        if (partyIsCreepy(model)) {
            println("The " + getVictimCharacter(model).getName() + " refuses to deal with you!");
        } else {
            if (!doMainEventAndShowDarkDeeds(model)) {
                return;
            }
        }
        if (darkDeedsMenu(getVictimCharacter(model), companions,
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
            List<String> options = new ArrayList<>();
            if (canAttack) {
                options.add("Attack " + victim);
            }
            if (withInteract) {
                options.add(0, this.interactText + " " + victim);
            } else {
                options.add(0, "Leave " + victim);
            }
            if (getModel().getParty().size() > 1) {
                options.add("Steal from " + victim);
            }
            options.add("Make Inquiry");
            int chosen = multipleOptionArrowMenu(getModel(), 24, 23, options);
            if (chosen == 0) {
                return false;
            }
            if (options.get(chosen).contains("Attack")) {
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
            boolean teleUsed = tryUseTelekinesis(thief);
            if (!teleUsed) {
                result = thief.testSkill(getModel(), Skill.Security,
                        PICK_POCKETING_BASE_SECURITY_DIFFICULTY + victimChar.getLevel());
                println(thief.getFirstName() + " attempts to grab the " + victim +
                        "'s purse (Security " + result.asString() + ").");
            }
            if (teleUsed || result.isSuccessful()) {
                GameStatistics.incrementGoldStolen(stealMoney);
                println(thief.getFirstName() + " successfully pick-pocketed " + stealMoney + " gold from the " + victim + ".");
                getModel().getParty().earnGold(stealMoney);
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

    private boolean tryUseTelekinesis(GameCharacter thief) {
        Spell sp = MyLists.find(getModel().getParty().getSpells(),
                s -> s instanceof TelekinesisSpell);
        if (sp != null) {
            print("Would you like " + thief.getFirstName() + " to use " + sp.getName() +
                    " instead of Security while pick-pocketing? (Y/N) ");
            if (yesNoInput()) {
                if (sp.castYourself(getModel(), this, thief)) {
                    return true;
                }
            }
        }
        return false;
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

    public static void addAssaultsToNotoriety(Model model, GameState state, int numberOfAssaults) {
        addToNotoriety(model, state,  numberOfAssaults * ASSAULT_NOTORIETY);
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
                addAssaultsToNotoriety(getModel(), this, enemies.size() - numberOfDead);
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

    public static DailyEventState eventDependentOnNotoriety(Model model, WorldHex worldHex) {
        if (model.getParty().getNotoriety() > 0) {
            if (worldHex.getLocation() != null &&
                    (worldHex.getLocation() instanceof TownLocation ||
                     worldHex.getLocation() instanceof CastleLocation)) {
                if (model.getParty().getNotoriety() <= 50) {
                    if (MyRandom.rollD10() < model.getParty().getNotoriety() / 10) {
                        return new ConstableEvent(model);
                    }
                } else {
                    return new ConstableGroupEvent(model);
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
            if (!model.isInOriginalWorld() && !pair.first) {
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

    private void askAboutPuzzleTubes(Model model, GameCharacter victimChar) {
        println(model.getParty().getLeader().getFirstName() + " brings out a Dwarven Puzzle " +
                "Tubes and shows it to " + victimChar.getName() + ".");
        leaderSay("Ever seen anything like this?");
        if (!model.isInOriginalWorld()) {
            portraitSay("Can't say I have. Looks expensive.");
            leaderSay("Never mind then.");
            return;
        }
        boolean hasKnowledge = victimChar.getCharClass().id() == Classes.NOB.id() ||
                victimChar.getCharClass().id() == Classes.ART.id() ||
                victimChar.getCharClass().id() == Classes.MAG.id();
        boolean isDwarf = victimChar.getRace().id() == Race.DWARF.id();
        MysteryOfTheTubesDestinationTask task = (MysteryOfTheTubesDestinationTask)
                MyLists.find(model.getParty().getDestinationTasks(),
                dt -> dt instanceof MysteryOfTheTubesDestinationTask);
        boolean isInKingdom = task.isInKingdom(model);
        if (isDwarf) {
            if (isInKingdom) {
                pointToToyMakersHut(model, task);
            } else {
                pointToToyMakersKingdom(model, task);
            }
            leaderSay("Thank you. You've been helpful.");
        } else if (hasKnowledge) { // not dwarf
            if (isInKingdom) {
                pointToToyMakersKingdom(model, task);
            } else {
                pointToPuzzleTubeAtTempleOrTown(model, task);
            }
            leaderSay("Thank you. You've been helpful.");
        } else {
            portraitSay("It looks like some kind of puzzle. I can't say I've ever seen anything like it.");
            leaderSay("Never mind then.");
        }
    }

    private void pointToPuzzleTubeAtTempleOrTown(Model model, MysteryOfTheTubesDestinationTask task) {
        model.getWorld().dijkstrasByLand(model.getParty().getPosition(), true);
        List<Point> path = model.getWorld().shortestPathToNearestTownOrCastle();
        Point puzzleLoc = path.get(path.size()-1);
        if (!DwarvenPuzzleTube.locationHasPuzzleTube(puzzleLoc)) {
            path = model.getWorld().shortestPathToNearestTemple();
            puzzleLoc = path.get(path.size()-1);
        }
        HexLocation location = model.getWorld().getHex(puzzleLoc).getLocation();
        if (!DwarvenPuzzleTube.locationHasPuzzleTube(puzzleLoc)) {
            throw new IllegalStateException("Could not find a location for puzzle tube.");
        }
        Destination dest = DwarvenPuzzleTube.makeTempleOrTownDestination(puzzleLoc, location);
        portraitSay("Oh yes. That's a dwarven puzzle tube, quite the spectacular craftmanship! I've seen one before " +
                dest.getPreposition() + " " + dest.getLongDescription() + ".");
        Point finalPuzzleLoc = puzzleLoc;
        if (!MyLists.any(model.getParty().getDestinationTasks(), dt -> dt.getPosition().equals(finalPuzzleLoc))) {
            model.getParty().addDestinationTask(new FindPuzzleDestinationTask(dest));
        }
    }

    private void pointToToyMakersHut(Model model, MysteryOfTheTubesDestinationTask task) {
        portraitSay("Yes. That's a dwarven puzzle tube. They were popular a long time ago. I believe they " +
                "were made by a famous toymaker named " +  DwarvenPuzzleConstants.TOYMAKER_NAME +
                ". He used to live not far from here, but it must be well over two decades ago.");
        leaderSay("Can you mark the location on my map?");
        portraitSay("Sure. ");
        task.progressToResidenceKnown();
    }

    private void pointToToyMakersKingdom(Model model, MysteryOfTheTubesDestinationTask task) {
        portraitSay("Yes. That's a dwarven puzzle tube. They were popular a long time ago. I believe they " +
                "were made by a famous toymaker. Rumour has it he was quite the eccentric.");
        leaderSay("Do you remember his name?");
        CastleLocation kingdomForToymaker = model.getWorld().getKingdomForPosition(task.getPosition());
        portraitSay("I'm sorry no. All I remember is that he used to live in the " +
                CastleLocation.placeNameToKingdom(kingdomForToymaker.getPlaceName()) +
                ". Maybe somebody from that part of the realm could tell you more.");
        task.progressToKingdomKnown();
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
            if (canAskAboutPuzzleTubes(getModel())) {
                options.add("Ask about puzzle tubes");
            }
            options.add("Ask custom topic");
            options.add("Cancel");
            int chosen = multipleOptionArrowMenu(getModel(), 24, 25, options);
            if (chosen == 0) {
                leaderSay(MyRandom.sample(List.of("Tell me about yourself.", "Who are you?",
                        "What do you do?", "I'm wondering about you. What's your story?")));
                portraitSay(getVictimSelfTalk());
            } else if (options.get(chosen).contains("bounties")) {
                askAboutBounties(getModel(), victimChar);
            } else if (options.get(chosen).contains("puzzle tubes")) {
                askAboutPuzzleTubes(getModel(), victimChar);
            } else if (options.get(chosen).contains("custom topic")) {
                askAboutCustomTopic(getModel(), victimChar);
            } else if (options.get(chosen).contains("Cancel")) {
                break;
            } else if (options.get(chosen).contains("news")) {
                askAboutNews(getModel(), kingdomCastle);
            } else if (options.get(chosen).contains("region")) {
                leaderSay(MyRandom.sample(List.of("Uhm, where are we?", "Tell me about this region.",
                        "What kingdom is this?", "What can you tell me about these lands?")));
                if (kingdomCastle == null) {
                    if (model.isInPastWorld()) {
                        if (model.getMainStory().getStoryParts().size() >= 9) {
                            PartSevenStoryPart partSeven = (PartSevenStoryPart) model.getMainStory().getStoryParts().get(8);
                            partSeven.askAboutRegion(model, this, getPortraitSubView());
                            return;
                        }
                    }
                    portraitSay(getRegionReply());
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
                specificTopicHook(model, queryAndResponse);
            }
        }
    }

    protected void specificTopicHook(Model model, MyPair<String, String> queryAndResponse) {
        leaderSay(queryAndResponse.first);
        portraitSay(queryAndResponse.second);
    }

    protected String getRegionReply() {
        return "This is the wilderness, far from the kingdoms of the world.";
    }

    private void askAboutCustomTopic(Model model, GameCharacter victim) {
        print("What topic would you like to ask about?");
        SubView sub = model.getSubView();
        SimpleInputDialogSubView inputDialog = new SimpleInputDialogSubView(sub, 25, "Enter a topic:");
        model.setSubView(inputDialog);
        waitForReturn();
        model.setSubView(sub);
        String topic = inputDialog.getInput();
        leaderSay(MyRandom.sample(List.of("Tell me what you know about " + topic + ".",
                "Know anything about " + topic + "?",
                "Can you tell me anything about " + topic + "?")));
        String answer = getCustomTopicReply(model, victim, topic);
        if (answer == null) {
            portraitSay("I'm sorry, but I don't know anything about " + topic + ".");
        } else {
            portraitSay(answer);
        }
    }

    protected String getCustomTopicReply(Model model, GameCharacter victim, String topic) {
        return GeneralInteractionConversations.getReplyFor(model, victim, topic);
    }

    private boolean canAskAboutPuzzleTubes(Model model) {
        return MyLists.any(model.getParty().getInventory().getBooks(), ri -> ri instanceof DwarvenPuzzleTube) &&
                MyLists.any(model.getParty().getDestinationTasks(), dt -> dt instanceof MysteryOfTheTubesDestinationTask);
    }

    private void askAboutNews(Model model, CastleLocation kingdomCastle) {
        leaderSay("Got any news to share?");
        if (kingdomCastle == null) {
            if (model.isInPastWorld()) {
                portraitSay(MyRandom.sample(List.of("The Quadrificus' Goons are everywhere. Harassing everybody. Thing's have never been this bad.",
                        "Things are bad since the Quadrificus took power.", "People are starving. The land is suffering. Things are bad.",
                        "Some people are resisting the Quadrificus. Some brave people!")));
                return;
            }
            portraitSay(getOutsideOfKingdomNews());
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
                    "I've heard " + kingdomCastle.getLordName() + " is planning a magic dueling contest soon.",
                    "Orcish raids seem to have become more common lately. Watch yourself friend.",
                    "Ever been to the Isle of Faith? The monks are restoring the monastery there.",
                    "I've heard there are communities of dwarves that live down in caves. I wonder what that's like.",
                    "Ships travel regularly between most coastal towns. Why walk when you can sail?")));
        }
    }

    protected String getOutsideOfKingdomNews() {
        return "Not much news out here. Haven't heard much in a while.";
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
