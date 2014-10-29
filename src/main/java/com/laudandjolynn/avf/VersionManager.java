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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jodd.io.findfile.ClassScanner;
import jodd.util.ClassLoaderUtil;
import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.laudandjolynn.avf.annotation.Action;
import com.laudandjolynn.avf.annotation.ActionCmdDefine;
import com.laudandjolynn.avf.cmd.ActionCmdWrapper;
import com.laudandjolynn.avf.cmd.ActionCommand;
import com.laudandjolynn.avf.cmd.Command;
import com.laudandjolynn.avf.ex.AvfException;
import com.laudandjolynn.avf.ex.ExceptionFactory;
import com.laudandjolynn.avf.ex.NonUniqueIdentity;
import com.laudandjolynn.avf.utils.ReflectionUtils;

/**
 * 版本管理器，支持多应用
 * 
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2014-2-19 下午3:37:46
 * @copyright: avf.laudandjolynn.com
 */
public class VersionManager {
	private final static Logger log = LoggerFactory
			.getLogger(VersionManager.class);
	private Application application = null;
	private String[] versions = null;
	private Set<String> packages = null;
	private boolean includeJars = false;
	private final ConcurrentMap<String, ActionCmdWrapper> COMMAND_COLLECTION = new ConcurrentHashMap<String, ActionCmdWrapper>();

	/**
	 * 构造函数
	 * 
	 * @param app
	 *            应用对象
	 * @param versions
	 *            应用版本号列表，从旧到新
	 */
	protected VersionManager(Application app, String[] versions) {
		this(app, versions, false);
	}

