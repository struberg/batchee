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

import org.apache.batchee.container.IController;
import org.apache.batchee.container.IExecutionElementController;
import org.apache.batchee.container.jobinstance.RuntimeJobExecution;
import org.apache.batchee.container.navigator.ModelNavigator;
import org.apache.batchee.container.navigator.NavigatorFactory;
import org.apache.batchee.container.services.IPersistenceManagerService;
import org.apache.batchee.container.servicesmanager.ServicesManagerImpl;
import org.apache.batchee.container.status.ExecutionStatus;
import org.apache.batchee.container.status.ExtendedBatchStatus;
import org.apache.batchee.jaxb.Flow;

import javax.batch.runtime.BatchStatus;
import java.util.List;
import java.util.logging.Logger;

public class FlowControllerImpl implements IExecutionElementController {

    private final static String CLASSNAME = FlowControllerImpl.class.getName();
    private final static Logger logger = Logger.getLogger(CLASSNAME);

    private final RuntimeJobExecution jobExecution;
    private final JobContextImpl jobContext;

    protected ModelNavigator<Flow> flowNavigator;

    protected Flow flow;
    private long rootJobExecutionId;

    private ExecutionTransitioner transitioner;

    //
    // The currently executing controller, this will only be set to the
    // local variable reference when we are ready to accept stop events for
    // this execution.
    private volatile IController currentStoppableElementController = null;

    private static IPersistenceManagerService _persistenceManagementService = ServicesManagerImpl.getInstance().getPersistenceManagerService();


    public FlowControllerImpl(RuntimeJobExecution jobExecution, Flow flow, long rootJobExecutionId) {
        this.jobExecution = jobExecution;
        this.jobContext = jobExecution.getJobContext();
        this.flowNavigator = NavigatorFactory.createFlowNavigator(flow);
        this.flow = flow;
        this.rootJobExecutionId = rootJobExecutionId;
    }

    @Override
    public ExecutionStatus execute() {
        if (!jobContext.getBatchStatus().equals(BatchStatus.STOPPING)) {
            transitioner = new ExecutionTransitioner(jobExecution, rootJobExecutionId, flowNavigator);
            return transitioner.doExecutionLoop();
        } else {
            return new ExecutionStatus(ExtendedBatchStatus.JOB_OPERATOR_STOPPING);
        }
    }


    @Override
    public void stop() {
        // Since this is not a top-level controller, don't try to filter based on existing status.. just pass
        // along the stop().
        IController stoppableElementController = transitioner.getCurrentStoppableElementController();
        if (stoppableElementController != null) {
            stoppableElementController.stop();
        }
    }

    @Override
    public List<Long> getLastRunStepExecutions() {
        return this.transitioner.getStepExecIds();
    }


}
