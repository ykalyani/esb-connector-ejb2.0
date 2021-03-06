/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.custom.connector;

import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.core.AbstractConnector;

import java.lang.reflect.Method;

public class CallEJBStatelessBean extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) {
        String methodName = getParameter(messageContext, EJBConstants.METHOD_NAME).toString();
        String returnName = (String) getParameter(messageContext, EJBConstants.RETURN);
        if (getParameter(messageContext, EJBConstants.RETURN) == null) {
            returnName = EJBConstants.RESPONSE;
        }
        Object ejbObj = EJBUtil.getEJBObject(messageContext, EJBConstants.JNDI_NAME);
        Object[] args = EJBUtil.buildArguments(messageContext, EJBConstants.STATELESS);
        Method method = EJBUtil.resolveMethod(ejbObj.getClass(), methodName, args.length);
        Object obj = EJBUtil.invokeInstanceMethod(ejbObj, method, args);
        if (!method.getReturnType().toString().equals(EJBConstants.VOID)) {
            messageContext.setProperty(returnName, obj);
        } else {
            messageContext.setProperty(EJBConstants.RESPONSE, EJBConstants.SUCCESS);
        }
    }
}