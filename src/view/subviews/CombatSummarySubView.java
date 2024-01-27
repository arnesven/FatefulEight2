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
        combatStats.calculateStatistics();
        this.loot = combatLoots;
    }

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX,
                blueBlock);
        int xOffset = X_OFFSET + 2;
        int row = Y_OFFSET+2;
        BorderFrame.drawString(model.getScreenHandler(), "Enemies Defeated: " + combatStats.getKilledEnemies(),
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Enemies Retreated: " + combatStats.getFledEnemies(),
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Max Damage: " + combatStats.getMaximumDamage() +
                        " (" + combatStats.getMaxDamager() + ")",
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Accuracy: " + combatStats.getAccuracy() + "%",
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "MVP: " + combatStats.getMVP(),
                xOffset, row++, MyColors.WHITE, MyColors.BLUE);



        row += 2;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.isDead()) {
                BorderFrame.drawString(model.getScreenHandler(), gc.getFullName() + " was slain",
                        xOffset, row-1, MyColors.RED, MyColors.BLUE);
                row++;
            }
        }
        row += 2;
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
