package model.items;

import model.items.accessories.*;
import model.items.books.*;
import model.items.clothing.*;
import model.items.designs.CraftingDesign;
import model.items.imbuements.*;
import model.items.potions.*;
import model.items.special.MagicBroom;
import model.items.spells.*;
import model.items.weapons.SkullWand;
import model.items.weapons.*;
import util.MyRandom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ItemDeck extends ArrayList<Item> {

    private static final int[] standardTiersPerLevel =
            //        0  1  2  3  4  5  6  7  8  9  10  11  12  13  14  15
            new int[]{0, 0, 0, 0, 0, 1, 1, 1, 1, 2,  2,  2,  3,  3,  3,  3};
    private static final double[] higherTierChancePerLevel =
            //             0     1     2     3     4     5     6     7
            new double[]{0.10, 0.15, 0.25, 0.35, 0.45, 0.05, 0.15, 0.25,
            //             8     9    10    11    12    13    14     15
                         0.35, 0.05, 0.15, 0.25, 0.05, 0.15, 0.25, 0.35};

    // two higher levels percentages:
    //             0     1      2       3      4       5       6     7
//                0.01, 0.025, 0.0625, 0.125, 0.2025, 0.0025, 0.15, 0.25,
    //             8     9    10    11    12    13    14     15
  //             0.35, 0.05, 0.15, 0.25, 0.05, 0.15, 0.25, 0.35};


    private static final int MAX_HIGHER_ITEM_TIERS = 4;
    private int standardTier = standardTiersPerLevel[1];
    private double higherTierChance = higherTierChancePerLevel[1];

    public ItemDeck() {
        this.addAll(allItems());
        Collections.shuffle(this);
    }

    public double getStandardHigherTierChance() {
        return higherTierChance;
    }

    public List<Item> draw(List<? extends Item> source, int count, Prevalence prevalence, double higherTierChance) {
        List<Item> drawn = new ArrayList<>();
        for (int i = count; i > 0; --i) {
            int tierOffset = randomizeTier(higherTierChance);
            Item it;
            do {
                it = MyRandom.sample(source);
            } while (!prevalenceOk(it.getPrevalence(), prevalence));

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

    private static boolean prevalenceOk(Prevalence itemPrevalence, Prevalence requestedPrevalence) {
        if (requestedPrevalence == Prevalence.unspecified) {
            return itemPrevalence != Prevalence.unique && itemPrevalence != Prevalence.veryRare;
        }
        return itemPrevalence == requestedPrevalence;
    }

    private int randomizeTier(double higherTierChance) {
        if (MyRandom.nextDouble() < (1.0 - higherTierChance)) {
            return standardTier;
        }
        return Math.min(1 + randomizeTier(0.05), MAX_HIGHER_ITEM_TIERS);
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

    public Item getRandomHeadgear() { return MyRandom.sample(allHeadGear()); }

    public Item getRandomShield() { return MyRandom.sample(allShields()); }

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
                new CraftingDesign(new GrandStaff()),
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
                new StrengthPotion(), new DexterityPotion(), new WitsPotion(), new CharismaPotion(),
                new BeerPotion(), new WinePotion(), new InvisibilityPotion(), new CommonPoison(), new LethalPoison());
    }

    public static List<PotionRecipe> allPotionRecipes() {
        return List.of(new PotionRecipe(new HealthPotion()), new PotionRecipe(new StaminaPotion()),
                new PotionRecipe(new RejuvenationPotion()), new PotionRecipe(new AntiParalysisPotion()),
                new PotionRecipe(new AntidotePotion()), new PotionRecipe(new UnstablePotion()),
                new PotionRecipe(new SleepingPotion()), new PotionRecipe(new StrengthPotion()),
                new PotionRecipe(new DexterityPotion()), new PotionRecipe(new WitsPotion()),
                new PotionRecipe(new CharismaPotion()), new PotionRecipe(new RevivingElixir()),
                new PotionRecipe(new CommonPoison()));
    }

    public static List<HeadGearItem> allHeadGear() {
        return List.of(new LeatherCap(), new JestersHat(), new PaintedMask(), new Crown(), new BoneMask(),
                new WolfHead(), new Circlet(), new Tiara(), new Diadem(), new SkullCap(), new Helm(),
                new FullHelm(), new GreatHelm(), new PirateCaptainsHat());
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
                new HolyChalice(),
                new Spyglass());
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
                new EmeraldRing(), new OrnateRing(), new GoldRing(), new MasterRing(),
                new ApprenticeRing(), new GreedyRing(), new LuckyTalisman(),
                new Pentagram(), new PlateNecklace(), new AnkhPendant(), new ShinyAmulet(),
                new SilverPendant());
    }

    public static List<Clothing> allApparel() {
        return List.of(new StuddedTunic(), new StuddedJerkin(), new OutlawArmor(), new LeatherArmor(),
                new RingMail(), new ChainMail(), new PlateMailArmor(), new DragonArmor(), new ScaleArmor(), new BreastPlate(),
                new FullPlateArmor(), new WarmCape(), new FancyJerkin(), new PilgrimsCloak(),
                new MagesRobes(), new ShamansRobes(), new CultistsRobes(), new WarlocksRobes(),
                new MesmersRobes(), new LeatherTunic(), new QuiltedArmor(), new FurArmor(), new Brigandine(),
                new TemplarArmor(), new GlassArmor(), new PirateVest(), new TradersDoublet(),
                new FormalUniform());
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
                new OpeningSpell(),
                new DimensionDoorSpell(),
                // WHITE
                new TurnUndeadSpell(),
                new ShiningAegisSpell(),
                new WardSpell(),
                new HealingWordSpell(),
                new SouthernCrossSpell(),
                new AuraOfAllureSpell(),
                new BlessSpell(),
                new ResurrectSpell(),
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
                new VomitSpell(),
                // BLACK
                new GazeOfDeathSpell(),
                new BlackPactSpell(),
                new RaiseBoneWalkerSpell(),
                new WeakenSpell(),
                new DarkShroudSpell(),
                new PoisonGasSpell(),
                new DrainLifeSpell(),
                new BoneArmorSpell(),
                new QuickeningSpell(),
                new EnthrallSpell(),
                // COLORLESS
                new DispelSpell(),
                new TransmuteSpell(),
                new CombineSpell(),
                new DragonTamingSpell(),
                new ChannelingSpell(),
                new ImbueWeaponSpell(),
                new TelekinesisSpell(),
                new SummonShipSpell(),
                new ScrollMakingSpell(),
                new MassSpell()
                );
    }

    public static List<Scroll> allScrolls() {
        List<Scroll> result = new ArrayList<>();
        for (Spell sp : allSpells()) {
            if (sp.canExistsAsScroll()) {
                result.add(new Scroll(sp));
            }
        }
        return result;
    }


    public static Collection<BookItem> allBooks() {
        return List.of(new HowToTameYourDragonBook(),
                new QuadMachineryBook(),
                new TreasureHuntersHandbook(),
                new PoetryBookOne(),
                new SeaTravelBook(),
                new ElfOriginBook(),
                new GelatinousBlobBook());
        // FEATURE: Travel Guide to the Four Kingdoms (info about ingredient/material prevalences, and more)
        // FEATURE: Advanced Archery
        // FEATURE: The Finer Points of Alchemy (useless)
        // FEATURE: Grave Robber's Handbook (useful tips for dungeons)
        // FEATURE: On the Tenacity of Dwarves (useless)
        // FEATURE: On the Peculiarities of Half-Orcs (useless)
        // FEATURE: On the Ingenuity of Halflings (useless)
        // FEATURE: Horse Racing 101
        // FEATURE: Runny Card Game for Dummies
        // FEATURE: Battle Tactics Basics
        // FEATURE: Magical Dueling Dos and Don'ts
    }

    public static List<Weapon> allWeapons() {
        List<Weapon> allWeapons = new ArrayList<>();
        allWeapons.addAll(allBlades());
        allWeapons.addAll(allBluntWeapons());
        allWeapons.addAll(allAxes());
        allWeapons.addAll(allWands());
        allWeapons.addAll(allBows());
        allWeapons.addAll(allSpears());
        allWeapons.addAll(allBrawlingWeapons());
        allWeapons.addAll(allSpecialWeapons());
        return allWeapons;
    }

    private static List<? extends Weapon> allBrawlingWeapons() {
        return List.of(new BrassKnuckles(), new SpikedKnuckles(), new Claws(), new Tonfa());
    }

    public static List<? extends Weapon> allSpears() {
        return List.of(new WoodenSpear(), new Spear(), new Javelins(), new Trident(),
                new Glaive(), new Halberd(), new Pike(), new ShortSpear(), new BecDeCorbin(), new Naginata(),
                new BoatHook(), new Harpoons());
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
                new GrandMaul(), new TripleFlail(), new IronStaff(), new GrandStaff(), new BoStaff(),
                new Nunchuck(), new TwinNunchucks());
    }

    public static List<? extends Weapon> allBlades() {
        return List.of(new Dirk(), new Dagger(), new ThrowingKnives(), new Kukri(), new ShortSword(),
                new OrcishKnife(), new RitualDagger(), new Scimitar(), new Falchion(), new Wakizashi(),
                new Broadsword(), new Longsword(), new Estoc(), new Rapier(), new Katana(), new TwoHandedSword(),
                new Claymore(), new BastardSword(), new Zweihander(), new DaiKatana(), new TwinDaggers(),
                new TwinKukris(), new Sai(), new TwinSais(), new Cutlass());
    }


    public static List<? extends WeaponImbuement> allImbuements() {
        return List.of(new AbsorptionImbuement(), new BurningImbuement(), new ChainLightningImbuement(),
                new DismembermentImbuement(), new ExtraDamageImbuement(), new FreezeImbuement(),
                new PoisonImbuement(), new StunImbuement(), new VigorImbuement(),
                new FinesseImbuement());
    }

    public static List<? extends Weapon> allSpecialWeapons() {
        return List.of(new Lute(), new FishingPole(), new ThrowingStars(), new Pistol(), new Musket(), new MagicBroom());
    }

    public void setStandardItemTier(int averageLevel) {
        if (averageLevel < standardTiersPerLevel.length) {
            int newStandard = standardTiersPerLevel[averageLevel];
            if (standardTier != newStandard) {
                System.out.println("New standard item tier! Tier " + newStandard);
                standardTier = newStandard;
            }
        }

        if (averageLevel < higherTierChancePerLevel.length) {
            double newChance = higherTierChancePerLevel[averageLevel];
            if (higherTierChance != higherTierChancePerLevel[averageLevel]) {
                System.out.println("New higher item tier chance! Chance " + newChance);
                higherTierChance = newChance;
            }
        }
    }


    public static List<? extends Weapon> allUniqueWeapons() {
        return List.of(new CalixaberSword(), new SwordOfVigor(), new StaffOfDeimosItem(),
                new LightningJavelins(), new AxeOfDismemberment());
    }

    public int getStandardItemTier() {
        return standardTier;
    }
}
