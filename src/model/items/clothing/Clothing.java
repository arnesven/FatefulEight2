package model.items.clothing;

import model.items.*;
import model.Model;
import model.characters.GameCharacter;
import model.items.analysis.ArmorAnalysis;
import model.items.analysis.ItemAnalysis;
import model.items.analysis.SkillBonusAnalysis;

import java.util.ArrayList;
import java.util.List;

public abstract class Clothing extends EquipableItem implements ArmorItem {

    private final int ap;
    private final boolean heavy;

    public Clothing(String name, int cost, int ap, boolean heavy) {
        super(name, cost);
        this.ap = ap;
        this.heavy = heavy;
    }

    public int getAP() {
        return ap;
    }

    public boolean isHeavy() {
        return heavy;
    }

    @Override
    public boolean isCraftable() {
        return true;
    }

    @Override
    public boolean isSellable() {
        return true;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.add(this);
    }

    @Override
    public String getShoppingDetails() {
        StringBuilder result = new StringBuilder();
        result.append(getSkillBonusesAsString());
        return ", Armor " + getAP() + " " + (isHeavy()?"HEAVY":"LIGHT") + result.toString();
    }

    @Override
    public void equipYourself(GameCharacter gc) {
        gc.equipClothingFromInventory(this);
    }

    @Override
    public String getSound() {
        return "chainmail1";
    }

    @Override
    public boolean isAnalyzable() {
        return getAP() > 0 || !getSkillBonuses().isEmpty();
    }

    @Override
    public List<ItemAnalysis> getAnalyses(Model model) {
        List<ItemAnalysis> result = new ArrayList<>();
        if (getAP() > 0) {
            result.add(new ArmorAnalysis(this));
        }
        if (!getSkillBonuses().isEmpty()) {
            result.add(new SkillBonusAnalysis(this));
        }
        return result;
    }


    @Override
    public boolean supportsHigherTier() {
        return true;
    }

    @Override
    public Item makeHigherTierCopy(int tier) {
        return new HigherTierClothing((Clothing)copy(), tier);
    }
}
