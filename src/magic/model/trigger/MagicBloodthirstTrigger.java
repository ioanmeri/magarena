package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;

public class MagicBloodthirstTrigger extends MagicWhenComesIntoPlayTrigger {

    private final int amount;
    
    public MagicBloodthirstTrigger(final int amount) {
        this.amount = amount;
    }

    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {
        return (player.getOpponent().hasState(MagicPlayerState.WasDealtDamage)) ?
            new MagicEvent(
                permanent,
                this,
                amount > 1 ?
                    "SN enters the battlefield with " +  amount + " +1/+1 counters on it." :
                    "SN enters the battlefield with a +1/+1 counter on it."
            ):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
        game.doAction(new MagicChangeCountersAction(
            event.getPermanent(),
            MagicCounterType.PlusOne,
            amount,
            false
        ));
    }
    @Override
    public boolean usesStack() {
        return false;
    }
}

