package com.laudandjolynn.avf.action1;

import com.laudandjolynn.avf.BaseAction;
import com.laudandjolynn.avf.annotation.Action;
import com.laudandjolynn.avf.annotation.ActionCmdDefine;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2014-2-19 下午3:37:46
 * @copyright: avf.laudandjolynn.com
 */
@Action(name = "TestAction1", version = "1.0")
public class Action1 extends BaseAction {
	@ActionCmdDefine(name = "action1")
	public void action1() {
		System.out.println(Action1.class.getName() + ",action1");
	}

	@ActionCmdDefine(name = "action2", namespace = "test_namespace2")
	public void action2() {
		System.out.println(Action1.class.getName()
				+ ",test_namespace2.action2");
	}
}
