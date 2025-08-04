package model.combat.loot;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.ItemDeck;
import model.items.MysteriousMap;
import model.items.imbuements.WeaponImbuement;
import model.items.weapons.Weapon;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class StandardCombatLoot extends CombatLoot {

    private int gold = 0;
    private List<Item> items = new ArrayList<>();

    public StandardCombatLoot(Model model, int bonus) {
        int dieRoll = MyRandom.rollD10() + bonus;
        if (dieRoll <= 7) {
            gold = dieRoll - 1;
        } else if (dieRoll <= 9) {
            items.add(makeItem(model, 0.1));
        }
        if (dieRoll == 7) {
            gold++;
        }
        gold = (int)Math.round(getGoldFactor(model) * gold);
        if (dieRoll >= 10) {
            items.add(makeItem(model, 0.5));
        }
    }

    private Item makeItem(Model model, double tierChance) {
        Item toReturn = model.getItemDeck().getRandomItem(tierChance);
        if (toReturn instanceof Weapon && MyRandom.rollD6() + MyRandom.rollD6() == 12) {
            WeaponImbuement imbuement = MyRandom.sample(ItemDeck.allImbuements());
            ((Weapon) toReturn).setImbuement(imbuement);
        }
        return toReturn;
    }

    protected double getGoldFactor(Model model) {
        double factor = 1.0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.getEquipment().getAccessory() != null) {
                factor *= gc.getEquipment().getAccessory().getGoldFromLootFactor();
            }
        }
        return factor;
    }

    public StandardCombatLoot(Model model) {
        this(model, 0);
    }

    @Override
    public String getText() {
        StringBuilder bldr = new StringBuilder();
        if (items.size() > 0) {
            bldr.append(items.get(0).getName());
        }
        for (int i = 1; i < items.size(); ++i) {
            bldr.append("\n" + items.get(i).getName());
        }
        return bldr.toString();
    }

    @Override
    protected void specificGiveYourself(Party party) {
        for (Item it : items) {
            party.getInventory().addItem(it);
        }
    }

    @Override
    public int getGold() {
        return gold;
    }

    protected List<Item> getItems() {
        return items;
    }

    protected void setGold(int g) {
        gold = g;
    }
}
