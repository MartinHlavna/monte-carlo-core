package sk.uniza.fri.hlavna2.diss.monte.carlo.core.swing;

import java.util.LinkedList;
import java.util.List;
import javax.swing.SwingWorker;
import sk.uniza.fri.hlavna2.diss.monte.carlo.core.MonteCarloCommand;
import sk.uniza.fri.hlavna2.diss.monte.carlo.core.MonteCarloSolver;
import sk.uniza.fri.hlavna2.diss.monte.carlo.core.SimulationEndedListener;

/**
 *
 * @author Martin Hlavňa <mato.hlavna@gmail.com>
 */
public abstract class AbstractMonteCarloWorker<T, V> extends SwingWorker<T, V> {

    protected final int iterationsCount;
    protected final MonteCarloSolver solver;
    protected final MonteCarloCommand command;
    protected boolean isStopped;
    private List<SimulationEndedListener> listeners;

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

    public void addSimulationEndedListener(SimulationEndedListener listener) {
        listeners.add(listener);
        if (isStopped) {
            listener.simulationEnded(command);
        }
    }

    public void removeSimulationEndedListener(SimulationEndedListener listener) {
        listeners.remove(listener);
    }

}
