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

public class MonumentEvent extends DailyEventState {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x22);

    public MonumentEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "some kind of monument";
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit monument", "There's an interesting monument not far from here");
    }

    @Override
    protected void doEvent(Model model) {
        model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Monument"));
        println("You see several large stone statues, adorned with plaques. This " +
                "monument was erected to honor the memory of a group " +
                "of heroes who once saved this world from a terrible fate. " +
                "One plaque has a detailed account of how the world was " +
                "saved from eternal damnation.");
        randomSayIfPersonality(PersonalityTrait.intellectual, new ArrayList<>(), "Fascinating...");
        randomSayIfPersonality(PersonalityTrait.rude, List.of(model.getParty().getLeader()), "Boring...");
        if (model.getParty().size() > 1) {
            println("Reading the text inspires dialogue and contemplation among the party members.");
        } else {
            println("Reading the text inspires you with a desire to do great deeds.");
        }
        println("Each party member gains 25 experience!");
        model.getLog().waitForAnimationToFinish();
        MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) ->
            model.getParty().giveXP(model, gc, 25));
    }
}
