package model;

import model.characters.GameCharacter;
import model.enemies.Enemy;
import sound.SoundEffects;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class SteppingMatrix<T> {
    private final int columns;
    private final int rows;
    private final List<List<T>> grid;
    private final List<T> list = new ArrayList<T>();
    private Point selected;

    public SteppingMatrix(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        grid = new ArrayList<>();
        for (int i = 0; i < columns; ++i) {
            ArrayList<T> innerList = new ArrayList<T>();
            for (int j = 0; j < rows; ++j) {
                innerList.add(null);
            }
            grid.add(innerList);
        }
    }

    public List<T> getElementList() {
        return list;
    }

    public int getRows() {
        return rows; }

    public int getColumns() {
        return columns;
    }

    public void addElement(int col, int row, T comb) {
        if (comb == null) {
            throw new IllegalStateException("Cannot add null to a stepping matrix");
        }
        grid.get(col).set(row, comb);
        if (selected == null || getSelectedElement() == null) {
            selected = new Point(col, row);
        }
        list.add(comb);
    }

    public void addElements(List<T> stuff) {
        int col = 0;
        int row = 0;
        for (T t : stuff) {
            addElement(col++, row, t);
            if (col == getColumns()) {
                row++;
                col = 0;
            }
        }
    }

    public T getSelectedElement() {
        return getElementAt(selected.x, selected.y);
    }

    public T getElementAt(int col, int row) {
        return grid.get(col).get(row);
    }

    public Point getSelectedPoint() {
        return selected; // invariant: should always point to a combatant
    }

    public void setSelectedPoint(T elem) {
        Point p = getPositionFor(elem);
        selected = p;
    }

    public Point getPositionFor(T elem) {
        for (int row = 0; row < getRows(); ++row) {
            for (int col = 0; col < getColumns(); ++col) {
                if (elem == getElementAt(col, row)) {
                    return new Point(col, row);
                }
            }
        }
        throw new NoSuchElementException();
    }

    public void remove(T elem) {
        list.remove(elem);
        Point p = getPositionFor(elem);
        grid.get(p.x).set(p.y, null);
        if (list.size() > 0) {
            T newSelected = list.get(0);
            Point p2 = getPositionFor(newSelected);
            selected = p2;
        }
    }

    public void step(int dx, int dy, boolean firstTime) {
        if (list.size() < 2) {
            return;
        }
        SoundEffects.matrixSelect();
        T nextSelected = findExactMatch(dx, dy, firstTime);
        if (nextSelected != null) {
            selected = getPositionFor(nextSelected);
            return;
        }
        nextSelected = findApproxMatch(dx, dy, firstTime);
        if (nextSelected != null) {
            selected = getPositionFor(nextSelected);
            return;
        }
        if (firstTime) {
            step(-dx, -dy, false);
        }
    }

    public void step(int dx, int dy) {
        step(dx, dy, true);
    }

    private T findExactMatch(int dx, int dy, boolean searchForward) {
        List<T> candidates = new ArrayList<>();
        candidates.addAll(list);
        candidates.remove(getSelectedElement());
        final Point origin = getPositionFor(getSelectedElement());

        candidates.removeIf((T t) -> {
            Point p = getPositionFor(t);
            if (dx != 0) {
                return Math.signum(p.x - origin.x) != Math.signum(dx) || p.y != origin.y;
            }
            return Math.signum(p.y - origin.y) != Math.signum(dy) || p.x != origin.x;
        });

        if (candidates.isEmpty()) {
            return null;
        }
        sortByDistance(candidates, origin);
        if (searchForward) {
            return candidates.get(0);
        }
        return candidates.get(candidates.size()-1);
    }


    private T findApproxMatch(int dx, int dy, boolean searchForward) {
        List<T> candidates = new ArrayList<>();
        candidates.addAll(list);
        candidates.remove(getSelectedElement());
        final Point origin = getPositionFor(getSelectedElement());

        candidates.removeIf((T t) -> {
                    Point p = getPositionFor(t);
                    if (dx != 0) {
                        return Math.signum(p.x - origin.x) != Math.signum(dx);
                    }
                    return Math.signum(p.y - origin.y) != Math.signum(dy);
                }
        );
        if (candidates.isEmpty()) {
            return null;
        }
        sortByDistance(candidates, origin);
        if (searchForward) {
            return candidates.get(0);
        }
        return candidates.get(candidates.size()-1);
    }

    private void sortByDistance(List<T> candidates, Point origin) {
        Collections.sort(candidates, (c1, c2) -> {
            Point p1 = getPositionFor(c1);
            Point p2 = getPositionFor(c2);
            Double distance1 = origin.distance(p1);
            Double distance2 = origin.distance(p2);
            return distance1.compareTo(distance2);
        });
    }

    public boolean handleKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            step(0, -1);
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            step(0, 1);
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            step(-1, 0);
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            step(1, 0);
            return true;
        }
        return false;
    }

    public void addElementLast(T it) {
        Point p = getPositionFor(null);
        addElement(p.x, p.y, it);
    }
}
