package model;

import sound.SoundEffects;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class SteppingMatrix<T> {
    private final int columns;
    private final int rows;
    private List<List<T>> grid;
    private List<T> list;
    private Point selected;
    private boolean simplifiedStepping = false;
    private boolean soundEnabled = true;

    public SteppingMatrix(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        clear();
    }

    public synchronized void clear() {
        list = new ArrayList<>();
        grid = new ArrayList<>();
        for (int i = 0; i < columns; ++i) {
            ArrayList<T> innerList = new ArrayList<T>();
            for (int j = 0; j < rows; ++j) {
                innerList.add(null);
            }
            grid.add(innerList);
        }
        selected = null;
    }

    public synchronized List<T> getElementList() {
        return list;
    }

    public int getRows() {
        return rows; }

    public int getColumns() {
        return columns;
    }

    public synchronized void addElement(int col, int row, T comb) {
        if (comb == null) {
            throw new IllegalStateException("Cannot add null to a stepping matrix");
        }
        if (grid.get(col).get(row) != null) {
            System.err.println("Warning, overwrite of element in stepping matrix, at column " + col + ", row " + row + "!");
        }
        grid.get(col).set(row, comb);
        if (selected == null || getSelectedElement() == null) {
            selected = new Point(col, row);
        }
        list.add(comb);
    }

    public synchronized void addElements(List<T> stuff) {
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

    public synchronized T getSelectedElement() {
        if (selected == null) {
            return null;
        }
        return getElementAt(selected.x, selected.y);
    }

    public synchronized T getElementAt(int col, int row) {
        return grid.get(col).get(row);
    }

    public Point getSelectedPoint() {
        return selected; // invariant: should always point to a combatant
    }

    public synchronized void setSelectedElement(T elem) {
        Point p = getPositionFor(elem);
        selected = p;
    }

    public synchronized void setSelectedPoint(Point p) {
        setSelectedElement(getElementAt(p.x, p.y));
    }

    public synchronized Point getPositionFor(T elem) {
        for (int row = 0; row < getRows(); ++row) {
            for (int col = 0; col < getColumns(); ++col) {
                if (elem == getElementAt(col, row)) {
                    return new Point(col, row);
                }
            }
        }
        if (elem != null) {
            System.err.println("Could not find " + elem.toString() + " in matrix.");
        }
        throw new NoSuchElementException();
    }

    public synchronized void remove(T elem) {
        Point nextSelected = selected;
        if (elem == getSelectedElement()) {
            if (list.size() > 1) {
                step(1, 0);
                if (elem == getSelectedElement()) {
                    step(0, 1);
                }
                nextSelected = selected;
            } else {
                selected = null;
            }
        }
        list.remove(elem);
        Point p = getPositionFor(elem);
        grid.get(p.x).set(p.y, null);
        selected = nextSelected;
    }

    public synchronized void step(int dx, int dy, boolean firstTime) {
        if (list.size() < 2) {
            return;
        }
        if (soundEnabled) {
            SoundEffects.matrixSelect();
        }
        if (simplifiedStepping) {
            simplifiedStepping(dx, dy);
        } else {
            normalStepping(dx, dy, firstTime);
        }
    }

    public synchronized void step(int dx, int dy) {
        step(dx, dy, true);
    }

    private void normalStepping(int dx, int dy, boolean firstTime) {
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
            normalStepping(-dx, -dy, false);
        }
    }

    private void simplifiedStepping(int dx, int dy) {
        int index = list.indexOf(getSelectedElement());
        if (dx > 0 || dy > 0) {
            index = (index + 1) % list.size();
        } else {
            index = index - 1;
            if (index < 0) {
                index = list.size() - 1;
            }
        }
        selected = getPositionFor(list.get(index));
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
        return candidates.get(0);
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

    public synchronized boolean handleKeyEvent(KeyEvent keyEvent) {
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

    public synchronized void addElementLast(T it) {
        try {
            Point p = getPositionFor(null);
            addElement(p.x, p.y, it);
        } catch (NoSuchElementException nsee) {
            System.err.println("Warning could not find a place to put elemt in matrix");
        }
    }

    public void removeAll() {
        while (!list.isEmpty()) {
            remove(list.get(0));
        }
    }

    public boolean isFull() {
        return list.size() == columns * rows;
    }

    public void setSimplifiedSteppingEnabled(boolean b) {
        simplifiedStepping = b;
    }

    public int getMinimumRow() {
        for (int row = 0; row < getRows(); ++row) {
            for (int col = 0; col < getColumns(); ++col) {
                if (getElementAt(col, row) != null) {
                    return row;
                }
            }
        }
        return 0;
    }

    public void setSoundEnabled(boolean b) {
        this.soundEnabled = b;
    }
}
