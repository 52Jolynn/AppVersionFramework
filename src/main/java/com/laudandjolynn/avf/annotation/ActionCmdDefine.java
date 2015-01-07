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
package com.laudandjolynn.avf.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 指令注解
 * 用于标记某个方法为声明的指令
 * </pre>
 * 
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2014-02-19 上午10:55:37
 * @copyright: avf.laudandjolynn.com
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionCmdDefine {
	/**
	 * 指令名称
	 * 
	 * <pre>
	 * 建议以大写字母开头
	 * </pre>
	 * 
	 * @return
	 */
	public String name();

	/**
	 * 命名空间
	 * 
	 * <pre>
	 * 采用与java的package相同的命名标准。
	 * 如com.laudandjolynn.avf.GetUser
	 * 其中com.laudandjolynn.avf是命名空间
	 * GetUser是指令名称
	 * </pre>
	 * 
	 * @return
	 */
	public String namespace() default "";

	/**
	 * 引用版本号
	 * 
	 * <pre>
	 * 当新版本需要使用某个旧版本实现时，可以指定引用版本号，只需提供方法，方法体中不需要写任何实现。
	 * 若指定的版本找不到实现，则按命名空间、指令名称自动搜索其前一个版本
	 * 直至找到提供指令实现的版本。
	 * </pre>
	 * 
	 * @return
	 */
	public String refVersion() default "";
}