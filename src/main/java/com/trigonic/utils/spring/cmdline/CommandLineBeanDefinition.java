/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trigonic.utils.spring.cmdline;

import joptsimple.OptionSet;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.GenericBeanDefinition;

public class CommandLineBeanDefinition extends GenericBeanDefinition {
    private static final long serialVersionUID = 1L;

    public CommandLineBeanDefinition(Class<?> beanClass, CommandLineMetaData metaData, OptionSet optionSet) {
        setBeanClass(beanClass);
        populate(metaData, optionSet);
    }

    private void populate(CommandLineMetaData metaData, OptionSet optionSet) {
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        for (OptionHandler handler : metaData.getOptionHandlers()) {
            handler.addPropertyValue(propertyValues, optionSet);
        }
        for (OperandHandler handler : metaData.getOperandHandlers()) {
            if (!handler.addPropertyValue(propertyValues, optionSet) && handler.isRequired()) {
                throw new OperandException(String.format("Operand [%s] is required", handler.getName()));
            }
        }
        setPropertyValues(propertyValues);
    }
}
