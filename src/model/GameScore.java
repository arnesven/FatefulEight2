package model;

import model.headquarters.Headquarters;
import model.horses.Horse;
import model.items.Item;
import model.items.ObolsDummyItem;
import model.items.spells.Spell;
import util.MyLists;
import util.MyPair;

import java.util.ArrayList;

public class GameScore extends ArrayList<MyPair<String, Integer>> {

    private static final int SCORE_PER_ACHIEVEMENT = 100;
    private static final int SCORE_PER_MAIN_STORY_STEP = 75;
    private static final int SCORE_FOR_MAIN_COMPLETED = 500;

    public void put(String key, int value) {
        add(new MyPair<>(key, value));
    }

    public static GameScore calculate(Model model) {
        GameScore gs = new GameScore();
        gs.put("Money", calcScoreFromMoney(model));
        gs.put("Possessions", calcPossessionValue(model));
        if (model.getParty().getHeadquarters() != null) {
            gs.put("Headquarters", calcHeadquartersScore(model));
        }
        gs.put("Achievements", model.getAchievements().numberOfCompleted(model) * SCORE_PER_ACHIEVEMENT);
        int mainStoryScore =  model.getMainStory().getStoryParts().size() * SCORE_PER_MAIN_STORY_STEP;
        if (model.getMainStory().isCompleted(model)) {
            mainStoryScore += SCORE_FOR_MAIN_COMPLETED;
            gs.put("Main Story Progress", mainStoryScore);
        } else {
            gs.put("Main Story", mainStoryScore);
        }
        return gs;
    }

    private static Integer calcScoreFromMoney(Model model) {
        int score = model.getParty().getGold();
        score += new ObolsDummyItem(model.getParty().getObols()).getCost();
        return score;
    }

    private static int calcPossessionValue(Model model) {
        int score = MyLists.intAccumulate(model.getParty().getPartyMembers(), gc ->
                gc.getEquipment().getWeapon().getCost() +
                gc.getEquipment().getClothing().getCost() +
                (gc.getEquipment().getAccessory() != null ? gc.getEquipment().getAccessory().getCost() : 0));

        score += MyLists.intAccumulate(model.getParty().getInventory().getAllItems(), Item::getCost);
        score += MyLists.intAccumulate(model.getParty().getHorseHandler(), Horse::getCost);
        score += MyLists.intAccumulate(model.getParty().getLearnedSpells(), Spell::getCost);
        score += MyLists.intAccumulate(model.getParty().getLearnedCraftingDesigns(), Item::getCost);
        score += MyLists.intAccumulate(model.getParty().getLearnedPotionRecipes(), Item::getCost);
        return score;
    }

    private static int calcHeadquartersScore(Model model) {
        Headquarters hq = model.getParty().getHeadquarters();
        assert hq != null;
        int score = hq.getCost();
        score += hq.getGold();
        score += MyLists.intAccumulate(hq.getItems(), Item::getCost);
        score += MyLists.intAccumulate(hq.getHorses(), Horse::getCost);
        return score;
    }

    public int getTotal() {
        return MyLists.intAccumulate(this, p -> p.second);
    }


    public static String makeTutorialText() {
        String text =
                "When you retire (after 100 days) your game will be scored. " +
                        "Your score is calculated in the following way:\n\n" +
                        "Money................total value\n" +
                        "Possessions..........total value\n" +
                        "Headquarters.........total value\n" +
                        "Achievements............" + SCORE_PER_ACHIEVEMENT + " each\n" +
                        "Main Story Progress......" + SCORE_PER_MAIN_STORY_STEP + " each\n" +
                        "Main Story Completed........." + SCORE_FOR_MAIN_COMPLETED + "\n\n" +
                "Your possessions include:\n" +
                        "* Party members' equipped items\n" +
                        "* Items in your inventory\n" +
                        "* Your horses\n" +
                        "* Learned spells, and recipes";
        return text;
    }
}
