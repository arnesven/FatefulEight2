package model.log;

import java.awt.event.KeyEvent;

public class WaitForReturnMode extends LogInputMode {
    private boolean ready = false;

    @Override
    boolean inputReady(GameLog gameLog) {
        return ready;
    }

    @Override
    public void handle(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_ENTER) {
            ready = true;
        }
    }
}
