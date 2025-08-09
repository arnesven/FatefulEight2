package model.ruins.themes;

import model.ruins.DungeonRoom;
import util.MyRandom;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.util.HashMap;
import java.util.Map;

public class DualColorDungeonTheme implements DungeonTheme {

    private final DungeonTheme color1;
    private final DungeonTheme color2;
    private DungeonTheme currentTheme;
    private double switchProbability;
    private Map<DungeonRoom, DungeonTheme> roomMap = new HashMap<>();

    public DualColorDungeonTheme(DungeonTheme color1, DungeonTheme color2) {
        this.color1 = color1;
        this.color2 = color2;
        currentTheme = MyRandom.flipCoin() ? color1 : color2;
        switchProbability = 0.5 + MyRandom.nextDouble() / 2;
    }

    @Override
    public void setRoom(DungeonRoom dungeonRoom) {
        if (!roomMap.containsKey(dungeonRoom)) {
            if (MyRandom.nextDouble() > switchProbability) {
                currentTheme = currentTheme == color2 ? color1 : color2;
            }
            roomMap.put(dungeonRoom, currentTheme);
        }
        currentTheme = roomMap.get(dungeonRoom);
    }

    @Override
    public Sprite[] getConnect() {
        return currentTheme.getConnect();
    }

    @Override
    public Sprite[] getLeft() {
        return currentTheme.getLeft();
    }

    @Override
    public Sprite[] getMid() {
        return currentTheme.getMid();
    }

    @Override
    public Sprite[] getRight() {
        return currentTheme.getRight();
    }

    @Override
    public Sprite getStairsDown() {
        return currentTheme.getStairsDown();
    }

    @Override
    public Sprite getStairsUp() {
        return currentTheme.getStairsUp();
    }

    @Override
    public Sprite getDoor(boolean isHorizontal, boolean isLeverDoor) {
        return currentTheme.getDoor(isHorizontal, isLeverDoor);
    }

    @Override
    public Sprite getDoorOverlay() {
        return currentTheme.getDoorOverlay();
    }

    @Override
    public Sprite getCrackedWall(boolean isHorizontal) {
        return currentTheme.getCrackedWall(isHorizontal);
    }

    @Override
    public Sprite32x32 getLockedDoor(boolean isHorizontal) {
        return currentTheme.getLockedDoor(isHorizontal);
    }

    @Override
    public Sprite getLever(boolean on) {
        return currentTheme.getLever(on);
    }

}
