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

import com.laudandjolynn.avf.utils.SecurityCoder;

/**
 * 指令包装器
 * 
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2014-02-19 上午10:55:37
 * @copyright: avf.laudandjolynn.com
 */
public class ActionCmdWrapper {
	private String appName;
	private String version;
	private String namespace;
	private String actionName;
	private Class<?> actionType;
	private String invokerName;
	private String refVersion;

	/**
	 * 默认构造函数
	 */
	public ActionCmdWrapper() {
	}

	/**
	 * 构造函数
	 * 
	 * @param appName
	 *            应用名称
	 * @param appVersion
	 *            应用版本号
	 * @param namespace
	 *            指令命名空间
	 * @param name
	 *            指令名称
	 */
	public ActionCmdWrapper(String appName, String appVersion,
			String namespace, String name) {
		this(appName, appVersion, namespace, name, null, null);
	}

	/**
	 * 构造函数
	 * 
	 * @param appName
	 *            应用名称
	 * @param version
	 *            应用版本号
	 * @param namespace
	 *            指令命名空间
	 * @param actionName
	 *            指令名称
	 * @param actionType
	 *            指令所属类
	 * @param invokerName
	 *            指令执行方法名
	 */
	public ActionCmdWrapper(String appName, String version, String namespace,
			String actionName, Class<?> actionType, String invokerName) {
		this.appName = appName;
		this.version = version;
		this.namespace = namespace;
		this.actionName = actionName;
		this.actionType = actionType;
		this.invokerName = invokerName;
		this.refVersion = version;
	}

	/**
	 * 取得应用名字
	 * 
	 * @return
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * 设置应用名字
	 * 
	 * @param appName
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * 取得应用版本号
	 * 
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 设置应用版本号
	 * 
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 取得指令命名空间
	 * 
	 * @return
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * 设置指令命名空间
	 * 
	 * @param namespace
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * 取得指令名称
	 * 
	 * @return
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * 设置指令名称
	 * 
	 * @param name
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * 取得指令所属类类型
	 * 
	 * @return
	 */
	public Class<?> getActionType() {
		return actionType;
	}

	/**
	 * 设置指令所属类类型
	 * 
	 * @param actionType
	 */
	public void setActionType(Class<?> actionType) {
		this.actionType = actionType;
	}

	/**
	 * 取得指令执行方法名称
	 * 
	 * @return
	 */
	public String getInvokerName() {
		return invokerName;
	}

	/**
	 * 设置指令执行方法名称
	 * 
	 * @param invokerName
	 */
	public void setInvokerName(String invokerName) {
		this.invokerName = invokerName;
	}

	/**
	 * 取得指令实现版本号
	 * 
	 * @return
	 */
	public String getRefVerion() {
		return refVersion;
	}

	/**
	 * 设置指令实现版本号
	 * 
	 * @param refVerion
	 */
	public void setRefVerion(String refVersion) {
		this.refVersion = refVersion;
	}

	@Override
	public String toString() {
		String string = "[appName=" + (appName == null ? "" : appName)
				+ ", version=" + (version == null ? "" : version)
				+ ", namespace=" + (namespace == null ? "" : namespace)
				+ ", actionName=" + (actionName == null ? "" : actionName)
				+ ", refVersion=" + (refVersion == null ? "" : refVersion)
				+ "]";
		return string;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appName == null) ? 0 : appName.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		result = prime * result
				+ ((actionName == null) ? 0 : actionName.hashCode());
		result = prime * result
				+ ((namespace == null) ? 0 : namespace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ActionCmdWrapper wrapper = (ActionCmdWrapper) obj;
		// 判断应用名
		if (appName == null) {
			if (wrapper.appName != null) {
				return false;
			}
		} else if (!appName.equals(wrapper.appName)) {
			return false;
		}
		// 判断版本号
		if (version == null) {
			if (wrapper.version != null) {
				return false;
			}
		} else if (!version.equals(wrapper.version)) {
			return false;
		}
		// 判断指令名
		if (actionName == null) {
			if (wrapper.actionName != null) {
				return false;
			}
		} else if (!actionName.equals(wrapper.actionName)) {
			return false;
		}
		// 判断命名空间
		if (namespace == null) {
			if (wrapper.namespace != null) {
				return false;
			}
		} else if (!namespace.equals(wrapper.namespace)) {
			return false;
		}
		return true;
	}

	public String getKey() {
		String sourceKey = "appName=" + (appName == null ? "" : appName)
				+ ", version=" + (version == null ? "" : version)
				+ ", namespace=" + (namespace == null ? "" : namespace)
				+ ", actionName=" + (actionName == null ? "" : actionName) + "";
		return SecurityCoder.base64Encoder(sourceKey.getBytes());
	}
}
