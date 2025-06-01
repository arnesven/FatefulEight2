package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.SwimAttire;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.CombatAdvantage;
import model.combat.conditions.VampirismCondition;
import model.enemies.Enemy;
import model.enemies.RowdyBoyEnemy;
import model.enemies.RowdyGirlEnemy;
import model.items.Item;
import model.map.HexLocation;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.states.CombatEvent;
import model.states.DailyEventState;
import model.states.ShopState;
import util.MyLists;
import util.MyRandom;
import view.combat.MansionTheme;
import view.subviews.PortraitSubView;
import view.subviews.SplitPartySubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.List;

public class BathHouseEvent extends DailyEventState {
    private final boolean prudeGender;
    private GameCharacter oldLeader = null;
    private final CharacterAppearance prudePerson;

    public BathHouseEvent(Model model) {
        super(model);
        if (model.getCurrentHex().getLocation() instanceof UrbanLocation) {
            this.prudeGender = ((UrbanLocation) model.getCurrentHex().getLocation()).getLordGender();
        } else {
            this.prudeGender = MyRandom.flipCoin();
        }
        prudePerson = PortraitSubView.makeRandomPortrait(Classes.MERCHANT, MyRandom.nextRace(), prudeGender);
        prudePerson.setSpecificClothing(new SwimAttire());
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit bathhouse", "We have a bathhouse here. It's nice");
    }

    @Override
    protected void doEvent(Model model) {
        println("The party passes a bath house. The entrance fee is 1 gold per person. " +
                "Do any party members want to take a bath (group B)?");
        SubView oldSub = model.getSubView();
        List<GameCharacter> groupB;
        List<GameCharacter> groupA;
        do {
            groupA = new ArrayList<>(model.getParty().getPartyMembers());
            groupB = new ArrayList<>();
            model.setSubView(new SplitPartySubView(oldSub, groupA, groupB));
            waitForReturnSilently();
            if (groupB.size() > model.getParty().getGold()) {
                println("You cannot afford that.");
            } else {
                break;
            }
        } while (true);
        for (GameCharacter gc : new ArrayList<>(groupB)) {
            if (gc.isSpecialCharacter()) {
                partyMemberSay(gc, "I refuse to do this.");
                groupB.remove(gc);
                groupA.add(gc);
            }
        }
        if (groupB.isEmpty()) {
            leaderSay("We don't have time to take a bath, we have other things to do.");
            return;
        }
        model.setSubView(oldSub);

        splitGroups(model, groupA, groupB);

        enterBathHouse(model, groupB);

        doBathHouseSubEvent(model, groupB);

        restoreParty(model, groupB);
    }

    private void splitGroups(Model model, List<GameCharacter> groupA, List<GameCharacter> groupB) {
        if (!groupA.isEmpty()) {
            partyMemberSay(groupA.get(0), "See you later.");
            partyMemberSay(groupB.get(0), "Bye for now.");
        }
        model.getParty().benchPartyMembers(groupA);

        if (groupA.contains(model.getParty().getLeader())) {
            oldLeader = model.getParty().getLeader();
            model.getParty().setLeader(groupB.get(0));
            println(model.getParty().getLeader().getName() + " has been temporarily set to the party leader.");
        }
    }


    private void enterBathHouse(Model model, List<GameCharacter> bathers) {
        showRandomPortrait(model, Classes.OFFICIAL, "Bath Staff");
        portraitSay("Welcome guests, that will be " + bathers.size() + " gold please.");
        model.getParty().addToGold(-bathers.size());
        leaderSay("There you go. Uhm... where are the pools?");
        portraitSay("Just down that hall, but...");
        leaderSay("What is it?");
        portraitSay("You must remove your clothing!");
        leaderSay("Oh!");
        portraitSay("The changing rooms are this way. You will find swimming attire inside.");
        leaderSay("... thank you.");
        randomSayIfPersonality(PersonalityTrait.prudish, List.of(model.getParty().getLeader()), "Eek! I don't want anybody " +
                "ogling me!");
        println("You change into your swimming attire and leave your gear in the lockers.");
        for (GameCharacter gc : bathers) {
            gc.setSpecificClothing(new SwimAttire());
            gc.unequipWeapon();
            gc.unequipArmor();
            gc.unequipAccessory();
        }
        removePortraitSubView(model);
    }

