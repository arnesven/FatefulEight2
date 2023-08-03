package model.quests;

import model.Inventory;
import model.Model;
import model.items.Item;
import model.items.special.PearlItem;
import model.states.QuestState;
import util.MyStrings;
import view.MyColors;
import view.subviews.ArrowMenuSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AncientStrongholdControlPanel {
    public static final int NUMBER_OF_PEARL_SLOTS = 4;
    public static final MyColors[] PEARL_COLORS = new MyColors[]{
            MyColors.PINK, MyColors.BLUE, MyColors.GREEN,
            MyColors.BLACK, MyColors.ORANGE, MyColors.PURPLE,
            MyColors.DARK_RED};
    public static final MyColors[] PEARL_HIGHLIGHT_COLORS = new MyColors[]{
            MyColors.LIGHT_PINK, MyColors.LIGHT_BLUE, MyColors.LIGHT_GREEN,
            MyColors.WHITE, MyColors.YELLOW, MyColors.PINK,
            MyColors.RED};
    public static final MyColors[] PEARL_SHADE_COLORS = new MyColors[]{
            MyColors.LIGHT_RED, MyColors.DARK_BLUE, MyColors.DARK_GREEN,
            MyColors.BLACK, MyColors.RED, MyColors.DARK_PURPLE,
            MyColors.GRAY_RED};
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

    private void toggleLever() {
        leverOn = !leverOn;
    }

    private void setDialsAccordingToCode() {
        List<Integer> result = strongholdModel.getCorrectionForResult(pearlSlots);
        Collections.sort(result);
        System.out.println("Correction according to code is " + result.toString());
        for (int i = 0; i < result.size(); ++i) {
            dialLevels[i] = result.get(i) + 1;
        }
    }

    private boolean canPullLever() {
        for (int i = 0; i < pearlSlots.length; ++i) {
            if (pearlSlots[i] == null) {
                return false;
            }
        }
        return true;
    }

    private void setPearl(int selectedIndex, MyColors selectedPearlColor) {
        pearlSlots[selectedIndex] = selectedPearlColor;
    }

    private boolean isCodeUnique() {
        for (int i = 0; i < pearlSlots.length; ++i) {
            for (int j = i+1; j < pearlSlots.length; ++j) {
                if (pearlSlots[i] == pearlSlots[j] && pearlSlots[i] != MyColors.DARK_RED) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean codeIsCorrect() {
        for (int i = 0; i < pearlSlots.length; ++i) {
            if (dialLevels[i] != 3) {
                return false;
            }
        }
        return true;
    }

    public boolean handleLever(Model model, QuestState state, boolean isElevator) {
        if (isLeverOn()) {
            if (isCodeUnique()) {
                state.println("The lever seems to be locked into position.");
            } else {
                state.println("You pull the lever up again.");
                toggleLever();
            }
        } else {
            if (canPullLever()) {
                toggleLever();
                state.print("You pull the lever down. ");
                if (isCodeUnique()) {
                    state.println("The machine hums curiously.");
                    setDialsAccordingToCode();
                    if (codeIsCorrect()) {
                        if (isElevator) {
                            state.println("Suddenly a hidden door to your left opens!");
                        } else {
                            state.println("The strange machine is now working. The cogs are turning, " +
                                    "pistons are pumping and it's making quite a lot of noise.");
                            model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                                    "Most intriguing. I guess the combination was right. But how does this help us?");
                        }
                    } else if (isElevator) {
                        state.println("Suddenly a large trap door in the floor opens below your feet! " +
                                "The shute promptly whisks your party out of the stronghold.");
                        return false;
                    } else {
                        model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                                "I don't think this combination of pearls is right...");
                    }
                } else {
                    state.println("The machine is silent. Hmm, is there something wrong with this combination of pearls?");
                }
            } else {
                state.println("The lever seems to be locked into position. Perhaps something is missing?");
            }
        }
        return true;
    }

    public void handlePearl(Model model, QuestState state, int selectedIndex) {
        if (getPearlSlot(selectedIndex) != null && isLeverOn()) {
            state.println("The pearl cannot be removed. Something is holding it firmly in place.");
        } else { // Set or replace pearl
            if (model.getParty().getInventory().getSpecialItems().isEmpty()) {
                state.println("A pearl clearly goes in this slot, but you have none to put in it.");
            } else {
                MyColors selectedPearlColor = getSelectedPearlColor(model, state, selectedIndex, getPearlSlot(selectedIndex) != null);
                MyColors previousColor = getPearlSlot(selectedIndex);
                setPearl(selectedIndex, selectedPearlColor);
                if (selectedPearlColor == null) {
                    state.println("You remove the pearl from the slot.");
                    model.getParty().getInventory().addSpecialItem(PearlItem.makeFromColor(previousColor));
                } else {
                    state.println("You place the pearl into the slot.");
                    if (previousColor != null) {
                        model.getParty().getInventory().addSpecialItem(PearlItem.makeFromColor(previousColor));
                    }
                    Item found = null;
                    for (Item it : model.getParty().getInventory().getSpecialItems()) {
                        if (it.getName().contains(getPearlNameForColor(selectedPearlColor))) {
                            found = it;
                        }
                    }
                    model.getParty().getInventory().removeSpecialItem((PearlItem) found);
                }
            }
        }
    }

    private MyColors getSelectedPearlColor(Model model, QuestState state, int xShift, boolean remove) {
        List<String> optionList = new ArrayList<>();
        for (MyColors color : PEARL_COLORS) {
            String name = getPearlNameForColor(color);
            int count = countNumberOf(model.getParty().getInventory().getSpecialItems(), name);
            if (count > 0) {
                optionList.add(name + " (" + count + ")");
            }
        }
        if (remove) {
            optionList.add("Remove");
        }
        int[] selectedAction = new int[1];
        model.setSubView(new ArrowMenuSubView(model.getSubView(),
                optionList, SubView.X_MAX - 10 + xShift*4, 22, ArrowMenuSubView.NORTH_WEST) {
            @Override
            protected void enterPressed(Model model, int cursorPos) {
                selectedAction[0] = cursorPos;
                model.setSubView(getPrevious());
            }
        });
        state.waitForReturnSilently();
        if (selectedAction[0] == optionList.size()-1 && remove) {
            return null;
        }
        for (MyColors color : PEARL_COLORS) {
            if (optionList.get(selectedAction[0]).contains(getPearlNameForColor(color))) {
                return color;
            }
        }
        throw new IllegalStateException("Could not find selected option in list.");
    }

    private String getPearlNameForColor(MyColors color) {
        String name = MyStrings.capitalize(color.name());
        if (color == MyColors.DARK_RED) {
            name = "Crimson";
        }
        return name;
    }

    private int countNumberOf(List<Item> items, String name) {
        int count = 0;
        for (Item it : items) {
            if (it.getName().contains(name)) {
                count++;
            }
        }
        return count;
    }

    public String getPearlNameForSlot(int cursorPos) {
        return getPearlNameForColor(getPearlSlot(cursorPos));
    }
}
