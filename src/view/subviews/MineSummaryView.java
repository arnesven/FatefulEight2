package view.subviews;

import model.Model;
import view.BorderFrame;
import view.MyColors;

public class MineSummaryView extends SubView {
    private int steps = 0;
    private int rocksMined = 0;
    private int gemstones = 0;
    private int gold = 0;
    private int materials = 0;
    private int obols = 0;
    private int enemies = 0;
    private int roomsDiscovered = 1;
    private int maxLevel = 1;

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX,
                blueBlock);
        int row = Y_OFFSET + 1;
        BorderFrame.drawString(model.getScreenHandler(), String.format("Steps taken:       %d", steps),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("Deepest level:     %d", maxLevel),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("Monsters defeated: %d", enemies),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("Rooms discovered:  %d", roomsDiscovered),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);
        row++;
        BorderFrame.drawString(model.getScreenHandler(), String.format("Rocks mined:       %d", rocksMined),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("Materials gained:  %d", materials),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("Obols gained:      %d", obols),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("Gold gained:       %d", gold),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("Gemstones gained:  %d", gemstones),
                X_OFFSET, row++, MyColors.WHITE, MyColors.BLUE);

    }

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "MINE SUMMARY";
    }

    public void incrementSteps() {
        steps++;
    }

    public void incrementObjectsMined() {
        rocksMined++;
    }

    public void incrementGemstones() {
        gemstones++;
    }

    public void incrementGold(int gold) {
        this.gold += gold;
    }

    public void incrementMaterials(int materials) {
        this.materials = materials;
    }

    public void incrementObols(int obols) {
        this.obols = obols;
    }

    public void incrementEnemiesDefeated() {
        this.enemies++;
    }

    public void incrementRoomsDiscovered() {
        this.roomsDiscovered++;
    }

    public void setMaxLevel(int level) {
        if (level > maxLevel) {
            maxLevel = level;
        }
    }
}
