package model.map.locations;

import model.headquarters.Headquarters;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.states.dailyaction.AdvancedDailyActionState;
import view.sprites.Sprite;

import java.awt.*;

public abstract class TownishLocation extends HexLocation implements UrbanLocation {
    public TownishLocation(String name) {
        super(name);
    }

    @Override
    public String getLocationType() {
        return "Town";
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public Point getTravelNodePosition() {
        return new Point(AdvancedDailyActionState.TOWN_MATRIX_COLUMNS-1, AdvancedDailyActionState.TOWN_MATRIX_ROWS-2);
    }

    @Override
    public String getLordName() {
        throw new IllegalStateException("Should not be called!");
    }

    @Override
    public String getLordDwelling() {
        throw new IllegalStateException("Should not be called!");
    }

    @Override
    public String getLordTitle() {
        throw new IllegalStateException("Should not be called!");
    }

    @Override
    public boolean getLordGender() {
        throw new IllegalStateException("Should not be called!");
    }

    @Override
    public Sprite getExitSprite() {
        throw new IllegalStateException("Should not be called!");
    }

    @Override
    public Point getCareerOfficePosition() {
        throw new IllegalStateException("Should not be called!");
    }

    @Override
    public Headquarters getRealEstate() {
        throw new IllegalStateException("Should not be called!");
    }
}
