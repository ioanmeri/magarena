[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {   
            final int count = permanent.getKicker() >= 5 ? 
                    2 * permanent.getKicker() :
                    permanent.getKicker();
            game.doAction(new MagicChangeCountersAction(
                permanent,
                MagicCounterType.PlusOne,
                count,
                true
            ));
            return MagicEvent.NONE;
        }
    },
    new MagicPermanentActivation( 
        [
            MagicConditionFactory.ManaCost("{1}{R}"),
            MagicCondition.PLUS_COUNTER_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{R}"),
                new MagicRemoveCounterEvent(source,MagicCounterType.PlusOne,1)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to target creature or player\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage = new MagicDamage(event.getSource(),target,1);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
