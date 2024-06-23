package model.map.wars;

import model.Model;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import util.MyLists;
import util.MyRandom;
import view.MyColors;

import java.io.Serializable;
import java.util.*;

public class WarHandler implements Serializable {
    private final List<KingdomWar> currentWars = new ArrayList<>();

    private void startWar(CastleLocation loc1, CastleLocation loc2) {
        currentWars.add(new KingdomWar(loc1.getPlaceName(), loc2.getPlaceName(),
                loc1.getCastleColor(), loc2.getCastleColor()));
    }

    public List<KingdomWar> getWars() {
        return currentWars;
    }

    public void endWar(KingdomWar war) {
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
            if (MyRandom.rollD10() >= 1) { // TODO: 10 (or even less likely)
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
}
