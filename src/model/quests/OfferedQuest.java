package model.quests;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.map.WorldType;

import java.awt.*;
import java.util.List;
import java.io.Serializable;

public class OfferedQuest implements Serializable {
    private final String name;
    private final WorldType world;
    private final CharacterAppearance appearance;
    private final List<Point> remotePath;
    private final String place;
    private final String provider;
    private boolean completed = false;
    private int acceptedOnDay = 0;

    /**
     *
     * @param questName The name of the quest.
     * @param app The appearance of the quest giver.
     * @param path A path leading from the position where the quest was given, to the position where the quest takes place.
     * @param placeName The name of the place where the quest was offered in, e.g. the name of the town.
     * @param world Which world the quest was given in.
     */
    public OfferedQuest(String questName, CharacterAppearance app, List<Point> path,
                        String placeName, WorldType world, String provider) {
        this.name = questName;
        this.appearance = app;
        this.remotePath = path;
        this.place = placeName;
        this.world = world;
        this.provider = provider;
    }

    public List<Point> getRemotePath() {
        return remotePath;
    }

    public CharacterAppearance getAppearance() {
        return appearance;
    }

    public String getQuestName() {
        return name;
    }

    public boolean isPartyInOfferPosition(Model model) {
        // TODO: Fix support for quests in past world
        //if (world == WorldType.original) {
            return model.partyIsInOverworldPosition(remotePath.getFirst());
        //}
    }

    public boolean isPartyInRemotePosition(Model model) {
        // TODO: Fix support for quests in past world
        //if (world == WorldType.original) {
        return model.partyIsInOverworldPosition(remotePath.getLast());
    }

    public boolean isCompleted() {
        return completed;
    }

    public void accept(int day) {
        this.acceptedOnDay = day;
    }

    public void setCompleted(boolean b) {
        completed = b;
    }

    public boolean isAccepted() {
        return acceptedOnDay > 0;
    }

    public Point getOfferPosition() {
        return remotePath.getFirst();
    }

    public Point getRemotePosition() {
        return remotePath.getLast();
    }
    
    public String getLocationName() {
        return place;
    }

    public int getAcceptedOnDay() {
        return acceptedOnDay;
    }

    public String getProvider() {
        return provider;
    }

    public WorldType getWorld() {
        return world;
    }
}
