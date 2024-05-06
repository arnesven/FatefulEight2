package model.characters.preset;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.HairStyle3x2;
import model.characters.appearance.TopKnotHairStyle;
import view.MyColors;

public class MordKroftHairStyle extends HairStyle3x2 {
    private TopKnotHairStyle topKnotHairStyle = new TopKnotHairStyle(MyColors.WHITE, false, "Top Knot/Bald");

    public MordKroftHairStyle(int num, boolean forehead, boolean onTop, boolean inBack, boolean longInBack, String description) {
        super(num, forehead, onTop, inBack, longInBack, 0x00, 0x00, 0x00, 0x00, description);
    }

    public MordKroftHairStyle(String description) {
        this(9, true, true, false, false, description);
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

    @Override
    public int getFullBackHair() {
        return 0x32;
    }
}
