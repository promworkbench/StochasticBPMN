package org.processmining.newpackageivy.models.graphbased.directed.bpmn.stochastic;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StochasticGatewayWeightedFlow {
    private final Map<StochasticGatewayFlowSet, Double> weightedElements;
    private Double totalWeight;

    public StochasticGatewayWeightedFlow() {
        this.weightedElements = new HashMap<>();
        this.totalWeight = 0.0;
    }

    public void assignFlowWeight(final Double weight, final StochasticGatewayFlowSet flowSet) {
        Optional<Double> oldWeightOpt = Optional.ofNullable(this.weightedElements.getOrDefault(flowSet, null));
        oldWeightOpt.ifPresent(aDouble -> totalWeight -= aDouble);
        this.weightedElements.put(flowSet, weight);
        totalWeight += weight;
    }

    public void assignFlowWeight(final Double weight, final String ...flowSet) {
        this.assignFlowWeight(weight, new StochasticGatewayFlowSet(flowSet));
    }

    public Double getFlowWeight(final StochasticGatewayFlowSet flowSet) {
        return weightedElements.getOrDefault(flowSet, 0.0);
    }

    public Double getFlowWeight(final String ...flowSet) {
        return this.getFlowWeight(new StochasticGatewayFlowSet(flowSet));
    }

    public Double getFlowProbability(final StochasticGatewayFlowSet flowSet) {
        if (totalWeight == 0.0) {
            return 0.0;
        }
        return weightedElements.getOrDefault(flowSet, 0.0) / totalWeight;
    }

    public Double getFlowProbability(final String ...flowSet) {
        return this.getFlowProbability(new StochasticGatewayFlowSet(flowSet));
    }
}
