/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.batchee.jaxrs.client;

import javax.batch.operations.JobOperator;
import java.io.Closeable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public final class BatchEEJAXRSClientFactory {
    private static final Class<?>[] PROXY_API = new Class<?>[]{ JobOperator.class, Closeable.class };

    public static enum API {
        CXF, JAXRS2, AUTO
    }

    public static JobOperator newClient(final String baseUrl, final Class<?> jsonProvider, final API api) {
        InvocationHandler handler;
        switch (api) {
            case AUTO:
                try { // try JAXRS 2 first
                    handler = new BatchEEJAXRS2Client(baseUrl, jsonProvider);
                } catch (final Throwable th) {
                    handler = new BatchEEJAXRS1CxfClient(baseUrl, jsonProvider);
                }
                break;

            case CXF:
                handler = new BatchEEJAXRS1CxfClient(baseUrl, jsonProvider);
                break;

            case JAXRS2:
                handler = new BatchEEJAXRS2Client(baseUrl, jsonProvider);
                break;

            default:
                throw new IllegalArgumentException("enum value not yet handled, you surely forgot to implement it");
        }
        return JobOperator.class.cast(
            Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), PROXY_API, handler));
    }

    public static JobOperator newClient(final String baseUrl, final Class<?> jsonProvider) {
        return newClient(baseUrl, jsonProvider, API.AUTO);
    }

    public static JobOperator newClient(final String baseUrl) {
        try {
            return newClient(baseUrl, Thread.currentThread().getContextClassLoader().loadClass("com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider"), API.AUTO);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Jackson not available");
        }
    }

    private BatchEEJAXRSClientFactory() {
        // no-op
    }
}
