/*
 * Copyright 2007-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laudandjolynn.avf.cmd;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.laudandjolynn.avf.BaseAction;
import com.laudandjolynn.avf.ex.AvfException;
import com.laudandjolynn.avf.ex.ExceptionFactory;
import com.laudandjolynn.avf.utils.ReflectionUtils;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2014-02-19 下午3:25:35
 * @copyright: com.laudandjolynn.avf
 */
public class ActionCommand implements Command {
	private static final Logger log = LoggerFactory
			.getLogger(ActionCommand.class);
	private ActionCmdWrapper actionCmdWrapper;

	/**
	 * 构造函数
	 * 
	 * @param wrapper
	 */
	public ActionCommand(ActionCmdWrapper wrapper) {
		this.actionCmdWrapper = wrapper;
	}

	@Override
	public Object execute(Object parameter) throws AvfException {
		Class<?> clazz = actionCmdWrapper.getActionType();
		Object obj = ReflectionUtils.newInstance(clazz, new Object[] {},
				new Class<?>[] {});

		// 设置请求指令参数
		Method setRequestParams = ReflectionUtils.findMethod(BaseAction.class,
				"setParameter", new Object());
		try {
			setRequestParams.invoke(obj, parameter);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw ExceptionFactory.wrapException("set paramerter error.", e);
		}
		// 设置指令
		Method setCommand = ReflectionUtils.findMethod(clazz, "setCommand",
				Command.class);
		String cmdDesc = actionCmdWrapper.toString();
		try {
			setCommand.invoke(obj, this);
		} catch (Exception e) {
			throw ExceptionFactory.wrapException("set command: " + cmdDesc
					+ " error.", e);
		}

		// 获得指令方法
		Method method = ReflectionUtils.findMethod(clazz,
				actionCmdWrapper.getInvokerName(), new Object[0]);
		log.info("execute command: " + cmdDesc);
		try {
			return method.invoke(obj, new Object[0]);
		} catch (Exception e) {
			throw ExceptionFactory.wrapException("execute command: " + cmdDesc
					+ " error.", e);
		}
	}

	@Override
	public String toString() {
		return "[ActionCommand: " + actionCmdWrapper.toString() + "]";
	}
}