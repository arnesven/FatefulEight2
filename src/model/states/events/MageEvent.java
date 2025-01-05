package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.combat.loot.CombatLoot;
import model.combat.loot.SingleItemCombatLoot;
import model.enemies.ApprenticeEnemy;
import model.enemies.Enemy;
import model.enemies.ServantEnemy;
import model.items.Equipment;
import model.items.Item;
import model.items.ItemDeck;
import model.items.accessories.Accessory;
import model.items.clothing.*;
import model.items.spells.Spell;
import model.items.weapons.*;
import model.states.ShopState;
import model.states.duel.MagicDuelEvent;
import util.MyPair;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class MageEvent extends MagicExpertGeneralInteractionEvent {
    private final boolean withIntro;
    private final CharacterAppearance appearance;

    public MageEvent(Model model, boolean withIntro, CharacterAppearance appearance) {
        super(model, "Talk to", MyRandom.randInt(20, 50));
        this.withIntro = withIntro;
        this.appearance = appearance;
    }

    public MageEvent(Model model) {
        this(model, true, PortraitSubView.makeRandomPortrait(Classes.MAGE));
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit mage", "I know a mage here, " + heOrShe(MyRandom.flipCoin()) + " has spells");
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        if (withIntro) {
            println("The party encounters a mage who seems eager to discuss the more academic aspects of magic.");
            randomSayIfPersonality(PersonalityTrait.intellectual, new ArrayList<>(), "Finally some intellectual interaction.");
            showExplicitPortrait(model, appearance, "Mage");
        }
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        if (MyRandom.rollD6() > 2) {
            return favoriteSpell(model);
        }
        return MageIsOffended(model);
    }

    private boolean MageIsOffended(Model model) {
        println("After talking a bit with the mage, a heated discussion erupts about the finer points of magic.");
        portraitSay("Now really. You can't be serious! That's just preposterous.");
        leaderSay("I'm afraid your wrong about this.");
        portraitSay("Most aggravating! I challenge you to a magic duel to settle this dispute!");
        print("Do you accept dueling the mage? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay("We don't have time for that.");
            portraitSay("Coward. I think this proves my point.");
            leaderSay("Eh? Whatever.");
            return true;
        }
        GameCharacter mage =  getVictimCharacter(model);
        MagicDuelEvent duelEvent = new MagicDuelEvent(model, false, mage, null);
        duelEvent.setShowOpponentColor(false);
        duelEvent.setShowOpponentGauge(false);
        duelEvent.run(model);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, appearance, "Mage");
        if (duelEvent.getWinner() == mage) {
            portraitSay("There! Now we know who's the better magic user. My word is the one we'll be " +
                    "trusting on the disputed matter.");
            leaderSay("Ahh... whatever.");
            return true;
        }
        leaderSay("That should teach you.");
        portraitSay("Grrr... I thought I would win!");
        println("The mage stomps off in anger.");
        println(duelEvent.getWinner().getFullName() + " got 50 experience for winning the duel.");
        model.getParty().giveXP(model, duelEvent.getWinner(), 50);
        return false;
    }

    private boolean favoriteSpell(Model model) {
        if (withIntro) {
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
                    randomSayIfPersonality(PersonalityTrait.unkind, new ArrayList<>(), "Hah... you are hardly a prodigy.");
                }
            } else {
                leaderSay("Uhm... " + model.getParty().getInventory().getSpells().get(0).getName() + "?");
                portraitSay("Yes, that's an excellent one!");
            }
        }
        portraitSay("Say, I happen to have a few spell books here that I could be convinced of " +
                "parting with for a few gold coins. Are you interested?");
        ShopState.pressToEnterShop(this);
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
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, appearance, "Mage");
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a trained magic user. I'm educated in magical types and spell casting.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter gc = new GameCharacter("Mage", "", appearance.getRace(), Classes.MAGE, appearance,
                Classes.NO_OTHER_CLASSES, new Equipment(mageRandomWeapon(), mageRandomClothes(),
                (Accessory) MyRandom.sample(ItemDeck.allJewelry()).copy()));
        gc.setLevel(MyRandom.randInt(1, 4));
        return gc;
    }

    private Clothing mageRandomClothes() {
        return MyRandom.sample(List.of(new MesmersRobes(), new MagesRobes(),
                new WarlocksRobes(), new ShamansRobes(), new CultistsRobes()));
    }

    private Weapon mageRandomWeapon() {
        return MyRandom.sample(List.of(new MagesStaff(), new OldStaff(), new IronStaff(),
                new PineWand(), new ClaspedOrb(), new YewWand()));
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
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
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_IF_ADVANTAGE;
    }

    @Override
    protected List<CombatLoot> getExtraCombatLoot(Model model) {
        return List.of(new SingleItemCombatLoot(model.getItemDeck().getRandomSpell()));
    }
}
