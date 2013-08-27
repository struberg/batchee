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
package org.apache.batchee.container.impl;

import org.apache.batchee.container.jobinstance.RuntimeJobExecution;
import org.apache.batchee.container.util.PartitionDataWrapper;
import org.apache.batchee.jaxb.Batchlet;
import org.apache.batchee.jaxb.Chunk;
import org.apache.batchee.jaxb.Decision;
import org.apache.batchee.jaxb.Flow;
import org.apache.batchee.jaxb.Partition;
import org.apache.batchee.jaxb.Split;
import org.apache.batchee.jaxb.Step;

import java.util.concurrent.BlockingQueue;

public class ExecutionElementControllerFactory {
    public static BaseStepControllerImpl getStepController(final RuntimeJobExecution jobExecutionImpl, final Step step,
                                                           final StepContextImpl stepContext, final long rootJobExecutionId,
                                                           final BlockingQueue<PartitionDataWrapper> analyzerQueue) {
        final Partition partition = step.getPartition();
        if (partition != null) {

            if (partition.getMapper() != null) {
                return new PartitionedStepControllerImpl(jobExecutionImpl, step, stepContext, rootJobExecutionId);
            }

            if (partition.getPlan() != null) {
                if (partition.getPlan().getPartitions() != null) {
                    return new PartitionedStepControllerImpl(jobExecutionImpl, step, stepContext, rootJobExecutionId);
                }
            }
        }

        final Batchlet batchlet = step.getBatchlet();
        if (batchlet != null) {
            if (step.getChunk() != null) {
                throw new IllegalArgumentException("Step contains both a batchlet and a chunk.  Aborting.");
            }
            return new BatchletStepControllerImpl(jobExecutionImpl, step, stepContext, rootJobExecutionId, analyzerQueue);
        } else {
            final Chunk chunk = step.getChunk();
            if (chunk == null) {
                throw new IllegalArgumentException("Step does not contain either a batchlet or a chunk.  Aborting.");
            }
            return new ChunkStepControllerImpl(jobExecutionImpl, step, stepContext, rootJobExecutionId, analyzerQueue);
        }
    }

    public static DecisionControllerImpl getDecisionController(RuntimeJobExecution jobExecutionImpl, Decision decision) {
        return new DecisionControllerImpl(jobExecutionImpl, decision);
    }

    public static FlowControllerImpl getFlowController(RuntimeJobExecution jobExecutionImpl, Flow flow, long rootJobExecutionId) {
        return new FlowControllerImpl(jobExecutionImpl, flow, rootJobExecutionId);
    }

    public static SplitControllerImpl getSplitController(RuntimeJobExecution jobExecutionImpl, Split split, long rootJobExecutionId) {
        return new SplitControllerImpl(jobExecutionImpl, split, rootJobExecutionId);
    }
}
