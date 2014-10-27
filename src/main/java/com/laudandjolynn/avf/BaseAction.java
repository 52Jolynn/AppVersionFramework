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
package com.laudandjolynn.avf;

import com.laudandjolynn.avf.cmd.Command;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2014-02-19 下午6:24:00
 * @copyright: avf.laudandjolynn.com
 */
public class BaseAction {
	protected Object parameter;
	protected Command command;

	public BaseAction() {
	}

	/**
	 * 
	 * @param command
	 *            指令
	 * @param parameter
	 *            请求参数
	 */
	public BaseAction(Command command, Object parameter) {
		this.command = command;
		this.parameter = parameter;
	}

	/**
	 * 取得请求参数
	 * 
	 * @return
	 */
	public Object getParameter() {
		return parameter;
	}

	/**
	 * 设置请求参数
	 * 
	 * @param parameter
	 */
	public final void setParameter(Object parameter) {
		this.parameter = parameter;
	}

	/**
	 * 取得指令对象
	 * 
	 * @return
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * 设置指令对象
	 * 
	 * @param command
	 */
	public final void setCommand(Command command) {
		this.command = command;
	}
}