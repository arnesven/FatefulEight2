package model.items.books;

import model.items.Item;
import view.MyColors;

public class PoetryBookOne extends BookItem {
    public PoetryBookOne() {
        super("Purple Book", 5, MyColors.DARK_PURPLE, "The Particularist Eleven", "Sir Ken Lions", makeContent());
    }

    private static String makeContent() {
        return "Men the world could not ignore\n" +
                "drew their glinting glaives of guilts\n" +
                "Toruned the tips to touch the floor\n" +
                "held their hands upon the hilts\n" +
                "\n" +
                "So they stood and did proclaim\n" +
                "words that would their will extend\n" +
                "Awake those without a name\n" +
                "The system is about to end!\n" +
                "\n" +
                "We Eleven who advance\n" +
                "come you to inform\n" +
                "Coming is a circumstance\n" +
                "One that will the world reform\n" +
                "\n" +
                "To the corporatist elite\n" +
                "we foolishly our power gave\n" +
                "we let them lull us in deceit\n" +
                "we let them our world enslave\n" +
                "\n" +
                "Profit is their sole intent\n" +
                "usurping all supplies\n" +
                "Ourselves and the environment\n" +
                "they exploit and brutalize\n" +
                "\n" +
                "The proportion of this theft\n" +
                "a decadent degree\n" +
                "while the world of our is left\n" +
                "in ignorance and poverty\n" +
                "\n" +
                "The world will fall into decay\n" +
                "if we give these lords our trust\n" +
                "Do not hear when people say\n" +
                "things are now the way they must\n" +
                "\n" +
                "We must purge these ill illusions\n" +
                "or face our devastation\n" +
                "Nullify the constitutions\n" +
                "which did lay the fraud's foundation\n" +
                "\n" +
                "Do not look above for aid\n" +
                "renounce the politics\n" +
                "All officials have been paid\n" +
                "trivial trinkets for the tricks\n" +
                "\n" +
                "Those of an organization\n" +
                "which does 'round a god revolve\n" +
                "cannot grasp the situation\n" +
                "therefore cannot it resolve\n" +
                "\n" +
                "Falsehood is what they greate\n" +
                "and misguide pride\n" +
                "Pride which turns to holy hate\n" +
                "and does further us divide\n" +
                "\n" +
                "Yet the root of all these pranks\n" +
                "are credits, the true crime\n" +
                "For charlatans and montebanks\n" +
                "are the zeitgeist of our time\n" +
                "\n" +
                "We will bring a revolution\n" +
                "cleansing like a storm's\n" +
                "'tis not one of retribution\n" +
                "rather one of our norms\n" +
                "\n" +
                "Rabid fervid chauvinism\n" +
                "and all other old appeals\n" +
                "are diverging from the prism\n" +
                "fading from the timeline wheels\n" +
                "\n" +
                "Consciousness will have rebirth!\n" +
                "The thinking that will come to be\n" +
                "has matured and sees the earth\n" +
                "as a single entity\n" +
                "\n" +
                "And the mind of these adults\n" +
                "further comes to realize\n" +
                "war upon itself results\n" +
                "in the entity's demise\n" +
                "\n" +
                "We eleven will now break\n" +
                "our final time we have arisen\n" +
                "Such were all the words they spake\n" +
                "then their blades did glare and glisten!\n" +
                "\n" +
                "Swords that cut away their head\n" +
                "sent their souls to hell or heaven\n" +
                "Just before, We are, they said,\n" +
                "Particularist Eleven!";
    }

    @Override
    public Item copy() {
        return new PoetryBookOne();
    }
}
