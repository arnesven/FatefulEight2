package model.characters.appearance;

public class Ears {
    public static Ears[] allEars = new Ears[]{
            new Ears("Race Default", 0, 0),
            new Ears("Hair", 0xA5, 0xB5),
            new Ears("Hair Elf", 0x73, 0x83),
            new Ears("Hair Halfling", 0x79, 0x89),
            new Ears("Very Hairy", 0xC6, 0xD6)
    };
    private final String name;
    private final int left;
    private final int right;

    public Ears(String name, int left, int right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    public String getName() {
        return name;
    }

    public void setYourself(AdvancedAppearance appearance) {
        appearance.setRaceSpecificEars(left == 0);
        appearance.setEars(new int[]{left, right});
    }
}
