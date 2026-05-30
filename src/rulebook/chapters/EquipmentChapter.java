package rulebook.chapters;

import model.horses.*;
import model.items.*;
import model.items.accessories.Accessory;
import model.items.accessories.SkullRing;
import model.items.books.ElfOriginBook;
import model.items.clothing.Clothing;
import model.items.clothing.LeatherArmor;
import model.items.potions.Potion;
import model.items.special.LargeTentUpgradeItem;
import model.items.special.TentUpgradeItem;
import model.items.weapons.Dagger;
import model.items.weapons.Weapon;
import rulebook.ImageScreenHandler;
import util.MyLists;
import util.MyStrings;
import view.help.TutorialHaggling;
import view.help.TutorialWeaponPairing;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static rulebook.GenerateRulebook.PATH_BASE;

public class EquipmentChapter extends RulebookChapter {
    public EquipmentChapter() {
        super("Equipment");
    }

    @Override
    public void generate(BufferedWriter writer) throws IOException {
        makeImages();

        makeEquippingSubchapter(writer);
        makeSellingSubchapter(writer);
        makeHigherTierSubchapter(writer);
        makeHagglingSubChapter(writer);
        makeWeaponsSubchapter(writer);
        makeApparelSubchapter(writer);
        makeAccessoriesSubchapter(writer);
        makePotionsSubChapter(writer);
        makeOtherGoodsAndServicesSubChapter(writer);
    }

    private void makeEquippingSubchapter(BufferedWriter writer) throws IOException {
        writer.write("## Equipping Items\n");
        writer.write("Each character can equip a weapon, a piece of apparel, and an accessory. " +
                "This hard limit of three items may seem restrictive at first, but it is meant to " +
                "simply character management, especially when one player controls multiple characters. " +
                "Apart from this rule there are other restrictions which must be followed when equipping items:\n\n");
        writer.write("* A character cannot equip an off-hand item (most often this is a shield) and a two-handed weapon at the same time.\n");
        writer.write("* A character cannot equip a piece of armor (apparel or accessory) which is Heavy if his or her class does not permit it.\n");
        writer.write("* A Character cannot equip items with a total weight that exceeds his or her carrying capacity.\n");
        writer.newLine();
    }

    private void makeSellingSubchapter(BufferedWriter writer) throws IOException {
        writer.write("## Selling Items\n");
        writer.write("You normally sell items for half their cost but this is modified by a character's Mercantile skill. " +
                "However, not all merchants will accept all goods. \n");
        writer.newLine();
    }

    private void makeHigherTierSubchapter(BufferedWriter writer) throws IOException {
        writer.write("## Higher Tiers\n");
        writer.write("Sometimes characters will fine nicer items, items of higher quality. Such items are called <i>Higher Tier</i> items. " +
                "Naturally, such items are more valuable and much less common than their basic counterparts.\n");
        writer.newLine();
        writer.write("| Tier  | Name | Cost Multiplier | Weapon | Apparel | Accessory |\n");
        writer.write("|------|-------|-----------------|--------|---------|-----------|\n");
        writer.write("|  0   | Basic |       |         |           |\n");
        writer.write("| 1 | " + Item.TIER_PREFIXES[0] + " | x3 | Extra '8' in damage table. | <p>1 more Physical/Magical Armor than basic<sup>a</sup></p> |  <p>Bonus provided by basic item + 1<sup>b</sup></p>|\n");
        writer.write("| 2 | " + Item.TIER_PREFIXES[1] + " | x5 | Extra '6' and '9' in damage table. | <p>2 more Physical/Magical Armor than basic<sup>a</sup></p> | <p>Bonus provided by basic item + 2<sup>b</sup></p>\n");
        writer.write("| 3 | " + Item.TIER_PREFIXES[2] + " | x7 | Two extra '8's in damage table. | <p>3 more Physical/Magical Armor than basic<sup>a</sup></p> | <p>Bonus provided by basic item + 3<sup>b</sup></p>\n");
        writer.write("| 4 | " + Item.TIER_PREFIXES[3] + " | x9 | Extra '6', '7' and '9' in damage table.  | <p>4 more Physical/Magical Armor than basic<sup>a</sup></p> | <p>Bonus provided by basic item + 4<sup>b</sup></p>\n");
        writer.newLine();
        writer.write("<p><sup>a</sup> <i>Only if basic item provided any Physical/Magical armor.</i>\n");
        writer.write("<p><sup>b</sup> <i>This bonus is doubled if it applies to Health Points or Speed.</i>\n");
        writer.newLine();
        writer.write("Some examples:\n");
        Item it = new Dagger().makeHigherTierCopy(2);
        writer.write("* A <i>" + it.getName() + "</i> would cost " + it.getCost() +
                " gold and deal " + ((Weapon)it).getDamageTableAsString() + " damage.\n");
        it = new LeatherArmor().makeHigherTierCopy(3);
        writer.write("* A <i>" + it.getName() + "</i> would cost " + it.getCost() +
                " gold and provide " + ((Clothing)it).getAP() + " Physical Armor.\n");
        it = new SkullRing().makeHigherTierCopy(4);
        writer.write("* A <i>" + it.getName() + "</i> would cost " + it.getCost() + " gold and provide " +
                it.getShoppingDetails().replaceFirst(",", "") + ".\n");
        writer.newLine();
    }

