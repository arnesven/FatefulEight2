package model.log;

import model.Model;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameLog {
    private static final int LOG_WIDTH_COLUMNS = 80;
    private List<String> content = new LinkedList<>();
    private StringBuilder currentLine = new StringBuilder();
    private List<Character> animationBuffer = new LinkedList<>();
    private long PRINT_DELAY_MS = 20;
    private long elapsedTime = 0;
    private LogInputMode inputMode = null;
    private char caret = ' ';
    private char FILLED_BLOCK = (char)(0xFF);
    private int caretBlinkCount = 0;

    public int size() {
        return content.size() + 1;
    }

    public List<String> getContents() {
        List<String> all = new ArrayList<>();
        all.addAll(content);
        String curr = currentLine.toString();
        if (inputMode != null && isAnimationDone()) {
            curr += inputMode.getBufferAsString();
        }
        if (inputMode != null) {
            curr += caret;
        }
        all.add(0, curr);
        return all;
    }

    public synchronized void addAnimated(String s) {
        for (int i = 0; i < s.length(); ++i) {
            animationBuffer.add(s.charAt(i));
        }
        checkForAutoWrapping();
    }

    public synchronized boolean isAnimationDone() {
        return animationBuffer.isEmpty();
    }

    public synchronized void update(long timeSinceLast, Model model) {
        if (!isAnimationDone()) {
            this.elapsedTime += timeSinceLast;
            if (this.elapsedTime > PRINT_DELAY_MS) {
                this.elapsedTime = 0;
                addToCurrentLine(animationBuffer.get(0));
                animationBuffer.remove(0);
                model.madeChanges();
                checkForAutoWrapping();
            }
        } else if (inputMode != null) {
            if (this.caretBlinkCount % 8 == 0) {
                if (caret == FILLED_BLOCK) {
                    caret = ' ';
                } else {
                    caret = FILLED_BLOCK;
                }
            }
            this.caretBlinkCount++;
            model.madeChanges();
        }
    }

    private void checkForAutoWrapping() {
        if (!animationBuffer.isEmpty()) {
            if (animationBuffer.get(0) == ' ' && nextWordWontFit()) {
                animationBuffer.remove(0);
                addToCurrentLine('\n');
            }
        }
    }

    private boolean nextWordWontFit() {
        for (int i = 1; i < animationBuffer.size(); i++) {
            if (animationBuffer.get(i) == ' ') {
                return false;
            }
            if (currentLine.length() + i >= LOG_WIDTH_COLUMNS) {
                return true;
            }
        }
        return false;
    }

    private void addToCurrentLine(Character character) {
        currentLine.append(character);
        if (currentLine.length() == LOG_WIDTH_COLUMNS || character.charValue() == '\n') {
            content.add(0, currentLine.toString());
            currentLine = new StringBuilder();
        }
    }

    public void acceptSingleCharInput() {
        this.inputMode = new SingleCharInputMode();
    }

    public void acceptLineInput() {
        this.inputMode = new LineInputMode();
    }

    public void waitForReturn() {
        this.inputMode = new WaitForReturnMode();
    }


    public void waitForReturnSilently() {
        this.inputMode = new WaitForReturnMode(){
            @Override
            public boolean echoInput() {
                return false;
            }
        };
    }

    public boolean inputReady() {
        return inputMode.inputReady(this); // TODO: Got a nullptr execption here...
    }

    public synchronized String getInput() {
        while (!animationBuffer.isEmpty()) {
            if (inputMode.echoInput()) {
                addToCurrentLine(animationBuffer.get(0));
            }
            animationBuffer.remove(0);
            if (!animationBuffer.isEmpty() && animationBuffer.get(0) == ' ' && nextWordWontFit()) {
                animationBuffer.remove(0);
                if (inputMode.echoInput()) {
                    addToCurrentLine('\n');
                }
            }
        }

        String toReturn = inputMode.getBufferAsString();
        String toPrint = toReturn + "\n";
        inputMode.clear();
        if (inputMode.echoInput()) {
            for (int i = 0; i < toPrint.length(); ++i) {
                addToCurrentLine(toPrint.charAt(i));
            }
        }
        return toReturn;
    }

    public boolean isAcceptingInput() {
        return inputMode != null;
    }

    public void keyTyped(KeyEvent key, Model model) {
        inputMode.handle(key);
        if (inputMode.inputReady(this)) {
            caret = ' ';
            model.madeChanges();
        }
    }

    public void waitForAnimationToFinish() {
        while (!isAnimationDone()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setContent(List<String> logContent) {
        this.content = logContent;
    }
}
