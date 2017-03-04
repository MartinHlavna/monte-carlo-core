package sk.uniza.fri.hlavna2.diss.monte.carlo.core.swing;

import java.util.LinkedList;
import java.util.List;
import javax.swing.SwingWorker;
import sk.uniza.fri.hlavna2.diss.monte.carlo.core.MonteCarloCommand;
import sk.uniza.fri.hlavna2.diss.monte.carlo.core.MonteCarloSolver;
import sk.uniza.fri.hlavna2.diss.monte.carlo.core.SimulationEndedListener;

/**
 * Abstract SwingWorker for MonteCarloSimulation.
 *
 *
 * @author Martin Hlavňa {@literal <mato.hlavna@gmail.com>}
 * @param <T>
 * @param <V>
 */
public abstract class AbstractMonteCarloWorker<T, V> extends SwingWorker<T, V> {

    protected final int iterationsCount;
    protected final MonteCarloSolver solver;
    protected final MonteCarloCommand command;
    protected boolean isStopped;
    private final List<SimulationEndedListener> listeners;

    /**
     * Initialize this swing worker
     *
     * @param iterationsCount Number of iterations to solve
     * @param solver Solver to use
     * @param command Command to simulate against
     */
    public AbstractMonteCarloWorker(int iterationsCount, MonteCarloSolver solver, MonteCarloCommand command) {
        this.iterationsCount = iterationsCount;
        this.solver = solver;
        this.command = command;
        listeners = new LinkedList<>();
    }

    @Override
    protected void done() {
        super.done();
        for (SimulationEndedListener listener : listeners) {
            listener.simulationEnded(command);
        }
    }

    /**
     * Register for the end of the simulation. If simulation has been ended listener will be invoked immidiatly
     *
     * @param listener Listener for the end of the simulation
     */
    public void addSimulationEndedListener(SimulationEndedListener listener) {
        listeners.add(listener);
        if (isStopped) {
            listener.simulationEnded(command);
        }
    }

    /**
     * Remove listener for the end of the simulation
     *
     * @param listener Listener to remove
     */
    public void removeSimulationEndedListener(SimulationEndedListener listener) {
        listeners.remove(listener);
    }

}
