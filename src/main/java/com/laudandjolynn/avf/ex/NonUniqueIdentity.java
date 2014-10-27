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
package com.laudandjolynn.avf.ex;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2014-02-19 下午6:14:14
 * @copyright: www.laudandjolynn.com
 */
public class NonUniqueIdentity extends RuntimeException {
	private static final long serialVersionUID = -6887409453281025959L;

	public NonUniqueIdentity(String message) {
		super(message);
	}
}
