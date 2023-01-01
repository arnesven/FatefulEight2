package view;

import model.CorruptSaveFileException;
import model.Model;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SelectSaveSlotMenu extends SelectableListMenu {
    private String[] slotNames = new String[]{"auto", "slot1"};
    private GameView mainGameView;

    public SelectSaveSlotMenu(GameView startGameMenu) {
        super(startGameMenu, 18, 7);
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    public GameView getNextView(Model model) {
        if (mainGameView != null) {
            return mainGameView;
        }
        return super.getNextView(model);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        if (buildContent(model, xStart, yStart).size() == 1) {
            return List.of(new TextDecoration("No saves found!", xStart+1, yStart+1, MyColors.WHITE, MyColors.BLUE, true));
        }
        return List.of(new TextDecoration("Which Slot?", xStart+1, yStart+1, MyColors.WHITE, MyColors.BLUE, true));
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> result = new ArrayList<>();
        int row = 2;
        for (String slotName : slotNames) {
            String fileName = slotName + "_save.ff8";
            if (new File(fileName).exists()) {
                result.add(new SelectableListContent(xStart + 1, yStart + (row++), slotName) {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        try {
                            model.startGameFromSave(fileName);
                            setTimeToTransition(true);
                            mainGameView = new MainGameView();
                        } catch (FileNotFoundException e) {
                            model.transitionToDialog(new SimpleMessageView(SelectSaveSlotMenu.this, "No save file found."));
                            //setTimeToTransition(true);
                        } catch (CorruptSaveFileException csfe) {
                            model.transitionToDialog(new SimpleMessageView(SelectSaveSlotMenu.this, "Save file is incompatible or corrupt. Loading aborted."));
                        }
                    }
                });
            }
        }
        result.add(new SelectableListContent(xStart + 6, yStart + 6, "CANCEL") {
            @Override
            public void performAction(Model model, int x, int y) {
                setTimeToTransition(true);
            }
        });
        return result;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
