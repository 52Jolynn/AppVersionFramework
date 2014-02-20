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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.laudandjolynn.avf.ex.ExceptionFactory;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2012-9-24 下午6:28:29
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
			throw ExceptionFactory.wrapException("没有找到指定方法!", e);
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
			throw ExceptionFactory.wrapException("没有找到指定方法!", e);
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
			throw ExceptionFactory.wrapException("创建" + cl.getName() + "实例失败!",
					e);
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
			throw ExceptionFactory.wrapException("创建" + className + "实例失败!", e);
		}
	}

	/**
	 * 获得指定类的父类，父类的父类……
	 * 
	 * @param result
	 *            结果集
	 * @param c
	 * @return
	 */
	public static Class<?>[] getSuperclassRecursion(Class<?>[] result,
			Class<?> c) {
		Class<?> superClass = c.getSuperclass();
		if (superClass == Object.class) {
			return result;
		}
		if (result == null) {
			result = new Class<?>[] { superClass };
		} else {
			int size = result.length;
			result = Arrays.copyOf(result, size + 1);
			result[size] = superClass;
		}
		return getSuperclassRecursion(result, superClass);
	}

	/**
	 * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
	 */
	public static void setFieldValue(final Object object,
			final String fieldName, final Object value) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null)
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");
		makeAccessible(field);
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
	 */
	public static Object getFieldValue(final Object object,
			final String fieldName) {
		Field field = getDeclaredField(object, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + object + "]");
		}
		makeAccessible(field);
		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 直接调用对象方法,无视private/protected修饰符.
	 */
	public static Object invokeMethod(final Object object,
			final String methodName, final Class<?>[] parameterTypes,
			final Object[] parameters) throws InvocationTargetException {
		Method method = getDeclaredMethod(object, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method ["
					+ methodName + "] on target [" + object + "]");
		}
		method.setAccessible(true);
		try {
			return method.invoke(object, parameters);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 */
	protected static Field getDeclaredField(final Object object,
			final String fieldName) {
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 */
	protected static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * 循环向上转型,获取对象的DeclaredMethod.
	 */
	protected static Method getDeclaredMethod(Object object, String methodName,
			Class<?>[] parameterTypes) {
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * 通过反射,获得Class定义中声明的父类的泛型参数的类型. eg. public UserDao extends
	 * HibernateDao<User>
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be
	 *         determined
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得Class定义中声明的父类的泛型参数的类型. eg. public UserDao extends
	 * HibernateDao<User>
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be
	 *         determined
	 */
	@SuppressWarnings("rawtypes")
	public static Class getSuperClassGenricType(final Class clazz,
			final int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			log.warn(clazz.getSimpleName()
					+ "'s superclass not ParameterizedType");
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			log.warn("Index: " + index + ", Size of " + clazz.getSimpleName()
					+ "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			log.warn(clazz.getSimpleName()
					+ " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class) params[index];
	}

	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static IllegalArgumentException convertToUncheckedException(
			Exception e) {
		if (e instanceof IllegalAccessException
				|| e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException)
			return new IllegalArgumentException("Refelction Exception.", e);
		else
			return new IllegalArgumentException(e);
	}
}