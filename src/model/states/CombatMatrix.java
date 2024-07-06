package model.states;

import model.combat.Combatant;
import model.Party;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.enemies.Enemy;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CombatMatrix extends SteppingMatrix<Combatant> {
    private static final int FRONT_ROW_Y = 1;
    private static final int BACK_ROW_Y = 0;
    // x y
    private static final int GRID_COLUMNS = 8;
    private static final int GRID_ROWS = 7;

    public CombatMatrix() {
        super(GRID_COLUMNS, GRID_ROWS);
    }
    
    public void addEnemies(List<Enemy> enemies) {
        List<Enemy> enms = new ArrayList<>();
        enms.addAll(enemies);
        int i = 0;
        while (!enms.isEmpty()) {
            int width = 0;
            List<Enemy> sub = new ArrayList<>();
            while (!enms.isEmpty() && width + enms.get(0).getWidth() <= getColumns()) {
                Enemy e = enms.get(0);
                sub.add(e);
                enms.remove(e);
                width += e.getWidth();
            }
            addCentered(getRows() - 3 + i, sub, width);
            enms.removeAll(sub);
            ++i;
        }
    }

    private void addCentered(int row, List<? extends Combatant> sub, int width) {
        int startCol = (getColumns() - width) / 2;
        for (Combatant comb : sub) {
            super.addElement(startCol, row, comb);
            startCol += comb.getWidth();
        }
    }

    public void addParty(Party party) {
        addCentered(BACK_ROW_Y, party.getBackRow(), party.getBackRow().size());
        addCentered(FRONT_ROW_Y, party.getFrontRow(), party.getFrontRow().size());
    }

    public void addAllies(List<GameCharacter> allies) {
        List<GameCharacter> firstRow = allies;
        List<GameCharacter> secondRow = new ArrayList<>();
        if (allies.size() > 8) {
            firstRow = allies.subList(0, 8);
            secondRow = allies.subList(8, Math.min(16, allies.size()));
        }
        addCentered(FRONT_ROW_Y + 1, firstRow, firstRow.size());
        if (!secondRow.isEmpty()) {
            addCentered(FRONT_ROW_Y + 2, secondRow, secondRow.size());
        }
    }

    public Combatant getCombatant(int col, int row) {
        return getElementAt(col, row);
    }

    public void toggleFormationFor(GameCharacter combatant) {
        Point p = getPositionFor(combatant);
        remove(combatant);
        if (p.y == FRONT_ROW_Y) {
            fillFirstAvailable(BACK_ROW_Y, combatant);
        } else {
            fillFirstAvailable(FRONT_ROW_Y, combatant);
        }
        setSelectedElement(combatant);
    }

    private void fillFirstAvailable(int row, GameCharacter combatant) {
        int startX = getColumns() / 2 - 1;
        int incr = 1;
        do {
            if (getElementAt(startX, row) == null) {
                addElement(startX, row, combatant);
                break;
            }
            if (startX < getColumns() / 2) {
                startX += incr;
            } else {
                startX -= incr;
            }
            incr += 1;
        } while (true);
    }

    public void moveSelectedToParty() {
        if (getSelectedElement() instanceof GameCharacter) {
            return;
        }
        for (Combatant c : getElementList()) {
            if (c instanceof GameCharacter) {
                setSelectedElement(c);
                return;
            }
        }
    }

    public void moveSelectedToEnemy() {
        if (getSelectedElement() instanceof Enemy) {
            return;
        }
        for (Combatant c : getElementList()) {
            if (c instanceof Enemy) {
                setSelectedElement(c);
                return;
            }
        }
    }
}
