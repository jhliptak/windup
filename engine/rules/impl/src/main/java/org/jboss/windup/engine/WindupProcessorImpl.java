package org.jboss.windup.engine;

import java.util.List;

import javax.inject.Inject;

import org.jboss.windup.addon.engine.WindupProcessor;
import org.jboss.windup.engine.provider.ListenerChainProvider;
import org.jboss.windup.engine.visitor.GraphVisitor;
import org.jboss.windup.graph.GraphContext;
import org.jboss.windup.graph.WindupContext;
import org.jboss.windup.graph.dao.JavaClassDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindupProcessorImpl implements WindupProcessor
{

    private static final Logger LOG = LoggerFactory.getLogger(WindupProcessorImpl.class);

    @Inject
    WindupContext windupContext;
    
    @Inject
    GraphContext graphContext;

    @Inject
    private ListenerChainProvider provider;

    @Inject
    JavaClassDao javaClassDao;

    @Inject
    ConfigurationProcessorImpl configProcessor;

    @Override
    public void execute()
    {
        final List<GraphVisitor> listenerChain = provider.getListenerChain();

        LOG.info("Executing: " + listenerChain.size() + " listeners...");
        // LOG.info("Executing: " + listenerChain);
        for (final GraphVisitor visitor : listenerChain)
        {
            LOG.info("Processing: " + visitor + " - Class: " + visitor.getClass());
            visitor.run();
        }

        configProcessor.run(graphContext);

        LOG.info("Execution complete.");
    }
}
