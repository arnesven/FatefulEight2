package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.enemies.CompanionEnemy;
import model.enemies.Enemy;
import model.items.weapons.*;
import model.journal.JournalEntry;
import model.tasks.DeliverParcelTask;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class MakeDeliveryEvent extends GeneralInteractionEvent {
    private final DeliverParcelTask deliverTask;
    private final GameCharacter recipientCharacter;

    public MakeDeliveryEvent(Model model, DeliverParcelTask deliverParcelTask) {
        super(model, "Talk to", MyRandom.randInt(10));
        this.deliverTask = deliverParcelTask;
        CharacterAppearance portrait = deliverTask.makePortrait();
        this.recipientCharacter = new GameCharacter(deliverParcelTask.getSender() + "'s " +
                deliverTask.getRecipient(), "", portrait.getRace(),
                Classes.None, portrait);
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        println(deliverTask.getDeliveryString());

        showExplicitPortrait(model, recipientCharacter.getAppearance(),
                deliverTask.getSender() + "'s " + deliverTask.getRecipient());
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        String parcelName = deliverTask.getParcel().getName().toLowerCase();
        if (model.getParty().getLeader().hasPersonality(PersonalityTrait.friendly)) {
            println("I'm sorry to barge in on you like this, but I have something for you. It's a " +
                    parcelName + " from somebody you know.");
        } else if (model.getParty().getLeader().hasPersonality(PersonalityTrait.aggressive)) {
            println("Here, a " + parcelName + " for you. Now pay up.");
        } else {
            println("I have a delivery for you, a " + parcelName + ".");
        }
        portraitSay("Oh, yes, I've been expecting this! Thank you.");
        portraitSay("Let me find my purse so I can pay you.");
        deliverTask.receivePayment(model);
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm just a commoner. I take work where I can find it.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return recipientCharacter;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        List<Enemy> list = new ArrayList<>();
        for (int i = MyRandom.randInt(6); i > 0; --i) {
            list.add(new CompanionEnemy(PortraitSubView.makeRandomPortrait(Classes.None), Classes.None, randomWeapon()));
        }
        return list;
    }

    private Weapon randomWeapon() {
        return MyRandom.sample(List.of(new Club(), new LongStaff(), new Warhammer(), new ShortSword()));
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return null;
    }
}
