package model.characters.appearance;

import model.races.Race;
import view.MyColors;

import java.io.Serializable;

public abstract class FaceDetail implements Serializable {
    public static final FaceDetail NO_FACE_DETAIL = new NoFaceDetail();
    public static final FaceDetail[] ALL_DETAILS = new FaceDetail[]{
            NO_FACE_DETAIL, new GlassesDetail(), new EarringsDetail(), new GlassesAndEarringsDetail(),
            new EyePatchDetail()};

    public MyColors color = MyColors.WHITE;
    public String name;

    public FaceDetail(String name) {
        this.name = name;
    }

    public abstract void applyYourself(AdvancedAppearance appearance, Race race, boolean coversEars);


    public void setColor(MyColors color) {
        this.color = color;
    }

    public MyColors getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    private static class NoFaceDetail extends FaceDetail {
        public NoFaceDetail() {
            super("None");
        }

        @Override
        public void applyYourself(AdvancedAppearance appearance, Race race, boolean coversEars) {

        }
    }
}
