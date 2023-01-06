package model.states.dailyaction;

import model.Model;
import view.sprites.Sprite;
import view.subviews.TavernSubView;

public class InnShoppingNode extends ShoppingNode {
    public InnShoppingNode(Model model) {
        super(model);
    }

    @Override
    public String getName() {
        return "Merchant";
    }

    @Override
    public Sprite getBackgroundSprite() {
        return TavernSubView.FLOOR;
    }

    @Override
    public Sprite getForegroundSprite() {
        return null;
    }
}
