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
        add(new PaddyWillowbrushCharacter());
        add(new EldethMarkolakCharacter());
        add(new DaraStormcloudCharacter());
        add(new JordynStrongCharacter());
        add(new PuyetGranthamCharacter());
        add(new TorhildAmbershardCharacter());
        add(new BungoDarkwoodCharacter());
        add(new ArielleStormchapelCharacter());
        add(new ZhandraMerkatysCharacter());
        add(new LorfirBriarfellCharacter());
        add(new AtalyaBalefrostCharacter());
        add(new DeniseBoydCharacter());
        add(new GorgaBonecragCharacter());
        add(new LeodorSunshadowCharacter());
        add(new MordKroftCharacter());
        add(new MialeeSeverinCharacter());
        add(new VzaniAnglerCharacter());
        add(new MiklosAutumntoftCharacter());
        add(new ThorbaltRamcrownCharacter());
        add(new RolfFrytCharacter());
        add(new WilliamYdrenwaldCharacter());
        add(new HazelVanDevriesCharacter());
        add(new MegarEvermeadCharacter());
        add(new LonnieLiebgottCharacter());
        add(new RiboxAnariCharacter());
        add(new LianaClearwaterCharacter());
        add(new ZephyreFirefistCharacter());
        add(new MuldanEbonclawCharacter());
        add(new FattyGoldenrodCharacter());
        add(new StellaComptonCharacter());
        add(new EthelthaneVeldtCharacter());
        add(new HurinHammerfallCharacter());
        add(new BazUrGhanCharacter());
        add(new VendelaGawainsCharacter());
        add(new EmilyFourhornCharacter());
        add(new IvanMcIntoshCharacter());
        add(new SebastianSmithCharacter());
        add(new JennaWildflowerCharacter());
        add(new AlewynSolethalCharacter());
        add(new MelethainGauthCharacter());
        System.out.println(size() + " characters loaded!");
    }
}
