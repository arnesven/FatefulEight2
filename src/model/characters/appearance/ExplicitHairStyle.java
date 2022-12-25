package model.characters.appearance;

public class ExplicitHairStyle extends HairStyle {
    private final int left;
    private final int center;
    private final int right;
    private final int topLeft;
    private final int top;
    private final int topRight;

    public ExplicitHairStyle(boolean inForehead, int left, int center, int right, int topLeft, int top, int topRight) {
        super(inForehead);
        this.left = left;
        this.center = center;
        this.right = right;
        this.topLeft = topLeft;
        this.top = top;
        this.topRight = topRight;
    }

    public int getForeheadLeft() { return left; }

    public int getForeheadCenter() { return center; }

    public int getForeheadRight() { return right; }

    public int getHeadTopLeft() { return topLeft; }

    public int getHeadTop() { return top; }

    public int getHeadTopRight() {return topRight; }

}
