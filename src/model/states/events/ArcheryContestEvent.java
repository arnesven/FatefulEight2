package model.states.events;

import model.ItemDeck;
import model.Model;
import model.characters.GameCharacter;
import model.characters.GoblinCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Equipment;
import model.items.Item;
import model.items.weapons.*;
import model.map.CastleLocation;
import model.races.Race;
import model.states.RecruitState;
import model.states.ShopState;
import util.MyRandom;
import view.subviews.ArcheryContestBoardSubView;
import view.subviews.PortraitSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static model.classes.Classes.None;

public class ArcheryContestEvent extends TournamentEvent {
    private final CastleLocation castle;
    private List<Weapon> bows;

    public ArcheryContestEvent(Model model, CastleLocation castleLocation) {
        super(model, castleLocation);
        this.castle = castleLocation;
        bows = new ArrayList<>(ItemDeck.allBows());
        bows.add(new CompetitionBow());
        bows.add(new CompetitionBow());
    }

    @Override
    protected void doEvent(Model model) {
//        print("The " + castle.getLordTitle() + " is hosting a archery competition today. " +
//                "Do you wish to attend? (Y/N) ");
//        if (!yesNoInput()) {
//            return;
//        }
//        println("Outside the castle walls many tents and pavilions have been erected. And there, " +
//                "on a long stretch of lawn, several archery targets have been placed.");
//        println("As you wander around you see marksmen, fair ladies, noblemen, merchants and commoners " +
//                "all bustling about and getting ready for the contest. Some people are lining up at a little booth " +
//                "where a small gentleman in fancy clothing is accepting coins and writing things down in big ledgers.");
//        model.getLog().waitForAnimationToFinish();
//        showOfficial();
//        portraitSay("Yes, we're still accepting participants. Are you here to compete in the archery contest?");
//        leaderSay("Perhaps. What are the parameters?");
//        portraitSay("The entry fee is " + ENTRY_FEE + " gold. Twenty marksmen will enter the competition, which has three rounds. " +
//                "In the first round each contestant fires a single arrow at the target. The ten who made the best shots qualify." +
//                "In the second round each contestant must shoot at apples which have been tossed into the air. The contestants who " +
//                "can hit the most apples tossed at the same time will qualify to the third round. In the third round, each contestant is " +
//                "given three arrows and score points for how well they hit the target. The contestant who scores the most points in the final " +
//                "round wins a marvelous prize - the Golden Bow. Oh and you'll also receive the " + castle.getLordTitle() +
//                " blessing - which is probably equally valuable.");
        showOfficial();
        portraitSay("But all of you can't enter the contest, in fact we only have room for one more. Are you still interested?");
        boolean sponsored = false;
        if (model.getParty().getGold() < ENTRY_FEE) {
            println("You are about to reply that you can't afford it when a shady fellow in a hood steps up behind you.");
            showSponsor();
            portraitSay("You look like your the capable sort, I can front the money for the competition. " +
                    "But if you win I'll want the bow you used during the contest. What do you say?");
            print("Do you accept the strangers sponsorship? (Y/N) ");
            sponsored = true;
        } else {
            print("Will you enter one of your party members into the contest? (Y/N) ");
        }
        removePortraitSubView(model);
        if (yesNoInput()) {
            enterTournament(model, sponsored);
        } else {
            leaderSay("I think we'll just watch this time.");
            println("You spend the day watching the contest, which is both exciting and interesting. " +
                    "Afterwards you feel inspired to become a better bowman.");
        }
    }

