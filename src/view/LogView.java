package view;

import model.Model;
import view.help.HelpView;
import view.widget.TopText;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LogView extends GameView {
    public static final char RED_COLOR = (char)0xF1;
    public static final char CYAN_COLOR = (char)0xF2;
    public static final char YELLOW_COLOR = (char)0xF3;
    public static final char GRAY_COLOR = (char)0xF4;
    public static final char DEFAULT_COLOR = (char)0xF0;
    public static final char WHITE_COLOR = (char)0xFA;
    public static final char GOLD_COLOR = (char)0xFC;

    private static final String SPECIAL_CHARS = makeSpecialCharsString();
    public static final MyColors DEFAULT_TEXT_COLOR = MyColors.LIGHT_GREEN;

    private static final MyColors[] COLOR_FOR_CHAR = new MyColors[]{
            DEFAULT_TEXT_COLOR, MyColors.LIGHT_RED,
            MyColors.CYAN, MyColors.YELLOW, MyColors.GRAY, MyColors.PEACH,
            MyColors.TAN, MyColors.LIGHT_YELLOW, MyColors.LIGHT_BLUE, MyColors.GREEN,
            MyColors.WHITE, MyColors.ORANGE, MyColors.GOLD, MyColors.LIGHT_GRAY,
            MyColors.BEIGE};

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
        int logStartIndex = Math.min(totalRows - 1 + scroll + 5, model.getLog().size()-1);
        int cutOff = Math.min(totalRows - 1 + scroll, model.getLog().size()-1);
        int row = rowOffset;
        MyColors colorToUse = DEFAULT_TEXT_COLOR;
        for (; logStartIndex >= 0; logStartIndex--) {
            String s = model.getLog().getContents().get(logStartIndex);
            int col = 0;
            for (int i = 0; i < s.length(); i++) {
                String substr = s.charAt(i) + "";
                if (SPECIAL_CHARS.contains(substr)) {
                    colorToUse = getColorForSpecialChar(s.charAt(i));
                } else if (logStartIndex <= cutOff) {
                    BorderFrame.drawString(model.getScreenHandler(), substr, col++, row, colorToUse);
                }
            }

            if (logStartIndex <= cutOff) {
                row++;
            }
            if (row == DrawingArea.WINDOW_ROWS) {
                break;
            }
        }
    }

    private static MyColors getColorForSpecialChar(char c) {
        int i = SPECIAL_CHARS.indexOf(c);
        if (0 <= i && i < SPECIAL_CHARS.length()) {
            return COLOR_FOR_CHAR[i];
        }
        return DEFAULT_TEXT_COLOR;
    }

//    private static MyColors getColorForLine(String s) {
//        if (s.startsWith("!")) {
//            return MyColors.LIGHT_RED;
//        } else if (s.contains("DAY")) {
//            return MyColors.CYAN;
//        } else if (s.contains("\"")) {
//            return MyColors.YELLOW;
//        } else if (s.contains("Autosaving")) {
//            return MyColors.GRAY;
//        }
//        return MyColors.LIGHT_GREEN;
//    }

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
        } else if (keyEvent.isControlDown() && keyEvent.getKeyCode() == KeyEvent.VK_P) {
            printToFile(model);
        }

         if (model.getLog().isAcceptingInput()) {
            model.getLog().keyTyped(keyEvent, model);
        }
    }

    private void printToFile(Model model) {
        try {
            Date date = new Date();
            String fileName = "ff8_log_" + date.toString() + ".txt";
            fileName = fileName.replace(' ', '_');
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (String s : model.getLog().getContents()) {
                writer.write(s);
            }
            writer.close();
            model.transitionToDialog(new SimpleMessageView(model.getView(), "Log written to " + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String makeSpecialCharsString() {
        StringBuilder bldr = new StringBuilder();
        for (int i = 0; i < 0xF; i++) {
            bldr.append((char)(0xF0 + i));
        }
        return bldr.toString();
    }
}
