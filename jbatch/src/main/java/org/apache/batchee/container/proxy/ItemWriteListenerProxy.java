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
package org.apache.batchee.container.proxy;

import org.apache.batchee.container.exception.BatchContainerRuntimeException;

import javax.batch.api.chunk.listener.ItemWriteListener;
import java.util.List;

public class ItemWriteListenerProxy extends AbstractProxy<ItemWriteListener> implements ItemWriteListener {
    ItemWriteListenerProxy(final ItemWriteListener delegate) {
        super(delegate);
    }

    @Override
    public void afterWrite(final List<Object> items) {
        try {
            this.delegate.afterWrite(items);
        } catch (final Exception e) {
            this.stepContext.setException(e);
            throw new BatchContainerRuntimeException(e);
        }
    }

    @Override
    public void beforeWrite(final List<Object> items) {

        try {
            this.delegate.beforeWrite(items);
        } catch (final Exception e) {
            this.stepContext.setException(e);
            throw new BatchContainerRuntimeException(e);
        }
    }

    @Override
    public void onWriteError(final List<Object> items, final Exception ex) {
        try {
            this.delegate.onWriteError(items, ex);
        } catch (Exception e) {
            this.stepContext.setException(e);
            throw new BatchContainerRuntimeException(e);
        }
    }
}
