package model.states.warehouse;

import util.MatrixFunction;
import util.MyMatrices;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.WarehouseSubView;

import java.util.List;
import java.awt.*;
import java.util.function.Predicate;

public class Warehouse {

    private static final Sprite32x32 DOOR_BOTTOM =
            new Sprite32x32("warehousedoorbottom", "warehouse.png", 0x02,
                    MyColors.LIGHT_YELLOW, MyColors.GRAY, MyColors.BEIGE);
    private static final Sprite32x32 DOOR_LEFT =
            new Sprite32x32("warehousedoorleft", "warehouse.png", 0x12,
                    MyColors.LIGHT_YELLOW, MyColors.GRAY, MyColors.BEIGE);
    private static final Sprite32x32 DOOR_RIGHT =
            new Sprite32x32("warehousedoorright", "warehouse.png", 0x13,
                    MyColors.LIGHT_YELLOW, MyColors.GRAY, MyColors.BEIGE);
    private static final Sprite32x32 DOOR_TOP =
            new Sprite32x32("warehousedoortop", "warehouse.png", 0x03,
                    MyColors.LIGHT_YELLOW, MyColors.GRAY, MyColors.BEIGE);

    private WarehouseObject[][] matrix;
    private final List<String> template;
    private Point playerStartPosition;
    private Point doorPosition;

    public Warehouse(List<String> template) {
        this.template = template;
        matrix = new WarehouseObject[8][9];
        for (int j = 0; j < template.size(); ++j) {
            for (int i = 0; i < template.get(j).length(); ++i) {
                char c = template.get(j).charAt(i);
                if (c == '@') {
                    playerStartPosition = new Point(i, j);
                } else if (c == 'D') {
                    doorPosition = new Point(i, j);
                }
                matrix[i][j] = makeFromChar(c);
            }
        }
    }

    public Warehouse() {
        this(MyRandom.sample(WarehouseLevels.TEMPLATES));
    }

    public List<String> getTemplate() {
        return template;
    }

    private WarehouseObject makeFromChar(char ch) {
        switch (ch) {
            case 'H' :
                return new HeavyWarehouseCrate();
            case '*' :
                return new WarehouseCrate();
            case '!' :
                return new SpecialWarehouseCrate();
        }
        return null;
    }

    public void drawObjects(ScreenHandler screenHandler) {
        MyMatrices.traversRowWise(matrix, (matrix, x, y) -> {
            if (matrix[x][y] != null) {
                WarehouseObject wobj = matrix[x][y];
                screenHandler.register(wobj.getSprite().getName(),
                        WarehouseSubView.convertToScreen(new Point(x, y)),
                        wobj.getSprite(), 1);
            }
        });

        Sprite doorSprite = DOOR_BOTTOM;
        if (doorPosition.y == 0) {
            doorSprite = DOOR_TOP;
        } else if (doorPosition.x == 0) {
            doorSprite = DOOR_LEFT;
        } else if (doorPosition.x == matrix.length - 1) {
            doorSprite = DOOR_RIGHT;
        }
        screenHandler.register(doorSprite.getName(),
                WarehouseSubView.convertToScreen(doorPosition),
                doorSprite);
    }

    private boolean isBox(Point position) {
        if (isInBounds(position)) {
            return matrix[position.x][position.y] != null;
        }
        return false;
    }

    public boolean isFree(Point position) {
        if (isInBounds(position)) {
            return matrix[position.x][position.y] == null;
        }
        return false;
    }

    public boolean canMoveBox(Point position, Point dxdy) {
        if (!isBox(position)) {
            return false;
        }
        if (matrix[position.x][position.y].isImmobile()) {
            return false;
        }
        Point newPosition = new Point(position.x + dxdy.x, position.y + dxdy.y);
        if (!isFree(newPosition)) {
            return false;
        }
        return true;
    }

    public void addObject(Point position, WarehouseObject wobj) {
        if (!isFree(position)) {
            System.err.println("Warning, adding an object to already occupied cell at " + position);
        }
        matrix[position.x][position.y] = wobj;
    }

    public WarehouseObject removeObject(Point position) {
        if (!isBox(position)) {
            System.err.println("Warning, nothing to remove at " + position);
        }
        WarehouseObject toReturn = matrix[position.x][position.y];
        matrix[position.x][position.y] = null;
        return toReturn;
    }

    private boolean isInBounds(Point position) {
        return new Rectangle(0, 0, matrix.length, matrix[0].length).contains(position);
    }

    public boolean canMoveInto(Point newPosition, Point direction) {
        return isInBounds(newPosition) && (isFree(newPosition) ||
                canMoveBox(newPosition, direction));
    }

    public boolean gameIsWon() {
        return matrix[doorPosition.x][doorPosition.y] instanceof SpecialWarehouseCrate;
    }

    public Point getPlayerStartingPosition() {
        return playerStartPosition;
    }

    public boolean checkForOtherBoxRemove() {
        return matrix[doorPosition.x][doorPosition.y] instanceof WarehouseCrate;
    }

    public Point getSpecialBoxPosition() {
        return MyMatrices.findElement(matrix, warehouseObject -> warehouseObject instanceof SpecialWarehouseCrate);
    }
}
