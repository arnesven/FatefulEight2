package model.headquarters;

import model.Model;
import model.characters.GameCharacter;
import model.states.GameState;
import util.MyRandom;
import view.subviews.HeadquartersSubView;
import view.subviews.PortraitSubView;

import java.util.List;

public class TransferCharacterHeadquartersAction extends HeadquartersAction {
    private final HeadquartersSubView subView;

    public TransferCharacterHeadquartersAction(Model model, HeadquartersSubView subView) {
        super(model, "Transfer character");
        this.subView = subView;
    }

    @Override
    public GameState run(Model model) {
        transferCharacter(model, this, subView);
        return null;
    }

    private static boolean headquartersFull(Model model) {
        return model.getParty().getHeadquarters().getMaxCharacters() ==
                model.getParty().getHeadquarters().getCharacters().size();
    }

    private void transferCharacter(Model model, GameState state, HeadquartersSubView subView) {
        boolean pickup;
        if (canDoPickup(model) && canDoDropOff(model)) {
            state.print("Would you like to leave (Y) or pick up (N) a character? ");
            pickup = !state.yesNoInput();
        } else if (canDoPickup(model)) {
            pickup = true;
        } else if (canDoDropOff(model)) {
            pickup = false;
        } else {
            state.println("You can neither pick up or leave characters at headquarters.");
            if (headquartersFull(model)) {
                state.println("(Headquarters can not hold more than " + model.getParty().getHeadquarters().getMaxCharacters() + " characters.)");
            }
            if (!headquartersHasCharacters(model)) {
                state.println("(No characters at headquarters.)");
            }
            if (model.getParty().isFull()) {
                state.println("(Your party is full.)");
            }
            if (model.getParty().size() == 1) {
                state.println("(You cannot leave your last party member.)");
            }
            return;
        }

        if (pickup) {
            print("Which character do you want to pick up from headquarters? ");
            subView.selectCharacterEnabled(true);
            state.waitForReturnSilently();
            GameCharacter selected = subView.getSelectedCharacter();
            subView.selectCharacterEnabled(false);
            state.leaderSay("Hello there " + selected.getFirstName() + ".");
            PortraitSubView portraitSubView = new PortraitSubView(subView, selected.getAppearance(), selected.getName());
            model.setSubView(portraitSubView);
            portraitSubView.portraitSay(model, this,
                    "Oh hello, " + model.getParty().getLeader().getFirstName() + ". How can I help?");
            print("Are you sure you want to pick up " + selected.getFirstName() + " from headquarters? (Y/N) ");
            if (!yesNoInput()) {
                leaderSay("Never mind.");
                model.getLog().waitForAnimationToFinish();
                model.setSubView(subView);
                return;
            }
            state.leaderSay("I want you to come with me.");
            portraitSubView.portraitSay(model, state, MyRandom.sample(
                    List.of("Great", "I'm ready", "That's good. I was getting bored.", "Finally!", "Really? Yay!",
                            "Fair enough.", "Okay with me.", "Right-oh.", "Off we go then.")));
            state.println(selected.getName() + " came back to the party.");
            model.getLog().waitForAnimationToFinish();
            model.setSubView(subView);
            model.getParty().getHeadquarters().getCharacters().remove(selected);
            model.getParty().add(selected, false);
            subView.updateCharacters(model);
        } else {
            do {
                print("Which party member do you want to leave at headquarters? ");
                GameCharacter selected = model.getParty().partyMemberInput(model, state,
                        model.getParty().getRandomPartyMember(model.getParty().getLeader()));
                if (selected == model.getParty().getLeader()) {
                    state.println("You cannot leave your leader.");
                } else {
                    if (model.getParty().getTamedDragons().get(selected) != null) {
                        print("Warning: leaving " + selected.getName() +
                                " at headquarters will release " + selected.getFirstName() + "'s tamed dragon. Is that OK? (Y/N) ");
                        if (!yesNoInput()) {
                            break;
                        }
                    } else {
                        print("Are you sure you want to leave " + selected.getFirstName() + " at headquarters? (Y/N) ");
                        if (!yesNoInput()) {
                            break;
                        }
                    }
                    state.leaderSay("I want you to stay at headquarters for a while " + selected.getFirstName() + ".");
                    model.getParty().partyMemberSay(model, selected,
                            List.of("Sure.", "Fine.", "Okay.", "No problem.", "That's OK with me.",
                                    "Alright.", "Oki-doki.", "Okay, bye.", "I understand.", "My pleasure."));
                    dropOffAtHeadquarters(model, state, selected);
                    subView.updateCharacters(model);
                    break;
                }
            } while (true);
        }
    }

    private boolean headquartersHasCharacters(Model model) {
        return !model.getParty().getHeadquarters().getCharacters().isEmpty();
    }


    public static void dropOffAtHeadquarters(Model model, GameState state, GameCharacter selected) {
        state.println(selected.getName() + " has left the party and will remain at headquarters until picked up.");
        model.getLog().waitForAnimationToFinish();
        model.getParty().remove(selected, false, false, 0);
        model.getParty().getHeadquarters().getCharacters().add(selected);
    }

    public static boolean canDoDropOff(Model model) {
        return model.getParty().size() > 1 && !headquartersFull(model);
    }

    private boolean canDoPickup(Model model) {
        return !model.getParty().isFull() && headquartersHasCharacters(model);
    }

}
