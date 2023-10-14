package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.CombatLoot;
import model.combat.SingleItemCombatLoot;
import model.enemies.ApprenticeEnemy;
import model.enemies.Enemy;
import model.enemies.ServantEnemy;
import model.items.Equipment;
import model.items.Item;
import model.items.ItemDeck;
import model.items.accessories.Accessory;
import model.items.accessories.RubyRing;
import model.items.clothing.MagesRobes;
import model.items.spells.Spell;
import model.items.weapons.MagesStaff;
import model.items.weapons.OldWand;
import model.states.DailyEventState;
import model.states.ShopState;
import util.MyPair;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class MageEvent extends DarkDeedsEvent {
    private final boolean withIntro;
    private final CharacterAppearance appearance;

    public MageEvent(Model model, boolean withIntro, CharacterAppearance appearance) {
        super(model);
        this.withIntro = withIntro;
        this.appearance = appearance;
    }

    public MageEvent(Model model) {
        this(model, true, PortraitSubView.makeRandomPortrait(Classes.MAGE));
    }

    @Override
    protected void doEvent(Model model) {
        if (withIntro) {
            println("The party encounters a mage who seems eager to discuss the more academic aspects of magic.");
            showExplicitPortrait(model, appearance, "Mage");
            if (darkDeedsMenu("Mage", makeCharacter(appearance), MyRandom.randInt(20, 50),
                    makeCompanions(), ProvokedStrategy.FIGHT_IF_ADVANTAGE)) {
                return;
            }
            portraitSay("So friend, please tell me, which is your favorite spell?");
            if (model.getParty().getInventory().getSpells().isEmpty() ||
                    MyRandom.rollD10() > model.getParty().getInventory().getSpells().size() + 5) {
                MyPair<Boolean, GameCharacter> result = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.MagicAny, 6);
                if (result.first) {
                    Spell wanted = model.getItemDeck().getRandomSpell();
                    model.getParty().partyMemberSay(model, result.second,
                            List.of("I've always been greatly intrigued by " + wanted.getName() + "."));
                    portraitSay("Yes of course, that's a fascinating spell!");
                    model.getParty().partyMemberSay(model, result.second,
                            List.of("But alas, I have not had the opportunity to learn it..."));
                    portraitSay("You know... I happen to have a copy of the spell book here. Please, take it!");
                    model.getParty().partyMemberSay(model, result.second,
                            List.of("Oh, I couldn't!", "Do you really mean it?", "Are you sure?"));
                    portraitSay("I insist! Anything to help a fellow scholar!");
                    model.getParty().getInventory().add(wanted);
                    println("You gained a copy of " + wanted.getName() + ".");
                } else {
                    model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                            List.of("Uhm... 'Abracadabra'?", "Uhm... 'Hocus Pocus'?", "Uhm... 'Wingardium Leviosa'?"));
                    portraitSay("Hmph. Well we can't all be magical prodigies I suppose...");
                }
            } else {
                leaderSay("Uhm... " + model.getParty().getInventory().getSpells().get(0).getName() + "?");
                portraitSay("Yes, that's an excellent one!");
            }
        } else {
            if (darkDeedsMenu("Mage", makeCharacter(appearance), MyRandom.randInt(20, 50),
                    makeCompanions(), ProvokedStrategy.FIGHT_IF_ADVANTAGE)) {
                return;
            }
        }
        portraitSay("Say, I happen to have a few spell books here that I could be convinced of " +
                "parting with for a few gold coins. Are you interested?");
        waitForReturn();
        List<Item> spellsbooks = new ArrayList<>();
        for (int i = 0; i < MyRandom.randInt(3, 5); ++i) {
            spellsbooks.add(model.getItemDeck().getRandomSpell());
        }
        int[] prices = new int[spellsbooks.size()];
        for (int i = 0; i < prices.length; ++i) {
            prices[i] = Math.max(1, spellsbooks.get(i).getCost() + MyRandom.randInt(-6, 6));
        }
        ShopState shop = new ShopState(model, "Mage", spellsbooks, prices);
        shop.run(model);
        println("You part ways with the mage.");
    }

    private List<Enemy> makeCompanions() {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = MyRandom.randInt(2); i > 0; --i) {
            enemies.add(new ApprenticeEnemy(PortraitSubView.makeRandomPortrait(Classes.MAGE), new OldWand()));
        }
        for (int i = MyRandom.randInt(2); i > 0; --i) {
            enemies.add(new ServantEnemy(PortraitSubView.makeRandomPortrait(Classes.None)));
        }
        return enemies;
    }

    @Override
    protected List<CombatLoot> getExtraCombatLoot(Model model) {
        return List.of(new SingleItemCombatLoot(model.getItemDeck().getRandomSpell()));
    }

    private GameCharacter makeCharacter(CharacterAppearance appearance) {
        GameCharacter gc = new GameCharacter("Mage", "", appearance.getRace(), Classes.MAGE, appearance,
                Classes.NO_OTHER_CLASSES, new Equipment(new MagesStaff(), new MagesRobes(),
                (Accessory) MyRandom.sample(ItemDeck.allJewelry()).copy()));
        gc.setLevel(MyRandom.randInt(1, 4));
        return gc;
    }
}
