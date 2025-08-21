package model.map.wars;

import model.Model;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.map.locations.ArdhCastle;
import model.map.locations.ArkvaleCastle;
import model.map.locations.BogdownCastle;
import model.map.locations.SunblazeCastle;
import util.MyLists;
import util.MyRandom;
import view.MyColors;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class WarHandler implements Serializable {

    private final List<KingdomWar> currentWars = new ArrayList<>();
    private Map<String, Map<String, List<PitchedBattleSite>>> siteMap;

    public WarHandler() {
        siteMap = buildSiteMap();
    }

    public static String[] getAllKingdomNames() {
        return new String[]{BogdownCastle.CASTLE_NAME, ArkvaleCastle.CASTLE_NAME,
                SunblazeCastle.CASTLE_NAME, ArdhCastle.CASTLE_NAME};
    }

    private void startWar(CastleLocation loc1, CastleLocation loc2) {
        List<PitchedBattleSite> battleSites = getBattleSites(loc1, loc2);

        currentWars.add(new KingdomWar(loc1.getPlaceName(), loc2.getPlaceName(),
                loc1.getCastleColor(), loc2.getCastleColor(),
                new ArrayList<>(battleSites.subList(0, 2)),
                battleSites.get(2),
                new ArrayList<>(battleSites.subList(3, 5))));
    }

    private List<PitchedBattleSite> getBattleSites(CastleLocation loc1, CastleLocation loc2) {
        return siteMap.get(loc1.getName()).get(loc2.getName());
    }

    public List<KingdomWar> getWars() {
        return currentWars;
    }

    public void endWar(KingdomWar war) {
        System.out.println(war.getAggressor() + " and " + war.getDefender() + " are no longer at war.");
        currentWars.remove(war);
    }

    public List<KingdomWar> getWarsForKingdom(CastleLocation kingdom) {
        return MyLists.filter(currentWars,
                (KingdomWar w) ->
                        w.isAggressor(kingdom) ||
                        w.isDefender(kingdom));
    }

    public void updateWars(Model model) {
        if (currentWars.isEmpty()) {
            if (MyRandom.rollD10() >= 10 && model.isInOriginalWorld()) {
                List<CastleLocation> castles =
                        MyLists.transform(
                            MyLists.filter(model.getWorld().getLordLocations(),
                                (UrbanLocation urb) -> (urb instanceof CastleLocation)),
                            (UrbanLocation urb) -> (CastleLocation)urb);
                Collections.shuffle(castles);
                startWar(castles.get(0), castles.get(1));
                System.out.println(castles.get(0).getPlaceName() + " and " + castles.get(1).getPlaceName() + " are now at war!");
            }
        }
    }

    private static Map<String, Map<String, List<PitchedBattleSite>>> buildSiteMap() {
        Map<String, Map<String, List<PitchedBattleSite>>> siteMap = new HashMap<>();
        PitchedBattleSite bog = new SwampyBattleSite(new Point(16, 13), MyColors.GREEN, "of " + BogdownCastle.CASTLE_NAME);
        PitchedBattleSite ark = new PitchedBattleSite(new Point(35, 12), MyColors.WHITE, "of " + ArkvaleCastle.CASTLE_NAME);
        PitchedBattleSite sun = new PitchedBattleSite(new Point(16, 25), MyColors.YELLOW, "of " + SunblazeCastle.CASTLE_NAME);
        PitchedBattleSite ard = new PitchedBattleSite(new Point(35, 24), MyColors.GREEN, "of " + ArdhCastle.CASTLE_NAME);

        PitchedBattleSite n1 = new HillyBattleSite(new Point(19, 11), MyColors.WHITE, "of Mount Vormund");
        PitchedBattleSite n2 = new WoodedBattleSite(new Point(26, 12), MyColors.GREEN, "of Paxton Woods");
        PitchedBattleSite n3 = new PitchedBattleSite(new Point(31, 11), MyColors.WHITE, "of Urntown");

        PitchedBattleSite w1 = new RiverBattleSite(new Point(15, 17), MyColors.GREEN, "at Roukon River");
        PitchedBattleSite w2 = new PitchedBattleSite(new Point(16, 21), MyColors.GREEN, "of Erinde Fields");
        PitchedBattleSite w3 = new SparselyHillyBattleSite(new Point(15, 24), MyColors.YELLOW, "of Zand");

        PitchedBattleSite x1 = new PitchedBattleSite(new Point(19, 14), MyColors.GREEN, "of Urh Plains");
        PitchedBattleSite x2 = new WoodedBattleSite(new Point(25, 19), MyColors.GREEN, "at the Crossroads");
        PitchedBattleSite x3 = new RiverBattleSite(new Point(32, 20), MyColors.GREEN, "on the Thelnius");

        PitchedBattleSite y1 = new WoodedBattleSite(new Point(31, 17), MyColors.GREEN, "of Caleb's Clearing");
        PitchedBattleSite y2 = new HillyBattleSite(new Point(27, 20), MyColors.GREEN, "of Theln Hills");
        PitchedBattleSite y3 = new LakesideBattleSite(new Point(22, 23), MyColors.GREEN, "of Lake Acker");

        PitchedBattleSite e1 = new SwampyBattleSite(new Point(36, 14), MyColors.GREEN, "of Arksea Swamp");
        PitchedBattleSite e2 = new WoodedBattleSite(new Point(35, 18), MyColors.GREEN, "of Ashtonshire Woods");
        PitchedBattleSite e3 = new SparselyHillyBattleSite(new Point(37, 21), MyColors.YELLOW, "of Ronk Hills");

        PitchedBattleSite s1 = new HillyBattleSite(new Point(20, 25), MyColors.GREEN, "of Hunter's Hills");
        PitchedBattleSite s2 = new LakesideBattleSite(new Point(26, 26), MyColors.YELLOW, "on Meadhome Beach");
        PitchedBattleSite s3 = new PitchedBattleSite(new Point(31, 25), MyColors.GREEN, "of Sheffield");

        String BOG = BogdownCastle.CASTLE_NAME;
        String ARK = ArkvaleCastle.CASTLE_NAME;
        String SUN = SunblazeCastle.CASTLE_NAME;
        String ARD = ArdhCastle.CASTLE_NAME;

        // Bogdown                Arkvale
        //           N1  N2  N3
        //   W1    X1         Y1     E1
        //
        //   W2        X2 Y2         E2
        //
        //   W3     Y3        X3     E3
        //           S1  S2  S3
        // Sunblaze                 Ardh

        siteMap = Map.of(
                BOG, Map.of(ARK, List.of(bog, n1, n2, n3, ark),
                        SUN, List.of(bog, w1, w2, w3, sun),
                        ARD, List.of(bog, x1, x2, x3, ard)),
                ARK, Map.of(BOG, List.of(ark, n3, n2, n1, bog),
                        SUN, List.of(ark, y1, y2, y3, sun),
                        ARD, List.of(ark, e1, e2, e3, ard)),
                SUN, Map.of(BOG, List.of(sun, w3, w2, w1, bog),
                        ARK, List.of(sun, y3, y2, y1, ark),
                        ARD, List.of(sun, s1, s2, s3, ard)),
                ARD, Map.of(BOG, List.of(ard, x3, x2, x1, bog),
                        ARK, List.of(ard, e3, e2, e1, ark),
                        SUN, List.of(ard, s3, s2, s1, sun)));
        return siteMap;
    }
}
