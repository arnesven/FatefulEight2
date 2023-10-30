package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatLoot;
import model.enemies.Enemy;
import view.BorderFrame;
import view.MyColors;
import view.sprites.FilledBlockSprite;

import java.util.List;
import java.util.Map;

public class CombatSummarySubView extends SubView {
    private final int enemies;
    private final List<CombatLoot> loot;
    private final int fledEnemies;

    public CombatSummarySubView(Model model, int killedEnemies, int fledEnemies, List<CombatLoot> combatLoots) {
        this.enemies = killedEnemies;
        this.fledEnemies = fledEnemies;
        this.loot = combatLoots;
    }

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX,
                blueBlock);

        BorderFrame.drawString(model.getScreenHandler(), "Enemies Defeated: " + enemies,
                X_OFFSET+3, Y_OFFSET+3, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Enemies Retreated: " + fledEnemies,
                X_OFFSET+3, Y_OFFSET+4, MyColors.WHITE, MyColors.BLUE);
        int row = Y_OFFSET+6;

        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.isDead()) {
                BorderFrame.drawString(model.getScreenHandler(), gc.getFullName() + " was slain",
                        X_OFFSET+3, row-1, MyColors.RED, MyColors.BLUE);
                row++;
            }
        }
        row += 2;
        BorderFrame.drawString(model.getScreenHandler(), "Loot: ",
                X_OFFSET+3, row++, MyColors.WHITE, MyColors.BLUE);
        int gold = 0;
        int rations = 0;
        int ingredients = 0;
        int materials = 0;
        for (CombatLoot l : loot) {
            if (!l.getText().equals("")) {
                BorderFrame.drawString(model.getScreenHandler(), l.getText(), X_OFFSET+3, row++,
                        MyColors.WHITE, MyColors.BLUE);
            }
            gold += l.getGold();
            rations += l.getRations();
            ingredients += l.getIngredients();
            materials += l.getMaterials();
        }

        if (gold > 0) {
            BorderFrame.drawString(model.getScreenHandler(), gold + " Gold", X_OFFSET+3, row++,
                    MyColors.WHITE, MyColors.BLUE);
        }

        if (rations > 0) {
            BorderFrame.drawString(model.getScreenHandler(), rations + " Rations", X_OFFSET+3, row++,
                    MyColors.WHITE, MyColors.BLUE);
        }

        if (ingredients > 0) {
            BorderFrame.drawString(model.getScreenHandler(), ingredients + " Ingredients", X_OFFSET+3, row++,
                    MyColors.WHITE, MyColors.BLUE);
        }

        if (materials > 0) {
            BorderFrame.drawString(model.getScreenHandler(), materials + " Materials", X_OFFSET+3, row++,
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
