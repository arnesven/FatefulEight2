package model.mainstory;

import model.Model;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.states.GameState;
import model.states.events.OrcsBattleEvent;

import java.awt.*;

public class GainSupportOfNeighborKingdomByFightingOrcsTask extends GainSupportOfNeighborKingdomTask {
    public GainSupportOfNeighborKingdomByFightingOrcsTask(String neighbor, Point position, String kingdom, String castle, Point battlePosition) {
        super(neighbor, position, kingdom, castle, battlePosition);
    }

    @Override
    protected GameState makeBattleEvent(Model model, CastleLocation defender, CastleLocation invader) {
        return new SpecialOrcsBattleEvent(model);
    }

    @Override
    protected VisitLordEvent innerMakeLordEvent(Model model, UrbanLocation neighbor, CastleLocation kingdom) {
        return new GetSupportFromLordEvent(model, neighbor, this, kingdom) {
            @Override
            protected String getShortThreatDescription(CastleLocation kingdom) {
                return "the orcish horde";
            }

            @Override
            protected String getLongThreatDescription(CastleLocation kingdom) {
                return "We're facing a large invasion force of Orcs from the " +
                        model.getMainStory().getExpandDirectionName().toLowerCase();
            }
        };
    }

    private class SpecialOrcsBattleEvent extends OrcsBattleEvent {
        public SpecialOrcsBattleEvent(Model model) {
            super(model, true);
        }

        @Override
        protected void doEvent(Model model) {
            super.doEvent(model);
            if (super.wasVictorious()) {
                GainSupportOfNeighborKingdomByFightingOrcsTask.this.setCompleted(true);
            }
        }
    }
}
