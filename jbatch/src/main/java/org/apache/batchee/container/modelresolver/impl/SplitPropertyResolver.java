/*
 * Copyright 2012 International Business Machines Corp.
 * 
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package org.apache.batchee.container.modelresolver.impl;

import org.apache.batchee.container.modelresolver.PropertyResolverFactory;
import org.apache.batchee.jaxb.Flow;
import org.apache.batchee.jaxb.Split;

import java.util.Properties;

public class SplitPropertyResolver extends AbstractPropertyResolver<Split> {


    public SplitPropertyResolver(boolean isPartitionStep) {
        super(isPartitionStep);
    }

    @Override
    public Split substituteProperties(final Split split, final Properties submittedProps, final Properties parentProps) {
        // resolve all the properties used in attributes and update the JAXB model
        split.setId(this.replaceAllProperties(split.getId(), submittedProps, parentProps));
        split.setNextFromAttribute(this.replaceAllProperties(split.getNextFromAttribute(), submittedProps, parentProps));

        // Resolve all the properties defined for this step
        Properties currentProps = parentProps;
        for (final Flow flow : split.getFlows()) {
            PropertyResolverFactory.createFlowPropertyResolver(this.isPartitionedStep).substituteProperties(flow, submittedProps, currentProps);
        }

        return split;
    }

}