    private void makeHagglingSubChapter(BufferedWriter writer) throws IOException {
        writer.write("## Haggling\n");
        Scanner scan = new Scanner(TutorialHaggling.TEXT);
        boolean inTable = false;
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.contains("Result Discount")) {
                writer.write("```\n");
                inTable = true;
            }
            writer.write(line + "\n");
            if (inTable && !line.contains("%") && !line.contains("Result Discount")) {
                inTable = false;
                writer.write("```\n");
            }
        }
        writer.newLine();
    }

    private void makeWeaponsSubchapter(BufferedWriter writer) throws IOException {
        writer.write("## Weapons\n");
        writer.write("Weapons are used in combat. A character can only equip one weapon, or one weapon pair.\n");
        writer.write("Melee weapons can only be used if the character is located in the front row. Ranged weapons can be used from either row.");
        writer.write("Two-handed weapons have roman numeral II in the upper right corner in the weapon list below.\n");
        writer.write("### Paired Weapons\n");
        Scanner scan = new Scanner(TutorialWeaponPairing.TEXT);
        for (String line = scan.nextLine(); scan.hasNext(); line = scan.nextLine()) {
            if (!line.contains("Inventory Menu")) {
                if (line.startsWith("-")) {
                    line = "* " + line.replaceFirst("-", "");
                }
                writer.write(line + "\n");
                if (line.contains("The penalty is")) {
                    writer.write("```\n");
                }
            }
        }
        writer.write("```");
        writer.newLine();

        writer.write("### Imbuements\n");
        writer.write("An imbuement is a magical property ensconced on a weapon. See the Magic chapter for a list of " +
                "weapon imbuements and their effects. Weapons with imbuements have a red dot in the lower right corner in " +
                "the weapon list below.\n");
        List<Weapon> weapons = ItemDeck.allWeapons();
        weapons.sort(Comparator.comparing(Item::getName));

        writer.write("## Weapon List\n");
        writer.newLine();
        writer.write("<font size=1>\n");
        writer.newLine();
        writer.write("| Image | Weapon | Skill | Damage | Range | Cost | Wt | Prevalence | Notes |\n");
        writer.write("|-------|--------|-------|--------|-------|------|----|------------|-------|\n");
        for (Weapon w : weapons) {
            String range = "Melee";
            if (w.isRangedAttack()) {
                range = "Ranged";
            }
            if (w.isTwoHanded()) {
                range += " Two-Hand";
            }
            double weight = w.getWeight() / 1000.0;

            String extra = w.getExtraText();
            if (w.getSpeedModifier() != 0) {
                extra = "Speed " + MyStrings.withPlus(w.getSpeedModifier()) + " " + extra;
            }
            if (extra.startsWith(",")) {
                extra = extra.substring(1, extra.length());
            }
            writer.write("| " + imgTag(w) + " | " + w.getName() + " | " + w.getSkill().getName() + " | " +
                    w.getDamageTableAsString() + " | " + range + " | " + w.getCost() + " | " + weight + " | " +  MyStrings.capitalize(w.getPrevalence().name()) + " | " +
                    extra + " |\n");
        }

        writer.write("\n</font>\n");
        writer.newLine();
    }

    private void makeApparelSubchapter(BufferedWriter writer) throws IOException {
        writer.write("## Apparel\n");
        writer.newLine();
        List<Clothing> clothing = new ArrayList<>(ItemDeck.allApparel());
        clothing.sort(Comparator.comparing(Item::getName));

        writer.newLine();
        writer.write("<font size=1>\n");
        writer.newLine();
        writer.write("| Image | Item | Cost | Wt | Prevalence | Notes |\n");
        writer.write("|-------|------|------|----|------------|-------|\n");
        for (Clothing c : clothing) {
            double weight = c.getWeight() / 1000.0;
            String extra = c.getShoppingDetails();
            if (extra.startsWith(",")) {
                extra = extra.replaceFirst(",", "");
            }
            writer.write("| " + imgTag(c) + " | " + c.getName() + " | " +
                    c.getCost() + " | " + weight + " | " +  MyStrings.capitalize(c.getPrevalence().name()) + " | " +
                    extra + " |\n");
        }

        writer.write("\n</font>\n");
        writer.newLine();
    }

    private void makeAccessoriesSubchapter(BufferedWriter writer) throws IOException {
        writer.write("## Accessories\n");
        writer.newLine();
        List<Accessory> accessories = new ArrayList<>();
        accessories.addAll(ItemDeck.allShoes());
        accessories.addAll(ItemDeck.allShields());
        accessories.addAll(ItemDeck.allHeadGear());
        accessories.addAll(ItemDeck.allJewelry());
        accessories.addAll(ItemDeck.allGloves());
        accessories.addAll(ItemDeck.allOtherOffhandItems());

        accessories.sort(Comparator.comparing(Item::getName));

        writer.newLine();
        writer.write("<font size=1>\n");
        writer.newLine();
        writer.write("| Image | Item | Cost | Wt | Prevalence | Notes |\n");
        writer.write("|-------|------|------|----|------------|-------|\n");
        for (Accessory c : accessories) {
            double weight = c.getWeight() / 1000.0;
            String extra = c.getShoppingDetails();
            if (extra.startsWith(",")) {
                extra = extra.replaceFirst(",", "");
            }
            writer.write("| " + imgTag(c) + " | " + c.getName() + " | " +
                    c.getCost() + " | " + weight + " | " +  MyStrings.capitalize(c.getPrevalence().name()) + " | " +
                    extra + " |\n");
        }

        writer.write("\n</font>\n");
        writer.newLine();
    }

    private void makePotionsSubChapter(BufferedWriter writer) throws IOException {
        writer.write("## Potions and Beverages\n");
        writer.newLine();
        List<Item> potions = new ArrayList<>(ItemDeck.allPotions());
        potions.addAll(makeHigherTierPotions());
        potions.sort(Comparator.comparing(Item::getName));

        writer.newLine();
        writer.write("<font size=1>\n");
        writer.newLine();
        writer.write("| Image | Potion | Cost | Wt | Prevalence | Notes |\n");
        writer.write("|-------|--------|------|----|------------|-------|\n");
        for (Item c : potions) {
            double weight = c.getWeight() / 1000.0;
            String extra = c.getShoppingDetails();
            if (extra.startsWith(",")) {
                extra = extra.replaceFirst(",", "");
            }
            writer.write("| " + imgTag(c) + " | " + c.getName() + " | " +
                    c.getCost() + " | " + weight + " | " +  MyStrings.capitalize(c.getPrevalence().name()) + " | " +
                    extra + " |\n");
        }

        writer.write("\n</font>\n");
        writer.newLine();
    }

    private List<Item> makeHigherTierPotions() {
        List<Item> result = new ArrayList<>();
        List<Potion> higherTiers = MyLists.transform(MyLists.filter(ItemDeck.allPotions(), Item::supportsHigherTier),
                it -> (Potion)it);
        for (Potion p : higherTiers) {
            for (int tier = 1; tier <= Item.MAX_TIER; ++tier) {
                result.add(p.makeHigherTierCopy(tier));
            }
        }
        return result;
    }

    private void makeOtherGoodsAndServicesSubChapter(BufferedWriter writer) throws IOException {
        writer.write("## Other Goods and Services\n");
        writer.newLine();
        writer.write("| Image | Goods or Service | Cost | Weight | Notes |\n");
        writer.write("|-------|------------------|------|--------|-------|\n");
        writer.write("| " + imgTag(new GoldDummyItem(1)) + " | Gold | 1 | 0 | Currency |\n");
        writer.write("| " + imgTag(new ObolsDummyItem(1)) + " | Obols | 10 for 1 | 0 | Sub-currency |\n");
        writer.write("| " + imgTag(new FoodDummyItem(1)) + " | Rations          | 5 for 1 | 0.5 | |\n");
        writer.write("| " + imgTag(new IngredientsDummyItem(1)) + " | Ingredients | 5 each | 0.1 | Used for Alchemy |\n");
        writer.write("| " + imgTag(new MaterialsDummyItem(1)) + " | Materials | 5 each | 1 | Used for Crafting |\n");
        writer.write("| " + imgTag(new Lockpick()) + " | Lockpicks | 3 each | 1 |  |\n");
        writer.write("| " + imgTag(new TentUpgradeItem()) + " | 4-Person Tent | 50 | 5 |  |\n");
        writer.write("| " + imgTag(new TentUpgradeItem()) + " | 5-Person Tent | 75 | 6 |  |\n");
        writer.write("| " + imgTag(new LargeTentUpgradeItem()) + " | 6-Person Tent | 85 | 7 |  |\n");
        Item book = new ElfOriginBook();
        writer.write("| " + imgTag(book) + " | Book | Varies | 0.1 |  |\n");
        for (Item it : ItemDeck.allTreasures()) {
            writer.write("| " + imgTag(it) + " | " + it.getName() + " | " + it.getCost() + " | " + (it.getCost() / 1000.0) + " | " + it.getShoppingDetails() + " |\n");
        }
        for (Horse h : makeHorses()) {
            Item it = new HorseStartingItem(h);
            writer.write("| " + imgTag(it) + " | " + h.getName() + " | " + h.getCost() + " | -50 | " + it.getShoppingDetails().replaceFirst(", ", "") + " |\n");
        }

        writer.write("|   | Rope (50m) | 5 | 5 | |\n");
        writer.write("|   | Cooking Gear | 10 | 2 | |\n");
        writer.write("| | Meal at Tavern | 1 | | |\n");
        writer.write("| | Double or Single Room at Tavern | 1 | | Sleeping in a real bead restores 1 SP. |\n");
        writer.write("| | Boat Trip | 1-3 per person | | |");
        writer.write("| | Carriage Trip | 0.5-2 Per person | | |\n");
        writer.write("| | Charter Boat | 25+ | | |\n");
        writer.write("| | Charter Carriage | 15+ | | |\n");
        writer.write("| | Alley Dice (Gambling) | 1 Obol | | |\n");
        writer.write("| | Low Stakes Card Game | 10 Obols | | |\n");
        writer.write("| | High Stakes Card Game | 35 Obols | | |\n");
        // TODO: Rations, Obols, Tents, Cooking Gear, Rope, Lock Picks, Materials, Ingredients
        // TODO: Food & Lodging at inns.
        // TODO: Travel fares. Charting boats/carriages
        writer.newLine();
    }

    private void makeImages() {
        List<Item> items = new ArrayList<>(ItemDeck.allItems());
        items.addAll(makeHigherTierPotions());
        items.add(new FoodDummyItem(1));
        items.add(new IngredientsDummyItem(1));
        items.add(new Lockpick());
        items.add(new MaterialsDummyItem(1));
        items.add(new ObolsDummyItem(1));
        items.add(new GoldDummyItem(1));
        items.add(new TentUpgradeItem());
        items.add(new LargeTentUpgradeItem());
        for (Horse h : makeHorses()) {
            items.add(new HorseStartingItem(h));
        }
        items.add(new ElfOriginBook());
        items.addAll(ItemDeck.allTreasures());

        for (Item it : items) {
            try {
                ImageScreenHandler screenHandler = new ImageScreenHandler();
                it.drawYourself(screenHandler, 0, 0);
                BufferedImage img = screenHandler.getImage(32, 32);
                File outFile = new File(PATH_BASE + "/images/" + getFileName(it));
                if (!outFile.exists()) {
                    outFile.createNewFile();
                }
                ImageIO.write(img, "png", outFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private List<Horse> makeHorses() {
        List<Horse> horses = MyLists.uniqueify(HorseHandler.getAllHorses(), Horse::getName);
        horses.add(new OldMaidHorse());
        horses.add(new OldPony());
        return horses;
    }

    private String getFileName(Item it) {
        return "item_" + it.getName().toLowerCase().replaceAll(" ", "_") + ".png";
    }

    private String imgTag(Item c) {
        return "<img src=\"../images/" + getFileName(c) + "\">";
    }
}
