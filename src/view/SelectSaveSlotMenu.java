package view;

import model.CorruptSaveFileException;
import model.GameData;
import model.Model;
import model.characters.GameCharacter;
import sound.SoundEffects;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SelectSaveSlotMenu extends SelectableListMenu {
    private static final String FILE_POSTFIX = "_save.ff8";
    private final boolean loading;
    private String[] slotNames = new String[]{"auto", "slot1", "slot2", "slot3"};
    private GameData[] gameDatas = new GameData[slotNames.length];
    private boolean[] corruptData = new boolean[slotNames.length];
    private GameView mainGameView;

    public SelectSaveSlotMenu(GameView startGameMenu, boolean loading) {
        super(startGameMenu, DrawingArea.WINDOW_COLUMNS - 1,
                DrawingArea.WINDOW_ROWS - 1);
        this.loading = loading;
    }

    @Override
    public void transitionedFrom(Model model) {
        
    }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
        for (int i = 0; i < slotNames.length; ++i) {
            corruptData[i] = false;
            try {
                gameDatas[i] = Model.readGameData(slotNames[i] + FILE_POSTFIX);
            } catch (FileNotFoundException fnfe) {
                // No savefile found for that slot.
            } catch (CorruptSaveFileException e) {
                corruptData[i] = true;
            }
        }
        update(model);
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
        return List.of(new TextDecoration((loading?"Load from":"Save to") + " which slot?", xStart+1, yStart+1, MyColors.WHITE, MyColors.BLUE, true));
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> result = new ArrayList<>();
        int row = 3;
        int index = 0;
        for (String slotName : slotNames) {
            String fileName = slotName + FILE_POSTFIX;
            GameData data = gameDatas[index];
            boolean corrupt = corruptData[index];
            result.add(new SelectableListContent(xStart + 4, yStart + row, slotName) {
                @Override
                public void performAction(Model model, int x, int y) {
                    if (loading) {
                        model.prepareForStartGameFromSave(fileName);
                        mainGameView = new LoadingGameView();
                    } else { // saving
                        if (data != null) {
                            model.transitionToDialog(new YesNoMessageView(SelectSaveSlotMenu.this, "Are you sure you want to overwrite this save?") {
                                @Override
                                protected void doAction(Model model) {
                                    model.saveToFile(slotName);
                                    SoundEffects.gameSaved();
                                }
                            });
                        } else {
                            model.saveToFile(slotName);
                            SoundEffects.gameSaved();
                        }
                    }
                    setTimeToTransition(true);
                }

                @Override
                public boolean isEnabled(Model model) {
                    return !loading || (!corrupt && data != null);
                }

                @Override
                public void drawDecorations(Model model, int x, int y) {
                    if (data != null) {
                        int seconds = (int) (data.milliSecondsPlayed / 1000);
                        int hours = seconds / (60 * 60);
                        seconds -= hours * (60 * 60);
                        int minutes = seconds / 60;
                        seconds -= minutes * 60;
                        String title = data.party.getLeader().getName() + "'s Company, DAY " + data.day + ". Time " +
                                String.format("%d:%02d:%02d", hours, minutes, seconds);
                        BorderFrame.drawString(model.getScreenHandler(), title, x + 7, y, MyColors.WHITE, MyColors.BLUE);
                        int xShift = 7;
                        for (GameCharacter gc : data.party.getPartyMembers()) {
                            gc.drawAppearance(model.getScreenHandler(), x + xShift, y + 1);
                            xShift += 7;
                        }
                    } else {
                        if (corrupt) {
                            String title = "* Data has bad version or is corrupt! *";
                            BorderFrame.drawString(model.getScreenHandler(), title, x + 7, y, MyColors.RED, MyColors.BLUE);
                        } else {
                            String title = "* No Data *";
                            BorderFrame.drawString(model.getScreenHandler(), title, x + 7, y, MyColors.LIGHT_GRAY, MyColors.BLUE);
                        }
                    }
                }
            });
            index++;
            row += 11;
        }
        result.add(new SelectableListContent(DrawingArea.WINDOW_COLUMNS/2-3, row-1, loading?"CANCEL":"RETURN") {
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
