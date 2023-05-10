package model.classes;

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
}
