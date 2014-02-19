package com.laudandjolynn.avf.action2;

import com.laudandjolynn.avf.BaseAction;
import com.laudandjolynn.avf.annotation.Action;
import com.laudandjolynn.avf.annotation.ActionCmdDefine;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2014-2-19 下午3:37:46
 * @copyright: avf.laudandjolynn.com
 */
@Action(name = "TestAction2", version = "1.1", refVersion = "1.0")
public class Action2 extends BaseAction {
	@ActionCmdDefine(name = "action1", refVersion = "1.1")
	public void action1() {
		System.out.println(Action2.class.getName() + ",action1");
	}

	/**
	 * 此指令指向1.0版本的，namespace="test_namespace2"的action2指令
	 */
	@ActionCmdDefine(name = "action2", namespace = "test_namespace2", refVersion = "1.0")
	public void action2() {
	}
}
