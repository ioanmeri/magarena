package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.condition.MagicArtificialCondition;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

import java.util.EnumSet;

public class Faerie_Conclave {
    private static final MagicStatic PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.set(2,1);
        }
    };
    private static final MagicStatic AB = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
        @Override
        public long getAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final long flags) {
            return flags|MagicAbility.Flying.getMask();
        }
    };
    private static final MagicStatic ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
            flags.add(MagicSubType.Faerie);
        }
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicType.Creature.getMask();
        }
    };
    private static final MagicStatic C = new MagicStatic(MagicLayer.Color, MagicStatic.UntilEOT) {
        @Override
        public int getColorFlags(final MagicPermanent permanent,final int flags) {
            return MagicColor.Blue.getMask();
        }        
    };

    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{new MagicArtificialCondition(
                MagicManaCost.ONE_BLUE.getCondition(),
                MagicManaCost.BLUE_BLUE.getCondition())},
            new MagicActivationHints(MagicTiming.Animate),
            "Animate") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_BLUE)};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    source.getController(),
                    this,
                    "Until end of turn, " + source + 
                    " becomes a 2/1 blue Faerie creature with flying. " + 
                    "It's still a land.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicBecomesCreatureAction(event.getPermanent(),PT,AB,ST,C));
        }
    };
}
