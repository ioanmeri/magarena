def action = {
    final MagicGame game, final MagicEvent event ->
    final int cmc = event.getRefInt();
    final MagicTargetFilter filter = new MagicTargetFilter.MagicCMCCardFilter(
        MagicTargetFilter.Factory.card(MagicTargetType.Library, MagicType.Creature),
        MagicTargetFilter.Operator.EQUAL,
        cmc
    );
    final MagicTargetChoice choice = new MagicTargetChoice(
        filter,
        "a creature card with converted mana cost ${cmc} from your library"
    );
    game.addEvent(new MagicSearchOntoBattlefieldEvent(
        event,
        choice
    ));
}

def event = {
    final MagicPermanent source, final int cmc ->
    return new MagicEvent(
        source,
        cmc,
        action,
        "PN searches PN's library for a creature card with converted mana cost RN, put that card onto the battlefield, then shuffle your library."
    );
}

[
    new MagicPermanentActivation(
        [MagicCondition.SORCERY_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "Search"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}{P/G}"),
                new MagicTapEvent(source), 
                new MagicSacrificePermanentEvent(source, MagicTargetChoice.SACRIFICE_CREATURE),
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            // canPlay check uses NO_COST
            if (payedCost == MagicPayedCost.NO_COST) {
                return MagicEvent.NONE;
            }
            final int cmc = ((MagicPermanent)payedCost.getTarget()).getConvertedCost() + 1;
            return event(source, cmc);
        }
    },
    new MagicPermanentActivation(
        [MagicCondition.SORCERY_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "Pay 2 life"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}"),
                new MagicPayLifeEvent(source, 2),
                new MagicTapEvent(source), 
                new MagicSacrificePermanentEvent(source, MagicTargetChoice.SACRIFICE_CREATURE),
            ];
        }
    
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            // canPlay check uses NO_COST
            if (payedCost == MagicPayedCost.NO_COST) {
                return MagicEvent.NONE;
            }
            final int cmc = ((MagicPermanent)payedCost.getTarget()).getConvertedCost() + 1;
            return event(source, cmc);
        }
    }
]
