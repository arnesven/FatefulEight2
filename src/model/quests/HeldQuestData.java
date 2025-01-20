package model.quests;

import model.characters.appearance.CharacterAppearance;

import java.awt.*;
import java.util.List;
import java.io.Serializable;

public class HeldQuestData implements Serializable {
    private CharacterAppearance appearance;
    private List<Point> remotePath;

    public HeldQuestData(CharacterAppearance app, List<Point> path) {
        this.appearance = app;
        this.remotePath = path;
    }

    public List<Point> getRemotePath() {
        return remotePath;
    }

    public CharacterAppearance getAppearance() {
        return appearance;
    }
}
