package model;

import model.actions.DailyAction;
import model.actions.Loan;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.CombatLoot;
import model.combat.Combatant;
import model.horses.HorseHandler;
import model.items.Inventory;
import model.items.Lockpick;
import model.items.spells.*;
import model.map.UrbanLocation;
import model.states.GameState;
import model.states.SpellCastException;
import sound.SoundEffects;
import util.MyLists;
import view.sprites.CombatCursorSprite;
import util.MyPair;
import util.MyRandom;
import view.*;
import view.sprites.*;
import view.subviews.SelectPartyMemberSubView;
import view.subviews.SubView;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class Party implements Serializable {
    private static MyColors[] partyMemberColors = new MyColors[]{
                MyColors.LIGHT_BLUE, MyColors.GREEN, MyColors.LIGHT_RED,
                MyColors.LIGHT_YELLOW, MyColors.PURPLE, MyColors.PEACH,
                MyColors.RED, MyColors.CYAN};
    private final Inventory inventory = new Inventory();
    private final List<GameCharacter> partyMembers = new ArrayList<>();
    private final List<GameCharacter> frontRow = new ArrayList<>();
    private final List<GameCharacter> backRow = new ArrayList<>();
    private final List<GameCharacter> bench = new ArrayList<>();
    private final List<MyPair<Point, TimedAnimationSprite>> callouts = new ArrayList<>();
    private final Map<String, Summon> summons = new HashMap<>();
    private final Set<String> templeBannings = new HashSet<>();
    private Point position;
    private Point previousPosition;
    private int gold = 30;
    private int obols = 0;
    private int reputation = 0;
    private boolean onRoad = true;
    private final LoopingSprite[] cursorSprites;
    private GameCharacter leader;
    private int lastSuccessfulRecruitDay = -500;
    private final Set<String> specialCharactersRecruited = new HashSet<>();
    private Loan currentLoan = null;
    private HorseHandler horseHandler = new HorseHandler();
    private List<GameCharacter> recruitmentPersistence = null;
    private boolean seminarHeld = false;
    private int notoriety = 0;
    private int carryingCapInKilos = 0;

    public Party() {
        position = new Point(26, 19);
        cursorSprites = makeCursorSprites();
    }

    private LoopingSprite[] makeCursorSprites() {
        LoopingSprite[] result = new LoopingSprite[8];
        int i = 0;
        for (MyColors col : partyMemberColors) {
            result[i++] = new CombatCursorSprite(col);
        }
        return result;
    }

    public synchronized void add(GameCharacter gameCharacter) {
        if (leader == null) {
            leader = gameCharacter;
        }
        for (GameCharacter other : partyMembers) {
            setInitialAttitude(gameCharacter, other);
            setInitialAttitude(other, gameCharacter);
        }
        partyMembers.add(gameCharacter);
        gameCharacter.setParty(this);
        carryingCapInKilos += gameCharacter.getRace().getCarryingCapacity();
        if (gameCharacter.getCharClass().isBackRowCombatant()) {
            backRow.add(gameCharacter);
        } else {
            frontRow.add(gameCharacter);
        }
    }

    private void setInitialAttitude(GameCharacter gameCharacter, GameCharacter other) {
        int attitude = gameCharacter.getRace().getInitialAttitudeFor(other.getRace());
        if (other == leader && attitude < 0) {
            attitude = 0;
        }
        System.out.println("Initial attitude for " + gameCharacter.getFirstName() +
                " vs " + other.getName() + " is " + attitude);
        gameCharacter.addToAttitude(other, attitude);
    }

    public synchronized void drawYourself(ScreenHandler screenHandler) {
        int count = 0;
        for (GameCharacter gc : partyMembers) {
            Point p = getLocationForPartyMember(count);
            gc.drawYourself(screenHandler, p.x, p.y, partyMemberColors[count]);
            count++;
        }
        List<MyPair<Point, TimedAnimationSprite>> toRemove = new ArrayList<>();
        for (MyPair<Point, TimedAnimationSprite> p : callouts) {
            if (!p.second.isDone()) {
                screenHandler.register(p.second.getName(), p.first, p.second, 2);
            } else {
                toRemove.add(p);
            }
        }
        callouts.removeAll(toRemove);
    }

    public Point getLocationForPartyMember(int count) {
        int x = count < 4 ? 0 : DrawingArea.WINDOW_COLUMNS - BorderFrame.CHARACTER_WINDOW_COLUMNS;
        int y = count < 4 ? 2 + count*11 : 2 + (count-4)*11;
        return new Point(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public void move(Model model, int dx, int dy) {
        this.previousPosition = new Point(position);
        model.getWorld().move(position, dx, dy);
    }

    public void setPosition(Point newPosition) {
        this.previousPosition = new Point(position);
        this.position = new Point(newPosition);
    }

    public int getGold() {
        return gold;
    }

    public int getReputation() {
        return reputation;
    }

    public int getFood() {
        return inventory.getFood();
    }

    public List<DailyAction> getDailyAction(Model model) {
        return model.getCurrentHex().getDailyActions(model);
    }

    public int size() {
        return partyMembers.size();
    }

    private void allRecoverHp(int i) {
        partyMembers.forEach(gameCharacter -> gameCharacter.addToHP(i));
    }

    private void allRecoverSP(int i) {
        partyMembers.forEach(gameCharacter -> gameCharacter.addToSP(i));
    }

    public boolean isOnRoad() {
        return onRoad;
    }

    public void setOnRoad(boolean b) {
        this.onRoad = b;
    }

    public GameCharacter getPartyMember(int i) {
        return partyMembers.get(i);
    }

    public boolean isWipedOut() {
        return MyLists.all(partyMembers, (GameCharacter gc) -> gc.getHP() <= 0);
    }

    public List<GameCharacter> getPartyMembers() {
        return partyMembers;
    }

    public Sprite getInitiativeSymbol(Combatant combatant) {
        return CharSprite.make((char)0x00, MyColors.WHITE, getColorForPartyMember(combatant), MyColors.BLACK);
    }

    public MyColors getColorForPartyMember(Combatant combatant) {
        return partyMemberColors[partyMembers.indexOf(combatant)];
    }

    public List<GameCharacter> getFrontRow() {
        return frontRow;
    }

    public List<GameCharacter> getBackRow() {
        return backRow;
    }

    public Sprite getCursorForPartyMember(GameCharacter partyMember) {
        return cursorSprites[partyMembers.indexOf(partyMember)];
    }

    public GameCharacter getLeader() {
        return leader;
    }

    public void setLeader(GameCharacter gc) {
        if (gc != null && gc.isDead()) {
            throw new IllegalStateException("Should not set a leader that is dead!");
        }
        if (gc != null && !partyMembers.contains(gc)) {
            throw new IllegalStateException("Set a leader which was not part of the party.");
        }
        this.leader = gc;
    }

    public void toggleFormationFor(GameCharacter combatant) {
        if (frontRow.contains(combatant)) {
            frontRow.remove(combatant);
            backRow.add(combatant);
        } else {
            backRow.remove(combatant);
            frontRow.add(combatant);
        }
    }

    public int livingCharactersInFrontRow() {
        return MyLists.filter(getFrontRow(), (GameCharacter gc) -> !gc.isDead()).size();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean isFull() {
        return partyMembers.size() == 8;
    }

    public void addToGold(int cost) {
        gold += cost;
    }

    public int partyStrength() {
        return MyLists.intAccumulate(partyMembers, GameCharacter::getCharacterStrength);
    }

    public int getLastSuccessfulRecruitDay() {
        return lastSuccessfulRecruitDay;
    }

    public void recruit(GameCharacter gc, int day) {
        add(gc);
        lastSuccessfulRecruitDay = day;
    }

    public void consumeRations(boolean forFree) {
        allRecoverHp(1);
        if (!forFree) {
            addToFood(-size());
        }
    }

    public void consumeRations() {
        consumeRations(false);
    }

    public void lodging(int cost) {
        allRecoverHp(2);
        allRecoverSP(1);
        addToGold(-1 * cost);
    }

    public List<GameCharacter> getMembersEligibleFor(CharacterClass charClass) {
        return MyLists.filter(partyMembers, (GameCharacter gc) ->
            gc.getCharClass().id() != charClass.id() && gc.canAssumeClass(charClass.id()));
    }

    public boolean appointNewLeader() {
        int max = -1;
        GameCharacter best = null;
        for (GameCharacter gc : partyMembers) {
            if (!gc.isDead() && gc.getRankForSkill(Skill.Leadership) > max) {
                best = gc;
                max = gc.getRankForSkill(Skill.Leadership);
            }
        }
        setLeader(best);
        return best != null;
    }

    public void partyMemberSay(Model model, GameCharacter gc, String text) {
        model.getLog().waitForAnimationToFinish();
        MyPair<Integer, String> pair = CalloutSprite.getSpriteNumForText(text);
        model.getLog().addAnimated(gc.getName() + ": " + LogView.YELLOW_COLOR + "\"" + pair.second +
                "\"\n" + LogView.DEFAULT_COLOR);
        int index = partyMembers.indexOf(gc);
        Point p = getLocationForPartyMember(index);
        p.x += 3;
        p.y += 2;
        CalloutSprite spr = new CalloutSprite(pair.first);
        addCallout(p, spr);
    }

    private synchronized void addCallout(Point p, CalloutSprite spr) {
        callouts.removeIf((MyPair<Point, TimedAnimationSprite> pa) -> pa.first.x == p.x && pa.first.y == p.y);
        callouts.add(new MyPair<>(p, spr));
    }

    public void partyMemberSay(Model model, GameCharacter gc, List<String> alternatives) {
        partyMemberSay(model, gc, MyRandom.sample(alternatives));
    }

    public void addToFood(int i) {
        inventory.setFood(Math.max(0, inventory.getFood() + i));
    }

    public boolean hasSummon(UrbanLocation destination) {
        return summons.containsKey(destination.getPlaceName());
    }

    public void addSummon(UrbanLocation destination) {
        if (!hasSummon(destination)) {
            this.summons.put(destination.getPlaceName(), new Summon());
        }
    }

    public void giveXP(Model model, GameCharacter gc, int xp) {
        if (gc.getLevel() == 0) {
            return;
        }
        System.out.println(gc.getName() + " got " + xp + " XP.");
        boolean levelUp = false;
        if (gc.getXpToNextLevel() <= xp) {
            levelUp = true;
            partyMemberSay(model, gc, List.of("I am learning every day.^",
                    "Experience is its own reward.^",
                    "Faster, better, stronger, harder.^",
                    "My skills are improving!^",
                    "Just look at me now!^",
                    "Advancement!^",
                    "I think I'm getting the hang of this.^",
                    "I'm good, there's just no denying it.^"));
            SoundEffects.playSound("levelup");
            if (model.getSettings().levelUpSummaryEnabled()) {
                model.transitionToDialog(new LevelUpSummaryView(model, gc));
            } else {
                model.getLog().addAnimated(gc.getName() + " has advanced to level " + (gc.getLevel() + 1) + "!\n");
            }

        }
        gc.addToXP(xp);
        if (levelUp) {
            gc.addToHP(1000);
            gc.addToSP(1000);
        }
    }

    private GameCharacter findBestPerformer(Skill skill, List<GameCharacter> performers) {
        int most = -1;
        GameCharacter best = null;
        for (GameCharacter gc : performers) {
            if (gc.getRankForSkill(skill) > most && !getBench().contains(gc)) {
                best = gc;
                most = gc.getRankForSkill(skill);
            }
        }
        return best;
    }

    private GameCharacter findBestPerformer(Skill skill) {
        return findBestPerformer(skill, partyMembers);
    }

    public MyPair<Boolean, GameCharacter> doSoloSkillCheckWithPerformer(Model model, GameState event, Skill skill, int difficulty) {
        GameCharacter performer = null;
        while (true) {
            model.getSpellHandler().acceptSkillBoostingSpells(model.getParty(), skill);
            try {
                if (size() > 1) {
                    GameCharacter best = findBestPerformer(skill);
                    event.print("Which party member should perform the Solo " + skill.getName() + " " + difficulty + " check?");
                    event.print(" (Recommended " + best.getName() + "): ");
                    model.getTutorial().skillChecks(model);
                    performer = partyMemberInput(model, event, best);
                } else {
                    performer = partyMembers.get(0);
                }
                break;
            } catch (SpellCastException spe) {
                System.out.println("Solo skill check interrupted by spell " + spe.getSpell().getName());
                if (spe.getSpell() instanceof SkillBoostingSpell) {
                    spe.getSpell().castYourself(model, event, spe.getCaster());
                }
            }
        }
        model.getSpellHandler().unacceptSkillBoostingSpells(skill);

        boolean before = MyRandom.randInt(2) == 0;
        if (before && size() > 1) {
            partyMemberSay(model, performer, List.of("Leave it to me!", "Shouldn't be too hard.", "I think I can do it.",
                    "I'll do my best.", "You can count on me.", "I'll give it my all.", "I'll give it a try.",
                    "Time to roll up my sleeves."));
        }
        SkillCheckResult result = doSkillCheckWithReRoll(model, event, performer, skill, difficulty, 20, 0);
        if (!before) {
            if (result.isSuccessful()) {
                partyMemberSay(model, performer, List.of("Piece of cake!", "All done.", "No trouble at all.",
                        "That was fun!", "Child's play!", "I rock!"));
                if (performer != getLeader()) {
                    performer.addToAttitude(getLeader(), 1);
                }
            } else {
                partyMemberSay(model, performer, List.of("Sorry!", "Nope, can't do it.", "Aaaagh!#", "Phooey",
                        "Well, I tried.", "What, I failed?", "Darn it!"));
                if (performer != getLeader()) {
                    performer.addToAttitude(getLeader(), -1);
                }
            }
        }
        return new MyPair<>(result.isSuccessful(), performer);
    }

    public boolean doSoloSkillCheck(Model model, GameState event, Skill skill, int difficulty) {
        return doSoloSkillCheckWithPerformer(model, event, skill, difficulty).first;
    }

    public boolean doCollaborativeSkillCheck(Model model, GameState event, Skill skill, int difficulty, List<GameCharacter> performers) {
        GameCharacter performer = null;
        while (true) {
            model.getSpellHandler().acceptSkillBoostingSpells(model.getParty(), skill);
            try {
                if (size() > 1) {
                    GameCharacter best = findBestPerformer(skill, performers);
                    event.print("Which party member should be the primary performer of the Collaborative " + skill.getName() + " " + difficulty + " check?");
                    event.print(" (Recommended " + best.getName() + "): ");
                    model.getTutorial().skillChecks(model);
                    do {
                        performer = partyMemberInput(model, event, best);
                    } while (!performers.contains(performer));
                } else {
                    performer = performers.get(0);
                }
                break;
            } catch (SpellCastException spe) {
                if (spe.getSpell() instanceof SkillBoostingSpell) {
                    spe.getSpell().castYourself(model, event, spe.getCaster());
                }
            }
        }
        model.getSpellHandler().unacceptSkillBoostingSpells(skill);

        int bonus = 0;
        for (GameCharacter gc : performers) {
            if (!bench.contains(gc)) {
                if (gc != performer) {
                    SkillCheckResult assistResult = gc.testSkill(skill, getCollaborativeDifficulty(performer, gc));
                    if (assistResult.isSuccessful()) {
                        giveXP(model, gc, 5);
                        event.println(gc.getName() + " helps out (" + assistResult.asString() + ").");
                        bonus++;
                    } else {
                        event.println(gc.getName() + " fumbles (" + assistResult.asString() + ").");
                    }
                }
            }
        }
        SkillCheckResult result = doSkillCheckWithReRoll(model, event, performer, skill, difficulty, 15, bonus);
        if (result.isSuccessful() && size() > 1) {
            partyMemberSay(model, getLeader(), List.of("We did it!", "Good job team!", "We're great!", "Alright!",
                    "You guys are awesome!3", "Spectacular!", "Phenomenal!", "Outstanding!", "Huzzah!"));
        } else if (size() > 1) {
            partyMemberSay(model, getLeader(), List.of("...", "That could have gone better", "Shoot!#",
                    "Come on, we have to do better.", "Well, better luck next time.", "Damn, so close!"));
        }
        return result.isSuccessful();
    }

    private int getCollaborativeDifficulty(GameCharacter mainPerformer, GameCharacter helper) {
        int attitude = helper.getAttitude(mainPerformer);
        if (attitude < -10) {
            return 9;
        }
        if (attitude < -1) {
            return 8;
        }
        if (attitude > 10) {
            return 5;
        }
        if (attitude > 1) {
            return 6;
        }
        return 7;
    }

    public boolean doCollaborativeSkillCheck(Model model, GameState event, Skill skill, int difficulty) {
        return doCollaborativeSkillCheck(model, event, skill, difficulty, partyMembers);
    }

    public boolean doCollaborativeSkillCheckExcluding(Model model, GameState event, Skill skill, int difficulty, List<GameCharacter> excluding) {
        List<GameCharacter> remaining = new ArrayList<>();
        remaining.addAll(partyMembers);
        remaining.removeAll(excluding);
        return doCollaborativeSkillCheck(model, event, skill, difficulty, remaining);
    }

    public List<GameCharacter> doCollectiveSkillCheckWithFailers(Model model, GameState event, Skill skill, int difficulty) {
        event.print("Preparing to perform a Collective " + skill.getName() + " " + difficulty + " check. Press enter.");
        model.getTutorial().skillChecks(model);
        while (true) {
            model.getSpellHandler().acceptSkillBoostingSpells(model.getParty(), skill);
            try {
                event.waitForReturn(true);
                break;
            } catch (SpellCastException spe) {
                if (spe.getSpell() instanceof SkillBoostingSpell) {
                    spe.getSpell().castYourself(model, event, spe.getCaster());
                }
            }
        }
        model.getSpellHandler().unacceptSkillBoostingSpells(skill);
        List<GameCharacter> failers = new ArrayList<>();
        for (GameCharacter gc : partyMembers) {
            if (!bench.contains(gc)) {
                SkillCheckResult individualResult = doSkillCheckWithReRoll(model, event, gc, skill, difficulty, 10, 0);
                if (!individualResult.isSuccessful()) {
                    failers.add(gc);
                }
            }
        }
        if (failers.isEmpty()) {
            event.println("Each party member successfully completed the skill check!");
        } else {
            event.println("The collective skill check has failed.");
        }
        return failers;
    }

    public boolean doCollectiveSkillCheck(Model model, GameState event, Skill skill, int difficulty) {
        return doCollectiveSkillCheckWithFailers(model, event, skill, difficulty).isEmpty();
    }

    public SkillCheckResult doSkillCheckWithReRoll(Model model, GameState event, GameCharacter performer, Skill skill, int difficulty, int exp, int bonus) {
        SkillCheckResult result;
        do {
            result = performer.testSkill(skill, difficulty, bonus);
            event.println(performer.getFirstName() + " performs " + skill.getName() + " " + result.asString());
            if (result.isSuccessful()) {
                if (exp > 0) {
                    giveXP(model, performer, exp);
                }
                break;
            } else if (performer.getSP() == 0) {
                break;
            } else {
                event.print("Use 1 Stamina Point to re-roll? (Y/N) ");
                if (event.yesNoInput()) {
                    performer.addToSP(-1);
                } else {
                    break;
                }
            }
        } while (true);
        return result;
    }

    public GameCharacter partyMemberInput(Model model, GameState event, GameCharacter preselected) {
        SubView previous = model.getSubView();
        SelectPartyMemberSubView subView = new SelectPartyMemberSubView(model, preselected);
        model.setSubView(subView);
        try {
            event.waitForReturn(true);
            model.setSubView(previous);
        } catch (SpellCastException spe) {
            model.setSubView(previous);
            throw spe;
        }
        return subView.getSelectedCharacter();
    }

    public void randomPartyMemberSay(Model model, List<String> strings) {
        partyMemberSay(model, MyRandom.sample(partyMembers), strings);
    }

    public synchronized int remove(GameCharacter gc, boolean transferEquipment, boolean payGold, int gold) {
        if (transferEquipment) {
            gc.transferEquipmentToParty(this);
        }
        int amount = 0;
        if (payGold) {
            amount = Math.min(getGold(), gold);
            addToGold(-amount);
        }
        partyMembers.remove(gc);
        if (gc == leader) {
            if (partyMembers.isEmpty()) {
                leader = null;
            } else {
                leader = partyMembers.get(0);
            }
        }
        frontRow.remove(gc);
        backRow.remove(gc);
        bench.remove(gc);
        carryingCapInKilos -= gc.getRace().getCarryingCapacity();
        return amount;
    }

    public void giveCombatLoot(List<CombatLoot> combatLoot) {
        for (CombatLoot l : combatLoot) {
            l.giveYourself(this);
        }
    }

    public void addToReputation(int rep) {
        reputation += rep;
    }

    public Point getPreviousPosition() {
        return previousPosition;
    }

    public GameCharacter getRandomPartyMember() {
        return MyRandom.sample(getPartyMembers());
    }

    public GameCharacter getRandomPartyMember(GameCharacter butNot) {
        if (size() < 2) {
            throw new IllegalStateException("Cannot call getRandomPartyMember with parameter when party size < 2");
        }
        GameCharacter gc = butNot;
        while (gc == butNot) {
            gc = getRandomPartyMember();
        }
        return gc;
    }

    public Map<String, Summon> getSummons() {
        return summons;
    }

    public boolean isBannedFromTemple(String name) {
        return templeBannings.contains(name);
    }

    public void banFromTemple(String name) {
        templeBannings.add(name);
    }

    public void benchPartyMembers(List<GameCharacter> benchers) {
        for (GameCharacter gc : benchers) {
            if (partyMembers.contains(gc)) {
                backRow.remove(gc);
                frontRow.remove(gc);
                bench.add(gc);
            }
        }
    }


    public void unbenchPartyMembers(List<GameCharacter> unbenchers) {
        for (GameCharacter gc : unbenchers) {
            if (partyMembers.contains(gc)) {
                bench.remove(gc);
                frontRow.add(gc);
            }
        }
    }

    public void unbenchAll() {
        frontRow.addAll(bench);
        bench.clear();
    }

    public List<GameCharacter> getBench() {
        return bench;
    }

    public void markSpecialCharacter(GameCharacter chara) {
        specialCharactersRecruited.add(chara.getName());
    }

    public boolean isSpecialCharacterMarked(GameCharacter chara) {
        return specialCharactersRecruited.contains(chara.getName());
    }

    public void setLoan(Loan loan) {
        this.currentLoan = loan;
    }

    public Loan getLoan() {
        return currentLoan;
    }

    public boolean hasHorses() {
        return horseHandler.size() > 0;
    }

    public boolean canRide() {
        return horseHandler.canRide(partyMembers);
    }

    public HorseHandler getHorseHandler() {
        return horseHandler;
    }

    public boolean canBuyMoreHorses() {
        return horseHandler.size() < partyMembers.size() + 2;
    }

    public List<GameCharacter> getRecruitmentPersistence() {
        return recruitmentPersistence;
    }

    public void setRecruitmentPersistence(List<GameCharacter> recruitables) {
        this.recruitmentPersistence = recruitables;
    }

    public boolean isSeminarHeld() {
        return seminarHeld;
    }

    public void setSeminarHeld(boolean seminarHeld) {
        this.seminarHeld = seminarHeld;
    }

    public int getObols() {
        return obols;
    }

    public void setObols(int playerObols) {
        obols = playerObols;
    }

    public void addToNotoriety(int i) {
        this.notoriety += i;
    }

    public int getNotoriety() {
        return this.notoriety;
    }

    public int getCarryingCapacity() {
        return carryingCapInKilos * 1000 +
                horseHandler.size() * 50 * 1000;
    }

    public boolean doSoloLockpickCheck(Model model, GameState state, int difficulty) {
        int newDiff = Lockpick.askToUseLockpick(model, state, difficulty);
        boolean success = doSoloSkillCheck(model, state, Skill.Security, newDiff);
        if (difficulty == newDiff) {
            Lockpick.checkForBreakage(model, state, success);
        }
        return success;
    }
}
