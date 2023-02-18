package view;

import model.CorruptSaveFileException;
import model.GameData;
import model.Model;
import model.characters.GameCharacter;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectSaveSlotMenu extends SelectableListMenu {
    private static final String FILE_POSTFIX = "_save.ff8";
    private String[] slotNames = new String[]{"auto", "slot1"};
    private GameData[] gameDatas = new GameData[slotNames.length];
    private GameView mainGameView;

    public SelectSaveSlotMenu(GameView startGameMenu) {
        super(startGameMenu, 64, 24);
    }

    @Override
    public void transitionedFrom(Model model) {
        
    }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
        for (int i = 0; i < slotNames.length; ++i) {
            try {
                gameDatas[i] = Model.readGameData(slotNames[i] + FILE_POSTFIX);
            } catch (IOException | ClassNotFoundException e) {

            }
        }
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
        int index = 0;
        for (String slotName : slotNames) {
            String fileName = slotName + FILE_POSTFIX;
            GameData data = gameDatas[index];
            if (new File(fileName).exists()) {
                result.add(new SelectableListContent(xStart + 1, yStart + row, slotName) {
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

                    @Override
                    public void drawDecorations(Model model, int x, int y) {
                        if (data != null) {
                            String title = data.party.getLeader().getName() + "'s Company, DAY " + data.day + ", REP " + data.party.getReputation();
                            BorderFrame.drawString(model.getScreenHandler(), title, x + 7, y, MyColors.WHITE, MyColors.BLUE);
                            int xShift = 7;
                            for (GameCharacter gc : data.party.getPartyMembers()) {
                                gc.drawAppearance(model.getScreenHandler(), x + xShift, y + 1);
                                xShift += 7;
                            }
                        }
                    }
                });
            }
            index++;
            row += 10;
        }
        result.add(new SelectableListContent(40-3, yStart + 22, "CANCEL") {
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
