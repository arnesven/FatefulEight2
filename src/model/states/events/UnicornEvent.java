package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.horses.Unicorn;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

import java.util.List;

public class UnicornEvent extends DailyEventState {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x13);

    public UnicornEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Unicorn"));
        println("A shimmering horse-like creature approaches the party. " +
                "A long spiraling horn protrudes from its forehead. It " +
                "seems perfectly tame and gladly lets the party members " +
                "stroke its mane.");
        model.getParty().randomPartyMemberSay(model, List.of("It's beautiful!3", "Magnificent creature...",
                "Hey there pal...3", "Magical...", "I feel like I'm dreaming."));
        MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) -> gc.addToSP(100));
        model.getParty().randomPartyMemberSay(model, List.of("I feel so refreshed.3",
                "Weird. It's like I've woken up from a long troubled sleep.",
                "I feel so good.3"));
        println("The unicorn seems very friendly and is appraising the party.");
        calculatePartyAlignment(model, this);
        if (getPartyAlignment(model) > 0 && MyRandom.rollD10() > 8) {
            println("The unicorn seems to have taken to you and allows members of your party to mount it.");
            println("You have gained a horse, the unicorn. The unicorn is a steed.");
            model.getParty().getHorseHandler().addHorse(new Unicorn());
        } else {
            println("The unicorn neighs softly and then trots away into the bushes.");
        }
    }
}
