package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.UpperThelnTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.MAG;
import static model.races.Race.HALF_ORC;

public class VendelaGawainsCharacter extends PresetCharacter {
    public VendelaGawainsCharacter() {
        super("Vendela", "Gawains", HALF_ORC, ASN,
                new VendelaGawains(), new CharacterClass[]{ASN, NOB, WIT, MAG},
                UpperThelnTown.NAME, List.of(PersonalityTrait.greedy, PersonalityTrait.anxious, PersonalityTrait.friendly));
    }
}
