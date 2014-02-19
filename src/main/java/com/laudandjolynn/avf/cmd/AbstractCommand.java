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

import com.laudandjolynn.avf.VersionManager;

/**
 * 抽象命令类
 * 
 * @author Laud
 * 
 */
public abstract class AbstractCommand implements Command {
	private VersionManager appService = null;

	public AbstractCommand(VersionManager appService) {
		this.appService = appService;
	}

	/**
	 * 取得指令
	 * 
	 * @param appName
	 * @param appVersion
	 * @param namespace
	 * @param name
	 * @return
	 */
	public Command getCommand(String appName, String appVersion,
			String namespace, String name) {
		Command cmd = appService.getCommand(appName, appVersion, namespace,
				name);
		return cmd;
	}

	/**
	 * 取得指令
	 * 
	 * @param name
	 * @return
	 */
	public Command getCommand(String name) {
		Command cmd = appService.getCommand(name);
		return cmd;
	}
}