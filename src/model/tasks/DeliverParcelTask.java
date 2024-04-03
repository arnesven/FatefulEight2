package model.tasks;

import model.Model;
import model.actions.DailyAction;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.items.parcels.Parcel;
import model.journal.JournalEntry;
import model.races.Race;
import model.states.GameState;
import model.states.events.MakeDeliveryEvent;
import view.subviews.PortraitSubView;

import java.awt.*;

public class DeliverParcelTask extends DestinationTask {
    private final Parcel parcel;
    private final String recipient;
    private final Boolean recipientGender;
    private final int promisedGold;
    private final String sender;
    private boolean completed;
    private final String shortDescription;
    private final Race recipientRace;

    public DeliverParcelTask(String sender, Parcel parcel, Point point, String longDescription, String shortDescription,
                             String recipient, Race race, Boolean gender, int promisedGold) {
        super(point, longDescription);
        this.sender = sender;
        this.parcel = parcel;
        this.shortDescription = shortDescription;
        this.recipient = recipient;
        this.recipientRace = race;
        this.recipientGender = gender;
        this.promisedGold = promisedGold;
        this.completed = false;
    }

    public String getDeliveryString() {
        String manOrWoman = recipientGender ? "woman" : "man";
        String old = recipient.contains("grand") ? "an old " : "a ";
        return "You find the " + shortDescription + ". Inside is " + old + manOrWoman + ". " +
                GameState.heOrSheCap(recipientGender) + " looks a little startled at your sudden appearance.";
    }

    public Parcel getParcel() {
        return parcel;
    }

    public String getRecipient() {
        return recipient;
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return new DailyAction("Deliver " + parcel.getName(),
                new MakeDeliveryEvent(model, this));
    }

    @Override
    public boolean isFailed(Model model) {
        return !model.getParty().getInventory().getParcels().contains(parcel);
    }

    @Override
    public boolean givesDailyAction(Model model) {
        return !isFailed(model) && !completed && model.getParty().getPosition().equals(getPosition());
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new JournalEntry() {
            @Override
            public String getName() {
                return "Deliver a " + parcel.getName().toLowerCase();
            }

            @Override
            public String getText() {
                if (completed) {
                    return "You delivered a " + parcel.getName().toLowerCase() + " to " + sender + "'s " +
                            recipient + " in exchange for " + promisedGold + " gold.";
                }
                return "You have agreed to deliver a " + parcel.getName().toLowerCase() + " to " + sender + "'s " +
                        recipient + " in exchange for " + promisedGold + " gold. This person lives " +
                        getDestinationDescription() + ".";

            }

            @Override
            public boolean isComplete() {
                return completed;
            }

            @Override
            public boolean isFailed() {
                return false;
            }

            @Override
            public boolean isTask() {
                return true;
            }

            @Override
            public Point getPosition(Model model) {
                if (completed) {
                    return null;
                }
                return DeliverParcelTask.this.getPosition();
            }
        };
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return new JournalEntry() {
            @Override
            public String getName() {
                return "Deliver a " + parcel.getName().toLowerCase();
            }

            @Override
            public String getText() {
                return "You had agreed to deliver a " + parcel.getName().toLowerCase() + " to " + sender + "'s " +
                        recipient + ", but you have lost it somewhere along the way.";
            }

            @Override
            public boolean isComplete() {
                return false;
            }

            @Override
            public boolean isFailed() {
                return true;
            }

            @Override
            public boolean isTask() {
                return true;
            }

            @Override
            public Point getPosition(Model model) {
                return null;
            }
        };
    }

    public CharacterAppearance makePortrait() {
        if (recipient.contains("grand")) {
            return PortraitSubView.makeOldPortrait(Classes.None, recipientRace, recipientGender);
        }
        return PortraitSubView.makeRandomPortrait(Classes.None, recipientRace, recipientGender);
    }

    public void receivePayment(Model model) {
        model.getParty().getInventory().remove(parcel);
        model.getParty().addToGold(promisedGold);
        model.getLog().addAnimated("The party received " + promisedGold + " gold.\n");
        completed = true;
        JournalEntry.printJournalUpdateMessage(model);
    }

    public String getSender() {
        return sender;
    }
}
