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

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.paoding.rose.scanning.vfs.FileObject;
import net.paoding.rose.scanning.vfs.FileSystemManager;
import net.paoding.rose.scanning.vfs.FileType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

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
	private String[] packages = null;
	private final ConcurrentMap<String, ActionCmdWrapper> COMMAND_COLLECTION = new ConcurrentHashMap<String, ActionCmdWrapper>();

	/**
	 * 构造函数
	 * 
	 * @param app
	 * @param versions
	 *            应用版本号列表，从旧到新
	 * @param packages
	 *            包名列表，形如com/laudandjolynn/avf
	 */
	public VersionManager(Application app, String[] versions, String[] packages) {
		this.application = app;
		this.versions = versions;
		this.packages = packages;
		scanAnnotation();
		takeChargeOfActionCmd();
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
		if (StringUtils.isEmpty(currentVersion)) {
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
	private void scanAnnotation() {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = null;
		int size = packages == null ? 0 : packages.length;
		if (size == 0) {
			return;
		}
		for (int i = 0; i < size; i++) {
			String BASE_PACKAGE = packages[i];
			try {
				resources = resourcePatternResolver.getResources("classpath*:"
						+ BASE_PACKAGE);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				throw ExceptionFactory.wrapException(
						"find annotation file error！", e);
			}

			FileSystemManager fsm = new FileSystemManager();
			for (Resource res : resources) {
				try {
					URI uri = res.getURI();
					FileObject rootObject = fsm.resolveFile(uri.toString());
					checkClasses(rootObject, rootObject, BASE_PACKAGE);
				} catch (IOException e) {
					log.error(e.getMessage());
					throw ExceptionFactory.wrapException("解析类文件出错！", e);
				}
			}
		}
	}

	/**
	 * 检查指令
	 * 
	 * @param fileObject
	 */
	private void checkClasses(FileObject rootObject, FileObject fileObject,
			String BASE_PACKAGE) throws IOException {
		FileObject[] children = fileObject.getChildren();
		for (FileObject child : children) {
			FileType fileType = child.getType();
			boolean hasChildren = fileType.hasChildren();
			boolean hasContent = fileType.hasContent();
			if (hasChildren) {
				checkClasses(rootObject, child, BASE_PACKAGE);
			} else if (hasContent && !hasChildren) {
				checkAnnotation(rootObject, fileObject, child, BASE_PACKAGE);
			}
		}
	}

	/**
	 * 
	 * @param rootObject
	 *            根目录
	 * @param currentObject
	 *            当前目录
	 * @param resource
	 *            当前资源
	 * @param BASE_PACKAGE
	 *            包名
	 * @throws IOException
	 */
	private void checkAnnotation(FileObject rootObject,
			FileObject currentObject, FileObject resource, String BASE_PACKAGE)
			throws IOException {
		String className = rootObject.getName().getRelativeName(
				resource.getName());
		if (!className.endsWith(".class")) {
			return;
		}
		className = StringUtils.removeEnd(className, ".class");
		className = BASE_PACKAGE + "/" + className;
		className = className.replace('/', '.');
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
			throw ExceptionFactory.wrapException("", e);
		}
		if (clazz.isAnnotationPresent(Action.class)) {
			Action action = clazz.getAnnotation(Action.class);
			Method[] methods = clazz.getMethods();
			String appName = application.getAppName();
			String appVersion = action.version();
			String globalRefVersion = action.refVersion();
			for (Method method : methods) {
				if (method.isAnnotationPresent(ActionCmdDefine.class)) {
					ActionCmdDefine define = method
							.getAnnotation(ActionCmdDefine.class);
					String name = define.name();
					String namespace = define.namespace();
					ActionCmdWrapper wrapper = new ActionCmdWrapper(appName,
							appVersion, namespace, name);
					String key = wrapper.getKey();
					if (COMMAND_COLLECTION.containsKey(key)) {
						throw ExceptionFactory.wrapException("重复声明的命令："
								+ wrapper.toString(), new NonUniqueIdentity(key
								+ "不唯一！"));
					}

					String localRefVersion = define.refVersion();
					// 若声明了指令实现引用版本
					if (StringUtils.isNotEmpty(localRefVersion)) {
						wrapper.setRefVerion(localRefVersion);
					} else if (StringUtils.isNotEmpty(globalRefVersion)
							&& !globalRefVersion.equals(appVersion)) {
						wrapper.setRefVerion(globalRefVersion);
					}
					String invokerName = method.getName();
					wrapper.setInvokerName(invokerName);
					wrapper.setActionType(clazz);
					log.debug("found command：actionName【" + action.name()
							+ "】, command【" + wrapper.toString() + "】, key【"
							+ key + "】");
					COMMAND_COLLECTION.put(key, wrapper);
				}
			}
		}
	}

	/**
	 * 查找指令的引用实现
	 */
	private void takeChargeOfActionCmd() {
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
			if (StringUtils.isNotEmpty(preAppVersion)) {
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
		int len = packages == null ? 0 : packages.length;
		if (len == 0) {
			log.debug("could not find command: " + name);
			return null;
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
		return null;
	}
}
