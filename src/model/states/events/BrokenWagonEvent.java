package model.states.events;

import model.Model;
import model.classes.Skill;
import model.states.DailyEventState;
import util.MyRandom;
import view.sprites.MiniPictureSprite;

import java.util.List;

public class BrokenWagonEvent extends SalvageEvent {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x03);

    public BrokenWagonEvent(Model model) {
        super(model, "Broken Wagon", " broken down wagon", 5);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Find broken wagon",
                "I was here recently and noticed " + getDistantDescription() + ". It may still be here");
    }

    @Override
    protected MiniPictureSprite getMinipicSprite() {
        return SPRITE;
    }

    @Override
    protected String getMinipicSubviewText() {
        return "Broken Down Wagon";
    }

    @Override
    protected List<String> getPartyMemberComments() {
        return List.of("I wonder what happened here.", "What a mess.",
                "Ambushed by raiders?", "Maybe this was a merchant caravan?");
    }
}
