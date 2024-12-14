package model.tasks;

import model.Model;
import model.map.*;
import util.MyPair;
import util.MyRandom;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class Destination implements Serializable {
    private final Point position;
    private final String longDescription;
    private final String shortDescription;
    private final String preposition;

    public Destination(Point p, String longD, String shortD, String preposition) {
        this.position = p;
        this.longDescription = longD;
        this.shortDescription = shortD;
        this.preposition = preposition;
    }

    public Point getPosition() {
        return position;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getPreposition() {
        return preposition;
    }

    public static Point randomPositionWithoutLocation(Model model) {
        Point position;
        do {
            position = model.getWorld().getRandomPositionWithinBounds();
            WorldHex hex = model.getWorld().getHex(position);
            HexLocation loc = hex.getLocation();
            if (!(hex instanceof SeaHex) && (loc == null || loc.isDecoration())) {
                break;
            }
        } while (true);
        return position;
    }

    public static Destination generateDwellingDestination(Model model) {
        System.out.println("Making random destination!");
        Point position = randomPositionWithoutLocation(model);
        return makeDwellingDestinationAtPosition(model, position);
    }


    public static Destination generateNaturalLandmarkDestinationAtPosition(Model model, Point position) {
        MyPair<String, String> landmarkAndPrep = MyRandom.sample(List.of(
                new MyPair<>("hollow tree", "in"),
                new MyPair<>("large log", "under"),
                new MyPair<>("dry river bed", "near"),
                new MyPair<>("large rock", "under")));
        return makeDestination(model, position, landmarkAndPrep.first, landmarkAndPrep.second);
    }

    public static Destination makeDwellingDestinationAtPosition(Model model, Point position) {
        System.out.println("Position: (" + position.x + ", " + position.y + ")");
        String dwelling = MyRandom.sample(java.util.List.of(
                "hut", "house", "cottage", "lodge", "tower",
                "shack", "villa", "cave", "tent", "cabin"));
        return makeDestination(model, position, dwelling, "in");
    }

    public static Destination makeDestination(Model model, Point position, String object, String preposition) {
        WorldHex hex = model.getWorld().getHex(position);
        String article = "a ";
        if ("aeoui".contains(object.charAt(0)+"")) {
            article = "an ";
        }
        StringBuilder description = new StringBuilder(article + object);
        String shortDescription;
        if (hex.hasRoad()) {
            description.append(" on the road");
            shortDescription = article + object + " by the side of the road";
        } else {
            String inThe = " in the ";
            if (hex instanceof TundraHex || hex instanceof PlainsHex) {
                inThe = " on the ";
            }
            description.append(inThe).append(hex.getTerrainName());
            shortDescription = description.toString();
        }

        boolean foundLandMark = false;
        for (Point dxdy : Direction.getDxDyDirections(position)) {
            Point newPoint = new Point(position.x + dxdy.x, position.y + dxdy.y);
            HexLocation loc = model.getWorld().getHex(newPoint).getLocation();
            if (loc instanceof TempleLocation || loc instanceof RuinsLocation || loc instanceof UrbanLocation ||
                    loc instanceof InnLocation) {
                int direction = Direction.opposite(Direction.getDirectionForDxDy(position, dxdy));
                String directionName = Direction.longNameForDirection(direction);
                description.append(" just ").append(directionName).append(" of the ").append(loc.getName());
                foundLandMark = true;
            }
        }

        if (!foundLandMark) {
            model.getWorld().dijkstrasByLand(position, true);
            List<Point> path = model.getWorld().shortestPathToNearestTownOrCastle();
            Point dxdy = new Point(path.get(1).x - path.get(0).x, path.get(1).y - path.get(0).y);
            int direction = Direction.opposite(Direction.getDirectionForDxDy(position, dxdy));
            String directionName = Direction.longNameForDirection(direction);
            HexLocation loc = model.getWorld().getHex(path.get(path.size()-1)).getLocation();
            description.append(" ").append(directionName).append(" of the ").append(loc.getName());
        }

        System.out.println("Position: (" + position.x + ", " + position.y + ")");
        System.out.println("Long: " + description.toString());
        System.out.println("Short: " + shortDescription);
        return new Destination(position, description.toString(), shortDescription, preposition);
    }
}
