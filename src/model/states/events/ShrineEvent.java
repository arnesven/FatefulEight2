package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.items.spells.BlessSpell;
import model.states.DailyEventState;
import util.MyRandom;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

import java.util.ArrayList;
import java.util.List;

public class ShrineEvent extends DailyEventState {

    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x40);

    public ShrineEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "a shrine";
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit shrine", "We're close to " + getDistantDescription());
    }

    @Override
    protected void doEvent(Model model) {
        model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Shrine"));
        println("The party passes a small shrine to a local deity.");
        leaderSay("Maybe we should pay our respects?");
        randomSayIfPersonality(PersonalityTrait.romantic, List.of(model.getParty().getLeader()),
                "I'd like to. I like to think that someone is watching over me.");
        randomSayIfPersonality(PersonalityTrait.critical, List.of(model.getParty().getLeader()),
                "Feels like a waste of time to me.");

        List<GameCharacter> stillHavent = new ArrayList<>(model.getParty().getPartyMembers());
        while (!stillHavent.isEmpty() && model.getParty().getGold() > 0) {
            print("Would you like one of the party members to make a small offering at the shrine (cost of 1 gold)? (Y/N) ");
            if (!yesNoInput()) {
                break;
            }
            GameCharacter gc = null;
            if (stillHavent.size() > 1) {
                print("Which party member? ");
                gc = model.getParty().partyMemberInput(model, this, stillHavent.get(0));
            } else {
                gc = stillHavent.get(0);
            }
            stillHavent.remove(gc);
            model.getParty().loseGold(1);
            println(gc.getFirstName() + " places a coin in a bowl on the altar in the shrine.");
            if (gc.hasPersonality(PersonalityTrait.critical) || gc.hasPersonality(PersonalityTrait.rude)) {
                println("Then " + heOrShe(gc.getGender()) + " " +
                        MyRandom.sample(List.of("shakes " + hisOrHer(gc.getGender()) + " fist toward the sky.",
                                "sticks out " + hisOrHer(gc.getGender()) + " tongue at the little statue within the shrine.")));
            } else {
                println("Then " + heOrShe(gc.getGender()) + " " +
                        MyRandom.sample(List.of("whispers a quick prayer.",
                                "clasps " + hisOrHer(gc.getGender()) + " hands together tightly.",
                                "bows deeply to the little statue within the shrine.",
                                "stands for a second in silence.")));
                BlessSpell.applyBlessing(model, this, 3, gc);
            }
        }
        println("You leave the shrine.");
    }
}
