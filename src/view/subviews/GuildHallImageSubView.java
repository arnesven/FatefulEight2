package view.subviews;

import java.util.HashMap;
import java.util.Map;

public class GuildHallImageSubView extends ImageSubView {
    private static Map<String, GuildHallImageSubView> instances = new HashMap<>();

    private GuildHallImageSubView(String title) {
        super("guildhall", title.toUpperCase(), "You are at the " + title, true);
    }

    public static SubView getInstance(String title) {
        if (!instances.containsKey(title)) {
            instances.put(title, new GuildHallImageSubView(title));
        }
        return instances.get(title);
    }
}
