package PSP;

import java.io.PrintWriter;
import java.util.Collection;

import org.opt4j.common.logger.TsvLogger;
import org.opt4j.core.Individual;
import org.opt4j.core.IndividualFactory;
import org.opt4j.core.Objective;
import org.opt4j.core.Value;
import org.opt4j.core.optimizer.Archive;
import org.opt4j.core.optimizer.Optimizer;
import org.opt4j.core.problem.Evaluator;
import org.opt4j.start.Constant;

import com.google.inject.Inject;

import general.PrivateAccessor;

/**
 * A logger that prints additional information about the PSP solutions, such as
 * cost, duration, undt, reqsk, overwork and phenotype before normalisation.
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 *
 */

public class PSPLogger extends TsvLogger {

	@SuppressWarnings("unchecked")
	@Inject
	public PSPLogger(Optimizer optimizer, IndividualFactory individualFactory,
			Archive archive, Evaluator evaluator, 
			@Constant(value = "filename", namespace = TsvLogger.class) String filename,
			@Constant(value = "evaluationStep", namespace = TsvLogger.class) int evaluationStep, 
			@Constant(value = "iterationStep", namespace = TsvLogger.class) int iterationStep) {
		super(optimizer, individualFactory, archive, evaluator, filename, evaluationStep, iterationStep);
	}
	
	@Override
	public void logHeader(Collection<Objective> objectives) {
		String header = getCommentDelimiter() + "iteration" + getColumnDelimiter() + "evaluations"
				+ getColumnDelimiter() + "runtime[s]";
		for (Objective objective : objectives) {
			header += getColumnDelimiter() + objective.getName() + "[" + objective.getSign() + "]";
		}
		PrintWriter out = (PrintWriter) PrivateAccessor.getPrivateFieldSuperclass(this, "out");
		header += (getColumnDelimiter() + "cost" + getColumnDelimiter() + "duration" + getColumnDelimiter() + "undt" + getColumnDelimiter() + "reqsk" + getColumnDelimiter() + "overwork" + getColumnDelimiter() + "phenotypeBeforeNormalization"); 
		out.println(header);
	}

	@Override
	protected String getIndividual(Individual individual) {
		String output = "";
		PSPObjectives objectives = (PSPObjectives) individual.getObjectives();

		for (Objective objective : objectives.getKeys()) {
			Value<?> value = objectives.get(objective);
			PSPObjectives.ObjectiveComponentsInfo valueInfo = objectives.getInfo(objective);
			assert value != null : "Objective " + objective.getName() + " not set for individual " + individual;
			assert valueInfo != null : "Additional info (cost, duration, undt, reqsk and overwork)  not set for individual " + individual;

			String valueString;
			if (value == null || value.getValue() == null) {
				System.err.println(this + ": Value of objective " + objective.getName() + " is null.");
				valueString = "NULL";
			} else {
				String v = value.getValue().toString();
				if (v.contains("\t")) {
					System.err.println(this + ":value must not contain the tab character:" + v);
					v.replace("\t", "_");
				}
				valueString = v;
			}
			output += getColumnDelimiter() + valueString;
			output += (getColumnDelimiter() + valueInfo.cost + getColumnDelimiter() + valueInfo.duration + getColumnDelimiter() + valueInfo.undt + getColumnDelimiter() + valueInfo.reqsk + getColumnDelimiter() + valueInfo.overwork);
			output += getColumnDelimiter() + individual.getPhenotype().toString();
		}
		return output;
	}
}
