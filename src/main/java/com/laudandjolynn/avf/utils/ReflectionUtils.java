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
package com.laudandjolynn.avf.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.laudandjolynn.avf.ex.ExceptionFactory;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2014-02-24 下午6:28:29
 * @copyright: avf.laudandjolynn.com
 */
public class ReflectionUtils {
	private static Logger log = LoggerFactory.getLogger(ReflectionUtils.class);

	/**
	 * @param clazz
	 * @param name
	 *            方法名称
	 * @param args
	 *            参数列表
	 * @return
	 */
	public static Method findMethod(Class<?> clazz, String name, Object... args) {
		try {
			if (args != null && args.length > 0) {
				int size = args.length;
				Class<?>[] argsClazz = new Class<?>[size];
				for (int i = 0; i < size; i++) {
					argsClazz[i] = args[i].getClass();
				}
				return clazz.getMethod(name, argsClazz);
			} else {
				return clazz.getMethod(name, new Class<?>[0]);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw ExceptionFactory
					.wrapException("can't find method " + name, e);
		}
	}

	/**
	 * 
	 * @param clazz
	 *            对象类型
	 * @param name
	 *            方法名称
	 * @param argsClazz
	 *            参数类型
	 * @return
	 */
	public static Method findMethod(Class<?> clazz, String name,
			Class<?>... argsClazz) {
		try {
			return clazz.getMethod(name, argsClazz);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw ExceptionFactory
					.wrapException("can't find method " + name, e);
		}
	}

	/**
	 * 创建实例
	 * 
	 * @param cl
	 *            类
	 * @param args
	 *            构造函数参数列表
	 * @param constructParameterType
	 *            构造函数参数类型
	 * @return
	 */
	public static Object newInstance(Class<?> cl, Object[] args,
			Class<?>... constructParameterType) {
		Object obj = null;
		try {
			Constructor<?> constructor = cl
					.getConstructor(constructParameterType);
			obj = constructor.newInstance(args);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw ExceptionFactory.wrapException(
					"can't not create instance of " + cl.getName(), e);
		}
		return obj;
	}

	/**
	 * 创建实例
	 * 
	 * @param className
	 *            类名
	 * @param args
	 *            构造函数参数列表
	 * @param constructParameterType
	 *            构造函数参数类型
	 * @return
	 */
	public static Object newInstance(String className, Object[] args,
			Class<?>... constructParameterType) {
		try {
			Class<?> cl = Class.forName(className);
			Constructor<?> constructor = cl
					.getConstructor(constructParameterType);
			Object obj = constructor.newInstance(args);
			return obj;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw ExceptionFactory.wrapException(
					"can't not create instance of " + className, e);
		}
	}
}