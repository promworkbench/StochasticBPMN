package org.processmining.stochasticbpmn.algorithms;

import org.processmining.framework.plugin.PluginContext;
import org.processmining.stochasticbpmn.models.YourFirstInput;
import org.processmining.stochasticbpmn.models.bpmn.stochastic.StochasticBpmn;
import org.processmining.stochasticbpmn.models.YourSecondInput;
import org.processmining.stochasticbpmn.parameters.YourParameters;

public class YourAlgorithm {

	/**
	 * The method that implements your algorithm.
	 * 
	 * Note that this method only uses the boolean which is stored in the parameters.
	 * Nevertheless, it could have used the integer and/or the String as well.
	 * 
	 * @param context The context where to run in.
	 * @param input1 The first input.
	 * @param input2 The second input.
	 * @param parameters The parameters to use.
	 * @return The output.
	 */
	public StochasticBpmn apply(PluginContext context, YourFirstInput input1, YourSecondInput input2, YourParameters parameters) {
		/**
		 * Put your algorithm here, which computes an output form the inputs provided the parameters.
		 */
		long time = -System.currentTimeMillis();
		parameters.displayMessage("[YourAlgorithm] Start");
		parameters.displayMessage("[YourAlgorithm] First input = " + input1.toString());
		parameters.displayMessage("[YourAlgorithm] Second input = " + input2.toString());
		parameters.displayMessage("[YourAlgorithm] Parameters = " + parameters.toString());
	    StochasticBpmn output = parameters.isYourBoolean() ? new StochasticBpmn() : new StochasticBpmn();
		time += System.currentTimeMillis();
		parameters.displayMessage("[YourAlgorithm] Output = " + output.toString());
		parameters.displayMessage("[YourAlgorithm] End (took " + time/1000.0 + "  seconds).");
	    return output;
	}
}
