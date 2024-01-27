 package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatLoot;
import model.enemies.Enemy;
import model.states.CombatStatistics;
import view.BorderFrame;
import view.MyColors;
import view.sprites.FilledBlockSprite;

import java.util.List;
import java.util.Map;

public class CombatSummarySubView extends SubView {
    private final List<CombatLoot> loot;
    private final CombatStatistics combatStats;

    public CombatSummarySubView(CombatStatistics combatStats, List<CombatLoot> combatLoots) {
        this.combatStats = combatStats;
        this.loot = combatLoots;
    }

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX,
                blueBlock);
        int xOffset = X_OFFSET + 2;
        int row = Y_OFFSET+2;

        row = printStatistics(model, xOffset, row);
        row += 2;
        row = printSlainPartyMembers(model, xOffset, row);
        row += 2;
        row = printLoot(model, xOffset, row);
    }

    private int printLoot(Model model, int xOffset, int row) {
        BorderFrame.drawString(model.getScreenHandler(), "Loot: ",
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        int gold = 0;
        int rations = 0;
        int ingredients = 0;
        int materials = 0;
        int obols = 0;
        for (CombatLoot l : loot) {
            if (!l.getText().equals("")) {
                String text = l.getText();
                String[] parts = text.split("\\n");
                for (int i = 0; i < parts.length; ++i) {
                    BorderFrame.drawString(model.getScreenHandler(), parts[i], xOffset, row++,
                            MyColors.WHITE, MyColors.BLUE);
                }
            }
            gold += l.getGold();
            rations += l.getRations();
            ingredients += l.getIngredients();
            materials += l.getMaterials();
            obols += l.getObols();
        }

        if (gold > 0) {
            BorderFrame.drawString(model.getScreenHandler(), gold + " Gold", xOffset, row++,
                    MyColors.WHITE, MyColors.BLUE);
        }

        if (rations > 0) {
            BorderFrame.drawString(model.getScreenHandler(), rations + " Rations", xOffset, row++,
                    MyColors.WHITE, MyColors.BLUE);
        }

        if (ingredients > 0) {
            BorderFrame.drawString(model.getScreenHandler(), ingredients + " Ingredients", xOffset, row++,
                    MyColors.WHITE, MyColors.BLUE);
        }

        if (materials > 0) {
            BorderFrame.drawString(model.getScreenHandler(), materials + " Materials", xOffset, row++,
                    MyColors.WHITE, MyColors.BLUE);
        }

        if (obols > 0) {
            BorderFrame.drawString(model.getScreenHandler(), materials + " Obols", xOffset, row++,
                    MyColors.WHITE, MyColors.BLUE);
        }
        return row;
    }

    private int printSlainPartyMembers(Model model, int xOffset, int row) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.isDead()) {
                BorderFrame.drawString(model.getScreenHandler(), gc.getFullName() + " was slain",
                        xOffset, row-1, MyColors.RED, MyColors.BLUE);
                row++;
            }
        }
        return row;
    }

    private int printStatistics(Model model, int xOffset, int row) {
        BorderFrame.drawString(model.getScreenHandler(), "Enemies Defeated: " + combatStats.getKilledEnemies(),
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Enemies Retreated: " + combatStats.getFledEnemies(),
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        row++;
        BorderFrame.drawString(model.getScreenHandler(), "Max Damage Dealt: " + combatStats.getMaximumDamage() +
                        " (" + combatStats.getMaxDamager() + ")",
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Total Damage Dealt: " + combatStats.getTotalDamage() ,
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Average Damage Dealt: " + String.format("%1.1f", combatStats.getAverageDamage()),
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Accuracy: " + combatStats.getAccuracy() + "%",
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        row++;
        BorderFrame.drawString(model.getScreenHandler(), "Max Damage Taken: " + combatStats.getMaxEnemyDamage(),
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Total Damage Taken: " + combatStats.getTotalEnemyDamage() ,
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Damage Reduced: " + combatStats.getReducedDamage() ,
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        row++;
        BorderFrame.drawString(model.getScreenHandler(), "Damage Avoided: " + combatStats.getAvoidedDamage() ,
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        row++;
        BorderFrame.drawString(model.getScreenHandler(), "Round Par: " + combatStats.getRoundPar(),
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Rounds: " + combatStats.getRoundsTakenWithBird(),
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        row++;
        BorderFrame.drawString(model.getScreenHandler(), "MVP: " + combatStats.getMVP(),
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        return row;
    }

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "COMBAT SUMMARY";
    }
}
