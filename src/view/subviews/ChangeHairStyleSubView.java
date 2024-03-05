package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.HairStyle;
import model.states.events.SimpleTunicPortraitClothing;
import util.Arithmetics;
import view.BorderFrame;
import view.MyColors;
import view.sprites.ArrowSprites;

import java.awt.event.KeyEvent;

public class ChangeHairStyleSubView extends SubView {
    private final GameCharacter character;
    private final AdvancedAppearance appearanceCopy;
    private final HairStyle[] hairStyleSet;
    private final MyColors[] colorSet;
    private int selectedColor;
    private int selectedRow;
    private int selectedHairstyle;
    private boolean isDone = false;

    public ChangeHairStyleSubView(Model model, GameCharacter gc) {
        this.character = gc;
        this.appearanceCopy = (AdvancedAppearance)(character.getAppearance().copy());
        this.selectedHairstyle = 0;
        this.selectedRow = 0;
        this.hairStyleSet = new HairStyle[HairStyle.allHairStyles.length+1];
        hairStyleSet[0] = appearanceCopy.getHairStyle();
        for (int i = 1; i < hairStyleSet.length; i++) {
            hairStyleSet[i] = HairStyle.allHairStyles[i-1];
        }
        this.colorSet = HairStyle.allHairColors;
        this.selectedColor = 0;
        for (MyColors col : colorSet) {
            if (col == appearanceCopy.getHairColor()) {
                break;
            }
            selectedColor++;
        }
        setSelectedHairStyle();
    }

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX, blueBlock);
        BorderFrame.drawCentered(model.getScreenHandler(), "New Hairstyle:", Y_OFFSET + 3, MyColors.WHITE, MyColors.BLUE);
        appearanceCopy.drawYourself(model.getScreenHandler(), X_OFFSET+12, Y_OFFSET+5);

        model.getScreenHandler().put(X_OFFSET + 10, Y_OFFSET + 7, ArrowSprites.LEFT);
        model.getScreenHandler().put(X_OFFSET + 20, Y_OFFSET + 7, ArrowSprites.RIGHT);

        {
            MyColors fgColor = selectedRow == 0 ? MyColors.BLACK : MyColors.YELLOW;
            MyColors bgColor = selectedRow == 0 ? MyColors.LIGHT_YELLOW : MyColors.BLUE;
            BorderFrame.drawCentered(model.getScreenHandler(), hairStyleSet[selectedHairstyle].getDescription(),
                    Y_OFFSET + 13, fgColor, bgColor);
        }

        {
            MyColors fgColor = selectedRow == 1 ? MyColors.BLACK : MyColors.YELLOW;
            MyColors bgColor = selectedRow == 1 ? MyColors.LIGHT_YELLOW : MyColors.BLUE;
            BorderFrame.drawCentered(model.getScreenHandler(), colorSet[selectedColor].toString().replace("_", " "),
                    Y_OFFSET + 15, fgColor, bgColor);
        }

        {
            MyColors fgColor = selectedRow == 2 ? MyColors.BLACK : MyColors.YELLOW;
            MyColors bgColor = selectedRow == 2 ? MyColors.LIGHT_YELLOW : MyColors.BLUE;
            BorderFrame.drawCentered(model.getScreenHandler(), "DONE",
                    Y_OFFSET + 17, fgColor, bgColor);
        }

    }

    @Override
    protected String getUnderText(Model model) {
        return character.getFirstName() + " is getting a new haircut.";
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENT - BARBER SHOP";
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            if (selectedRow == 0) {
                selectedHairstyle = Arithmetics.decrementWithWrap(selectedHairstyle, hairStyleSet.length);
            } else {
                selectedColor = Arithmetics.decrementWithWrap(selectedColor, colorSet.length);
            }
            setSelectedHairStyle();
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (selectedRow == 0) {
                selectedHairstyle = Arithmetics.incrementWithWrap(selectedHairstyle, hairStyleSet.length);
            } else {
                selectedColor = Arithmetics.incrementWithWrap(selectedColor, colorSet.length);
            }
            setSelectedHairStyle();
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            selectedRow = Arithmetics.decrementWithWrap(selectedRow, 3);
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            selectedRow = Arithmetics.incrementWithWrap(selectedRow, 3);
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            if (selectedRow == 2) {
                this.isDone = true;
                return false;
            }
            return true;
        }
        return super.handleKeyEvent(keyEvent, model);
    }

    private void setSelectedHairStyle() {
        appearanceCopy.setHairStyle(hairStyleSet[selectedHairstyle]);
        appearanceCopy.setHairColor(colorSet[selectedColor]);
        appearanceCopy.setClass(character.getCharClass());
        appearanceCopy.setSpecificClothing(new SimpleTunicPortraitClothing());
    }

    public boolean isDone() {
        return isDone;
    }

    public AdvancedAppearance getFinalAppearance() {
        return appearanceCopy;
    }
}
