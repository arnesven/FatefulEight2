package view;

import model.Model;
import sound.BackgroundMusic;
import sound.ClientSound;
import sound.ClientSoundManager;
import util.Arithmetics;
import view.party.DrawableObject;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.List;

public class JukeboxDialog extends SelectableListMenu {

    private static final int COLUMN_SKIP = 15;
    private int selected;
    private BackgroundMusic[] songs = BackgroundMusic.values();

    public JukeboxDialog(GameView view) {
        super(view, 40, 16);
        this.selected = 0;
    }

    @Override
    public void transitionedTo(Model model) {
        playSelectedSong();
    }

    private void playSelectedSong() {
        ClientSoundManager.playBackgroundMusic(songs[selected]);
    }

    @Override
    public void transitionedFrom(Model model) { }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        return List.of(new DrawableObject(xStart+1, yStart+1) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                String title = "- JUKEBOX -";
                BorderFrame.drawCentered(model.getScreenHandler(), title, y, MyColors.WHITE, MyColors.BLUE);
                y += 3;
                BorderFrame.drawString(model.getScreenHandler(), "Song:", x + 1, y,
                        MyColors.WHITE, MyColors.BLUE);
                y += 2;
                BorderFrame.drawString(model.getScreenHandler(), "File Name:", x + 1, y,
                        MyColors.WHITE, MyColors.BLUE);
                BorderFrame.drawString(model.getScreenHandler(), songs[selected].getFileName() + ".mp3", xStart + COLUMN_SKIP, y,
                        MyColors.WHITE, MyColors.BLUE);
                y +=2;
                BorderFrame.drawString(model.getScreenHandler(), "Volume:", x + 1, y,
                        MyColors.WHITE, MyColors.BLUE);

            }
        });
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 4, String.format("%-16s", songs[selected].name())) {

                    @Override
                    public void turnLeft(Model model) {
                        selected = Arithmetics.decrementWithWrap(selected, songs.length);
                    }

                    @Override
                    public void turnRight(Model model) {
                        selected = Arithmetics.incrementWithWrap(selected, songs.length);
                    }
                },
                new CarouselListContent(xStart + COLUMN_SKIP, yStart + 8, String.format("%3.1f", songs[selected].getVolume().amplitude)) {

                    @Override
                    public void turnLeft(Model model) {
                        songs[selected].getVolume().amplitude -= 0.5f;
                        ClientSoundManager.changeSoundVolume(songs[selected].getFileName(), songs[selected].getVolume());
                    }

                    @Override
                    public void turnRight(Model model) {
                        songs[selected].getVolume().amplitude += 0.5f;
                        ClientSoundManager.changeSoundVolume(songs[selected].getFileName(), songs[selected].getVolume());
                    }
                }
        );
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setTimeToTransition(true);
        } else {
            List<ListContent> content = buildContent(model, 0, 0);
            if (content.get(getSelectedRow()) instanceof CarouselListContent) {
                CarouselListContent carousel = (CarouselListContent) content.get(getSelectedRow());
                if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                    carousel.turnLeft(model);
                    madeChanges();

                } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                    carousel.turnRight(model);
                    madeChanges();
                }
            }
            if (getSelectedRow() == 0) {
                playSelectedSong();
            }
        }
    }
}
