package model.states.maze;

import util.Arithmetics;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite8x8;
import view.subviews.GardenMazeSubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GardenMaze {
    //                                                    UP                 RIGHT            DOWN            LEFT
    private static final int[][] DXDYS = new int[][]{new int[]{0, -1}, new int[]{1, 0}, new int[]{0, 1}, new int[]{-1, 0}};
    private static final int MAX_DISTANCE = 6;
    private final Point statuePos;
    private final int entryX;
    private boolean[][] grid;
    private static final Sprite8x8[] MAZE_SPRITES = makeMazeSprites();
    private static final Sprite8x8 STATUE_SPRITE = new Sprite8x8("mazestatue", "maze.png", 0x46,
            MyColors.GREEN, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.LIGHT_BLUE);
    private static final Sprite8x8 MAZE_ENTRY = new Sprite8x8("mazestatue", "maze.png", 0x47,
            MyColors.DARK_GREEN, MyColors.TAN, MyColors.GREEN, MyColors.LIGHT_BLUE);
    private static final Sprite8x8 RED_DOT_SPRITE = new Sprite8x8("mazestatue", "maze.png", 0x36,
            MyColors.RED, MyColors.TAN, MyColors.GREEN, MyColors.LIGHT_BLUE);

    private GardenMaze(int width, int height, int entryColumn, Point statuePos) {
        grid = new boolean[width][height];
        this.statuePos = statuePos;
        this.entryX = entryColumn;
    }


    public static GardenMaze makeFromPlan(String[] mazePlan, int entryColumn) {
        GardenMaze maze = new GardenMaze(mazePlan[0].length(),
                mazePlan.length, entryColumn, new Point(2, 2));
        int row = 0;
        for (String str : mazePlan) {
            for (int i = 0; i < str.length(); ++i) {
                maze.grid[i][row] = str.charAt(i) != ' ';
            }
            row++;
        }
        return maze;
    }

    public static GardenMaze generate(int width, int height, int entryColumn) {
        GardenMaze maze = new GardenMaze(width*2-1, height*2-1, entryColumn,
                new Point(MyRandom.randInt(width), height/2 + MyRandom.randInt(height/2)));
        maze.fillDoors();
        Point current = new Point(width, height);
        Set<Point> visited = new HashSet<>();
        maze.recursiveGenerate(current, visited);
        maze.printMaze(entryColumn*2, 0, 0);
        return maze;
    }

    private void recursiveGenerate(Point current, Set<Point> visited) {
        if (visited.contains(current)) {
            return;
        }
        visited.add(current);
        List<Integer> facings = new ArrayList<>(List.of(0, 1, 2, 3));
        Collections.shuffle(facings);
        for (int facing : facings) {
            int xPos = current.x + DXDYS[facing][0];
            int yPos = current.y + DXDYS[facing][1];
            Point nextPoint = new Point(current.x + 2*DXDYS[facing][0],
                    current.y + 2*DXDYS[facing][1]);
            if (inGrid(xPos, yPos)) {
                if (!visited.contains(nextPoint)) {
                    grid[xPos][yPos] = false;
                    recursiveGenerate(nextPoint, visited);
                } else if (MyRandom.randInt(8) == 0) {
                    grid[xPos][yPos] = false;
                }
            }
        }
    }

    private void fillDoors() {
        for (int y = 0; y < grid[0].length; ++y) {
            for (int x = 0; x < grid.length; ++x) {
                grid[x][y] = true;
            }
        }
    }

    public void setPerspective(GardenMazeSubView subView, int x, int y, int currentFacing) {
        int xPos = x * 2;
        int yPos = y * 2;
        //printMaze(xPos, yPos, currentFacing);

        List<Boolean> leftSide = new ArrayList<>();
        List<Boolean> rightSide = new ArrayList<>();

        int[] leftDir = DXDYS[Arithmetics.decrementWithWrap(currentFacing, DXDYS.length)];
        int[] rightDir = DXDYS[Arithmetics.incrementWithWrap(currentFacing, DXDYS.length)];
        int distance = 0;
        int statueDistance = -1;
        boolean showExit = false;
        for (int i = 0; i < MAX_DISTANCE; ++i) {
            if (statuePos.x * 2 == xPos && statuePos.y * 2 == yPos) {
                statueDistance = i;
            }
            // System.out.println("Current: " + xPos + "," + yPos);
            int leftX = xPos + leftDir[0];
            int leftY = yPos + leftDir[1];
            // System.out.println("Left-pos: " + leftX + "," + leftY);
            leftSide.add(isOpening(leftX, leftY));

            int rightX = xPos + rightDir[0];
            int rightY = yPos + rightDir[1];
            // System.out.println("Right-pos: " + rightX + "," + rightY);
            rightSide.add(isOpening(rightX, rightY));

            int[] increment = DXDYS[currentFacing];
            xPos += increment[0];
            yPos += increment[1];
            if (!inGrid(xPos, yPos) || grid[xPos][yPos]) {
                break;
            }
            xPos += increment[0];
            yPos += increment[1];
            distance++;
        }
        System.out.println("Walls: LEFT RIGHT");
        for (int i = 0; i < leftSide.size(); ++i) {
            System.out.println("    " + leftSide.get(i) + " " + rightSide.get(i));
        }
        System.out.println("Statue distance: " + statueDistance);
        showExit = (xPos == entryX*2 && yPos == -1);
        System.out.println("SHow exit: " + showExit);
        subView.setWalls(leftSide, rightSide, distance, statueDistance, showExit);
    }

    private Boolean isOpening(int x, int y) {
        return (x == entryX * 2 && y == -1) || (inGrid(x, y) && !grid[x][y]);
    }

    private void printMaze(int x, int y, int currentFacing) {
        for (int row = 0; row < grid[0].length; ++row) {
            for (int col = 0; col < grid.length; ++col) {
                if (col == x && row == y) {
                    if (currentFacing == 0) {
                        System.out.print('^');
                    } else if (currentFacing == 1) {
                        System.out.print('>');
                    } else if (currentFacing == 2) {
                        System.out.print('v');
                    } else {
                        System.out.print('<');
                    }
                } else if (grid[col][row]) {
                    System.out.print('#');
                } else {
                    System.out.print(' ');
                }
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private boolean inGrid(int x, int y) {
        return 0 <= x && x < grid.length && 0 <= y && y < grid[0].length;
    }

    public void goForward(Point currentPoint, int currentFacing) {
        int[] dir = DXDYS[currentFacing];
        int newX = currentPoint.x + dir[0];
        int newY = currentPoint.y + dir[1];

        if (inGrid(newX, newY)) {
            currentPoint.x = newX;
            currentPoint.y = newY;
        }
    }

    public void drawMap(ScreenHandler screenHandler, int xOffset, int yOffset, Point current) {
        int xExtra = (SubView.X_MAX - SubView.X_OFFSET - grid.length/2) / 2;
        xOffset += xExtra;
        for (int y = 0; y < grid[0].length/2+1; y++) {
            for (int x = 0; x < grid.length/2+1; x++) {
                int sum = 0;
                for (int facing = 0; facing < 4; ++facing) {
                    int xPos = x*2 + DXDYS[facing][0];
                    int yPos = y*2 + DXDYS[facing][1];
                    sum += !isOpening(xPos, yPos) ? ((int)(Math.pow(2, facing))) : 0;
                }
                screenHandler.put(xOffset + x, yOffset + y, MAZE_SPRITES[sum]);
                if (statuePos.x == x && statuePos.y == y) {
                    screenHandler.register(STATUE_SPRITE.getName(),
                            new Point(xOffset + statuePos.x, yOffset + statuePos.y), STATUE_SPRITE);
                }
                if (current != null && current.x == x && current.y == y && (System.currentTimeMillis() / 500) % 2 == 0) {
                    screenHandler.register(RED_DOT_SPRITE.getName(),
                            new Point(xOffset + x, yOffset + y), RED_DOT_SPRITE, 2);
                }
            }
        }
        screenHandler.put(xOffset + entryX, yOffset - 1, MAZE_ENTRY);
    }

    private static Sprite8x8[] makeMazeSprites() {
        Sprite8x8[] result = new Sprite8x8[16];
        for (int i = 0; i < result.length; ++i) {
            result[i] = new Sprite8x8("gardenmaze" + i, "maze.png", 0x50 + (i/8)*0x10 + (i % 8),
                    MyColors.DARK_GREEN, MyColors.TAN, MyColors.BEIGE, MyColors.GRAY_RED);
        }
        return result;
    }

    public boolean isStatuePoint(Point currentPoint) {
        System.out.println("Statue point: " + statuePos + ", current pos: " + currentPoint);
        return currentPoint.equals(statuePos);
    }
}
