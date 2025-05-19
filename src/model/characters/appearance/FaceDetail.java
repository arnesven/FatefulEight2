package model.characters.appearance;

import model.races.Race;
import view.MyColors;

import java.io.Serializable;

public abstract class FaceDetail implements Serializable, Comparable<FaceDetail> {
    public static final FaceDetail[] ALL_DETAILS = new FaceDetail[]{
            new GlassesDetail(), new RoundEarringsDetail(),
            new TriangularEarringsDetail(),
            new EyePatchDetail(), new HeadBandDetail(), new RougeDetail()};

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

    @Override
    public int compareTo(FaceDetail o) {
        return this.name.compareTo(o.name);
    }
}