    private void enterTournament(Model model, boolean sponsored) {
        println("Which party member should enter the tournament?");
        GameCharacter chosen = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
        partyMemberSay(chosen, "I'll enter the contest.");
        if (!sponsored) {
            println("You pay the official " + ENTRY_FEE + " gold.");
            model.getParty().addToGold(-ENTRY_FEE);
        } else {
            println("The stranger pays the entry fee to the official.");
        }
        showOfficial();
        portraitSay("Thank you. Naturally you will need a bow. You are allowed to use your own, but we do " +
                "sell excellent bows made especially for competitions.");
        if (chosen.getEquipment().getWeapon() instanceof CrossbowWeapon) {
            partyMemberSay(chosen, "Can I use a crossbow?");
            portraitSay("It is allowed, but I wouldn't recommend it. Crossbows are easy enough to aim with, but it is " +
                    "not very easy to adjust the power of the shot.");
            partyMemberSay(chosen, "I understand.");
        }
        print("Do you wish to purchase a bow from the official? (Y/N) ");
        if (yesNoInput()) {
            buyBow(model);
        } else {
            partyMemberSay(chosen, "I already have a bow.");
        }

        stillNeedABow(model, chosen);

        do {
            println("Please ensure " + chosen.getFirstName() + " is equipped with the bow " +
                    "you would like to use in the tournament. Then press enter to continue.");
            waitForReturnSilently();
        } while (!bowEquipped(chosen));

        Weapon bowToUse = chosen.getEquipment().getWeapon();
        println(chosen.getName() + " will use a " + bowToUse.getName().toLowerCase() + " in the contest.");

        List<GameCharacter> contestants = makeMarksmen(model);
        contestants.add(chosen);
        Collections.shuffle(contestants);
        playRoundOne(model, chosen, bowToUse, contestants);
        //playRoundTwo(model, chosen, bowToUse, contestants);
        //playRoundThree(model, chosen, bowToUse, contestants);
    }

    private void stillNeedABow(Model model, GameCharacter chosen) {
        for (GameCharacter chara : model.getParty().getPartyMembers()) {
            if (chara.getEquipment().getWeapon() instanceof BowWeapon) {
                return;
            }
        }

        for (Item it : model.getParty().getInventory().getAllItems()) {
            if (it instanceof BowWeapon) {
                return;
            }
        }

        partyMemberSay(chosen, "Wait... I actually don't have a bow.");
        portraitSay("Well... I guess you can use this bow.");
        model.getParty().getInventory().add(new TrainingBow());
        println("The party receives " + (new TrainingBow()).getName() + ".");
    }

    private void playRoundOne(Model model, GameCharacter chosen, Weapon bowToUse, List<GameCharacter> contestants) {
        portraitSay("To my left is a board with the names of all the contestants. Please have a look at it now.");
        waitForReturnSilently();
        SubView prevSubView = model.getSubView();
        model.setSubView(new ArcheryContestBoardSubView(contestants));
        waitForReturnSilently();
        model.setSubView(prevSubView);
    }