	/**
	 * 构造函数
	 * 
	 * @param app
	 *            应用对象
	 * @param versions
	 *            应用版本号列表，从旧到新
	 * @param includeJars
	 *            jar包是否有需要版本管理的指令
	 */
	protected VersionManager(Application app, String[] versions,
			boolean includeJars) {
		this.application = app;
		this.versions = versions;
		this.includeJars = includeJars;
		this.init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		this.scan();
		this.charge();
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	/**
	 * 获取指定版本的前一个版本
	 * 
	 * @param currentVersion
	 * @return 如若不存在，则返回null
	 */
	private String getPreviousVersion(String currentVersion) {
		if (StringUtil.isEmpty(currentVersion)) {
			return null;
		}
		int size = versions == null ? 0 : versions.length;
		if (size == 0) {
			throw new AvfException("必须指定应用版本列表！");
		}
		String resultVersion = null;
		for (int i = 0; i < size; i++) {
			String version = versions[i];
			if (version.equals(currentVersion)) {
				int index = i - 1;
				if (index >= 0) {
					resultVersion = versions[index];
				}
				break;
			}
		}
		if (resultVersion == null) {
			log.info("无法找到版本【" + currentVersion + "】的前一个版本，请确认其是否在应用版本列表【"
					+ Arrays.deepToString(versions) + "】中声明。");
		}
		return resultVersion;
	}

	/**
	 * 扫描注解
	 */
	private void scan() {
		ClassScanner scanner = new ClassScanner() {

			@Override
			protected void onEntry(EntryData arg0) throws Exception {
				checkAnnotation(arg0.getName());
			}

			@Override
			protected boolean acceptJar(File jarFile) {
				if (!includeJars) {
					return false;
				}
				return super.acceptJar(jarFile);
			}
		};

		File[] files = ClassLoaderUtil.getDefaultClasspath(ClassLoaderUtil
				.getDefaultClassLoader());
		scanner.setIncludeAllEntries(true);
		scanner.setIgnoreException(true);
		scanner.scan(files);
	}

	/**
	 * 
	 * @param className
	 * @throws IOException
	 */
	private void checkAnnotation(String className)
			throws ClassNotFoundException {
		Class<?> clazz = null;
		clazz = Class.forName(className);
		if (!clazz.isAnnotationPresent(Action.class)) {
			return;
		}
		if (packages == null) {
			packages = new HashSet<String>();
		}
		packages.add(clazz.getPackage().getName());
		Action action = clazz.getAnnotation(Action.class);
		Method[] methods = clazz.getMethods();
		String appName = application.getAppName();
		String appVersion = action.version();
		String globalRefVersion = action.refVersion();
		for (Method method : methods) {
			if (!method.isAnnotationPresent(ActionCmdDefine.class)) {
				continue;
			}
			ActionCmdDefine define = method
					.getAnnotation(ActionCmdDefine.class);
			String name = define.name();
			String namespace = define.namespace();
			ActionCmdWrapper wrapper = new ActionCmdWrapper(appName,
					appVersion, namespace, name);
			String key = wrapper.getKey();
			if (COMMAND_COLLECTION.containsKey(key)) {
				throw ExceptionFactory.wrapException(
						"重复声明的命令：" + wrapper.toString(), new NonUniqueIdentity(
								key + "不唯一！"));
			}

			String localRefVersion = define.refVersion();
			// 若声明了指令实现引用版本
			if (StringUtil.isNotEmpty(localRefVersion)) {
				wrapper.setRefVerion(localRefVersion);
			} else if (StringUtil.isNotEmpty(globalRefVersion)
					&& !globalRefVersion.equals(appVersion)) {
				wrapper.setRefVerion(globalRefVersion);
			}
			String invokerName = method.getName();
			wrapper.setInvokerName(invokerName);
			wrapper.setActionType(clazz);
			log.debug("found command：actionName【" + action.name()
					+ "】, command【" + wrapper.toString() + "】, key【" + key
					+ "】");
			COMMAND_COLLECTION.put(key, wrapper);
		}
	}

	/**
	 * 查找指令的引用实现
	 */
	private void charge() {
		Collection<ActionCmdWrapper> collection = COMMAND_COLLECTION.values();
		for (ActionCmdWrapper wrapper : collection) {
			String version = wrapper.getVersion();
			String refVersion = wrapper.getRefVerion();
			if (!version.equals(refVersion)) {
				String namespace = wrapper.getNamespace();
				String actionName = wrapper.getActionName();
				// 寻找最终的指令实现
				ActionCmdWrapper refWrapper = getExactVersionCmdWrapper(
						refVersion, namespace, actionName);
				if (refWrapper != null) {
					wrapper.setActionType(refWrapper.getActionType());
					wrapper.setInvokerName(refWrapper.getInvokerName());
				} else {// 找不到对应的指令实现，则从集合中删除
					String key = wrapper.getKey();
					COMMAND_COLLECTION.remove(key);
				}
			}
		}
	}

	/**
	 * 根据版本号取得命令包装对象
	 * 
	 * <pre>
	 * 若当前版本没有找到，则找其前一个版本，一直找到为止。到第一个版本若找不到，则返回null
	 * </pre>
	 * 
	 * @param appVersion
	 * @return
	 */
	private ActionCmdWrapper getExactVersionCmdWrapper(String appVersion,
			String namespace, String name) {
		String appName = application.getAppName();
		ActionCmdWrapper wrapper = new ActionCmdWrapper(appName, appVersion,
				namespace, name);
		String key = wrapper.getKey();
		if (COMMAND_COLLECTION.containsKey(key)) {
			return COMMAND_COLLECTION.get(key);
		} else {
			String preAppVersion = getPreviousVersion(appVersion);
			if (StringUtil.isNotEmpty(preAppVersion)) {
				return getExactVersionCmdWrapper(preAppVersion, namespace, name);
			}
			return null;
		}
	}

	/**
	 * 取得指定名称的命令
	 * 
	 * @param appVersion
	 *            应用版本号
	 * @param name
	 *            指令名称
	 * @return
	 */
	public Command getCommand(String appVersion, String name) {
		return getCommand(appVersion, null, name);
	}

	/**
	 * 取得指定名称的命令
	 * 
	 * @param appVersion
	 *            应用版本号
	 * @param namespace
	 *            指令命名空间
	 * @param name
	 *            指令名称
	 * @return
	 */
	public Command getCommand(String appVersion, String namespace, String name) {
		// 取得对应的指令处理对象
		String appName = application.getAppName();
		ActionCmdWrapper wrapper = new ActionCmdWrapper(appName, appVersion,
				namespace, name);
		String key = wrapper.getKey();
		if (COMMAND_COLLECTION.containsKey(key)) {
			wrapper = COMMAND_COLLECTION.get(key);
			return new ActionCommand(wrapper);
		} else {
			// 若没有提供对应版本实现的指令，则尝试递归搜索其前一个版本，直至找到该指令的实现，否则返回空
			ActionCmdWrapper refWrapper = getExactVersionCmdWrapper(appVersion,
					namespace, name);
			if (refWrapper != null) {
				// 加进指令集合中，再次调用时直接取用
				wrapper.setActionType(refWrapper.getActionType());
				wrapper.setInvokerName(refWrapper.getInvokerName());
				wrapper.setRefVerion(refWrapper.getVersion());
				COMMAND_COLLECTION.put(key, wrapper);
				log.info("found new command：" + wrapper.toString() + ", key 【"
						+ key + "】");
				return new ActionCommand(wrapper);
			}
		}
		return null;
	}

	/**
	 * 取得指令
	 * 
	 * @param name
	 * @return
	 */
	public Command getCommand(String name) {
		Command cmd = getCommand(versions[versions.length - 1], name);
		if (cmd != null) {
			return cmd;
		}
		for (String pname : packages) {
			pname = pname.replace("/", ".");
			String className = pname + "." + name;
			try {
				Command command = (Command) ReflectionUtils.newInstance(
						className, new Object[0], new Class<?>[0]);
				if (command != null) {
					return command;
				}
			} catch (Exception e) {
				continue;
			}
		}
		log.debug("could not find command: " + name);
		return null;
	}
}
