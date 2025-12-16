package model.mainstory.honorable;

import model.Model;
import model.items.Item;
import model.items.SpecialEasternWeapon;
import model.items.weapons.*;
import model.mainstory.GainSupportOfHonorableWarriorsTask;
import model.states.DailyEventState;
import util.MyLists;
import view.MyColors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PresentSwordToShingenEvent extends DailyEventState {
    private final GainSupportOfHonorableWarriorsTask task;
    private final boolean withIntro;

    public PresentSwordToShingenEvent(Model model, GainSupportOfHonorableWarriorsTask gainSupportOfHonorableWarriorsTask,
                                      boolean withIntro) {
        super(model);
        this.task = gainSupportOfHonorableWarriorsTask;
        this.withIntro = withIntro;
    }

    @Override
    protected void doEvent(Model model) {
        if (withIntro) {
            model.getLog().waitForAnimationToFinish();
            setCurrentTerrainSubview(model);
            println("You enter into Lord Shingen's throne room once gain.");
            showExplicitPortrait(model, task.getShingenPortrait(), "Lord Shingen");
            portraitSay("Ah, you have returned. Do you have a gift for me?");
        } else {
            showExplicitPortrait(model, task.getShingenPortrait(), "Lord Shingen");
        }
        println("What would you like to present to Lord Shingen as a gift?");

        List<GiftOptions> options = new ArrayList<>(List.of(new NothingGiftOption(),
                new BlandEasternWeaponGiftOption(model, new Wakizashi(), ShingenWeapon.Wakisashis == task.getShingenWeapon()),
                new BlandEasternWeaponGiftOption(model, new Katana(), ShingenWeapon.Katana == task.getShingenWeapon()),
                new BlandEasternWeaponGiftOption(model, new DaiKatana(), ShingenWeapon.DaiKatana == task.getShingenWeapon()),
                new OtherBladeGiftOption(model)));

        for (Item it : MyLists.filter(model.getParty().getInventory().getAllItems(), it -> it instanceof SpecialEasternWeapon)) {
            options.add(1, new SpecialGiftOption((SpecialEasternWeapon)it, task.getShingenWeapon(), task.doesShingenLikeInscriptions(),
                    task.getShingenColor()));
        }

        List<GiftOptions> filteredOptions = MyLists.filter(options, GiftOptions::isValid);
        do {
            int choice = multipleOptionArrowMenu(model, 24, 24,
                    MyLists.transform(filteredOptions, GiftOptions::getName));

            GiftOptions opt = filteredOptions.get(choice);
            filteredOptions.remove(opt);
            if (opt.presentToShingen(model)) {
                finalizeAlliance(model);
                break;
            }
            if (opt.getName().equals("Nothing")) {
                break;
            }
            if (filteredOptions.size() > 1) {
                print("Do you want to present anything else to Lord Shingen? (Y/N) ");
                if (!yesNoInput()) {
                    break;
                }
            } else {
                break;
            }
        } while (true);

        println("You leave the palace.");
        model.getLog().waitForAnimationToFinish();
    }

    private void finalizeAlliance(Model model) {
        task.setSwordGiven();
        leaderSay("So, can we count on your support to overthrow Queen Valstine?");
        portraitSay("Of course. I'll ready my forces at once. The Honorable Warriors will answer this call!");
        leaderSay("Excellent!");
        portraitSay("However, the men and women will want to be properly inspired.");
        leaderSay("Don't tell me there's more for " + meOrUs() + " to...");
        portraitSay("No no, you've done quite enough friend. I have something quite special in mind. " +
                "We have an excellent theatre group here in our town. I know they have been devoutly practicing their new piece.");
        leaderSay("Oh, that sounds nice.");
        portraitSay("Yes. Please join me at the amphitheater tomorrow. Afterward, we can discuss the final details " +
                "of our new alliance.");
        leaderSay(iOrWeCap() + " will! Thank you Lord Shingen.");
        portraitSay("Good bye then.");
    }

    private abstract class GiftOptions {
        public abstract String getName();
        public abstract boolean isValid();
        public abstract boolean presentToShingen(Model model);
    }

    private class NothingGiftOption extends GiftOptions {
        @Override
        public String getName() {
            return "Nothing";
        }
        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean presentToShingen(Model model) {
            leaderSay("Not yet. Are you sure this is absolutely necessary?");
            portraitSay("Customs must be obeyed. My ancestors would not look kindly if I scorned them.");
            leaderSay("Alright. I'll be back with a nice sword for you.");
            portraitSay("Splendid. Until then.");
            return false;
        }
    }

    private abstract class BladeGiftOption extends GiftOptions {
        private final Weapon blade;

        public BladeGiftOption(Weapon w) {
            this.blade = w;
        }

        @Override
        public String getName() {
            return blade.getName();
        }

        @Override
        public boolean isValid() {
            return blade != null;
        }
    }

    private class OtherBladeGiftOption extends BladeGiftOption {
        public OtherBladeGiftOption(Model model) {
            super(MyLists.find(model.getParty().getInventory().getWeapons(),
                    w -> w.isOfType(BladedWeapon.class) &&
                            !w.isOfType(SmallBladedWeapon.class) &&
                            !w.isOfType(Wakizashi.class) &&
                            !w.isOfType(Katana.class) &&
                            !w.isOfType(DaiKatana.class) &&
                            !w.isOfType(SpecialEasternWeapon.class)));
        }

        @Override
        public boolean presentToShingen(Model model) {
           println("As you bring out the " + getName() + " and hand it to Shingen, he frowns visibly.");
           portraitSay("What in the world is this?");
           leaderSay("Uhm, a " + getName() + ". I thought you might like it.");
           portraitSay("No no no no no. This won't do at all! How could you possibly think this crude blade " +
                   "could be a suitable gift?");
           println("Lord Shingen hastily hands the " + getName() + " back to you.");
           leaderSay("I realize my mistake now. Please forgive me.");
           portraitSay("Hmph! Well, since you are new to our customs, I'll forgive you. But please find a more suitable " +
                   "blade as a token of our lasting alliance.");
           leaderSay("I shall.");
            return false;
        }
    }

    private class BlandEasternWeaponGiftOption extends BladeGiftOption {
        private final boolean shingenWeapon;

        public BlandEasternWeaponGiftOption(Model model, Weapon w, boolean rightTypeOfWeapon) {
            super(MyLists.find(model.getParty().getInventory().getWeapons(),
                    weapon -> weapon.isOfType(w.getClass())));
            this.shingenWeapon = rightTypeOfWeapon;
        }

        @Override
        public boolean presentToShingen(Model model) {
            println("You bring out a " + getName() + " and ceremoniously present it to Shingen.");
            leaderSay("I present to you this blade.");
            println("Shingen inspects the " + getName() + ".");

            if (task.doesShingenLikeInscriptions()) {
                portraitSay("Hmm... It's quite bland, and there's no inscription...");
            } else {
                portraitSay("Hmm... It's quite bland, but at least there's no inscription.");
            }
            if (shingenWeapon) {
                portraitSay("I do like this type of weapon though. It reminds me of when I was young and foolish, " +
                        "charging into battle armed with something like this.");
                portraitSay("But ultimately, it's not good enough to symbolize our dealings.");
            } else {
                portraitSay("I'm sorry, but this just isn't at all what I had in mind.");
                leaderSay("You don't like it?");
                portraitSay("It's just not good enough to symbolize our dealings.");
            }
            println("Lord shingen hands the " + getName() + " back to you.");
            leaderSay("So I'm going to have to find another sword? Are you sure this is necessary?");
            portraitSay("Customs must be obeyed. My ancestors would not look kindly if I scorned them.");
            leaderSay("Alright. I'll be back with a nice sword for you.");
            portraitSay("I'm looking forward to it.");
            return false;
        }
    }

    private class SpecialGiftOption extends GiftOptions {
        private final SpecialEasternWeapon blade;
        private final boolean matchingWeapon;
        private final boolean shingenLikesInscriptions;
        private final boolean matchesColor;

        public SpecialGiftOption(SpecialEasternWeapon blade, ShingenWeapon shingenWeapon,
                                 boolean shingenLikesInscriptions, MyColors shingenColor) {
            this.blade = blade;
            this.matchingWeapon = blade.matchesWeaponType(shingenWeapon);
            this.shingenLikesInscriptions = shingenLikesInscriptions;
            this.matchesColor = blade.matchesColor(shingenColor);
        }

        @Override
        public String getName() {
            return blade.getName();
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean presentToShingen(Model model) {
            println("You bring out a " + getName() + " and ceremoniously present it to Shingen.");
            leaderSay("I present to you this blade.");
            println("Shingen inspects the " + getName() + ".");

            portraitSay("Ah yes... very fine indeed! You must have visited the smith in the " +
                    "hills for this blade.");

            List<String> likes = new ArrayList<>();
            List<String> dislikes = new ArrayList<>();
            if (matchingWeapon) {
                likes.add("I like this type of weapon. It reminds me of when I was young and foolish and would " +
                        "charge into battle armed with something like this.");
            } else {
                dislikes.add("I don't like this type of blade. It doesn't fit my fighting style.");
            }

            if (shingenLikesInscriptions) {
                if (blade.hasInscription()) {
                    likes.add("I like the inscription here, 'Honor'. Very suitable indeed.");
                } else {
                    dislikes.add("It's a shame it doesn't have an inscription. The blade looks naked without one.");
                }
            } else {
                if (blade.hasInscription()) {
                    dislikes.add("I don't like the inscription. It cheapens the blade in my opinion.");
                } else {
                    likes.add("I'm happy there's no foolish inscription on the blade.");
                }
            }

            if (matchesColor) {
                likes.add("I particularly like the color of the hilt. It matches the emblem of our clan.");
            } else {
                dislikes.add("The color of the hilt is wrong, it reminds me of the banners of our enemies.");
            }

            Collections.shuffle(likes);
            Collections.shuffle(dislikes);

            if (dislikes.isEmpty()) {
                for (String like : likes) {
                    portraitSay(like);
                }
                portraitSay("I daresay, this blade is perfect! The ultimate symbol of our coming alliance!");
                model.getParty().removeFromInventory(blade);
                return true;
            }
            if (likes.isEmpty()) {
                portraitSay("But, I'm afraid I don't care for this blade at all.");
                leaderSay("What!? Why not? What's wrong with it?");
            } else {
                if (likes.size() == 1) {
                    portraitSay("It's not quite to my liking. I can't be seen with such a blade.");
                } else if (likes.size() == 2) {
                    portraitSay("The blade is fine, but it's not quite right.");
                }
                leaderSay("What's wrong with it?");
                portraitSay("It's not all bad.");
                for (String like : likes) {
                    portraitSay(like);
                }
                portraitSay("However...");
            }
            for (String dislike : dislikes) {
                portraitSay(dislike);
            }
            println("Lord Shingen hands the " + getName() + " back to you.");
            portraitSay("Please return with a more suitable blade.");
            leaderSay("I will.");
            return false;
        }
    }
}
