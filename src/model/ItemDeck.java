package model;

import model.items.*;
import model.items.accessories.*;
import model.items.clothing.*;
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

    public ItemDeck() {
        this.addAll(allItems());
        Collections.shuffle(this);
    }

    public List<Item> draw(List<? extends Item> source, int count, Prevalence prevalence) {
        List<Item> drawn = new ArrayList<>();
        for (int i = count; i > 0; --i) {
            Item it;
            do {
                it = MyRandom.sample(source);
            } while (it.getPrevalence() != prevalence && prevalence != Prevalence.unspecified);
            drawn.add(it.copy());
        }
        return drawn;
    }

    public List<Item> draw(int count, Prevalence prevalence) {
        return draw(this, count, prevalence);
    }

    public List<Item> draw(int count) {
        return draw(count, Prevalence.unspecified);
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

    public Item getRandomItem() { return draw(1).get(0); }

    private static Collection<? extends Item> allItems() {
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
        return allItems;
    }

    public static List<Potion> allPotions() {
        return List.of(new HealthPotion(), new StaminaPotion(), new RejuvenationPotion(), new AntiParalysisPotion(),
                new AntidotePotion(), new UnstablePotion(), new RevivingElixir(), new SleepingPotion());
        // TODO: Add sleeping potion?
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
                new TowerShield());
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
                new RingMail(), new ChainMail(), new DragonArmor(), new ScaleArmor(), new BreastPlate(),
                new FullPlateArmor(), new WarmCape(), new FancyJerkin(), new PilgrimsCloak(),
                new MagesRobes(), new ShamansRobes(), new CultistsRobes(), new WarlocksRobes(),
                new MesmersRobes(), new LeatherTunic(), new QuiltedArmor());
    }

    public static List<Spell> allSpells() {
        return List.of(
                new DispellSpell(),
                new LevitateSpell(),
                new MindControlSpell(),
                new ConjurePhantasmSpell(),
                // new CreatureComfortsSpell(),
                new TurnUndeadSpell(),
                // new ShiningOrbSpell(),
                new HealingWordSpell(),
                // new SouthernCrossSpell(),
                new AuraOfAllureSpell(),
                new FireworksSpell(),
                new MagmaBlastSpell(),
                new EntropicBoltSpell(),
                // new BindDaemonSpell(),
                // new BurningWeaponSpell(),
                // new TransfigurationSpell(),
                new AlchemySpell(),
                new HarmonizeSpell(),
                new CallOfTheWildSpell(),
                // new GiantGrowthSpell(),
                new GazeOfDeathSpell(),
                // new BlackPactSpell(),
                // new RaiseBoneWalkerSpell(),
                new WeakenSpell(),
                new DarkShroudSpell()
                );
    }

    public static List<Weapon> allWeapons() {
        List<Weapon> allWeapons = new ArrayList<>();
        allWeapons.addAll(allBlades());
        allWeapons.addAll(allBluntWeapons());
        allWeapons.addAll(allAxes());
        allWeapons.addAll(allWands());
        allWeapons.addAll(allBows());
        allWeapons.addAll(allSpears());
        return allWeapons;
    }

    public static List<? extends Weapon> allSpears() {
        return List.of(new WoodenSpear(), new Spear(), new Javelins(), new Trident(),
                new Glaive(), new Halberd(), new Pike());
    }

    public static List<? extends Weapon> allBows() {
        return List.of(new TrainingBow(), new ShortBow(), new CompositeBow(), new RepeatingCrossbow(),
                new HuntersBow(), new YewBow(), new BoneBow(), new Crossbow(), new LongBow());
    }

    public static List<? extends Weapon> allWands() {
        return List.of(new OldWand(), new PineWand(), new ClaspedOrb(), new YewWand(), new OrbWand(),
                new BoneWand(), new SkullWand());
    }

    public static List<? extends Weapon> allAxes() {
        return List.of(new Hatchet(), new ThrowingAxes(), new Pickaxe(), new BattleAxe(), new DoubleAxe(),
                new GreatAxe());
    }

    public static List<? extends Weapon> allBluntWeapons() {
        return List.of(new Club(), new LongStaff(), new OldStaff(), new MagesStaff(),
                new Scepter(), new MorningStar(), new Flail(), new Warhammer(), new Mace(),
                new GrandMaul());
    }

    public static List<? extends Weapon> allBlades() {
        return List.of(new Dirk(), new Dagger(), new ThrowingKnives(), new ShortSword(),
                new OrcishKnife(), new RitualDagger(), new Scimitar(), new Falchion(),
                new Broadsword(), new Longsword(), new Katana(), new TwoHandedSword(),
                new Claymore(), new BastardSword());
    }
}
