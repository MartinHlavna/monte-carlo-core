package sk.uniza.fri.hlavna2.diss.monte.carlo.core.swing;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import sk.uniza.fri.hlavna2.diss.monte.carlo.core.MonteCarloCommand;
import sk.uniza.fri.hlavna2.diss.monte.carlo.core.MonteCarloSolver;
import sk.uniza.fri.hlavna2.diss.monte.carlo.core.SimulationEndedListener;

/**
 * Abstract SwingWorker for MonteCarloSimulation.
 *
 *
 * @author Martin Hlavňa {@literal <mato.hlavna@gmail.com>}
 */
public abstract class AbstractMonteCarloWorker extends SwingWorker<Void, Object> {

    protected final int iterationsCount;
    protected final MonteCarloSolver solver;
    protected final MonteCarloCommand command;
    protected boolean isStopped;
    private final List<SimulationEndedListener> listeners;

    @Override
    protected Void doInBackground() throws Exception {
        int i = 0;
        while (i <= iterationsCount && !isStopped) {
            try {
                int j = iterationsCount - i >= 1000 ? 1000 : iterationsCount - i;
                solver.solve(j);
                Object result = command.getResult();
                publish(result, i);
                i += j > 0 ? j : 1;
            } catch (Exception ex) {
                Logger.getLogger(AbstractMonteCarloWorker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Object result = command.getResult();
        publish(result, i);
        return null;
    }

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

    /**
     * Stop simulation
     *
     * @see MonteCarloSolver
     */
    public void setStopped() {
        isStopped = true;
        solver.stop();
    }

}
