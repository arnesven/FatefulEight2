 package view.subviews;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatLoot;
import model.enemies.Enemy;
import model.states.CombatStatistics;
import util.Arithmetics;
import util.MyPair;
import view.BorderFrame;
import view.MyColors;
import view.sprites.FilledBlockSprite;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static view.party.SelectableListMenu.downScroll;
import static view.party.SelectableListMenu.upScroll;

 public class CombatSummarySubView extends SubView {
    private static final MyPair<String, MyColors> NEW_LINE = new MyPair<>("", MyColors.WHITE);
     private static final MyColors TITLE_COLOR = MyColors.LIGHT_GRAY;
     private final List<CombatLoot> loot;
    private final CombatStatistics combatStats;
    private int scrollShift = 0;

    public CombatSummarySubView(CombatStatistics combatStats, List<CombatLoot> combatLoots) {
        this.combatStats = combatStats;
        this.loot = combatLoots;
    }

    @Override
    protected void drawArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX,
                blueBlock);
        int row = Y_OFFSET;

        List<MyPair<String, MyColors>> content = buildContents(model);
        int viewHeight = Y_MAX - Y_OFFSET;
        int maxI = Math.min(content.size(), viewHeight + scrollShift);
        for (int i = scrollShift; i < maxI; i++) {
            MyPair<String, MyColors> p = content.get(i);
            int xOff = X_OFFSET;
            if (p.second != TITLE_COLOR) {
                xOff++;
            }
            BorderFrame.drawString(model.getScreenHandler(), p.first,
                    xOff, row++, p.second, MyColors.BLUE);
        }
        if (content.size() >= viewHeight) {
            if (scrollShift > 0) {
                model.getScreenHandler().put(X_MAX - 1, Y_OFFSET, upScroll);
            }
            if (scrollShift < content.size() - viewHeight) {
                model.getScreenHandler().put(X_MAX - 1, Y_MAX - 1, downScroll);
            }
        }
    }

    private List<MyPair<String, MyColors>> buildContents(Model model) {
        List<MyPair<String, MyColors>> result = new ArrayList<>();
        result.addAll(printSlainPartyMembers(model));
        result.addAll(printLoot());
        result.add(NEW_LINE);
        result.add(NEW_LINE);
        result.addAll(printStatistics());
        return result;
    }

    private List<MyPair<String, MyColors>> printLoot() {
        List<MyPair<String, MyColors>> result = new ArrayList<>();
        result.add(new MyPair<>("LOOT", TITLE_COLOR));
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
                    result.add(new MyPair<>(parts[i], MyColors.WHITE));
                }
            }
            gold += l.getGold();
            rations += l.getRations();
            ingredients += l.getIngredients();
            materials += l.getMaterials();
            obols += l.getObols();
        }

        if (gold > 0) {
            result.add(new MyPair<>(gold + " Gold", MyColors.WHITE));
        }

        if (rations > 0) {
            result.add(new MyPair<>(rations + " Rations", MyColors.WHITE));
        }

        if (ingredients > 0) {
            result.add(new MyPair<>(ingredients + " Ingredients", MyColors.WHITE));
        }

        if (materials > 0) {
            result.add(new MyPair<>(materials + " Materials", MyColors.WHITE));
        }

        if (obols > 0) {
            result.add(new MyPair<>(obols + " Obols", MyColors.WHITE));
        }
        return result;
    }

    private List<MyPair<String, MyColors>> printSlainPartyMembers(Model model) {
        List<MyPair<String, MyColors>> result = new ArrayList<>();
        boolean added = false;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.isDead()) {
                result.add(new MyPair<>(gc.getFullName() + " was slain", MyColors.RED));
                added = true;
            }
        }
        if (added) {
            result.add(NEW_LINE);
            result.add(NEW_LINE);
        }
        return result;
    }

    private List<MyPair<String, MyColors>> printStatistics() {
        List<MyPair<String, MyColors>> result = new ArrayList<>();
        result.add(new MyPair<>("STATISTICS", TITLE_COLOR));
        result.add(new MyPair<>("Enemies Defeated: " + combatStats.getKilledEnemies(), MyColors.WHITE));
        result.add(new MyPair<>("Enemies Retreated: " + combatStats.getFledEnemies(),
                MyColors.WHITE));
        result.add(NEW_LINE);
        result.add(new MyPair<>("Max Damage Dealt: " + combatStats.getMaximumDamage() +
                        " (" + combatStats.getMaxDamager() + ")", MyColors.WHITE));
        result.add(new MyPair<>("Total Damage Dealt: " + combatStats.getTotalDamage(), MyColors.WHITE));
        result.add(new MyPair<>("Average Damage Dealt: " + String.format("%1.1f", combatStats.getAverageDamage()),
                MyColors.WHITE));
        result.add(new MyPair<>("Accuracy: " + combatStats.getAccuracy() + "%", MyColors.WHITE));
        result.add(NEW_LINE);
        result.add(new MyPair<>("Max Damage Taken: " + combatStats.getMaxEnemyDamage(), MyColors.WHITE));
        result.add(new MyPair<>("Total Damage Taken: " + combatStats.getTotalEnemyDamage(), MyColors.WHITE));
        result.add(new MyPair<>("Damage Reduced: " + combatStats.getReducedDamage(), MyColors.WHITE));
        result.add(new MyPair<>("Damage Avoided: " + combatStats.getAvoidedDamage(), MyColors.WHITE));
        result.add(NEW_LINE);
        result.add(new MyPair<>("Combat Par: " + combatStats.getRoundPar() + " Rounds", MyColors.WHITE));
        result.add(new MyPair<>("Rounds Taken: " + combatStats.getRoundsTakenWithBird(), MyColors.WHITE));
        result.add(NEW_LINE);
        result.add(new MyPair<>("MVP: " + combatStats.getMVP(), MyColors.WHITE));
        return result;
    }

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "COMBAT SUMMARY";
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            scrollShift = Math.max(scrollShift - 1, 0);
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            scrollShift++;
        }
        return super.handleKeyEvent(keyEvent, model);
    }
}
