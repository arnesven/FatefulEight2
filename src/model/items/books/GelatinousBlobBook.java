package model.items.books;

import model.items.Item;
import view.MyColors;

public class GelatinousBlobBook extends BookItem {
    private static final String TEXT =
            "Herein are my collected notes on the strange creature known as the Gelatinous Blob.\n\n" +
            "INTRODUCTION\n" +
            "Gelatinous Blobs can be found in several places in the world, but not seldom in caves. " +
            "They appear to be a oozing jelly, with a few organs scattered inside. They reproduces " +
            "solely by splitting themselves in two, and grow from feeding on any organic material they can " +
            "absorb into their mass.\n\n" +
            "VARIETIES\n" +
            "There appears to be several sub-species of Gelatinous Blobs, all with different qualities. " +
            "It is not known how many such sub-species exist, but below is a detailed description of the " +
            "ones discovered so far.\n\n" +
            "Green Blobs have a toxic sludge oozing on their skin (or surface may be a more apt word to use). " +
            "Beware of their venomous attacks.\n\n" +
            "Yellow Blobs have electric current running through their bodies and can shock anyone who touches them.\n\n" +
            "Blue Blobs seem slightly more aggressive than those of other colors, but are otherwise no different.\n\n" +
            "Purple Blobs have been known to being able to generate magic energy, but not much more is known.\n\n" +
            "Brown Blobs are simply slightly larger than most blobs of other colors.\n\n" +
            "Pink Blobs can regenerate health quickly and can be a pain to dispose of.\n\n" +
            "Red Blobs appear to favor collecting metal shards and can sometimes expunge them " +
            "in a violent flurry, causing damage to anything or anybody nearby.\n\n" +
            "Orange Blobs can throw clumps of themselves at long distances.\n\n" +
            "White Blobs have the peculiar feature of being able to produce blobs of another color when " +
            "they split. They are also somewhat smaller and hardier than others.\n\n" +
            "Black Blobs are volatile and seem to be made up of some unstable compounds. They can burst " +
            "when attacked but also appear to decompose by themselves over time.\n\n" +
            "COMBAT ADVICE\n" +
            "A newly formed Gelatinous Blob is always weak, and indeed the original blob is weakened by splitting as well. " +
            "Some times it is easiest to let blobs multiply to the point where they are all easily destroyed. On the other hand, " +
            "some blobs (like the pink ones) grow so quickly that it seems most prudent to cut them down as fast as you possibly can. " +
            "In general, it is always best to assess the situation first, and then find the best " +
            "strategy depending on the type of blobs you are dealing with.";

    public GelatinousBlobBook() {
        super("Green Book", 20, MyColors.DARK_GREEN, "A Study of Gelatinous Blobs",
                "Chrusius Melason", TEXT);
    }

    @Override
    public Item copy() {
        return new GelatinousBlobBook();
    }
}
