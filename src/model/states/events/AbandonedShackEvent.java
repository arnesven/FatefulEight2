package model.states.events;

import model.Model;
import view.sprites.MiniPictureSprite;

public class AbandonedShackEvent extends SalvageEvent {

    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x51);

    public AbandonedShackEvent(Model model) {
        super(model, "n abandoned shack", 10);
    }

    @Override
    protected MiniPictureSprite getMinipicSprite() {
        return SPRITE;
    }

    @Override
    protected String getMinipicSubviewText() {
        return "Abandoned Shack";
    }
}
