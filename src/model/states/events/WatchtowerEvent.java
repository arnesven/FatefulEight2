package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.states.DailyEventState;
import util.MyLists;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

import java.util.ArrayList;
import java.util.List;

public class WatchtowerEvent extends DailyEventState {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x24);

    public WatchtowerEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Watchtower"));
        println("Up on a hill, an ancient watchtower sits silently and " +
            "resolutely. The party climbs the stone steps to find it " +
            "completely abandoned. However, there are majestic " +
            "statues, murals with powerful imagery and the view is " +
            "spectacular. From here however, you can see all the way " +
            "to the ocean.");
        randomSayIfPersonality(PersonalityTrait.intellectual, new ArrayList<>(),
                "Fascinating artwork!");
        randomSayIfPersonality(PersonalityTrait.romantic, new ArrayList<>(),
                "These statues really look like they could come alive at any moment.");
        randomSayIfPersonality(PersonalityTrait.calm, new ArrayList<>(),
                "A good place for contemplation and meditation.");
        model.getParty().randomPartyMemberSay(model, List.of("Wow, what a view!",
                "A peacful place."));
        println("Each party member gains 25 XP!");
        MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) ->
            model.getParty().giveXP(model, gc, 25));
    }
}
