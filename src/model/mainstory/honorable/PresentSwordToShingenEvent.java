package model.mainstory.honorable;

import model.Model;
import model.items.Item;
import model.items.weapons.*;
import model.mainstory.GainSupportOfHonorableWarriorsTask;
import model.states.DailyEventState;
import util.MyLists;

import java.util.ArrayList;
import java.util.List;

public class PresentSwordToShingenEvent extends DailyEventState {
    private final GainSupportOfHonorableWarriorsTask task;

    public PresentSwordToShingenEvent(Model model, GainSupportOfHonorableWarriorsTask gainSupportOfHonorableWarriorsTask) {
        super(model);
        this.task = gainSupportOfHonorableWarriorsTask;
    }

    @Override
    protected void doEvent(Model model) {
        model.getLog().waitForAnimationToFinish();
        setCurrentTerrainSubview(model);
        println("You enter into Lord Shingen's throne room once gain.");
        showExplicitPortrait(model, task.getShingenPortrait(), "Lord Shingen");
        portraitSay("Ah, you have returned. Do you have a gift for me?");
        println("What would you like to present to Lord Shingen as a gift?");

        List<GiftOptions> options = new ArrayList<>(List.of(new NothingGiftOption(),
                new BlandEasternWeaponGiftOption(model, new Wakizashi(), ShingenWeapon.Wakisashis == task.getShingenWeapon()),
                new BlandEasternWeaponGiftOption(model, new Katana(), ShingenWeapon.Katana == task.getShingenWeapon()),
                new BlandEasternWeaponGiftOption(model, new DaiKatana(), ShingenWeapon.DaiKatana == task.getShingenWeapon()),
                new OtherBladeGiftOption(model)));

        // TODO: Add options for special eastern weapon

        List<GiftOptions> filteredOptions = MyLists.filter(options, GiftOptions::isValid);
        do {
            int choice = multipleOptionArrowMenu(model, 24, 24,
                    MyLists.transform(filteredOptions, GiftOptions::getName));

            GiftOptions opt = filteredOptions.get(choice);
            filteredOptions.remove(opt);
            opt.presentToShingen(model);
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

    private abstract class GiftOptions {
        public abstract String getName();
        public abstract boolean isValid();
        public abstract void presentToShingen(Model model);
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
        public void presentToShingen(Model model) {
            leaderSay("Not yet. Are you sure this is absolutely necessary?");
            portraitSay("Customs must be obeyed. My ancestors would not look kindly if I scorned them.");
            leaderSay("Alright. I'll be back with a nice sword for you.");
            portraitSay("Splendid. Until then.");
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
                            !w.isOfType(DaiKatana.class)));
        }

        @Override
        public void presentToShingen(Model model) {
           println("As you bring out the " + getName() + " and hand it to Shingen, he frowns visibly.");
           portraitSay("What in the world is this?");
           leaderSay("Uhm, a " + getName() + ". I thought you might like it.");
           portraitSay("No no no no no. This won't do at all! How could you possibly think this crude blade " +
                   "could be a suitable gift?");
           println("Lord Shingen hastily hands the " + getName() + " back to you.");
           leaderSay("I realize my mistake now. Please forgive me.");
           portraitSay("Hmph! Well, since you are new to our customs, I'll forgive you. But please find a more suitable " +
                   "blade as a token of our lasting alliance.");
           leaderSay("I shall. Goodbye for now.");
           portraitSay("Good bye.");
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
        public void presentToShingen(Model model) {
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
            portraitSay("Splendid. Until then.");
        }
    }
}
