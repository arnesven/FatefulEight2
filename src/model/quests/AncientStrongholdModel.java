package model.quests;

import view.MyColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AncientStrongholdModel {

    public static final int FLOORS = 7;
    private static final Integer FULL = 2;
    private static final Integer HALF = 1;
    private static final Integer WRONG = 0;
    private MyColors[] code;
    private List<AncientStrongholdControlPanel> controlPanels = new ArrayList<>();

    public AncientStrongholdModel() {
        for (int i = 0; i < FLOORS; ++i) {
            controlPanels.add(new AncientStrongholdControlPanel(this));
        }
    }

    public static MyColors[] generateCode() {
        List<MyColors> list = new ArrayList<>(Arrays.asList(AncientStrongholdControlPanel.PEARL_COLORS));
        list.remove(MyColors.DARK_RED);
        Collections.shuffle(list);
        MyColors[] code = new MyColors[AncientStrongholdControlPanel.NUMBER_OF_PEARL_SLOTS];
        System.out.print("Ancient stronghold code is ");
        for (int i = 0; i < code.length; ++i) {
            code[i] = list.get(i);
            System.out.print(code[i].name() + ", ");
        }
        System.out.println(" ");
        return code;
    }

    public void setCode(MyColors[] code) {
        this.code = code;
    }

    public AncientStrongholdControlPanel getControlPanel(int index) {
        return controlPanels.get(index);
    }

    public List<Integer> getCorrectionForResult(MyColors[] pearlSlots) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < pearlSlots.length; ++i) {
            if (pearlSlots[i] == code[i] || pearlSlots[i] == MyColors.DARK_RED) {
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
