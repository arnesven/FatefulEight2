package view;

import model.HallOfFameData;
import model.HallOfFameEntry;
import model.Model;
import model.characters.GameCharacter;
import view.sprites.AvatarSprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HallOfFameView extends GameView {
    private HallOfFameData hallOfFameData;
    private static final MyColors[] colors = new MyColors[]{MyColors.RED, MyColors.ORANGE, MyColors.YELLOW,
            MyColors.LIGHT_GREEN, MyColors.GREEN, MyColors.LIGHT_BLUE, MyColors.BLUE, MyColors.PURPLE};

    public HallOfFameView(Model model) {
        super(false);
        this.hallOfFameData = model.loadHallOfFameData();
    }

    @Override
    public void transitionedTo(Model model) {


    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected void internalUpdate(Model model) {
        model.getScreenHandler().clearAll();
        BorderFrame.drawCentered(model.getScreenHandler(), "Hall of Fame", 3, MyColors.WHITE);
        int rank = 1;
        int xStart = 12;
        if (hallOfFameData != null) {
            for (HallOfFameEntry entry : hallOfFameData) {
                String line = String.format("%2d. %-40s %6d", rank, entry.getName() + "'s Company", entry.getScore());
                MyColors color = MyColors.WHITE;
                if (rank <= colors.length) {
                    color = colors[rank-1];
                }
                BorderFrame.drawString(model.getScreenHandler(), line,
                        xStart, 5 + 5*rank, color);
                int x = xStart + 3;
                for (AvatarSprite gc : entry.getCharacters()) {
                    model.getScreenHandler().register(gc.getName(), new Point(x, 6 + 5*rank), gc);
                    x += 4;
                }
                rank++;
            }

        }
    }

    @Override
    public GameView getNextView(Model model) {
        return new StartGameMenu();
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
        setTimeToTransition(true);
    }
}
