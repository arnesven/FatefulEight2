package model.characters.preset;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.HairStyle3x2;
import model.characters.appearance.TopKnotHairStyle;
import view.MyColors;

public class MordKroftHairStyle extends HairStyle3x2 {
    private TopKnotHairStyle topKnotHairStyle = new TopKnotHairStyle(MyColors.WHITE, false);

    public MordKroftHairStyle(int num, boolean forehead, boolean onTop, boolean inBack, boolean longInBack) {
        super(num, forehead, onTop, inBack, longInBack, 0x00, 0x00);
    }

    public MordKroftHairStyle() {
        this(9, true, true, false, false);
    }

    @Override
    public void apply(AdvancedAppearance appearance) {
        super.apply(appearance);
        topKnotHairStyle.apply(appearance);
    }

    @Override
    public int getNormalHair() {
        return 0x22;
    }
}