    private List<GameCharacter> makeMarksmen(Model model) {
        List<GameCharacter> result = new ArrayList<>();
        CharacterClass[] noClasses = new CharacterClass[]{Classes.None, Classes.None, Classes.None, Classes.None};
        result.add(new GameCharacter("Ruben", "Hodd", Race.NORTHERN_HUMAN, Classes.MAR,
                PortraitSubView.makeRandomPortrait(Classes.MAR, Race.NORTHERN_HUMAN, false), noClasses, randomBow()));
        result.add(new GameCharacter("Ivar", "Longfellow", Race.NORTHERN_HUMAN, Classes.MAR,
                PortraitSubView.makeRandomPortrait(Classes.MAR, Race.NORTHERN_HUMAN, false), noClasses, randomBow()));
        result.add(new GameCharacter("Fanny", "Ovuldsgain", Race.DWARF, Classes.NOB,
                PortraitSubView.makeRandomPortrait(Classes.NOB, Race.DWARF, true), noClasses, randomBow()));
        result.add(new GameCharacter("Tessa", "Crowvald", Race.HALF_ORC, Classes.AMZ,
                PortraitSubView.makeRandomPortrait(Classes.AMZ, Race.HALF_ORC, true), noClasses, randomBow()));
        result.add(new GameCharacter("Masked Ranger", "", Race.HALFLING, Classes.ASN,
                PortraitSubView.makeRandomPortrait(Classes.ASN, Race.HALFLING, MyRandom.flipCoin()), noClasses, randomBow()));
        result.add(new GameCharacter("Vanessa", "Firsov", Race.SOUTHERN_HUMAN, Classes.CAP,
                PortraitSubView.makeRandomPortrait(Classes.CAP, Race.SOUTHERN_HUMAN, true), noClasses, randomBow()));
        result.add(new GameCharacter("Goblin", "", Race.GOBLIN, Classes.GOBLIN,
                new GoblinCharacter(), new CharacterClass[]{Classes.GOBLIN, None, None, None}, randomBow()));
        result.add(new GameCharacter("Locky", "Roseville", Race.WOOD_ELF, Classes.FOR,
                PortraitSubView.makeRandomPortrait(Classes.FOR, Race.WOOD_ELF, false), noClasses, randomBow()));
        result.add(new GameCharacter("Endath", "Virmansee", Race.DARK_ELF, Classes.MAR,
                PortraitSubView.makeRandomPortrait(Classes.MAR, Race.DARK_ELF, true), noClasses, randomBow()));
        result.add(new GameCharacter("Filavandrel", "Oakborn", Race.HIGH_ELF, Classes.NOB,
                PortraitSubView.makeRandomPortrait(Classes.NOB, Race.HIGH_ELF, false), noClasses, randomBow()));
        result.add(new GameCharacter("Marcus", "Felbottom", Race.DWARF, Classes.AMZ,
                PortraitSubView.makeRandomPortrait(Classes.AMZ, Race.DWARF, false), noClasses, randomBow()));
        result.add(new GameCharacter("Woods Girl", "", Race.WOOD_ELF, None,
                PortraitSubView.makeRandomPortrait(None, Race.WOOD_ELF, true), noClasses, randomBow()));
        result.add(new GameCharacter("Anthony", "McCormic", Race.HALFLING, Classes.CAP,
                PortraitSubView.makeRandomPortrait(Classes.CAP, Race.HALFLING, false), noClasses, randomBow()));
        result.add(new GameCharacter("Shagra", "Vull", Race.HALF_ORC, Classes.BBN,
                PortraitSubView.makeRandomPortrait(Classes.BBN, Race.HALF_ORC, false), noClasses, randomBow()));
        result.add(new GameCharacter("Jeremy", "Inklepod", Race.SOUTHERN_HUMAN, Classes.DRU,
                PortraitSubView.makeRandomPortrait(Classes.DRU, Race.SOUTHERN_HUMAN, false), noClasses, randomBow()));
        result.add(new GameCharacter("The Fool", "", Race.NORTHERN_HUMAN, None,
                PortraitSubView.makeRandomPortrait(None, Race.NORTHERN_HUMAN, MyRandom.flipCoin()), noClasses, new Equipment(new ShortBow())));
        result.add(new GameCharacter("Jeim", "Ecthelion", Race.WOOD_ELF, Classes.NOB,
                PortraitSubView.makeRandomPortrait(Classes.NOB, Race.WOOD_ELF, false), noClasses, randomBow()));
        result.add(new GameCharacter("Rutte", "Kolantis", Race.DWARF, Classes.BRD,
                PortraitSubView.makeRandomPortrait(Classes.BRD, Race.DWARF, false), noClasses, randomBow()));
        result.add(new GameCharacter("Esmeralda", "Vix", Race.DARK_ELF, Classes.THF,
                PortraitSubView.makeRandomPortrait(Classes.THF, Race.DARK_ELF, true), noClasses, randomBow()));
        int lvl = (int)Math.round(RecruitState.calculateAverageLevel(model));
        for (GameCharacter chara : result) {
            chara.setLevel(lvl);
        }
        return result;
    }

    private Equipment randomBow() {
        return new Equipment(MyRandom.sample(bows));
    }

    private boolean bowEquipped(GameCharacter chosen) {
        return chosen.getEquipment().getWeapon() instanceof BowWeapon;
    }

    private void buyBow(Model model) {
        List<Item> items = new ArrayList<>();
        items.add(new CompetitionBow());
        ShopState merchantShop = new ShopState(model, "Contest Official", items, new int[]{14});
        merchantShop.setSellingEnabled(false);
        merchantShop.run(model);
        setCurrentTerrainSubview(model);
    }
}
