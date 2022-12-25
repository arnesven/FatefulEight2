package model.log;

import java.awt.event.KeyEvent;

public abstract class LogInputMode {
    private StringBuilder input = new StringBuilder();

    abstract boolean inputReady(GameLog gameLog);

    public String getBufferAsString() {
        return input.toString();
    }

    protected StringBuilder getBuffer() {
        return input;
    }

    public abstract void handle(KeyEvent key);

    protected static boolean isPrintableChar(KeyEvent key) {
        return ' ' <= key.getKeyChar() && key.getKeyChar() <= 'z';
    }

    public void clear() {
        input = new StringBuilder();
    }
}
