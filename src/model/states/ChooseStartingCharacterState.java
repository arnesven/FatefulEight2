package model.states;

import control.FatefulEight;
import model.Model;
import model.characters.GameCharacter;
import model.items.*;
import model.items.accessories.Accessory;
import model.items.accessories.ShieldItem;
import model.items.clothing.Clothing;
import model.items.clothing.JustClothes;
import model.items.special.MagicMirror;
import model.items.weapons.Weapon;
import model.journal.MainStorySpawnEast;
import model.journal.MainStorySpawnNorth;
import model.journal.MainStorySpawnSouth;
import model.journal.MainStorySpawnWest;
import model.mainstory.MainStoryStep;
import util.MyRandom;
import view.*;
import view.party.CharacterCreationView;

import java.util.ArrayList;
import java.util.List;

public class ChooseStartingCharacterState extends GameState {

    private CharacterCreationView charCreation = null;

    public ChooseStartingCharacterState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        print("How would you like to select your starting character? ");
        model.getLog().waitForAnimationToFinish();
        GameCharacter gc;
        Item selectedStartingItem = null;
        while (true) {
            List<String> options = new ArrayList<>(List.of("Choose Preset",
                    "Random Preset",
                    "Generate",
                    "Create Custom"));
            if (FatefulEight.inDebugMode()) {
                options.add("Full Party");
                options.add("Random Full");
            }
            int choice = multipleOptionArrowMenu(model, 30, 16, options);

            if (choice == 3) {
                gc = characterCreation(model);
                if (gc != null) {
                    break;
                }
            } else if (choice == 0) {
                gc = selectPreset(model);
                if (gc != null) {
                    model.getAllCharacters().remove(gc);
                    break;
                }
            } else if (choice == 1) {
                gc = randomPreset(model);
                if (gc != null) {
                    model.getAllCharacters().remove(gc);
                    break;
                }
            } else if (choice == 2) {
                gc = generateCharacter(model);
                if (gc != null) {
                    break;
                }
            } else if (choice == 3){
                gc = fullPartySelect(model);
                if (gc != null) {
                    break;
                }
            } else {
                gc = randomFullSelect(model);
                if (gc != null) {
                    break;
                }
            }
        }
        println("");
        model.transitionToDialog(new SelectInitialSettingsView(model));
        model.getParty().add(gc);
        print("You have selected your starting character: ");
        model.getLog().waitForAnimationToFinish();
        println(gc.getFullName() + " the " + gc.getRace().getName() + " " + gc.getCharClass().getFullName() + ".");
        return model.getCurrentHex().getDailyActionState(model);
    }

    private GameCharacter fullPartySelect(Model model) {
        FullPartySelectView view = new FullPartySelectView(model);
        model.transitionToDialog(view);
        print(" ");
        model.getLog().waitForAnimationToFinish();
        if (view.didCancel()) {
            return null;
        }
        List<GameCharacter> gcs = view.getCharacters();
        if (gcs.isEmpty()) {
            return null;
        }
        for (int i = 0; i < gcs.size()-1; ++i) {
            model.getAllCharacters().remove(gcs.get(i));
            model.getParty().add(gcs.get(i));
        }
        model.getAllCharacters().remove(gcs.get(gcs.size()-1));
        view.miscSetup(model);
        return gcs.get(gcs.size()-1);
    }

    private GameCharacter randomFullSelect(Model model) {
        List<GameCharacter> chars = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            GameCharacter gc = MyRandom.sample(model.getAllCharacters());
            chars.add(gc);
            model.getAllCharacters().remove(gc);
        }

        List<Weapon> weapons = ItemDeck.allWeapons();
        List<Clothing> allApparel = ItemDeck.allApparel();
        List<Accessory> allAccessories = new ArrayList<>();
        allAccessories.addAll(ItemDeck.allJewelry());
        allAccessories.addAll(ItemDeck.allGloves());
        allAccessories.addAll(ItemDeck.allShields());
        allAccessories.addAll(ItemDeck.allHeadGear());
        allAccessories.addAll(ItemDeck.allShoes());
        for (GameCharacter gc : chars) {
            gc.setLevel(8);
            Weapon w = (Weapon) MyRandom.sample(weapons).makeHigherTierCopy(1);
            Clothing c;
            do {
                c = (Clothing) MyRandom.sample(allApparel).makeHigherTierCopy(1);
            } while (c.isHeavy() != gc.getCharClass().canUseHeavyArmor());
            Accessory a;
            do {
                a = (Accessory) MyRandom.sample(allAccessories).makeHigherTierCopy(1);
            } while (a.isOffHandItem() == w.isTwoHanded());
            gc.setEquipment(new Equipment(w, c, a));
            if (model.getParty().size() < 7) {
                model.getParty().add(gc);
            }
        }

        model.getParty().goldTransaction(1000);
        model.getParty().getInventory().addToLockpicks(3);
        new MagicMirror().addYourself(model.getParty().getInventory());

        model.progressMainStoryForTesting(MainStoryStep.STARTED,
                new MainStorySpawnSouth());
                //MyRandom.sample(List.of(new MainStorySpawnEast(), new MainStorySpawnNorth(), new MainStorySpawnWest())));
        return chars.getLast();
    }

    private GameCharacter generateCharacter(Model model) {
        return nonCustomStartingCharacter(model, new GenerateStartingCharacterView(model));
    }

    private GameCharacter randomPreset(Model model) {
        return nonCustomStartingCharacter(model, new RandomPresetStartingCharacterView(model));
    }

    private GameCharacter selectPreset(Model model) {
        return nonCustomStartingCharacter(model, new SelectStaringCharacterView(model));
    }

    private GameCharacter nonCustomStartingCharacter(Model model, StartingCharacterView view) {
        while (true) {
            model.transitionToDialog(view);
            print(" ");
            model.getLog().waitForAnimationToFinish();
            GameCharacter gc = view.getFinalCharacter();
            if (gc == null) {
                return null;
            }
            if (chooseStartingItem(model, gc) != null) {
                return gc;
            }
        }
    }

    private GameCharacter characterCreation(Model model) {
        if (charCreation == null) {
            this.charCreation = new CharacterCreationView(model.getView());
        }
        while (true) {
            model.transitionToDialog(charCreation);
            print(" ");
            model.getLog().waitForAnimationToFinish();
            GameCharacter gc = charCreation.getFinishedCharacter();
            if (gc == null) {
                return null;
            }
            if (chooseStartingItem(model, gc) != null) {
                return gc;
            }
        }
    }

    private GameCharacter chooseStartingItem(Model model, GameCharacter gc) {
        ChooseStartingItemView startingItemView = new ChooseStartingItemView(model, gc);
        print(" ");
        model.transitionToDialog(startingItemView);
        model.getLog().waitForAnimationToFinish();
        if (!startingItemView.didCancel()) {
            for (Item it : startingItemView.getSelectedItems()) {
                addSelectedItem(model, gc, it.copy());
            }
            return gc;
        }
        return null;
    }

    public static void addSelectedItem(Model model, GameCharacter gc, Item startingItem) {
        assert startingItem != null;
        boolean canEquip = Equipment.canEquip(startingItem, gc).equals("");
        if (canEquip) {
            if (startingItem instanceof Weapon && gc.getEquipment().getWeapon() instanceof StartingItem) {
                gc.getEquipment().setWeapon((Weapon) startingItem);
                return;
            }
            if (startingItem instanceof Clothing && gc.getEquipment().getClothing() instanceof JustClothes) {
                gc.getEquipment().setClothing((Clothing) startingItem);
                return;
            }
            if (startingItem instanceof Accessory && gc.getEquipment().getAccessory() == null) {
                gc.getEquipment().setAccessory((Accessory) startingItem);
                return;
            }
        }

        if (startingItem instanceof HorseStartingItem) {
            model.getParty().getHorseHandler().addHorse(((HorseStartingItem) startingItem).getHorse());
            return;
        }
        startingItem.addYourself(model.getParty().getInventory());
    }
}
