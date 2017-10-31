package com.mesosphere.sdk.scheduler.plan.strategy;

import com.mesosphere.sdk.scheduler.plan.Element;
import com.mesosphere.sdk.scheduler.plan.ParentElement;
import com.mesosphere.sdk.scheduler.plan.PodInstanceRequirement;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by gabriel on 10/30/17.
 */
public class RoundRobinStrategy<C extends Element> extends InterruptibleStrategy<C> {

    @Override
    public Collection<C> getCandidates(Collection<C> elements, Collection<PodInstanceRequirement> dirtyAssets) {
        Collection<C> candidates = new ArrayList<>();
        for (C child : elements) {
            if (child instanceof ParentElement) {
                ParentElement p = (ParentElement) child;
                candidates.addAll(p.getStrategy().getCandidates(p.getChildren(), dirtyAssets));
            }
        }

        return candidates;
    }

    public StrategyGenerator<C> getGenerator() {
        return new Generator<>();
    }

    /**
     * This class generates Strategy objects of the appropriate type.
     *
     * @param <C> is the type of {@link Element}s to which the Strategy applies.
     */
    public static class Generator<C extends Element> implements StrategyGenerator<C> {
        @Override
        public Strategy<C> generate() {
            return new RoundRobinStrategy<>();
        }
    }
}
