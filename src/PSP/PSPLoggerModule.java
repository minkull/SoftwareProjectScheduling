package PSP;

import org.opt4j.common.logger.LoggerModule;
import org.opt4j.common.logger.TsvLogger;

/**
 * A {@link LoggerModule} for PSP.
 * 
 * @author Leandro Lei Minku (l.l.minku@cs.bham.ac.uk)
 *
 */

public class PSPLoggerModule extends LoggerModule{

	@Override
	public void config() {

		//bind(TsvLogger.class).in(SINGLETON);
		bind(TsvLogger.class).to(PSPLogger.class).in(SINGLETON);
		
		addOptimizerIterationListener(TsvLogger.class);
		addOptimizerStateListener(TsvLogger.class);

		int evaluationStep = this.evaluationStep;
		int iterationStep = this.iterationStep;

		if (!loggingPerEvaluation) {
			evaluationStep = -1;
		}
		if (!loggingPerIteration) {
			iterationStep = -1;
		}

		bindConstant("evaluationStep", TsvLogger.class).to(evaluationStep);
		bindConstant("iterationStep", TsvLogger.class).to(iterationStep);
	}
}
