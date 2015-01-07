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

import org.apache.commons.codec.binary.Base64;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2014-02-24 下午6:28:29
 * @copyright: avf.laudandjolynn.com
 */
public final class SecurityCoder {
	/**
	 * base64解码
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] base64Decoder(String data) {
		return Base64.decodeBase64(data);
	}

	/**
	 * 
	 * @param data
	 * @param urlSafe
	 * @return
	 */
	public static byte[] base64Decoder(String data, boolean urlSafe) {
		Base64 base64 = new Base64(urlSafe);
		return base64.decode(data);
	}

	/**
	 * base64编码
	 * 
	 * @param data
	 * @return
	 */
	public static String base64Encoder(byte[] data) {
		return Base64.encodeBase64String(data);
	}

	/**
	 * 
	 * @param data
	 * @param urlSafe
	 * @return
	 */
	public static String base64Encoder(byte[] data, boolean urlSafe) {
		Base64 base64 = new Base64(urlSafe);
		return base64.encodeToString(data);
	}
}
