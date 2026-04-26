package control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener {
    private final FatefulEight fatefulEight;

    public MyKeyListener(FatefulEight fatefulEight) {
        this.fatefulEight = fatefulEight;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER && keyEvent.isAltDown()) {
            fatefulEight.toggleFullScreen();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_F7) {
            FatefulEight.cycleLoadedDice();
        } else if (keyEvent.getKeyCode() != KeyEvent.VK_ALT) {
            fatefulEight.enqueueKeyEvent(keyEvent);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