    private void doBathHouseSubEvent(Model model, List<GameCharacter> groupB) {
        println("You enter into a room with a large pool with beautiful blue water.");
        int dieRoll = MyRandom.rollD10();
        if (dieRoll < 3) {
            rowdyBunchSubEvent(model, groupB);
        } else if (dieRoll < 8) {
            refreshingSwimSubEvent(model, groupB, true);
        } else if (dieRoll < 9) {
            friendSubEvent(model, groupB);
        } else if (dieRoll < 10) {
            prudeSubEvent(model, groupB, Classes.MERCHANT);
        } else {
            prudeSubEvent(model, groupB, Classes.NOB);
        }
    }

    private void rowdyBunchSubEvent(Model model, List<GameCharacter> groupB) {
        boolean gender = MyRandom.flipCoin();
        String genderString = gender ? "girl" : "boy";
        println("However there are a bunch of rowdy " + genderString + "s" + " here.");
        println("You try to have a relaxing swim but the obnoxious group soon starts to " +
                "heckle you, until you are properly annoyed.");
        partyMemberSay(groupB.get(groupB.size()-1), "Grrr... we should teach these adolescent sleaze bags a lesson.#");
        print("Do you kick the " + genderString + "s out of the bath house? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay("Maybe we should just leave. It's not worth picking a fight about.");
            partyMemberSay(groupB.get(groupB.size()-1), "What a disappointment.");
        } else {
            List<Enemy> rowdyBunch = makeRowdyBunch(gender);
            CombatEvent combat = new CombatEvent(getModel(), rowdyBunch, new MansionTheme(), false, CombatAdvantage.Neither);
            combat.setTimeLimit(5);
            combat.run(getModel());
            if (model.getParty().isWipedOut()) {
                return;
            }
            setCurrentTerrainSubview(model);
            if (allDead(groupB)) {
                return;
            }
            int numDead = 0;
            for (Enemy e : rowdyBunch) {
                if (e.isDead()) {
                    numDead++;
                }
            }
            if (numDead > 0) {
                GeneralInteractionEvent.addMurdersToNotoriety(model, this, numDead);
                leaderSay("Uh-oh... I'm sure we didn't mean to kill " + himOrHer(gender) + " right?");
                partyMemberSay(groupB.get(groupB.size() - 1), "It was an accident. I swear!");
                leaderSay("We better get out of here before we get caught...");
            } else {
                println("Now that the rowdy bunch has cleared off you are free to enjoy the bath house in peace.");
                refreshingSwimSubEvent(model, groupB, true);
            }
        }
    }

    private List<Enemy> makeRowdyBunch(boolean gender) {
        List<Enemy> rowdyBunch = new ArrayList<>();
        for (int i = MyRandom.randInt(5, 8); i > 0; --i) {
            Enemy e = null;
            if (gender) {
                e = new RowdyGirlEnemy('A');
            } else {
                e = new RowdyBoyEnemy('B');
            }
            rowdyBunch.add(e);
        }
        return rowdyBunch;
    }

    private void refreshingSwimSubEvent(Model model, List<GameCharacter> groupB, boolean alone) {
        println("You have a refreshing swim in the pool.");
        for (GameCharacter gc : groupB) {
            if (gc.getSP() < gc.getMaxSP() && !gc.hasCondition(VampirismCondition.class)) {
                gc.addToSP(1);
                println(gc.getName() + " gains 1 stamina point.");
            }
        }
        if (alone) {
            println("Since you are alone in the pool you can also mediate in silence and reflect on your recent deeds.");
            for (GameCharacter gc : groupB) {
                println(gc.getName() + " gains 20 experience points.");
                model.getParty().giveXP(model, gc, 20);
            }
        }
        leaderSay("Aaahhh... That feels amazing!");
    }

    private void friendSubEvent(Model model, List<GameCharacter> groupB) {
        FriendEvent friend = new FriendEvent(model);
        friend.doEvent(model);
    }

