package model.tasks;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Equipment;
import model.items.accessories.*;
import model.items.clothing.*;
import model.items.weapons.*;
import model.races.Race;
import model.states.GameState;
import model.states.events.*;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.io.Serializable;
import java.util.Map;
import java.util.List;

public class WritOfExecution implements Serializable {
    private static final int WRIT_DEADLINE_DAYS = 14;
    private static final int PAYMENT_GOLD = 100;

    private static final List<CharacterClass> CLASSES = List.of(
            Classes.NOB, Classes.PAL, Classes.SPY, Classes.SOR, Classes.THF, Classes.BRD);

    private static final Map<Integer, AssassinationEnding> ENDING_MAP =      // Lay-in-wait  Sneak    Enter    Leave    Poison
            Map.of(
                    Classes.NOB.id(), new AssassinationEnding(Classes.NOB, "eccentric aristocrat", AristocratAssassinationEndingEvent::new,
                            "is rather wealthy",
                            new Equipment(new HuntersBow(), new WarmCape(), new EmeraldRing())),
                    Classes.PAL.id(), new AssassinationEnding(Classes.PAL, "violent and dangerous knight", KnightAssassinationEndingEvent::new,
                            "is always clad in heavy armor and wields an enormous sword",
                            new Equipment(new Zweihander(), new PlateMailArmor(), new GreatHelm())),
                    Classes.SPY.id(), new AssassinationEnding(Classes.SPY, "information broker", InformationBrokerAssassinationEndingEvent::new,
                            "is very well connected but has not been seen by anybody for about a week",
                            new Equipment(new Longsword(), new LeatherTunic(), new GrayRing())),
                    Classes.SOR.id(), new AssassinationEnding(Classes.SOR, "powerful enchanter", EnchanterAssassinationEndingEvent::new,
                            "dabbles in strange magic and has many enchanted devices",
                            new Equipment(new GrandStaff(), new CultistsRobes(), new ShinyAmulet())),
                    Classes.THF.id(), new AssassinationEnding(Classes.THF, "notorious thief", ThiefAssassinationEndingEvent::new,
                            "is an expert at laying traps",
                            new Equipment(new OrcishKnife(), new PilgrimsCloak(), new LuckyTalisman())),
                    Classes.BRD.id(), new AssassinationEnding(Classes.BRD, "famous singer", SingerAssassinationEndingEvent::new,
                            "has recently hired a bodyguard",
                            new Equipment(new MorningStar(), new FancyJerkin(), new JestersHat())));


//    private static final Map<CharacterClass, String> CLASSES =    // Lay-in-wait  Sneak    Enter    Leave    Poison
//            Map.of(Classes.NOB, "wealthy aristocrat",             //    0.6        x        x                 x
//                    Classes.PAL, "violent and dangerous knight",  //    0.4        x        x                 x
//                    Classes.SPY, "information broker",            //     0                            x
//                    Classes.SOR, "powerful enchanter",            //    0.4                 x                 x
//                    Classes.THF, "notorious thief",               //    0.3        x                          x
//                    Classes.BRD, "famous singer");                //    0.8        x        x                (no, kills bodyguard)

    private final int dayAccepted;
    private final CharacterClass charClass;
    private final AdvancedAppearance appearance;
    private final String firstName;
    private final String lastName;
    private final Destination destination;
    private boolean clueGotten = false;
    private boolean locationFound = false;
    private boolean infoBrokerFound = false;

    public WritOfExecution(Model model) {
        this.dayAccepted = model.getDay();
        this.charClass = MyRandom.sample(CLASSES);
        this.appearance = PortraitSubView.makeRandomPortrait(charClass);
        this.firstName = GameState.randomFirstName(appearance.getGender());
        this.lastName = GameState.randomLastName();
        this.destination = Destination.generateUrbanDestination(model);
    }

    public static int getDeadlineInDays() {
        return WRIT_DEADLINE_DAYS;
    }

    public static int getPayment() {
        return PAYMENT_GOLD;
    }

    public Point getPosition() {
        return destination.getPosition();
    }

    public String getDestinationShortDescription() {
        return destination.getShortDescription();
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getLastName() { return lastName; }

    public String getDestinationLongDescription() {
        return destination.getPreposition() + " " + destination.getLongDescription();
    }

    public boolean hasExpired(Model model) {
        return getDaysLeft(model) < 0;
    }

    public GameCharacter makeVictim() {
        return new GameCharacter(firstName, lastName, appearance.getRace(),
                charClass, appearance, Classes.NO_OTHER_CLASSES, ENDING_MAP.get(charClass.id()).getEquipment());
    }

    public int getDaysLeft(Model model) {
        return dayAccepted + WRIT_DEADLINE_DAYS - model.getDay();
    }

    public boolean getGender() {
        return appearance.getGender();
    }

    public String getOccupationDescription() {
        return ENDING_MAP.get(charClass.id()).getOccupationDescription();
    }

    public boolean gotClue() {
        return clueGotten;
    }

    public void setGotClue(boolean b) {
        clueGotten = b;
    }

    public AssassinationEndingEvent makeEndingEvent(Model model) {
        return ENDING_MAP.get(charClass.id()).makeEnding(model);
    }

    public Race getRace() {
        return appearance.getRace();
    }

    public void setLocationFound(boolean b) {
        this.locationFound = b;
    }

    public boolean isLocationFound() {
        return locationFound;
    }

    public void setInfoBrokerFound(boolean b) {
        this.infoBrokerFound = b;
    }

    public boolean isInfoBrokerFound() {
        return infoBrokerFound;
    }

    public String getAdditionalInformation() {
        return ENDING_MAP.get(charClass.id()).getAdditionalInformation();
    }
}
