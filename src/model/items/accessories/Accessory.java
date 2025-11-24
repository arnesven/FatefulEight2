package model.items.accessories;

import model.items.Inventory;
import model.Model;
import model.characters.GameCharacter;
import model.items.ArmorItem;
import model.items.EquipableItem;
import model.items.Item;
import model.items.analysis.ArmorAnalysis;
import model.items.analysis.ItemAnalysis;
import model.items.analysis.SkillBonusAnalysis;
import model.items.spells.Spell;
import util.MyStrings;
import view.AnalyzeArmorDialog;
import view.AnalyzeDialog;
import view.AnalyzeSkillDialog;

import java.util.ArrayList;
import java.util.List;

public abstract class Accessory extends EquipableItem implements ArmorItem {
    public Accessory(String name, int cost) {
        super(name, cost);
    }

    public abstract int getAP();

    @Override
    public boolean isHeavy() { return false; }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    @Override
    public final String getShoppingDetails() {
        StringBuilder result = new StringBuilder();
        if (getAP() != 0) {
            result.append(", Armor ").append(getAP()).append(" ").append(isHeavy() ? "HEAVY" : "LIGHT");
        }
        if (getSpeedModifier() != 0) {
            result.append(", Speed ").append(MyStrings.withPlus(getSpeedModifier()));
        }
        if (getHealthBonus() != 0) {
            result.append(", Health ").append(MyStrings.withPlus(getHealthBonus()));
        }
        if (getSPBonus() != 0) {
            result.append(", Stamina ").append(MyStrings.withPlus(getSPBonus()));
        }
        result.append(getSkillBonusesAsString());
        if (!getExtraText().equals("")) {
            result.append(", ").append(getExtraText());
        }
        if (getMasteryFactor() != 1) {
            result.append(", Levels of mastery are attained ").append(MyStrings.numberWord(getMasteryFactor())).append(" times as fast");
        }
        if (getExperienceFactor() != 1.0) {
            int percent = (int)Math.round((getExperienceFactor()-1.0) * 100.0);
            result.append(", ").append(percent).append("% more Experience Points gained.");
        }
        if (getGoldFromLootFactor() != 1.0) {
            int percent = (int)Math.round((getGoldFromLootFactor()-1.0) * 100.0);
            result.append(", ").append(percent).append("% more gold and obols from loot.");
        }
        return result.toString();
    }

    public String getExtraText() {
        return "";
    }

    public int[] getDamageTable() { return null; }

    public int getHealthBonus() { return 0; }

    public int getSPBonus() { return 0; }

    public int getSpellDiscount(Spell sp) { return 0; }

    @Override
    public void equipYourself(GameCharacter gc) {
        gc.equipAccessoryFromInventory(this);
    }

    @Override
    public boolean isAnalyzable() {
        return canAnalyzeArmor() || canAnalyzeSkills();
    }

    @Override
    public List<ItemAnalysis> getAnalyses(Model model) {
        List<ItemAnalysis> result = new ArrayList<>();
        if (canAnalyzeArmor()) {
            result.add(new ArmorAnalysis(this));
        }
        if (canAnalyzeSkills()) {
            result.add(new SkillBonusAnalysis(this));
        }
        return result;
    }

    private boolean canAnalyzeArmor() {
        return getAP() > 0;
    }

    private boolean canAnalyzeSkills() {
        return !getSkillBonuses().isEmpty();
    }

    @Override
    public boolean supportsHigherTier() {
        return true;
    }

    @Override
    public Item makeHigherTierCopy(int tier) {
        return new HigherTierAccessory((Accessory)copy(), tier);
    }

    public boolean isOffHandItem() {
        return false;
    }

    @Override
    public boolean isCraftable() {
        return true;
    }

    @Override
    public boolean isSellable() {
        return true;
    }

    public int getMasteryFactor() { return 1; }

    public double getExperienceFactor() { return 1.0; }

    public double getGoldFromLootFactor() {
        return 1.0;
    }
}
