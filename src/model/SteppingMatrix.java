package model;

import model.characters.GameCharacter;
import model.enemies.Enemy;

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

    public void step(int dx, int dy) {
        if (list.size() < 2) {
            return;
        }

        List<T> candidates = new ArrayList<>();
        candidates.addAll(list);
        candidates.remove(getSelectedElement());

        final Point origin = getPositionFor(getSelectedElement());
        final Point steppedPoint = new Point(selected.x + dx, selected.y + dy);

        Collections.sort(candidates, new Comparator<T>() {
            @Override
            public int compare(T c1, T c2) {
                Point p1 = getPositionFor(c1);
                Double angleC1 = angleBetween(origin, steppedPoint, p1);
                Point p2 = getPositionFor(c2);
                Double angleC2 = angleBetween(origin, steppedPoint, p2);
                if (angleC1.equals(angleC2)) {
                    Double distance1 = origin.distance(p1);
                    Double distance2 = origin.distance(p2);
                    return distance1.compareTo(distance2);
                }
                return angleC1.compareTo(angleC2);
            }
        });
        selected = getPositionFor(candidates.get(0));
    }

    private static double angleBetween(Point origin, Point p1, Point p2) {
        double angle1 = Math.atan2(p1.y - origin.y, p1.x - origin.x);
        double angle2 = Math.atan2(p2.y - origin.y, p2.x - origin.x);
        return Math.abs(angle1 - angle2);
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
