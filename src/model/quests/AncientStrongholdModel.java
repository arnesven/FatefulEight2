package model.quests;

import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class AncientStrongholdModel {

    private static final Integer FULL = 2;
    private static final Integer HALF = 1;
    private static final Integer WRONG = 0;
    private final MyColors[] code;
    private List<AncientStrongholdControlPanel> controlPanels = new ArrayList<>();

    public AncientStrongholdModel() {
        controlPanels.add(new AncientStrongholdControlPanel(this));
        this.code = new MyColors[]{MyColors.BLACK, MyColors.BLUE, MyColors.ORANGE, MyColors.BLACK};
    }

    public AncientStrongholdControlPanel getControlPanel(int index) {
        return controlPanels.get(index);
    }

    public List<Integer> getCorrectionForResult(MyColors[] pearlSlots) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < pearlSlots.length; ++i) {
            if (pearlSlots[i] == code[i]) {
                result.add(FULL);
            } else if (exists(pearlSlots[i])) {
                result.add(HALF);
            } else {
                result.add(WRONG);
            }
        }
        return result;
    }

    private boolean exists(MyColors pearlSlot) {
        for (MyColors colors : code) {
            if (pearlSlot == colors) {
                return true;
            }
        }
        return false;
    }
}
