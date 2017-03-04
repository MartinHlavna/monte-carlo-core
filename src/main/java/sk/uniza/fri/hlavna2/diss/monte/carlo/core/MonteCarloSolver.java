/*
 * The MIT License
 *
 * Copyright 2017 Martin Hlavňa <mato.hlavna@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package sk.uniza.fri.hlavna2.diss.monte.carlo.core;

import java.util.HashMap;
import sk.uniza.fri.hlavna2.diss.monte.carlo.core.exception.MonteCarloStoppedException;
import sk.uniza.fri.hlavna2.diss.monte.carlo.core.utils.RandomStorageImpl;

/**
 * Solver for static Monte Carlo simulation
 *
 * @author Martin Hlavňa {@literal <mato.hlavna@gmail.com>}
 */
public class MonteCarloSolver {

    private final MonteCarloStatistics statistics;

    private final MonteCarloCommand command;

    private final RandomStorageImpl randomStorage;

    private boolean isStopped;

    private MonteCarloSolver(MonteCarloCommand command, MonteCarloStatistics statistics) {
        this.statistics = statistics;
        this.randomStorage = new RandomStorageImpl(new HashMap<>());
        this.command = command;

    }

    /**
     * Solve number of iterations. This method can be called number of times, resulting in better precistion of the
     * result.
     *
     * If simulation is stopped before calling of this method, this method will throw exception
     *
     * @param iterations Number of iterations to solve in this iteration
     */
    public void solve(int iterations) {
        if (isStopped) {
            throw new MonteCarloStoppedException();
        }
        int i;
        for (i = statistics.getIterationsRunned(); i < statistics.getIterationsRunned() + iterations; i++) {
            if (!isStopped) {
                command.simulate(randomStorage);
            }

        }
        statistics.setIterationsRunned(i);
    }

    /**
     * Create solver with given command, parameters, and default statistics
     *
     * @param command Command to use for simulation
     * @param parameters Parameters mostly used by command
     * @return Instance of the solver
     */
    public static MonteCarloSolver getSolver(MonteCarloCommand command, MonteCarloParameters parameters) {
        return MonteCarloSolver.getSolver(command, parameters, new MonteCarloStatistics());
    }

    /**
     * Create solver with given commnad, parameters and custom statistics
     *
     * @param command Command to use for simulation
     * @param parameters parameters mostly used by the command
     * @param statistics Custom statstics
     * @return Instance of the solver
     */
    public static MonteCarloSolver getSolver(MonteCarloCommand command, MonteCarloParameters parameters, MonteCarloStatistics statistics) {
        MonteCarloSolver solver = new MonteCarloSolver(command, statistics);
        command.init(parameters, statistics, solver.randomStorage);
        solver.randomStorage.setInitialized();
        return solver;
    }

    /**
     * Stop the simulation.
     *
     * If simulation is currently solving for number of iteration, Solver waits to the end of current simulation and
     * then stops solving. Another call to the solve method will throw exception
     */
    public void stop() {
        isStopped = true;
    }

    /**
     * Get command associated with this instance of the solver
     *
     * @return Command used by his solver
     */
    public MonteCarloCommand getCommand() {
        return command;
    }

}
