package view;

import model.Model;
import view.help.HelpView;
import view.widget.TopText;

import java.awt.event.KeyEvent;

public class LogView extends GameView {
    private TopText topText = new TopText();
    private GameView nextView;
    private GameView previousView;
    private int scroll = 0;

    public LogView(GameView previous) {
        super(false);
        previousView = previous;
        nextView = previous;
    }

    @Override
    public void transitionedTo(Model model) {
        update(model);
        model.getScreenHandler().setForegroundEnabled(false);
        setTimeToTransition(false);
    }

    @Override
    public void transitionedFrom(Model model) {
        model.getScreenHandler().setForegroundEnabled(true);
    }

    @Override
    protected void internalUpdate(Model model) {
        model.getScreenHandler().clearAll();
        BorderFrame.drawFrameTop(model.getScreenHandler());
        topText.drawYourself(model);
        drawLog(model, DrawingArea.WINDOW_ROWS-2, 2, scroll);
    }

    public static void drawLog(Model model, int totalRows, int rowOffset, int scroll) {
        int row = Math.min(totalRows, model.getLog().size()) - 1 + rowOffset;
        int count = 0;
        for (String s : model.getLog().getContents()) {
            count++;
            if (count < scroll+1) {
                continue;
            }
            BorderFrame.drawString(model.getScreenHandler(), s, 0, row--, getColorForLine(s));
            if (count == totalRows + scroll) {
                break;
            }
        }
    }

    private static MyColors getColorForLine(String s) {
        if (s.startsWith("!")) {
            return MyColors.LIGHT_RED;
        } else if (s.contains("DAY")) {
            return MyColors.CYAN;
        } else if (s.contains("\"")) {
            return MyColors.YELLOW;
        } else if (s.contains("Autosaving")) {
            return MyColors.GRAY;
        }
        return MyColors.LIGHT_GREEN;
    }

    @Override
    public GameView getNextView(Model model) {
        return nextView;
    }

    @Override
    public void handleKeyEvent(KeyEvent keyEvent, Model model) {
         if (keyEvent.getKeyCode() == KeyEvent.VK_F2) {
             nextView = previousView;
            setTimeToTransition(true);
         }  if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setTimeToTransition(true);
            nextView = new MenuView(this);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_F1) {
            setTimeToTransition(true);
            nextView = new HelpView(this);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
             this.scroll++;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
             if (scroll > 0) {
                 this.scroll--;
             }
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_PAGE_UP) {
             this.scroll += 20;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
             this.scroll = Math.max(this.scroll - 20, 0);
        }

         if (model.getLog().isAcceptingInput()) {
            model.getLog().keyTyped(keyEvent, model);
        }
    }
}
