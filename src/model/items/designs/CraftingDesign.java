package model.items.designs;

import model.Model;
import model.items.Inventory;
import model.items.Item;
import model.items.Prevalence;
import model.items.WeightlessItem;
import model.items.clothing.Clothing;
import model.items.weapons.Weapon;
import model.states.dailyaction.CraftItemState;
import view.AnalyzeDialog;
import view.CraftingDesignAnalysisDialog;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class CraftingDesign extends WeightlessItem {
    private static final Sprite ARMOR_SPRITE = new ItemSprite(6, 5, MyColors.WHITE, MyColors.BEIGE);
    private static final Sprite WEAPON_SPRITE = new ItemSprite(7, 5, MyColors.WHITE, MyColors.BEIGE);
    private static final Sprite ACCESSORY_SPRITE = new ItemSprite(8, 5, MyColors.WHITE, MyColors.BEIGE);
    private final Item craftable;

    public CraftingDesign(Item craftable) {
        super("Design '" + makeAbbreviation(craftable.getName()) + "'", 10);
        this.craftable = craftable;
    }

    @Override
    protected Sprite getSprite() {
        if (craftable instanceof Clothing) {
            return ARMOR_SPRITE;
        } else if (craftable instanceof Weapon) {
            return WEAPON_SPRITE;
        }
        return ACCESSORY_SPRITE;
    }

    @Override
    public boolean isCraftable() {
        return false;
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
        return ", A crafting design of '" + craftable.getName() +
                "', Material Cost: " + CraftItemState.calculateCost(this) +
                ", Difficulty: " + CraftItemState.calculateDifficulty(craftable);
    }

    @Override
    public Item copy() {
        return new CraftingDesign(craftable);
    }

    @Override
    public String getSound() {
        return "paper";
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    public String getCraftableName() {
        return craftable.getName();
    }

    public Item getCraftable() {
        return craftable;
    }

    @Override
    public boolean isAnalyzable() {
        return true;
    }

    @Override
    public String getAnalysisType() {
        return "Crafting Chance for";
    }

    @Override
    public AnalyzeDialog getAnalysisDialog(Model model) {
        return new CraftingDesignAnalysisDialog(model, this);
    }
}
