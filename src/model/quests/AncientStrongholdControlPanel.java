package model.quests;

import view.MyColors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AncientStrongholdControlPanel {
    public static final int NUMBER_OF_PEARL_SLOTS = 4;
    public static final MyColors[] PEARL_COLORS = new MyColors[]{
            MyColors.PINK, MyColors.BLUE, MyColors.GREEN,
            MyColors.BLACK, MyColors.ORANGE, MyColors.PURPLE};
    public static final MyColors[] PEARL_HIGHLIGHT_COLORS = new MyColors[]{
            MyColors.LIGHT_PINK, MyColors.LIGHT_BLUE, MyColors.LIGHT_GREEN,
            MyColors.WHITE, MyColors.YELLOW, MyColors.PINK};
    public static final MyColors[] PEARL_SHADE_COLORS = new MyColors[]{
            MyColors.LIGHT_RED, MyColors.DARK_BLUE, MyColors.DARK_GREEN,
            MyColors.BLACK, MyColors.RED, MyColors.DARK_PURPLE};
    private final AncientStrongholdModel strongholdModel;

    private MyColors[] pearlSlots = new MyColors[NUMBER_OF_PEARL_SLOTS];
    private int[] dialLevels = new int[]{0, 0, 0, 0};
    private boolean leverOn = false;

    public AncientStrongholdControlPanel(AncientStrongholdModel strongholdModel) {
        this.strongholdModel = strongholdModel;
    }

    public MyColors getPearlSlot(int index) {
        return pearlSlots[index];
    }

    public boolean isLeverOn() {
        return leverOn;
    }

    public int getDialLevel(int index) {
        return dialLevels[index];
    }

    public void pullLever() {
        leverOn = true;
        setDialsAccordingToCode();
    }

    private void setDialsAccordingToCode() {
        List<Integer> result = strongholdModel.getCorrectionForResult(pearlSlots);
        System.out.println("Correction according to code is " + result.toString());
        for (int i = 0; i < result.size(); ++i) {
            dialLevels[i] = result.get(i) + 1;
        }
    }

    public boolean canPullLever() {
        for (int i = 0; i < pearlSlots.length; ++i) {
            if (pearlSlots[i] == null) {
                return false;
            }
        }
        return true;
    }

    public void setPearl(int selectedIndex, MyColors selectedPearlColor) {
        pearlSlots[selectedIndex] = selectedPearlColor;
    }
}
