package model.items.books;

import model.items.Item;
import view.MyColors;
import view.help.DragonsHelpChapter;

public class HowToTameYourDragonBook extends BookItem {

    public HowToTameYourDragonBook() {
        super("Red Book", 25, MyColors.DARK_RED,
                "How to Tame Your Dragon", "Rastigan Veld", makeTextContent());
    }

    private static String makeTextContent() {
        return "Taming a dragon can be a tedious and potentially lethal " +
                "endeavor unless the aspiring dragon-master is properly prepared. " +
                "This volume attempts to compile notes and experiences on the " +
                "subject to assist dragon taming attempts.\n\n" +
                "DRAGON TYPES\n" +
                DragonsHelpChapter.TEXT + "\n\n" +
                "ATTRACTING A DRAGON\n" +
                "The first step is obviously to acquire a Dragon Taming spell book. " +
                "Such tomes are rare but have been found to be carried by vendors from " +
                "time to time. Although the spell may be expensive it is definitely worth " +
                "purchasing as taming a dragon is virtually impossible without it.\n\n" +
                "The spell is also convenient for attracting dragons. However, the " +
                "reader is urged to exercise extreme caution! Do not attract a dragon in " +
                "populated areas or if you are otherwise ill-prepared to carry out the " +
                "rest of the taming process. In any case, the spell has been found to work " +
                "best in mountainous or hilly areas, or on the tundra or in deserts. It may be hard " +
                "to attract a dragon in other locations. Furthermore, dragons are notoriously " +
                "suspicious of other dragons and will not come if another dragon is around.\n\n" +
                "DRAGON ENCOUNTERS\n" +
                "Once the dragon arrives it will undoubtedly engage you immediately in combat.\n" +
                "Warning: do not attack the dragon! Doing so will only enrage it more and will " +
                "make taming it even more difficult. Ideally the mage should attempt to calm the " +
                "beast first, by use of the Harmonize spell. This approach is recommended but one " +
                "should know that dragons which become too pacified may escape before the mage has " +
                "the opportunity to cast the taming spell.\n\n" +
                "The second part of the spell is the more difficult part. It requires the caster to " +
                "channel magical energy of a color related to the type of dragon. The color of magic " +
                "can often clearly be deduced from the color of the dragon itself, however sometimes " +
                "this may be tricky to deduce. It has been found that White Magic works well with " +
                "Bone Dragons and that for Elder Dragons magic of any color can be used. Should " +
                "the caster be successful in this attempt the dragon will become lethargic but it " +
                "should now appear tame, be faithful to its master and even let the masters and " +
                "their companions ride on its back.";
    }

    @Override
    public Item copy() {
        return new HowToTameYourDragonBook();
    }
}
