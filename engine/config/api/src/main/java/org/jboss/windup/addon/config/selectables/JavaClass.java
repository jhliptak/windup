/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.windup.addon.config.selectables;

import org.jboss.windup.addon.config.Selectable;
import org.ocpsoft.rewrite.config.ConditionBuilder;

/**
 * This type probably needs to be bonded / refactored into / make use of the current
 * {@link org.jboss.windup.graph.model.resource.JavaClass} API.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface JavaClass extends ConditionBuilder, Selectable
{
    JavaClass named(String string);

    String getName();
}
