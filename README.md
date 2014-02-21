AppVersionFramework
===================

应用版本管理，支持多应用、多版本管理，支持新版本使用旧版本的实现。

使用说明：
1、通过构建VersionManager类实现版本管理。
2、应用的需求实现，可以有两种方式，一种是直接继承AbstractCommand，重写execute方法；二是实现BaseAction类，使用annotation标记命令信息。

示例1：
```java
package com.laudandjolynn.avf.action1;

import com.laudandjolynn.avf.cmd.AbstractCommand;
import com.laudandjolynn.avf.cmd.Parameter;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2014-2-19 下午3:37:46
 * @copyright: avf.laudandjolynn.com
 * 
 */
public class TestCommand extends AbstractCommand {

	@Override
	public Object execute(Parameter parameter) {
	  //具体实现
		return null;
	}

}
```

示例2：
```java
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
```

VersionManager构造方式：
```java
Application app = new Application("test", "1.0");
String[] versions = new String[] { "1.0", "1.1" };
String[] packages = new String[] { "com/laudandjolynn/avf/action1",
				"com/laudandjolynn/avf/action2" };
VersionManager vm = new VersionManager(app, versions, packages);
```
