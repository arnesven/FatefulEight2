package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.combat.conditions.VampirismCondition;
import model.states.DailyEventState;
import util.MyLists;
import view.subviews.PortraitSubView;

public class StoneCircleEvent extends DailyEventState {
    public StoneCircleEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        AdvancedAppearance app = PortraitSubView.makeRandomPortrait(Classes.DRU);
        showExplicitPortrait(model, app, "Druid");
        println("In a wide field, the party encounters a ring of standing " +
                "stones. In the middle lay one large slab which seems to be " +
                "intended as an altar. A druid is there with a gathering of " +
                "a few followers and is just about to perform a nature " +
                "ritual. You wait until the ceremony has concluded.");
        if (vampireInParty(model)) {
            cureVampirismRitual(model, app);
        } else {
            offerDruidismLessons(model);
        }
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Find Stone circle", "We're close to a stone circle. Druids sometimes perform rituals there");
    }

    private boolean vampireInParty(Model model) {
        return MyLists.any(model.getParty().getPartyMembers(),
                (GameCharacter gc) -> gc.hasCondition(VampirismCondition.class));
    }

    private void cureVampirismRitual(Model model, AdvancedAppearance app) {
        portraitSay("Oh hello. I see you have been cursed by the darkest of afflictions.");
        GameCharacter vampire = MyLists.find(model.getParty().getPartyMembers(),
                (GameCharacter gc) -> gc.hasCondition(VampirismCondition.class));
        GameCharacter other = null;
        if (model.getParty().size() > 1) {
            other = model.getParty().getLeader();
            if (vampire == other) {
                other = model.getParty().getRandomPartyMember(vampire);
            }
            partyMemberSay(other, "What ever do you mean by that?");
            portraitSay("Isn't it obvious. One of you is a vampire.");
            partyMemberSay(other, "A VAMPIRE!? Are you serious?");
            portraitSay("Of course. Druids are not known for their humor.");
            if (model.getParty().size() > 2) {
                partyMemberSay(other, "Well, I'm not a vampire... hey, who is a vampire? Time to come clean!");
                println("An awkward silence follows...");
                partyMemberSay(other, "Seriously, WHO'S A VAMPIRE!?");
                partyMemberSay(vampire, "Okay okay, stop shouting. It's me okay?");
            } else {
                partyMemberSay(other, vampire.getFirstName() + ", are you a vampire?");
                partyMemberSay(vampire, "I'm not... Oh to hell with it. Fine, I'm a vampire!");
            }
            partyMemberSay(other, vampire.getFirstName() + ", really? Why didn't you tell us?");
            partyMemberSay(vampire, "Are you kidding? You'd kick me out of the party, or kill me on the spot.");
            partyMemberSay(other, "Well...");
            portraitSay("No need for that. Vampirism is an affliction which can be cured.");
            partyMemberSay(vampire, "Interesting. How does that work?");
            portraitSay("Well, it's not much different than the ritual you just witnessed. " +
                    "The incantations are different of course. Would you be willing to permit us to try to cure you from your vampirism?");
            partyMemberSay(other, "This is imparative " + vampire.getFirstName() + ". We won't be able to continue " +
                    "our work together unless you're cured of this blight.");
        } else { // vampire alone in party
            partyMemberSay(vampire, "What ever do you mean by that?");
            portraitSay("Isn't it obvious. You are a vampire.");
            partyMemberSay(vampire, "Okay I admit, I'm a vampire. What about it?");
            portraitSay("Well, vampirism is an affliction which can be cured.");
            partyMemberSay(vampire, "Interesting. How does that work?");
            portraitSay("Well, it's not much different than the ritual you just witnessed. " +
                    "The incantations are different of course. Would you be willing to permit us to try to cure you from your vampirism?");
        }
        partyMemberSay(vampire, "Hmm...");
        print("Do you let the druids try to cure you from the ritual? (Y/N) ");
        CureVampirismRitualEvent cureVampirismRitualEvent = new CureVampirismRitualEvent(model, vampire, app);
        if (yesNoInput()) {
            removePortraitSubView(model);
            cureVampirismRitualEvent.run(model);
        } else if (other != null) {
            partyMemberSay(other, "How can you refuse being cured? Are you mad?");
        }

        if (model.getParty().size() > 1 && !cureVampirismRitualEvent.wasCured()) {
            new CheckForVampireEvent(model).dealWithVampire(model, vampire, other);
        }
    }

    private void offerDruidismLessons(Model model) {
        print("Afterwards the druid offers teachings of druidism but asks for " +
                "some small compensation. ");
        model.getLog().waitForAnimationToFinish();
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.DRU);
        if (model.getParty().getGold() > 0 && change.noOfCandidates() > 0) {
            int amount = Math.min(model.getParty().getGold(), 10);
            print("Pay " + amount + " gold? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addToGold(-amount);
                change.doEvent(model);
            }
            println("You part ways with the druid.");
        } else if (change.noOfCandidates() > 0) {
            println("Since you have no money, you decline and depart the stone circle.");
        } else {
            println("But nobody in your party is suitable for such teachings. You depart the stone circle.");
        }
    }
}
