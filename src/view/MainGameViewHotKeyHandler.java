package view;

import view.party.PartyView;

import java.awt.event.KeyEvent;

public class MainGameViewHotKeyHandler {

    public static GameView handle(MainGameView gameView, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_C) {
            return new PartyView(gameView);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_A) {
            return new AchievementsView(gameView);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
            return new SkillsView(gameView);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_Z) {
            return new SpellsView(gameView);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_W) {
            return new FullMapView(gameView);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_Q) {
            return new JournalView(gameView);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_X) {
            return new ExitGameView(gameView);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_E) {
            return new InventoryView(gameView);
        }
        return null;
    }

    public static String getHotKeysAsText() {
        return  "Ctrl-C : Party View\n" +
                "Ctrl-S : Skills View\n" +
                "Ctrl-Z : Spells View\n" +
                "Ctrl-W : Map View\n" +
                "Ctrl-E : Inventory View\n" +
                "Ctrl-Q : Journal View\n" +
                "Ctrl-X : Quit Game\n";
    }
}
