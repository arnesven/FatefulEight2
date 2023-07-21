package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class PartyJokeEvent extends DailyEventState {
    private static final List<String> JOKE_LIST = List.of(
            "What's the difference between an elf and a half-elf? One swing of my axe!",
            "I just broke up with my 10 foot girlfriend. I can't believe it's all ogre.",
            "This small green man broke into my pantry and started eating all my rations. He was gobblin'!",
            "Can you spare some gold for a half-ling? Nah, sorry, I'm a little short!",
            "Dragon bosses are horrible! They're notorious for firing employees!");

    public PartyJokeEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().size() < 2) {
            new NoEventState(model).doEvent(model);
            return;
        }

        GameCharacter joker = model.getParty().getRandomPartyMember();
        println(joker.getFirstName() + " is telling a joke.");
        model.getParty().partyMemberSay(model, joker, JOKE_LIST);
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc != joker) {
                int diff = MyRandom.rollD10() - 5;
                if (diff >= 0) {
                    println(gc.getFirstName() + " enjoyed the joke.");
                    gc.addToAttitude(joker, diff);
                    joker.addToAttitude(gc, diff/2);
                    model.getParty().partyMemberSay(model, gc,
                            List.of("Hehehe", "Funny!", "That's good!"));
                } else {
                    println(gc.getFirstName() + " didn't enjoy the joke.");
                    gc.addToAttitude(joker, -diff);
                    joker.addToAttitude(gc, -diff/2);
                }
            }
        }
        model.getLog().waitForAnimationToFinish();
        showPartyAttitudesSubView(model);
    }
}
