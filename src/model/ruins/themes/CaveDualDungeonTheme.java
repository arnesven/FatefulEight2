package model.ruins.themes;

import model.ruins.objects.RoomDecoration;

import java.util.Random;

public class CaveDualDungeonTheme extends DualColorDungeonTheme {
    public CaveDualDungeonTheme(DungeonTheme color1, DungeonTheme color2) {
        super(color1, color2);
    }

    @Override
    public RoomDecoration makeDecoration(int position, Random random) {
        if (getCurrentTheme() instanceof CaveDungeonTheme) {
            return getCurrentTheme().makeDecoration(position, random);
        }
        return super.makeDecoration(position, random);
    }
}
