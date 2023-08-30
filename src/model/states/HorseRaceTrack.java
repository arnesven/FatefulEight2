package model.states;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.subviews.HorseRacingSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HorseRaceTrack {
    public static final int TRACK_WIDTH = 7;
    private static final int TRACK_LENGTH = 100;
    private static final int SLICES_TO_DRAW = 9;
    private static Sprite SIDE_TREE = new SideTreeSprite();
    private List<List<TrackTerrain>> track;

    public HorseRaceTrack() {
        track = new ArrayList<>();
        for (int i = TRACK_LENGTH; i > 0; --i) {
            List<TrackTerrain> slice = new ArrayList<>();
            for (int j = TRACK_WIDTH; j > 0; --j) {
                slice.add(TrackTerrain.randomTerrain());
            }
            track.add(slice);
        }
    }

    public void drawYourself(Model model, HorseRacingSubView horseRacingSubView) {
        Point position = horseRacingSubView.getPosition();
        int yShift = horseRacingSubView.getYShift();
        for (int y = 0; y < SLICES_TO_DRAW; ++y) {
            int slice = y - HorseRacingSubView.HORSE_VERTICAL_POSITION + position.y;
            if (slice < 0) {
                slice += TRACK_LENGTH;
            } else {
                slice = slice % TRACK_LENGTH;
            }
            if (y < SLICES_TO_DRAW-1 || yShift > 15) {
                model.getScreenHandler().register(SIDE_TREE.getName(),
                        horseRacingSubView.convertToScreen(-1, y + 1), SIDE_TREE, 0, 16, -yShift);
                model.getScreenHandler().register(SIDE_TREE.getName(),
                        horseRacingSubView.convertToScreen(7, y + 1), SIDE_TREE, 0, 0, -yShift);
                for (int x = 0; x < TRACK_WIDTH; ++x) {
                    Sprite spr = track.get(slice).get(x).getSprite();
                    model.getScreenHandler().register(spr.getName(),
                            horseRacingSubView.convertToScreen(x, y + 1),
                            spr, 0, 0, -yShift);
                }
            }
        }

    }

    private static class SideTreeSprite extends Sprite {
        public SideTreeSprite() {
            super("sidetree", "riding.png", 2, 3, 16, 32);
            setColor1(MyColors.GREEN);
            setColor2(MyColors.BLACK);
            setColor3(MyColors.BROWN);
            setColor4(MyColors.DARK_GREEN);
        }
    }
}
