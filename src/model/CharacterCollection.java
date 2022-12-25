package model;

import model.characters.*;
import model.classes.CharacterClass;

import java.util.ArrayList;

import static model.classes.Classes.*;
import static model.races.Race.*;

public class CharacterCollection extends ArrayList<GameCharacter> {
    public CharacterCollection() {
        add(new GameCharacter("Krusk", "Talandro", HALF_ORC, WIT,
                new KruskTalandro(), new CharacterClass[]{WIT, DRU, MAG, SOR}));
        add(new GameCharacter("Audrey", "Puddle", HALFLING, SPY,
                new AudreyPuddle(), new CharacterClass[]{BRD, SPY, AMZ, ART}));
        add(new GameCharacter("Paddy", "Willowbrush", HALFLING, MIN,
                new PaddyWillowbrush(), new CharacterClass[]{MIN, DRU, FOR, MAR}));
        add(new GameCharacter("Eldeth", "Markolak", DWARF, PRI,
                new EldethMarkolak(), new CharacterClass[]{PRI, BBN, FOR, SOR}));
        add(new GameCharacter("Dara", "Stormcloud", WOOD_ELF, ASN,
                new DaraStormcloud(), new CharacterClass[]{FOR, ASN, BRD, MAR}));
        add(new GameCharacter("Jordyn", "Strong", SOUTHERN_HUMAN, AMZ,
                new JordynStrong(), new CharacterClass[]{AMZ, BRD, FOR, PRI}));
        add(new GameCharacter("Puyet", "Grantham", HALF_ORC, ART,
                new PuyetGrantham(), new CharacterClass[]{ART, ASN, FOR, MIN}));
        add(new GameCharacter("Torhild", "Ambershard", DWARF, WIT,
                new TorhildAmbershard(), new CharacterClass[]{WIT, AMZ, MAR, SPY}));
        add(new GameCharacter("Bungo", "Darkwood", HALFLING, BBN,
                new BungoDarkwood(), new CharacterClass[]{BBN, CAP, NOB, PAL}));
        add(new GameCharacter("Arielle", "Stormchapel", NORTHERN_HUMAN, THF,
                new ArielleStormchapel(), new CharacterClass[]{THF, ASN, BBN, PAL}));
        add(new GameCharacter("Zhandra", "Merkatys", SOUTHERN_HUMAN, WIT,
                new ZhandraMerkatys(), new CharacterClass[]{WIT, BBN, MAR, MIN}));
        add(new GameCharacter("Lorfir", "Briarfell", WOOD_ELF, ART,
                new LorfirBriarfell(), new CharacterClass[]{ART, AMZ, DRU, THF}));
        add(new GameCharacter("Atalya", "Balefrost", HIGH_ELF, WIZ,
                new AtalyaBalefrost(), new CharacterClass[]{WIZ, BKN, DRU, SOR}));
        add(new GameCharacter("Denise", "Boyd", NORTHERN_HUMAN, ART,
                new DeniseBoyd(), new CharacterClass[]{ART, BKN, CAP, NOB}));
        add(new GameCharacter("Gorga", "Bonecrag", HALF_ORC, AMZ,
                new GorgaBonecrag(), new CharacterClass[]{AMZ, BKN, SPY, WIZ}));
        add(new GameCharacter("Leaodor", "Sunshadow", DARK_ELF, NOB,
                new LeodorSunshadow(), new CharacterClass[]{NOB, BKN, BRD, WIZ}));
        add(new GameCharacter("Mord", "Kroft", HALF_ORC, PAL,
                new MordKroft(), new CharacterClass[]{PAL, BBN, CAP, MAR}));
        add(new GameCharacter("Mialee", "Severin", DARK_ELF, WIT,
                new MialeeSeverin(), new CharacterClass[]{WIT, SOR, SPY, THF}));
        add(new GameCharacter("Vzani", "Angler", HALFLING, BRD,
                new VzaniAngler(), new CharacterClass[]{WIZ, MAG, PRI, THF}));
        add(new GameCharacter("Miklos", "Autumntoft", HIGH_ELF, MAR,
                new MiklosAutumntoft(), new CharacterClass[]{CAP, NOB, PAL, PRI}));
        add(new GameCharacter("Thorbalt", "Ramcrown", DWARF, CAP,
                new ThorbaltRamcrown(), new CharacterClass[]{ASN, CAP, MAG, BRD}));
        add(new GameCharacter("Rolf", "Fryt", NORTHERN_HUMAN, PRI,
                new RolfFryt(), new CharacterClass[]{PRI, MIN, SPY, WIT}));
        add(new GameCharacter("William", "Ydrenwald", NORTHERN_HUMAN, WIZ,
                new WilliamYdrenwald(), new CharacterClass[]{WIZ, DRU, MAG, SOR}));
        add(new GameCharacter("Hazel", "Van Devries", SOUTHERN_HUMAN, NOB,
                new HazelVanDevries(), new CharacterClass[]{NOB, ASN, MAG, THF}));
        add(new GameCharacter("Megar", "Evermead", DWARF, ART,
                new MegarEvermead(), new CharacterClass[]{ART, BKN, PAL, MIN}));

        add(new GameCharacter("Lonnie", "Liebgott", NORTHERN_HUMAN, MIN,
                new LonnieLiebgott(), new CharacterClass[]{NOB, MIN, MAG, THF}));

        System.out.println(size() + " characters loaded!");
    }
}
