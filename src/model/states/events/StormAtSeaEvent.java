package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

public class StormAtSeaEvent extends DailyEventState {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x31);

    public StormAtSeaEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Storm"));
        println("The captain suddenly shouts out:");
        printQuote("Captain", "You better hold on and get ready to get wet! " +
                "There are some intimidating storm clouds coming toward us.");
        partyMemberSay(model.getParty().getRandomPartyMember(), "Those waves are pretty huge!");
        leaderSay("Hold oooon!");
        println("The wind suddenly shifts and the boom swings at full speed across the deck, " +
                "knocking the down the captain.");
        printQuote("Sailor", "Captain! Captain can you hear me!");
        leaderSay("Is he dead?");
        printQuote("Sailor", "No, just unconscious.");
        leaderSay("What do we do?");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (MyRandom.rollD6() < gc.getLevel()) {
                partyMemberSay(gc, "We've got to drop canvas, and bring her around to take those waves head on!");
                leaderSay("Let's get to it!");
                println("The party manages to steer the ship through the storm. In the end, you are all completely " +
                        "exhausted, but at least you survived.");
                println("Each party member loses all SP but gains 30 experience!");
                MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc2) ->
                        gc2.addToSP(-999));
                MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc2) ->
                        model.getParty().giveXP(model, gc2, 30));
                return;
            }
        }
        println("The storm is violent and the ship soon starts breaking up into pieces.");
        setFledCombat(true);
    }
}