    private void prudeSubEvent(Model model, List<GameCharacter> groupB, CharacterClass prudeClass) {
        String genderString = prudeGender ? "women" : "men";
        String oppositeString = prudeGender ? "men" : "women";
        println("A few " + genderString + " are sitting next to the pool.");
        if (allAreGender(groupB, prudeGender)) {
            println("When you approach, one of them introduces " + himOrHer(prudeGender) + "self.");
            showExplicitPortrait(model, prudePerson, "Prude " + (prudeClass == Classes.MERCHANT ? "Merchant" : "Noble"));
            String personDescription;
            if (prudeClass == Classes.MERCHANT) {
                personDescription = "I'm a merchant with a shop in town.";
            } else {
                HexLocation location = model.getCurrentHex().getLocation();
                if (location instanceof UrbanLocation) {
                    personDescription = "I'm the " + ((UrbanLocation) location).getLordTitle() + " here.";
                } else {
                    personDescription = "I'm the person in charge here.";
                }
            }
            portraitSay(personDescription + " I like to come to this bath house sometimes, when it's quite.");
            leaderSay("Quiet? What do you mean?");
            portraitSay("Well sometimes there are adolescents here, playing around in the pool. They're such " +
                    "a rowdy bunch.");
            leaderSay("How annoying!");
            portraitSay("Yes, very. But it's equally annoying when there are " + oppositeString + " here ogling me and " +
                    "commenting on my physical appearance.");
            leaderSay("That's just rude.");
            portraitSay("Exactly. Say, you seem like an approachable individual. Maybe we could do business?");
            leaderSay("I'd love to, but...");
            if (prudeClass == Classes.MERCHANT) {
                portraitSay("My merchandise? Don't worry. I have my servants here carry a few exclusive items with me " +
                        "wherever I go, for situations just like these. And for security of course... Do you want to have a look?");
                waitForReturnSilently();
                List<Item> items = new ArrayList<>();
                for (int i = MyRandom.randInt(4, 8); i > 0; --i) {
                    Item it = model.getItemDeck().getRandomItem(0.98);
                    items.add(it);
                }
                int[] costs = new int[items.size()];
                int index = 0;
                for (Item it : items) {
                    costs[index++] = it.getCost() / 2;
                }
                ShopState shop = new ShopState(model, "Prude Merchant", items, costs, new boolean[]{true});
                shop.run(model);
                setCurrentTerrainSubview(model);
                println("You excuse yourself from the merchant.");
            } else if (model.getCurrentHex().getLocation() instanceof UrbanLocation &&
                    !model.getParty().hasSummon((UrbanLocation) model.getCurrentHex().getLocation())){
                String lordHome;
                UrbanLocation location = (UrbanLocation) model.getCurrentHex().getLocation();
                portraitSay("Just come by the " + location.getLordDwelling().toLowerCase() + " later. I have something I need help with.");
                model.getParty().addSummon(location);
                leaderSay("I'm looking forward to it.");
                portraitSay("Bye for now.");
                println("You excuse yourself from the lord.");
            } else {
                portraitSay("Please, tell me about yourself.");
                NoblemanEvent.tryToGetGold(model, this);
                println("You excuse yourself from the noble.");
            }
        } else {
            println("As you approach the " + genderString + ", one of them quickly gets up and stops you.");
            showExplicitPortrait(model, prudePerson, "Servant");
            portraitSay("Please don't disturb my master. " + heOrSheCap(prudeGender) + " likes to come to the bath house, " +
                    "but is very conscious about " + hisOrHer(prudeGender) + " body.");
            leaderSay("We just want to talk.");
            portraitSay("Please leave us alone. " + heOrSheCap(prudeGender) + " gets very uncomfortable when " + oppositeString +
                    " are present at the bath house.");
            partyMemberSay(groupB.get(groupB.size()-1), "Oh, jeez...");
        }
        refreshingSwimSubEvent(model, groupB, false);
    }

    private boolean allAreGender(List<GameCharacter> groupB, boolean gender) {
        return MyLists.all(groupB, (GameCharacter gc) -> gc.getGender() == gender);
    }

    private void restoreParty(Model model, List<GameCharacter> bathers) {
        if (!allDead(bathers)) {
            println("Since you are done at the bath house, you change back into your normal clothing again.");
            MyLists.forEach(bathers, GameCharacter::removeSpecificClothing);
        }
        model.getParty().unbenchAll();
        if (oldLeader != null) {
            model.getParty().setLeader(oldLeader);
            println(oldLeader.getName() + " is now the leader again.");
        }
    }

    private boolean allDead(List<GameCharacter> bathers) {
        return MyLists.all(bathers, GameCharacter::isDead);
    }
}
