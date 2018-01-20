package uk.badamson.mc.actor.message;

/**
 * <p>
 * {@linkplain Verb verbs} for describing simple actions.
 * </p>
 * <p>
 * This enumeration of {@link Verb} objects should include all actions that hand
 * signals can communicate. It should include all the commands in the US Army
 * Training Circular TC&nbsp;3-21.60, <cite>Visual Signals</cite>, dated 17
 * March 2017; that document replaced US Army Field Manual FM&nbsp;21-60 dated
 * September 1987.
 * </p>
 */
public enum SimpleVerb implements Verb {

    /**
     * <p>
     * A verb for declaring that the {@linkplain Sentence#getSubject() subject} of
     * the {@linkplain Sentence sentence} has changed formation, or to
     * {@linkplain Command command} that the subject changes formation.
     * </p>
     * <p>
     * The {@linkplain Sentence#getSubject() subject} of the sentence should be the
     * {@linkplain FormationName name of the formation}.
     * </p>
     */
    CHANGE_FORMATION,

    /**
     * <p>
     * A verb for declaring that the {@linkplain Sentence#getSubject() subject} of
     * the {@linkplain Sentence sentence} has assembled or rallied, or to
     * {@linkplain Command command} that the subject assembles or rallies.
     * </p>
     * <p>
     * The {@linkplain Sentence#getSubject() subject} of the sentence should be the
     * {@linkplain SimpleRelativeLocation location} at which the subject assembled.
     * </p>
     */
    ASSEMBLE,

    /**
     * <p>
     * A verb for declaring that the {@linkplain Sentence#getSubject() subject} of
     * the {@linkplain Sentence sentence} has joined or follows the
     * {@linkplain Sentence#getObjects() object}, or to {@linkplain Command command}
     * that the subject joins or follows the object.
     * </p>
     */
    JOIN,

    /**
     * <p>
     * A verb for declaring that the {@linkplain Sentence#getSubject() subject} of
     * the {@linkplain Sentence sentence} is moving moving quickly, or to
     * {@linkplain Command command} that the subject to move quickly.
     * </p>
     * <p>
     * This is slower movement than {@linkplain #RUSH rushing}.
     * </p>
     */
    QUICK_TIME,

    /**
     * <p>
     * A verb for declaring that the {@linkplain Sentence#getSubject() subject} of
     * the {@linkplain Sentence sentence} is rushing (moving very quickly), or to
     * {@linkplain Command command} that the subject to rush, increase speed, or
     * move at double time.
     * </p>
     * <p>
     * This is faster movement than {@linkplain #QUICK_TIME quick time}.
     * </p>
     */
    RUSH,

    /**
     * <p>
     * A verb for declaring that the {@linkplain Sentence#getSubject() subject} of
     * the {@linkplain Sentence sentence} is performing a battle drill, or to
     * {@linkplain Command command} that the subject performs the battle drill.
     * </p>
     */
    PERFORM_BATTLE_DRILL,

    /**
     * <p>
     * A verb for declaring that the {@linkplain Sentence#getSubject() subject} of
     * the {@linkplain Sentence sentence} has fixed a bayonet to their gun, or to
     * {@linkplain Command command} that the subject fixes their bayonet to their
     * gun.
     * </p>
     */
    FIX_BAYONET;

    public static final double INFORMATION_CONTENT = Math.log(values().length) / Math.log(2);

    /**
     * {@inheritDoc}
     * 
     * <p>
     * All simple verbs have the {@linkplain #INFORMATION_CONTENT same information
     * content}.
     * </p>
     */
    @Override
    public double getInformationContent() {
	return INFORMATION_CONTENT;
    }
}
