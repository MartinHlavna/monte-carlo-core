package sk.uniza.fri.hlavna2.diss.monte.carlo.core;

/**
 * Listener for end of the simulation
 *
 * @author Martin Hlavňa {@literal <mato.hlavna@gmail.com>}
 */
public interface SimulationEndedListener {

    /**
     * Simulation with given command has ended
     *
     * @param command Commnad that has been used
     */
    void simulationEnded(MonteCarloCommand command);
}
