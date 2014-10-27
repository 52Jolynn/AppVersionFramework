package com.laudandjolynn.avf;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2014年10月27日 上午9:39:07
 * @copyright: www.laudandjolynn.com
 */
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
public class VersionManagerFactory {
	/**
	 * 获取VersionManager
	 * 
	 * @param app
	 * @param versions
	 *            应用版本号列表，从旧到新
	 * @param packages
	 *            包名列表，形如com/laudandjolynn/avf
	 * @return
	 */
	public static VersionManager getVersionManager(Application app,
			String[] versions, String[] packages) {
		return new VersionManager(app, versions, packages);
	}
}
