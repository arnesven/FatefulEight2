package model.map;

public class ResourcePrevalence {
    public static final int NON_EXISTENT = 0;
    public static final int POOR = 1;
    public static final int FAIR = 2;
    public static final int GOOD = 3;

    public int ingredients;
    public int materials;

    public ResourcePrevalence(int ingredients, int materials) {
        this.ingredients = ingredients;
        this.materials = materials;
    }
}
