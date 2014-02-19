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
 * @date: 2012-12-28 下午4:42:36
 * @copyright: armisi.cn
 */
public class AvfException extends RuntimeException {
	private static final long serialVersionUID = 5587815705327896167L;
	private int msgCode;

	public AvfException() {
	}

	public AvfException(String msg) {
		super(msg);
	}

	/**
	 * 
	 * @param msgCode
	 *            消息码
	 * @param msg
	 */
	public AvfException(int msgCode, String msg) {
		super(msg);
		this.msgCode = msgCode;
	}

	/**
	 * 
	 * @param e
	 */
	public AvfException(Throwable e) {
		super(e);
	}

	/**
	 * 
	 * @param msg
	 * @param e
	 */
	public AvfException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * 设置消息码
	 * 
	 * @return
	 */
	public int getMsgCode() {
		return msgCode;
	}

	/**
	 * 取得消息码
	 * 
	 * @param msgCode
	 */
	public void setMsgCode(int msgCode) {
		this.msgCode = msgCode;
	}
}
