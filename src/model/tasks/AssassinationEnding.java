package model.tasks;

import model.Model;
import model.classes.CharacterClass;
import model.items.Equipment;
import model.states.events.AssassinationEndingEvent;

public class AssassinationEnding {
    private final CharacterClass charClass;
    private final String occupation;
    private final AssassinationEndingMaker maker;
    private final String additionalInfo;
    private final Equipment equipment;

    public AssassinationEnding(CharacterClass charClass, String targetOccupation, AssassinationEndingMaker maker, String additionalInfo, Equipment equipment) {
        this.charClass = charClass;
        this.occupation = targetOccupation;
        this.maker = maker;
        this.additionalInfo = additionalInfo;
        this.equipment = equipment;
    }

    public String getOccupationDescription() {
        return occupation;
    }

    public AssassinationEndingEvent makeEnding(Model m) {
        return maker.makeEvent(m);
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public String getAdditionalInformation() {
        return additionalInfo;
    }
}
