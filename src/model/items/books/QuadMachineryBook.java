package model.items.books;

import model.items.Item;
import view.MyColors;
import view.sprites.Sprite;

import java.util.HashMap;
import java.util.Map;

public class QuadMachineryBook extends BookItem {
    private static final Map<String, Sprite> FIGURES = makeFigures();

    public QuadMachineryBook() {
        super("Gold Book", 45, MyColors.GOLD,
                "Research Notes on Quad Machinery", "Mary Elaine Severin", makeTextContent());
    }

    private static String makeTextContent() {
        return "The Quad are perhaps most known for their magical mastery and little has been written " +
                "on the topic of their advanced magically-enhanced machinery.\n\n" +
                "Most of the infrastructure of the age of the Quad has been lost to ruin. " +
                "But there are still some strongholds scattered about the continent. Me and my team " +
                "discovered many strange magical machines within such stronghold. Here are some " +
                "of my findings about these curious devices.\n\n" +
                "The devices are powered by magical energy but each device must be correctly " +
                "configured with the proper power sources. The Quad were known to use spherical " +
                "orbs for many purposes, and it seems, they were used even for this.\n" +
                "Orbs came in seven colors; green, blue, black, pink, purple, " +
                "orange and crimson. Most machines need four orbs to function, placed into rounded " +
                "slots on a control panel. Not only were the color of orbs required to be correct, " +
                "but also the order. However, the crimson orbs functioned as wild cards, and could " +
                "substitute any other color.\n\n" +
                "The configuration of orbs could be viewed as a passcode which made the machine run. " +
                "I don't think we would have been able to get the machines running unless the control " +
                "panels were also fitted with some kind of power gauges, one for each slot. The gauges " +
                "have three zones; red, white and colorless.\n\n\n\n" +
                "<fig dials>\n\n\n\n\n" +
                "When no orbs are set in the slots, or when " +
                "the colors of the orbs are wrong, the dial points to the colorless part of the gauge. " +
                "If the colors are right however (and this took quite a lot of experimenting on my part) " +
                "the dial will point to red if the orb is in the right position, and white if it is in " +
                "the wrong position.\n\n" +
                "The only trouble is, that there seems to be no connection between the " +
                "positions of the gauges and the position of the orbs. This means that even if a gauge is " +
                "indicating red, i.e. that a orb is the correct color and the correct location, one cannot " +
                "be sure which orb it refers to.\n\n" +
                "Even more troublesome is the fact that many machines have some kind of lock-down mechanism " +
                "that locks the orbs in place once each slot has been filled. This means that you cannot " +
                "use different combinations on the same machine to try to figure out the combination.\n\n" +
                "However it does seem like it was common practice to use the same combination for all machines at " +
                "a site. So given enough machines, and orbs to try combinations with, eventually one should " +
                "be able to find the correct code which enables all of them!";
    }

    @Override
    public Item copy() {
        return new QuadMachineryBook();
    }

    @Override
    public Map<String, Sprite> getFigures() {
        return FIGURES;
    }

    private static Map<String, Sprite> makeFigures() {
        Map<String, Sprite> result = new HashMap<>();
        Sprite spr = new Sprite("dialsfigure", "book.png", 0, 1, 96, 32);
        spr.setColor1(MyColors.BLACK);
        spr.setColor2(MyColors.WHITE);
        spr.setColor3(MyColors.DARK_RED);
        result.put("dials", spr);
        return result;
    }
}
