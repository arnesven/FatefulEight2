package view.subviews;

import model.Model;
import model.quests.Quest;
import model.quests.QuestDifficulty;
import util.MyStrings;
import view.BorderFrame;
import view.MyColors;
import view.widget.TopText;

public class SelectQuestSubView extends SubView {
    private final SubView previous;
    private final Quest quest;
    private static final int WIDTH = 34;
    private static final int HEIGHT = 14;

    public SelectQuestSubView(SubView previous, Quest quest) {
        this.previous = previous;
        this.quest = quest;
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        int xStart = X_OFFSET + (X_MAX - X_OFFSET - WIDTH) / 2;
        int yStart = 11;
        BorderFrame.drawFrame(model.getScreenHandler(),
                xStart, 10, WIDTH, HEIGHT,
                MyColors.BLACK, MyColors.WHITE, MyColors.BLACK, true);
        BorderFrame.drawString(model.getScreenHandler(), quest.getName(), xStart+2, yStart, MyColors.WHITE, MyColors.BLACK);
        drawProvider(model, xStart, yStart);

        drawDifficulty(model, quest.getName().length() + xStart+2, yStart);
        drawRewards(model, xStart, yStart);
        drawDetails(model, xStart, yStart);
    }

    private void drawDetails(Model model, int xStart, int yStart) {
        int row = 2;
        for (String detail : quest.getDetails()) {
            String toPrint = detail.replace("(", "").replace(")", "");
            BorderFrame.drawString(model.getScreenHandler(), detail,
                    xStart + 18, yStart + (row++), MyColors.WHITE, MyColors.BLACK);
        }
    }

    private void drawRewards(Model model, int xStart, int yStart) {
        int row = 2;
        BorderFrame.drawString(model.getScreenHandler(), "Rewards", xStart + 10, yStart + row++, MyColors.WHITE, MyColors.BLACK);
        if (quest.getReward().getReputation() != 0) {
            int rep = quest.getReward().getReputation();
            BorderFrame.drawString(model.getScreenHandler(), "  " + MyStrings.withPlus(rep),
                    xStart + 10, yStart + (row++), (rep < 0 ? MyColors.RED : MyColors.WHITE), MyColors.BLACK);
            model.getScreenHandler().put(xStart + 10, yStart + (row-1), TopText.REP_ICON_SPRITE);
        }
        if (quest.getReward().getGold() != 0) {
            BorderFrame.drawString(model.getScreenHandler(), "  " + quest.getReward().getGold()*model.getParty().size(),
                    xStart + 10, yStart + (row++), MyColors.WHITE, MyColors.BLACK);
            model.getScreenHandler().put(xStart + 10, yStart + (row-1), TopText.GOLD_ICON_SPRITE);
        }
        if (quest.getReward().getExp() != 0) {
            BorderFrame.drawString(model.getScreenHandler(), "XP " + quest.getReward().getExp(),
                    xStart + 10, yStart + (row++), MyColors.WHITE, MyColors.BLACK);
        }
    }

    private void drawDifficulty(Model model, int xStart, int yStart) {
        MyColors diffColor = MyColors.RED;
        if (quest.getDifficulty() == QuestDifficulty.MEDIUM) {
            diffColor = MyColors.YELLOW;
        } else if (quest.getDifficulty() == QuestDifficulty.EASY) {
            diffColor = MyColors.GREEN;
        }
        BorderFrame.drawString(model.getScreenHandler(), quest.getDifficulty().toString(), xStart + 1, yStart, diffColor, MyColors.BLACK);

    }

    private void drawProvider(Model model, int xStart, int yStart) {
        quest.getPortrait().drawYourself(model.getScreenHandler(), xStart + 2, yStart+2);
        String provider = quest.getProvider();
        String[] providerParts = MyStrings.partition(provider, 12);
        for (int i = 0; i < providerParts.length; ++i) {
            int x = xStart + 1 + 5 - providerParts[i].length() / 2;
            BorderFrame.drawString(model.getScreenHandler(), providerParts[i], x, yStart+9+i, MyColors.WHITE, MyColors.BLACK);
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENING - QUEST OFFERED";
    }

    public boolean didAcceptQuest() {
        return false;
    }
}
