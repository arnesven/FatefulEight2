package model;

import model.characters.*;
import model.characters.preset.*;
import model.classes.CharacterClass;

import java.util.ArrayList;

import static model.classes.Classes.*;
import static model.races.Race.*;

public class CharacterCollection extends ArrayList<GameCharacter> {
    public CharacterCollection() {
        add(new KruskTalandroCharacter());
        add(new AudreyPuddleCharacter());
        add(new GameCharacter("Paddy", "Willowbrush", HALFLING, MIN,
                new PaddyWillowbrush(), new CharacterClass[]{MIN, DRU, FOR, MAR}));
        add(new EldethMarkolakCharacter());
        add(new DaraStormcloudCharacter());
        add(new JordynStrongCharacter());
        add(new GameCharacter("Puyet", "Grantham", HALF_ORC, ART,
                new PuyetGrantham(), new CharacterClass[]{ART, ASN, FOR, MIN}));
        add(new TorhildAmbershardCharacter());
        add(new BungoDarkwoodCharacter());
        add(new ArielleStormchapelCharacter());
        add(new ZhandraMerkatysCharacter());
        add(new LorfirBriarfellCharacter());
        add(new AtalyaBalefrostCharacter());
        add(new DeniseBoydCharacter());
        add(new GorgaBonecragCharacter());
        add(new GameCharacter("Leodor", "Sunshadow", DARK_ELF, NOB,
                new LeodorSunshadow(), new CharacterClass[]{NOB, BKN, BRD, WIZ}));
        add(new GameCharacter("Mord", "Kroft", HALF_ORC, PAL,
                new MordKroft(), new CharacterClass[]{PAL, BBN, CAP, MAR}));
        add(new MialeeSeverinCharacter());
        add(new GameCharacter("Vzani", "Angler", HALFLING, BRD,
                new VzaniAngler(), new CharacterClass[]{WIZ, MAG, PRI, THF}));
        add(new GameCharacter("Miklos", "Autumntoft", HIGH_ELF, MAR,
                new MiklosAutumntoft(), new CharacterClass[]{CAP, NOB, PAL, PRI}));
        add(new GameCharacter("Thorbalt", "Ramcrown", DWARF, CAP,
                new ThorbaltRamcrown(), new CharacterClass[]{ASN, CAP, MAG, BRD}));
        add(new GameCharacter("Rolf", "Fryt", NORTHERN_HUMAN, PRI,
                new RolfFryt(), new CharacterClass[]{PRI, MIN, SPY, WIT}));
        add(new WilliamYdrenwaldCharacter());
        add(new HazelVanDevriesCharacter());
        add(new MegarEvermeadCharacter());
        add(new LonnieLiebgottCharacter());
        add(new RiboxAnariCharacter());
        add(new LianaClearwaterCharacter());
        add(new ZephyreFirefistCharacter());
        add(new GameCharacter("Muldan", "Ebonclaw", DARK_ELF, PRI,
                new MuldanEbonclaw(), new CharacterClass[]{PRI, NOB, BBN, SOR}));
        add(new GameCharacter("Fatty", "Goldenrod", HALFLING, MIN,
                new FattyGoldenrod(), new CharacterClass[]{MIN, BBN, WIT, SOR}));
        add(new StellaComptonCharacter());
        add(new EthelthaneVeldtCharacter());
        add(new HurinHammerfallCharacter());
        add(new GameCharacter("Baz", "Ur-Ghan", HALF_ORC, AMZ,
                new BazUrGhan(), new CharacterClass[]{AMZ, BRD, FOR, MAR}));
        add(new VendelaGawainsCharacter());
        add(new EmilyFourhornCharacter());
        add(new IvanMcIntoshCharacter());
        add(new SebastianSmithCharacter());
        add(new JennaWildflowerCharacter());
        add(new GameCharacter("Alewyn", "Solethal", HIGH_ELF, ART,
                new AlewynSolethal(), new CharacterClass[]{ART, WIT, AMZ, MAG}));
        add(new MelethainGauthCharacter());
        System.out.println(size() + " characters loaded!");
    }
}
