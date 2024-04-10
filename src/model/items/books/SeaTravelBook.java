package model.items.books;

import model.items.Item;
import view.MyColors;

public class SeaTravelBook extends BookItem {
    public SeaTravelBook() {
        super("Blue Book", 5, MyColors.BLUE, "Common Maritime Travel Routes",
                "Office of Sea Commerce in Cape Paxton", makeContents());
    }

    private static String makeContents() {
        return "General maritime advisory notice:\n" +
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
                "Passenger routes, location and availability:\n\n" +
                "Outbound East Durham\n" +
                "Cape Paxton.....Sometimes\n" +
                "Roukon..........Sometimes\n" +
                "Ebonshire...........Often\n" +
                "Lower Theln........Rarely\n" +
                "Chartered Boats.....Often\n" + // Often = 2-3
                "\n\n\n" +
                "Outbound Cape Paxton\n" +
                "East Durham.........Often\n" +
                "Lower Theln.....Sometimes\n" +
                "Ebonshire.......Sometimes\n" +
                "Roukon.............Rarely\n" +
                "Chartered Boats.Sometimes\n" + // Sometimes = 4-5
                "\n\n\n" +
                "Outbound Roukon\n" +
                "Ebonshire.......Sometimes\n" +
                "Lower Theln.....Sometimes\n" +
                "East Durham........Rarely\n" +
                "Cape Paxton........Rarely\n" +
                "Chartered Boats.....Often\n" + // Often = 2-3
                "\n\n\n" +
                "Outbound Ebonshire\n" +
                "Ackerville.........Rarely\n" +
                "Little Erinde......Rarely\n" +
                "Roukon..........Sometimes\n" +
                "Lower Theln.....Sometimes\n" +
                "Cape Paxton.....Sometimes\n" +
                "East Durham.....Sometimes\n" +
                "Chartered Boats.Sometimes\n" + // Sometimes = 4-5
                "\n\n" +
                "Outbound Little Erinde\n" +
                "Ebonshire.......Sometimes\n" +
                "Ackerville......Sometimes\n" +
                "Lower Theln........Rarely\n" +
                "Chartered Boats.....Never\n" +
                "\n\n" +
                "Outbound Ackerville\n" +
                "Little Erinde...Sometimes\n" +
                "Ebonshire.......Sometimes\n" +
                "Chartered Boats.....Never\n" +
                "\n\n" +
                "Outbound Lower Theln\n" +
                "East Durham........Rarely\n" +
                "Cape Paxton........Rarely\n" +
                "Roukon.............Rarely\n" +
                "Ebonshire.......Sometimes\n" +
                "Upper Theln.........Often\n" +
                "Little Erinde......Rarely\n" +
                "Chartered Boats....Rarely\n" + // Rarely = 6+
                "" +
                "Outbund Upper Theln\n" +
                "Lower Theln.........Often\n" +
                "Cape Paxton........Rarely\n" +
                "Chartered Boats.....Never\n";
    }

    @Override
    public Item copy() {
        return new SeaTravelBook();
    }
}
