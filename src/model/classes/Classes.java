package model.classes;

import model.classes.normal.*;
import model.classes.npcs.*;
import model.classes.prestige.NinjaClass;
import model.classes.prestige.SamuraiClass;
import model.classes.special.*;

public interface Classes {
    CharacterClass AMZ = new AmazonClass();
    CharacterClass ART = new ArtisanClass();
    CharacterClass ASN = new AssassinClass();
    CharacterClass BBN = new BarbarianClass();
    CharacterClass BRD = new BardClass();
    CharacterClass BKN = new BlackKnightClass();
    CharacterClass CAP = new CaptainClass();
    CharacterClass FOR = new ForesterClass();
    CharacterClass DRU = new DruidClass();
    CharacterClass MAG = new MagicianClass();
    CharacterClass MAR = new MarksmanClass();
    CharacterClass MIN = new MinerClass();
    CharacterClass NOB = new NobleClass();
    CharacterClass PAL = new PaladinClass();
    CharacterClass PRI = new PriestClass();
    CharacterClass SOR = new SorcererClass();
    CharacterClass SPY = new SpyClass();
    CharacterClass THF = new ThiefClass();
    CharacterClass WIZ = new WizardClass();
    CharacterClass WIT = new WitchClass();
    CharacterClass None = new NoClass();

    // Prestige Classes:
    CharacterClass NINJA = new NinjaClass(); // TODO: Develop this and use these ?
    CharacterClass SAMURAI = new SamuraiClass();

    CharacterClass[] allClasses = new CharacterClass[]{None,
            AMZ, ART, ASN, BBN, BRD, BKN, CAP, FOR, DRU, MAG,
            MAR, MIN, NOB, PAL, PRI, SOR, SPY, THF, WIZ, WIT};

    // NPC Classes:
    CharacterClass MAGE = new MageNPCClass();
    CharacterClass FARMER = new FarmerNPCClass();
    CharacterClass MERCHANT = new MerchantNPCClass();
    CharacterClass BANDIT = new BanditNPCClass();
    CharacterClass ALCHEMIST = new AlchemistNPCClass();
    CharacterClass CHARLATAN = new CharlatanNPCClass();
    CharacterClass CONSTABLE = new ConstableNPCClass();
    CharacterClass TEMPLE_GUARD = new TempleGuardNPCClass();
    CharacterClass WARLOCK = new WarlockNPCClass();
    CharacterClass OFFICIAL = new OfficialNPCClass();
    CharacterClass BAKER = new BakerNPCClass();
    CharacterClass HERMIT = new HermitNPCClass();
    CharacterClass BARBER = new BarberNPCClass();
    CharacterClass PROFESSOR = new ProfessorNPCClass();
    CharacterClass TRAVELLER = new TravellerNPCClass();
    CharacterClass VAMPIRE = new VampireNPCClass();

    // Special Classes:
    CharacterClass BONE_WALKER = new BoneWalkerClass();
    CharacterClass GOBLIN = new GoblinClass();
    CharacterClass WITCH_KING = new WitchKingClass();
    CharacterClass RED_KNIGHT = new RedKnightClass();
    CharacterClass ENCHANTRESS = new EnchantressClass();
    CharacterClass FAMILIAR = new FamiliarClass();
    CharacterClass SWORD_MASTER = new SwordMasterClass();
    CharacterClass ARCANIST = new ArcanistClass();
    CharacterClass TREE_HERDER = new TreeHerderClass();

    CharacterClass[] NO_OTHER_CLASSES = Classes.NO_OTHER_CLASSES;
    CharacterClass BEAUTY = new BeautyNPCClass();
}
