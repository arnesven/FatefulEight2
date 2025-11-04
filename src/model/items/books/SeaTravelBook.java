package model.items.books;

import model.items.Item;
import model.map.TownLocation;
import model.map.locations.*;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class SeaTravelBook extends BookItem {
    private static final List<TownLocation> towns = new ArrayList<>(List.of(
            new EastDurhamTown(),
            new RoukonTown(),
            new EbonshireTown(),
            new LittleErindeTown(),
            new AckervilleTown(),
            new LowerThelnTown(),
            new UpperThelnTown()));;

    public SeaTravelBook() {
        super("Blue Book", 5, MyColors.BLUE, "Common Maritime Travel Routes",
                "Office of Sea Commerce in Cape Paxton", makeContents());
    }

    private static String makeContents() {
        String preamble = "General maritime advisory notice:\n" +
                "Beware of pirates when traveling with chartered ships, especially when " +
                "travelling north of Cape Paxton or west of Roukon.\n\n" +
                "There have also been reports of giant squid like creatures in " +
                "those parts of the world. Voyage there at your own risk!\n\n" +
                "Passenger fares:\n" +
                "Captains set their own prices and vary by location " +
                "but here are some by-person averages which you may use as guidelines:\n\n" +
                "Short Voyage:  1-2 gold\n" +
                "Medium Voyage: 2-3 gold\n" +
                "Long Voyage:   3-4 gold\n" +
                "\n\n\n\n"+
                "Passenger routes, location and availability:\n\n";

        StringBuilder bldr = new StringBuilder(preamble);
        int count = 0;
        for (TownLocation loc : towns) {
            count++;
            bldr.append("Outbound ").append(loc.getTownName()).append("\n");
            for (String s : loc.getSeaTravelRoutes()) {
                bldr.append(s);
                bldr.append("\n");
            }
            if (count == 3) {
                bldr.append("\n"); // Next page
            } else {
                bldr.append("\n\n\n");
            }
        }
        return bldr.toString();
    }

    @Override
    public Item copy() {
        return new SeaTravelBook();
    }
}
