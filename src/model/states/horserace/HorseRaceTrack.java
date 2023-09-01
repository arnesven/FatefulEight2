package model.states.horserace;

import model.Model;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.HorseRacingSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HorseRaceTrack {
    public static final int TRACK_WIDTH = 7;
    private static final int TRACK_LENGTH = 100;
    private static final int SLICES_TO_DRAW = 9;
    private static SideSprite SIDE_TREE = new SideSprite(2, 3);
    private static SideSprite SIDE_POLE = new SideSprite(4, 5);
    private static SideSprite SIDE_POLE_UL = new SideSprite(5, 5);
    private static SideSprite SIDE_POLE_UR = new SideSprite(6, 5);
    private static final MyColors BANNER_COLOR = MyColors.PINK;
    private static Sprite32x32 BANNER_LEFT = new Sprite32x32("bannerleft", "riding.png", 0x60,
            MyColors.BLACK, BANNER_COLOR, MyColors.PINK, MyColors.BEIGE);
    private static Sprite32x32 BANNER_CENTER = new Sprite32x32("bannercenter", "riding.png", 0x61,
            MyColors.BLACK, BANNER_COLOR, MyColors.PINK, MyColors.BEIGE);
    private static Sprite32x32 BANNER_RIGHT = new Sprite32x32("bannerright", "riding.png", 0x62,
            MyColors.BLACK, BANNER_COLOR, MyColors.PINK, MyColors.BEIGE);
    private List<List<TrackTerrain>> track;

    public HorseRaceTrack() {
        track = new ArrayList<>();
        track.add(fullPathSlice());
        track.add(fullPathSlice());
        for (int i = 2; i < TRACK_LENGTH-1; i++) {
            List<TrackTerrain> slice = new ArrayList<>();
            for (int j = TRACK_WIDTH; j > 0; --j) {
                slice.add(TrackTerrain.randomTerrain());
            }
            fixMultipleObstacles(slice);
            track.add(slice);
        }
        track.add(fullPathSlice());
    }

    private List<TrackTerrain> fullPathSlice() {
        List<TrackTerrain> result = new ArrayList<>();
        for (int i = 0; i < TRACK_WIDTH; ++i) {
            result.add(new PathTrackTerrain());
        }
        return result;
    }

    private void fixMultipleObstacles(List<TrackTerrain> slice) {
        int count = 0;
        for (int i = 0; i < slice.size(); ++i) {
            if (slice.get(i) instanceof ObstacleTrackTerrain) {
                count++;
                if (count == 2) {
                    count = 0;
                    slice.set(i, new PathTrackTerrain());
                }
            }
        }
    }

    public void drawYourself(Model model, HorseRacingSubView horseRacingSubView, HorseRacer player) {
        Point position = player.getPosition();
        int yShift = player.getYShift();
        for (int y = 0; y < SLICES_TO_DRAW; ++y) {
            int slice = y - HorseRacingSubView.HORSE_VERTICAL_POSITION + position.y;
            if (slice < 0) {
                slice += TRACK_LENGTH;
            } else {
                slice = slice % TRACK_LENGTH;
            }
            if (y < SLICES_TO_DRAW-1 || yShift > 15) {
                SideSprite left = SIDE_TREE;
                SideSprite right = SIDE_TREE;
                if (slice == 1) {
                    left = SIDE_POLE;
                    right = SIDE_POLE;
                } else if (slice == 0) {
                    left = SIDE_POLE_UL;
                    right = SIDE_POLE_UR;
                }
                drawAt(model.getScreenHandler(), horseRacingSubView, left, -1, y+1, 0, 16, -yShift);
                drawAt(model.getScreenHandler(), horseRacingSubView, right, 7, y+1, 0, 0, -yShift);
                for (int x = 0; x < TRACK_WIDTH; ++x) {
                    Sprite spr = track.get(slice).get(x).getSprite();
                    drawAt(model.getScreenHandler(), horseRacingSubView, spr, x, y+1, 0, 0, -yShift);
                }
                if (slice == 0) {
                    drawAt(model.getScreenHandler(), horseRacingSubView, BANNER_LEFT, 0, y+1, 2, 0, -yShift);
                    for (int i = 1; i < 6; ++i) {
                        drawAt(model.getScreenHandler(), horseRacingSubView, BANNER_CENTER, i, y + 1, 2, 0, -yShift);
                    }
                    drawAt(model.getScreenHandler(), horseRacingSubView, BANNER_RIGHT, 6, y+1, 2, 0, -yShift);

                }
            }
        }

    }

    private void drawAt(ScreenHandler screenHandler, HorseRacingSubView horseRacingSubView,
                        Sprite sprite, int x, int y, int prio, int xshift, int yshift) {
        screenHandler.register(sprite.getName(),
                horseRacingSubView.convertToScreen(x, y), sprite, prio, xshift, yshift);
    }

    public TrackTerrain getTerrain(Point position, int shift) {
        int extra = shift > 16 ? 1 : 0;
        int y = (position.y + (extra)) % TRACK_LENGTH;

        return track.get(y).get(position.x);
    }

    private static class SideSprite extends Sprite {
        public SideSprite(int col, int row) {
            super("side"+col+"x"+row, "riding.png", col, row, 16, 32);
            setColor1(MyColors.GREEN);
            setColor2(MyColors.BLACK);
            setColor3(MyColors.BROWN);
            setColor4(MyColors.DARK_GREEN);
        }
    }
}
