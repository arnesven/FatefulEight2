package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.NoEventState;
import view.MyColors;
import view.sprites.PastLandHexSprite;
import view.sprites.Sprite;
import view.subviews.SubView;

import java.awt.*;

public abstract class PastLandHex extends WorldHex {
    private final WorldHex template;

    public PastLandHex(WorldHex template, MyColors color, int roads,
                       int rivers, HexLocation loc, int state) {
        super(color, roads, rivers, loc, state);
        this.template = template;
    }

    protected Sprite getUpperLeftSprite(MyColors color, int roads, int rivers) {
        return new PastLandHexSprite(getTerrainName()+"ul", 0x00 + (roads % 4)*4 + rivers % 4, color);
    }

    protected Sprite getUpperRightSprite(MyColors color, int roads, int rivers) {
        return new PastLandHexSprite(getTerrainName()+"ur", 0x20  + ((roads / 4) % 4)*4 + (rivers / 4) % 4, color);
    }

    protected Sprite getLowerLeftSprite(MyColors color, int roads, int rivers) {
        return new PastLandHexSprite(getTerrainName()+"ll", 0x10 + (roads / 64)*4 + (rivers / 64), color);
    }

    protected Sprite getLowerRightSprite(MyColors color, int roads, int rivers) {
        return new PastLandHexSprite(getTerrainName()+"lr", 0x30 + ((roads / 16) % 4) * 4 + (rivers / 16) % 4, color);
    }

    @Override
    public String getTerrainName() {
        if (template == null) {
            return "pastterrain";
        }
        return template.getTerrainName();
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        DailyEventState event;
        for (int i = 0; i < 10; ++i) {
            event = template.generateEvent(model);
            if (!event.exclusiveToOriginalWorld()) {
                return event;
            }
        }
        for (int i = 0; i < 10; ++i) {
            event = generateTerrainSpecificEvent(model);
            if (!event.exclusiveToOriginalWorld()) {
                return event;
            }
        }
        return new NoEventState(model);
    }

    @Override
    protected DailyEventState generateTerrainSpecificEvent(Model model) {
        return template.generateTerrainSpecificEvent(model);
    }

    @Override
    protected SubView getSubView() {
        return template.getSubView();
    }

    @Override
    public String getTerrainDescription() {
        return template.getTerrainDescription();
    }

    @Override
    public ResourcePrevalence getResourcePrevalences() {
        return template.getResourcePrevalences();
    }

    @Override
    public WorldHex makePastSelf(Point position) {
        throw new IllegalStateException("Should not make past on past!");
    }
}
