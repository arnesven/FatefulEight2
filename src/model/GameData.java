package model;

import model.characters.appearance.CharacterAppearance;
import model.items.ItemDeck;
import model.map.WorldBuilder;
import model.ruins.RuinsDungeon;
import model.tutorial.TutorialHandler;
import util.MyRandom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameData implements Serializable {
    public Party party = new Party();
    public ItemDeck itemDeck = new ItemDeck();
    public QuestDeck questDeck = new QuestDeck();
    public int day = 1;
    public CharacterCollection allCharacters = new CharacterCollection();
    public List<String> logContent;
    public boolean mustStayInHex = false;
    public TimeOfDay timeOfDay = TimeOfDay.MORNING;
    public TutorialHandler tutorial = new TutorialHandler();
    public boolean freePlay = false;
    public boolean inUnderworld = false;
    public int caveSystemSeed = MyRandom.randInt(Integer.MAX_VALUE);
    public Map<String, RuinsDungeon> dungeons = new HashMap<>();
    public SettingsManager settings = new SettingsManager();
    public MainStory mainStory = new MainStory();
    public Map<String, CharacterAppearance> lordPortraits = new HashMap<>();
    public int worldState = WorldBuilder.ORIGINAL;
}
