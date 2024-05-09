package model.items.books;

import model.items.Item;
import view.MyColors;

public class ElfOriginBook extends BookItem {
    private static final String TEXT =
            "Contrary to the belief of many, elves are neither more ancient or of more exotic origins than humans.\n\n" +
            "There are however different varieties of elves which have developed from a hypothetical common ancestor. " +
            "Among academics this ancestor is called the 'Proto-Elf' and are believed to have first emerged somewhere in " +
            "the kingdom of Ardh.\n\n" +
            "Skeletal remains of proto-elves have since been found in many places of the world. Archeologists have made " +
            "some claims about the appearance of proto-elves. They are thought to have been roughly the same height " +
            "as a man with pale olive skin. Bone structure seem to indicate particularly muscular individuals, with a " +
            "somewhat slouching posture. Many skeletal remains tell of violent physical encounters.\n\n" +
            "It is believed proto-elves which settled in Arkvale and Bogdown developed a more advanced culture " +
            "and gradually turned into our present day High Elves. The reduced brightness of the sun in those regions " +
            "made the High Elves' skin pale over time.\n\n" +
            "Proto-elves which migrated to the northern plains of the Sunblaze kingdom and the expanse to the west of it became " +
            "skilled seafarers. Exposure to the elements in those lands gradually turned their skin darker. Today " +
            "we call these elves Dark Elves.\n\n" +
            "It is believed that a tribe of proto-elves migrated to the southern continent, but those who remained made " +
            "their homes in the forests of Ardh, and are known as Wood Elves today.\n\n" +
            "The language of elves (Leyren) is believed to have been spoken in some primitive form already by the proto-elves. " +
            "Even though High Elves consider themselves the conservators of Leyren, (or High Leyren, as it is called by them), " +
            "Leyren is spoken by all elves, and even some humans. Dark Elves speak it the least, since they often have " +
            "mercantile enterprises and the common tongue is used for those purposes. Wood Elves speak it with their own dialects " +
            "which to High Elves seem backward and primitive. Elves living in towns often speak a mix of Leyren and Common when " +
            "talking to each other. This creole language has no official name but is sometimes called 'elftalk' especially " +
            "among humans, dwarves and half-orcs who cannot distinguish it from normal Leyren.\n\n" +
            "Elves of different kinds can interbreed and scholars postulate that, due to the dwindling population of " +
            "High Elves and other elves who remain in their traditional dwellings, elves will eventually merge again into one " +
            "common race.\n\n" +
            "Indeed, in some places this seem to already be happening, and one may see elves that do not seem to " +
            "fit into any category. Such elves are sometimes called 'Low Elves', but mostly are simply referred to as 'Elves', " +
            "particularly by themselves, either because they do not want to be associated with a certain elven culture, or that " +
            "they are not sure about their exact heritage themselves.";

    public ElfOriginBook() {
        super("Gray Book", 8, MyColors.GRAY_RED, "On the Origins of Elves", "Aldous Horthvox", TEXT);
    }

    @Override
    public Item copy() {
        return new ElfOriginBook();
    }
}
