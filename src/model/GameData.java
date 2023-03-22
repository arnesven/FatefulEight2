package model;

import model.tutorial.TutorialHandler;

import java.io.Serializable;
import java.util.List;

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
}
