package model.quests;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestScene extends ArrayList<QuestSubScene> implements Serializable {

    private final String name;

    public QuestScene(String name, List<QuestSubScene> subScenes) {
        this.name = name;
        addAll(subScenes);
    }

    public String getName() {
        return name;
    }
}
