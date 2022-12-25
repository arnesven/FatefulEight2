package model.log;

import java.awt.event.KeyEvent;

public class SingleCharInputMode extends LogInputMode {
    @Override
    public boolean inputReady(GameLog gameLog) {
        return getBufferAsString().length() > 0;
    }

    @Override
    public void handle(KeyEvent key) {
        if (isPrintableChar(key)) {
            getBuffer().append(key.getKeyChar());
        }
    }
}
