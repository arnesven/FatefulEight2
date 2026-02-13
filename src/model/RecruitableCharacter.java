package model;

import model.characters.GameCharacter;
import model.characters.preset.PresetCharacter;
import model.items.Item;
import model.states.RecruitState;
import util.MyRandom;

import java.io.Serializable;
import java.util.List;

public class RecruitableCharacter implements Serializable {
    private GameCharacter character;
    private int startingGold;
    private Item startingItem;
    private RecruitInfo info;
    private int annoyance = 0;

    public RecruitableCharacter(GameCharacter gc, boolean randomStartingClass) {
        this.character = gc;
        if (randomStartingClass && gc instanceof PresetCharacter) {
            ((PresetCharacter)character).setRandomStartingClass();
        }
        startingGold = Math.max(0, MyRandom.randInt(gc.getCharClass().getStartingGold()-10,
                gc.getCharClass().getStartingGold()));
        if (!gc.getCharClass().getStartingItems().isEmpty()) {
            startingItem = MyRandom.sample(gc.getCharClass().getStartingItems()).copy();
        }
        info = RecruitInfo.none;
    }

    public RecruitableCharacter(GameCharacter gc) {
        this(gc, true);
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public RecruitInfo getInfo() {
        return info;
    }

    public void talkTo(Model model, RecruitState state) {
        if (info.doRecruitTalk(model, state, this)) {
            info = RecruitInfo.values()[info.ordinal() + 1];
        }
    }

    public void setLevel(int level) {
        character.setLevel(level);
    }

    public int getStartingGold() {
        return startingGold;
    }

    public String getFormattedString() {
        return info.getFormattedString(character, startingGold);
    }

    public Item getStartingItem() {
        return startingItem;
    }

    public void setStartingGold(int i) {
        startingGold = i;
    }

    public void setInfo(RecruitInfo recruitInfo) {
        this.info = recruitInfo;
    }


    public static List<RecruitableCharacter> makeOneRecruitable(GameCharacter chara, RecruitInfo recruitInfo) {
        RecruitableCharacter rgc = new RecruitableCharacter(chara, false);
        rgc.setInfo(recruitInfo);
        return List.of(rgc);
    }

    public static List<RecruitableCharacter> makeOneNamedRecruitable(GameCharacter chara) {
        return makeOneRecruitable(chara, RecruitInfo.name);
    }

    public void increaseAnnoyance() {
        annoyance += 1;
    }

    public boolean noLongerWantsToJoin() {
        return MyRandom.rollD6() + MyRandom.rollD6() + MyRandom.rollD6() + 1 < annoyance;
    }

    public boolean isGettingImpatient() {
        return MyRandom.rollD6() + MyRandom.rollD6() + 1 < annoyance;
    }
}
