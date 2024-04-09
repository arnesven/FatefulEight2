package model.tasks;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.races.Race;
import model.states.AcceptDeliveryEvent;
import model.states.GameState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.io.Serializable;

public class Bounty implements Serializable {

    private final String firstName;
    private final String lastName;
    private final int reward;
    private final boolean gender;
    private final boolean companions;
    private final String turnInTown;
    private final AdvancedAppearance appearance;
    private final Destination destination;

    public Bounty(String firstName, String lastName, int gold, boolean gender, boolean andCompanions, String town, Destination dest) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.reward = gold;
        this.gender = gender;
        this.companions = andCompanions;
        this.turnInTown = town;
        this.appearance = PortraitSubView.makeRandomPortrait(Classes.None, Race.ALL, gender);
        this.destination = dest;
    }

    public static Bounty generate(Model model, String town) {
        boolean gender = MyRandom.flipCoin();
        String firstName = GameState.randomFirstName(gender);
        String lastName = GameState.randomLastName();
        int gold = MyRandom.randInt(20, 120);
        boolean andCompanions = MyRandom.flipCoin();
        Destination dest = AcceptDeliveryEvent.makeRandomDestination(model);
        return new Bounty(firstName, lastName, gold, gender, andCompanions, town, dest);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public CharacterAppearance getAppearance() {
        return appearance;
    }

    public boolean getWithCompanions() {
        return companions;
    }

    public int getReward() {
        return reward;
    }

    public boolean getGender() {
        return gender;
    }

    public Point getPosition() {
        return destination.getPosition();
    }

    public String getTurnInTown() {
        return turnInTown;
    }

    public String getClue() {
        return destination.getLongDescription();
    }

    public String getDestinationShortDescription() {
        return destination.getShortDescription();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
