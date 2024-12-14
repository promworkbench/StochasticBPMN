package org.processmining.stochasticbpmn.models.graphbased.directed.bpmn.stochastic;

import org.processmining.stochasticbpmn.models.stochastic.Probability;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class StochasticGatewayWeightedFlow {
    private final Map<StochasticGatewayFlowSet, BigDecimal> weightedElements;
    private BigDecimal totalWeight;

    public StochasticGatewayWeightedFlow() {
        this.weightedElements = new HashMap<>();
        this.totalWeight = BigDecimal.ZERO;
    }

    public void assignFlowWeight(final BigDecimal weight, final StochasticGatewayFlowSet flowSet) {
        Optional<BigDecimal> oldWeightOpt = Optional.ofNullable(this.weightedElements.getOrDefault(flowSet, null));
        oldWeightOpt.ifPresent(oldWeight -> totalWeight = totalWeight.subtract(oldWeight));
        this.weightedElements.put(flowSet, weight);
        totalWeight = totalWeight.add(weight);
    }

    public void assignFlowWeight(final BigDecimal weight, final String ...flowSet) {
        this.assignFlowWeight(weight, new StochasticGatewayFlowSet(flowSet));
    }

    public BigDecimal getFlowWeight(final StochasticGatewayFlowSet flowSet) {
        return weightedElements.getOrDefault(flowSet, BigDecimal.ZERO);
    }

    public BigDecimal getFlowWeight(final String ...flowSet) {
        return this.getFlowWeight(new StochasticGatewayFlowSet(flowSet));
    }

    public BigDecimal getFlowWeight(final Collection<String> flows) {
        return this.getFlowWeight(new StochasticGatewayFlowSet(flows));
    }

    public Probability getFlowProbability(final StochasticGatewayFlowSet flowSet) {
        if (BigDecimal.ZERO.equals(totalWeight)) {
            return Probability.ZERO;
        }
        return Probability.of(weightedElements.getOrDefault(flowSet, BigDecimal.ZERO).divide(totalWeight, 30, RoundingMode.DOWN).stripTrailingZeros());
    }

    public Probability getFlowProbability(final String ...flowSet) {
        return this.getFlowProbability(new StochasticGatewayFlowSet(flowSet));
    }

    public Probability getFlowProbability(final Collection<String> flows) {
        return this.getFlowProbability(new StochasticGatewayFlowSet(flows));
    }

    public void remove(String... flow) {
        StochasticGatewayFlowSet flowSet = new StochasticGatewayFlowSet(flow);
        this.totalWeight = this.totalWeight.subtract(weightedElements.get(flowSet));
        weightedElements.remove(flowSet);
    }
}
