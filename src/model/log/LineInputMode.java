package model.log;

import java.awt.event.KeyEvent;

public class LineInputMode extends LogInputMode {
    private boolean enterTyped = false;

    @Override
    boolean inputReady(GameLog gameLog) {
        return enterTyped;
    }

    @Override
    public void handle(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            if (getBuffer().length() > 0) {
                enterTyped = true;
            }
        } else if (key.getKeyCode() == KeyEvent.VK_BACK_SPACE && getBuffer().length() > 0) {
            getBuffer().deleteCharAt(getBuffer().length()-1);
        } else if (isPrintableChar(key)) {
            getBuffer().append(key.getKeyChar());
        }
    }
}
