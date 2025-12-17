package model.states.events;

import model.Model;
import view.sprites.MiniPictureSprite;

import java.util.List;

public class AbandonedShackEvent extends SalvageEvent {

    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x51);

    public AbandonedShackEvent(Model model) {
        super(model, "Abandoned Shack", "n abandoned shack", 10);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Find abandoned shack",
                "I was here recently and noticed " + getDistantDescription() + ".");
    }

    @Override
    protected MiniPictureSprite getMinipicSprite() {
        return SPRITE;
    }

    @Override
    protected String getMinipicSubviewText() {
        return "Abandoned Shack";
    }

    @Override
    protected List<String> getPartyMemberComments() {
        return List.of("Looks like nobody's home.", "Nobody has been living here for some time.",
                "What a dump.", "I wonder who lived here.");
    }
}
