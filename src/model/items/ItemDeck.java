package model.items;

import model.items.accessories.*;
import model.items.books.HowToTameYourDragonBook;
import model.items.books.QuadMachineryBook;
import model.items.clothing.*;
import model.items.designs.CraftingDesign;
import model.items.potions.*;
import model.items.spells.*;
import model.items.weapons.SkullWand;
import model.items.weapons.*;
import util.MyRandom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ItemDeck extends ArrayList<Item> {

    private static final int LEVELS_PER_TIER = 5;
    private static final double HIGHER_TIER_CHANCE_FACTOR = 1.0 / (4.0*LEVELS_PER_TIER);
    private static final int MAX_HIGHER_ITEM_TIERS = 4;
    private int standardTier = 0;
    private double higherTierChance = HIGHER_TIER_CHANCE_FACTOR;

    public ItemDeck() {
        this.addAll(allItems());
        Collections.shuffle(this);
    }

    public List<Item> draw(List<? extends Item> source, int count, Prevalence prevalence, double higherTierChance) {
        List<Item> drawn = new ArrayList<>();
        for (int i = count; i > 0; --i) {
            int tierOffset = randomizeTier(higherTierChance);
            Item it;
            do {
                it = MyRandom.sample(source);
            } while (it.getPrevalence() != prevalence && prevalence != Prevalence.unspecified);

            if (tierOffset > 0 && it.supportsHigherTier()) {
                drawn.add(it.makeHigherTierCopy(tierOffset));
            } else if (standardTier > 0 && it.supportsHigherTier()) {
                drawn.add(it.makeHigherTierCopy(standardTier));
            } else {
                drawn.add(it.copy());
            }
        }
        return drawn;
    }

    private int randomizeTier(double higherTierChance) {
        if (MyRandom.nextDouble() < (1.0 - higherTierChance)) {
            return standardTier;
        }
        return Math.min(standardTier+1, MAX_HIGHER_ITEM_TIERS);
    }

    public List<Item> draw(int count, Prevalence prevalence, double higherTierChance) {
        return draw(this, count, prevalence, higherTierChance);
    }

    public List<Item> draw(int count, double higherTierChance) {
        return draw(count, Prevalence.unspecified, higherTierChance);
    }

    public List<Item> draw(int count, Prevalence prevalence) {
        return draw(this, count, prevalence, higherTierChance);
    }

    public List<Item> draw(int count) {
        return draw(count, Prevalence.unspecified, higherTierChance);
    }

    public Spell getRandomSpell() {
        return MyRandom.sample(allSpells());
    }

    public Clothing getRandomApparel() { return (Clothing) MyRandom.sample(allApparel()).copy(); }

    public Weapon getRandomWeapon() { return (Weapon) MyRandom.sample(allWeapons()).copy(); }

    public Accessory getRandomJewelry() { return (Accessory) MyRandom.sample(allJewelry()).copy(); }

    public ShoesItem getRandomShoes() { return (ShoesItem) MyRandom.sample(allShoes()).copy(); }

    public Weapon getRandomWand() { return (Weapon) MyRandom.sample(allWands()).copy(); }

    public Potion getRandomPotion() {
        return (Potion)MyRandom.sample(allPotions()).copy();
    }

    public CraftingDesign getRandomDesign() {return (CraftingDesign) MyRandom.sample(allCraftingDesigns()).copy(); }


    public Item getRandomItem(double higherTierChance) { return draw(1, higherTierChance).get(0); }

    public Item getRandomItem() { return getRandomItem(higherTierChance); }

    public static Collection<? extends Item> allItems() {
        List<Item> allItems = new ArrayList<>();
        allItems.addAll(allWeapons());
        allItems.addAll(allApparel());
        allItems.addAll(allJewelry());
        allItems.addAll(allShoes());
        allItems.addAll(allGloves());
        allItems.addAll(allHeadGear());
        allItems.addAll(allShields());
        allItems.addAll(allSpells());
        allItems.addAll(allPotions());
        allItems.addAll(allCraftingDesigns());
        allItems.addAll(allPotionRecipes());
        allItems.addAll(allScrolls());
        allItems.add(new Lockpick());
        allItems.addAll(allBooks());
        return allItems;
    }

    public static List<CraftingDesign> allCraftingDesigns() {
        return List.of(
                new CraftingDesign(new GreatHelm()),
                new CraftingDesign(new SpikedShield()),
                new CraftingDesign(new DragonArmor()),
                new CraftingDesign(new FullPlateArmor()),
                new CraftingDesign(new BastardSword()),
                new CraftingDesign(new Claymore()),
                new CraftingDesign(new Flail()),
                new CraftingDesign(new GrandMaul()),
                new CraftingDesign(new GreatAxe()),
                new CraftingDesign(new Pike()),
                new CraftingDesign(new RepeatingCrossbow()),
                new CraftingDesign(new RitualDagger()),
                new CraftingDesign(new SkullWand()),
                new CraftingDesign(new Katana()),
                new CraftingDesign(new Crown()),
                new CraftingDesign(new Diadem()),
                new CraftingDesign(new LuckyTalisman()),
                new CraftingDesign(new GlassArmor()),
                new CraftingDesign(new TemplarArmor())
                );
    }

    public static List<Potion> allPotions() {
        return List.of(new HealthPotion(), new StaminaPotion(), new RejuvenationPotion(), new AntiParalysisPotion(),
                new AntidotePotion(), new UnstablePotion(), new RevivingElixir(), new SleepingPotion(),
                new StrengthPotion(), new DexterityPotion(), new WitsPotion(), new CharismaPotion());
    }

    public static List<PotionRecipe> allPotionRecipes() {
        return List.of(new PotionRecipe(new HealthPotion()), new PotionRecipe(new StaminaPotion()),
                new PotionRecipe(new RejuvenationPotion()), new PotionRecipe(new AntiParalysisPotion()),
                new PotionRecipe(new AntidotePotion()), new PotionRecipe(new UnstablePotion()),
                new PotionRecipe(new SleepingPotion()), new PotionRecipe(new StrengthPotion()),
                new PotionRecipe(new DexterityPotion()), new PotionRecipe(new WitsPotion()),
                new PotionRecipe(new CharismaPotion()), new PotionRecipe(new RevivingElixir()));
    }

    public static List<HeadGearItem> allHeadGear() {
        return List.of(new LeatherCap(), new JestersHat(), new PaintedMask(), new Crown(), new BoneMask(),
                new WolfHead(), new Circlet(), new Tiara(), new Diadem(), new SkullCap(), new Helm(),
                new FullHelm(), new GreatHelm());
    }

    public static List<GlovesItem> allGloves() {
        return List.of(new DeftGloves(), new LeatherGloves(), new FancyGloves(), new ChainGloves(),
                new SteelGauntlets(), new PlateGauntlets());
    }

    public static List<ShieldItem> allShields() {
        return List.of(
                new Buckler(),
                new LargeShield(),
                new KiteShield(),
                new HeraldicShield(),
                new SpikedShield(),
                new TowerShield(),
                new HolyChalice());
    }

    public static List<ShoesItem> allShoes() {
        return List.of(
                new GoodShoes(),
                new ComfyShoes(),
                new SuedeBoots(),
                new LeatherBoots(),
                new PlatedBoots());
    }

    public static List<JewelryItem> allJewelry() {
        return List.of(new GrayRing(), new SkullRing(), new TopazRing(),
                new RedRing(), new GreenRing(), new BlackRing(), new BlueRing(),
                new RubyRing(), new SapphireRing(), new HeavyRing(),
                new ExpensiveRing(), new SwiftRing(), new EyeRing(), new AmberRing(),
                new EmeraldRing(), new OrnateRing(), new GoldRing(), new LuckyTalisman(),
                new Pentagram(), new PlateNecklace(), new AnkhPendant(), new ShinyAmulet());
    }

    public static List<Clothing> allApparel() {
        return List.of(new StuddedTunic(), new StuddedJerkin(), new OutlawArmor(), new LeatherArmor(),
                new RingMail(), new ChainMail(), new PlateMailArmor(), new DragonArmor(), new ScaleArmor(), new BreastPlate(),
                new FullPlateArmor(), new WarmCape(), new FancyJerkin(), new PilgrimsCloak(),
                new MagesRobes(), new ShamansRobes(), new CultistsRobes(), new WarlocksRobes(),
                new MesmersRobes(), new LeatherTunic(), new QuiltedArmor(), new FurArmor(), new Brigandine(),
                new TemplarArmor(), new GlassArmor());
    }

    public static List<Spell> allSpells() {
        return List.of(
                // BLUE
                new LevitateSpell(),
                new MindControlSpell(),
                new ConjurePhantasmSpell(),
                new CreatureComfortsSpell(),
                new EscapeSpell(),
                new TeleportSpell(),
                new FaceDancingSpell(),
                // WHITE
                new TurnUndeadSpell(),
                new ShiningAegisSpell(),
                new WardSpell(),
                new HealingWordSpell(),
                new SouthernCrossSpell(),
                new AuraOfAllureSpell(),
                new BlessSpell(),
                // RED
                new FireworksSpell(),
                new MagmaBlastSpell(),
                new EntropicBoltSpell(),
                new BindDaemonSpell(),
                new BurningWeaponSpell(),
                new ErodeSpell(),
                new FireWallSpell(),
                new ChainLightningSpell(),
                // GREEN
                new TransfigurationSpell(),
                new AlchemySpell(),
                new HarmonizeSpell(),
                new CallOfTheWildSpell(),
                new GiantGrowthSpell(),
                new SummonFamiliarSpell(),
                new SummonTreeHerderSpell(),
                new WerewolfFormSpell(),
                // BLACK
                new GazeOfDeathSpell(),
                new BlackPactSpell(),
                new RaiseBoneWalkerSpell(),
                new WeakenSpell(),
                new DarkShroudSpell(),
                new PoisonGasSpell(),
                new VampirismSpell(),
                new BoneArmorSpell(),
                // COLORLESS
                new DispelSpell(),
                new TransmuteSpell(),
                new CombineSpell(),
                new QuickeningSpell(),
                new DragonTamingSpell(),
                new ChannelingSpell()
                );
    }

    public static List<Scroll> allScrolls() {
        List<Scroll> result = new ArrayList<>();
        for (Spell sp : allSpells()) {
            result.add(new Scroll(sp));
        }
        return result;
    }


    private static Collection<? extends Item> allBooks() {
        return List.of(new HowToTameYourDragonBook(),
                new QuadMachineryBook());
    }

    public static List<Weapon> allWeapons() {
        List<Weapon> allWeapons = new ArrayList<>();
        allWeapons.addAll(allBlades());
        allWeapons.addAll(allBluntWeapons());
        allWeapons.addAll(allAxes());
        allWeapons.addAll(allWands());
        allWeapons.addAll(allBows());
        allWeapons.addAll(allSpears());
        allWeapons.add(new Lute());
        allWeapons.add(new FishingPole());
        allWeapons.add(new ThrowingStars());
        return allWeapons;
    }

    public static List<? extends Weapon> allSpears() {
        return List.of(new WoodenSpear(), new Spear(), new Javelins(), new Trident(),
                new Glaive(), new Halberd(), new Pike(), new ShortSpear(), new BecDeCorbin(), new Naginata());
    }

    public static List<? extends Weapon> allBows() {
        return List.of(new TrainingBow(), new ShortBow(), new CompositeBow(), new RepeatingCrossbow(),
                new HuntersBow(), new YewBow(), new BoneBow(), new CommonCrossbow(), new LongBow(), new HeavyCrossbow());
    }

    public static List<? extends Weapon> allWands() {
        return List.of(new OldWand(), new PineWand(), new ClaspedOrb(), new YewWand(), new OrbWand(),
                new BoneWand(), new SkullWand(), new IceRod(), new FireRod(), new ChargedRod(), new IvyRod());
    }

    public static List<? extends Weapon> allAxes() {
        return List.of(new Hatchet(), new ThrowingAxes(), new Pickaxe(), new RaidersAxe(), new BattleAxe(),
                new Choppa(), new TwinHatchets(), new DoubleAxe(), new GreatAxe(), new Sicle());
    }

    public static List<? extends Weapon> allBluntWeapons() {
        return List.of(new Club(), new LongStaff(), new OldStaff(), new MagesStaff(),
                new Scepter(), new MorningStar(), new Flail(), new Warhammer(), new Mace(),
                new GrandMaul(), new TripleFlail(), new IronStaff(), new GrandStaff(), new BoStaff());
    }

    public static List<? extends Weapon> allBlades() {
        return List.of(new Dirk(), new Dagger(), new ThrowingKnives(), new Kukri(), new ShortSword(),
                new OrcishKnife(), new RitualDagger(), new Scimitar(), new Falchion(), new Wakizashi(),
                new Broadsword(), new Longsword(), new Estoc(), new Rapier(), new Katana(), new TwoHandedSword(),
                new Claymore(), new BastardSword(), new Zweihander(), new DaiKatana(), new TwinDaggers(), new TwinKukris());
    }

    public void setStandardItemTier(int averageLevel) {
        int newStandard = averageLevel / LEVELS_PER_TIER;
        double newChance = ((averageLevel % LEVELS_PER_TIER)*2 + 1) * HIGHER_TIER_CHANCE_FACTOR;
        if (newStandard != standardTier) {
            System.out.println("New standard item tier! Tier " + newStandard);
            standardTier = newStandard;
        }
        if (newChance != higherTierChance) {
            System.out.println("New higher item tier chance! Chance " + newChance);
            higherTierChance = newChance;
        }
    }
}
