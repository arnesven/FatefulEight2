package model.items.special;

import model.Model;
import model.classes.Skill;
import model.combat.conditions.Condition;
import model.items.Item;
import model.items.Prevalence;
import model.items.SocketedItem;
import model.items.accessories.*;
import model.items.spells.Spell;
import util.MyLists;
import util.MyPair;
import view.GameView;
import view.MyColors;
import view.party.ConfigureSocketedItemMenu;
import view.party.SelectableListMenu;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FashionableSash extends Accessory implements SocketedItem {
    private static final Sprite SPRITE = new ItemSprite(12, 13, MyColors.DARK_GRAY, MyColors.RED, MyColors.ORANGE);

    private final Accessory[] innerItems = new Accessory[4];
    private List<Accessory> itemsAsList = new ArrayList<>();
    private List<MyPair<Skill, Integer>> skillBonuses = new ArrayList<>();

    public FashionableSash() {
        super("Fashionable Sash", 260);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 300 + MyLists.intAccumulate(itemsAsList, Accessory::getWeight);
    }

    @Override
    public String getExtraText() {
        if (itemsAsList.isEmpty()) {
            return "Not Configured";
        }
        return super.getExtraText();
    }

    @Override
    public Item copy() {
        return new FashionableSash();
    }

    @Override
    public String getSound() {
        return "chainmail1";
    }

    @Override
    public int getSpeedModifier() {
        return MyLists.intAccumulate(itemsAsList, Accessory::getSpeedModifier);
    }

    @Override
    public int getAP() {
        return MyLists.intAccumulate(itemsAsList, Accessory::getAP);
    }

    @Override
    public int getHealthBonus() {
        return MyLists.intAccumulate(itemsAsList, Accessory::getHealthBonus);
    }

    @Override
    public int getSPBonus() {
        return MyLists.intAccumulate(itemsAsList, Accessory::getSPBonus);
    }

    @Override
    public int getSpellDiscount(Spell sp) {
        return MyLists.intAccumulate(itemsAsList, (Accessory acc) -> acc.getSpellDiscount(sp));
    }

    @Override
    public boolean isOffHandItem() {
        return MyLists.any(itemsAsList, Accessory::isOffHandItem);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return skillBonuses;
    }

    @Override
    public boolean hasDualUseInMenu() {
        return true;
    }

    @Override
    public String getDualUseLabel() {
        return "Configure";
    }

    @Override
    public boolean supportsHigherTier() {
        return false;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.unique;
    }

    @Override
    public SelectableListMenu getDualUseMenu(GameView innerView, int x, int y) {
        return new ConfigureSocketedItemMenu(innerView, this);
    }

    @Override
    public boolean isHeavy() {
        return MyLists.any(itemsAsList, Accessory::isHeavy);
    }

    @Override
    public int getNumberOfSockets() {
        return innerItems.length;
    }

    public Accessory getInnerItem(int index) {
        return innerItems[index];
    }

    @Override
    public void setInnerItem(int index, Item accessory) {
        innerItems[index] = (Accessory) accessory;
        itemsAsList = MyLists.toNullessList(innerItems);
        updateSkillBonuses();
    }

    @Override
    public String getSocketLabels() {
        return "Feet    Head    Gloves  Jewelry";
    }

    @Override
    public List<Item> getItemsForSlot(Model model, int index) {
        List<Item> items = model.getParty().getInventory().getAllItems();
        switch (index) {
            case 0:
                return MyLists.filter(items, (Item acc) -> acc instanceof ShoesItem);
            case 1:
                return MyLists.filter(items, (Item acc) -> acc instanceof HeadGearItem);
            case 2:
                return MyLists.filter(items, (Item acc) -> acc instanceof GlovesItem);
            default:
                return MyLists.filter(items, (Item acc) -> acc instanceof JewelryItem);
        }
    }

    private void updateSkillBonuses() {
        Map<Skill, Integer> map = new HashMap<>();
        for (Accessory acc : itemsAsList) {
            for (MyPair<Skill, Integer> bonus : acc.getSkillBonuses()) {
                if (!map.containsKey(bonus.first)) {
                    map.put(bonus.first, 0);
                }
                map.put(bonus.first, map.get(bonus.first) + bonus.second);
            }
        }
        skillBonuses = MyLists.filter(
                        MyLists.transform(new ArrayList<>(map.entrySet()),
                                          (Map.Entry<Skill, Integer> entry) ->
                                                  new MyPair<>(entry.getKey(), entry.getValue())),
                                      (MyPair<Skill, Integer> pair) -> pair.second != 0);
    }

    @Override
    public boolean grantsConditionImmunity(Condition cond) {
        return MyLists.any(itemsAsList, (Accessory acc) -> acc.grantsConditionImmunity(cond));
    }
}
