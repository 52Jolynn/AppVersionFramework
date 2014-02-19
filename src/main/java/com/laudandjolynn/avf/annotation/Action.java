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
 * 指令标记
 * </pre>
 * 
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2014-2-19 上午10:57:42
 * @copyright: avf.laudandjolynn.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {
	/**
	 * 名称需全局唯一
	 * 
	 * @return
	 */
	public String name();

	/**
	 * 应用版本号
	 * 
	 * <pre>
	 * 这里的版本号是全局版本号，默认所有指令都采用此版本号。
	 * 除非指定了refVersion或指令中显式指定了版本号
	 * </pre>
	 * 
	 * @return
	 */
	public String version();

	/**
	 * 指令实现引用版本号
	 * 
	 * <pre>
	 * 使用指定版本提供的实现。默认取version声明的值，若此处指定值，则所有指令实现都指向此版本号的实现。
	 * 若指定的版本没有提供，则按命名空间、指令名称自动搜索其前一个版本，直至找到提供指令实现的版本。
	 * 若在具体的指令中指定了refVersion，则优先使用refVersion
	 * </pre>
	 * 
	 * @return
	 */
	public String refVersion() default "";
}