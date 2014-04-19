AppVersionFramework
===================
<h2>项目介绍</h2>
移动应用版本管理。在移动应用开发中，一定会面临APP版本升级带来的后端的实现代码版本的变化，但很多时候只是部分接口方法变更，而不是所有，因此需要一个架构统一对后端的实现 代码进行版本管理。本方案即尝试解决这个问题。目前已实现支持多应用、多版本管理，支持新、旧版本的切换。

<h2>使用说明<h2>
1、通过构建VersionManager类实现版本管理。
2、应用的需求实现，可以有两种方式，一种是直接继承AbstractCommand，重写execute方法，但使用这种方法不支持版本号，只能通过名称找到命令；二是实现BaseAction类，使用annotation标记命令信息。

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

VersionManager构造方式及使用：
```java
Application app = new Application("test", "1.0");
String[] versions = new String[] { "1.0", "1.1" };
String[] packages = new String[] { "com/laudandjolynn/avf/action1",
				"com/laudandjolynn/avf/action2" };
VersionManager vm = new VersionManager(app, versions, packages);
Command command = vm.getCommand("1.0", null, "action1");
command = vm.getCommand("1.1", null, "action1");
command = vm.getCommand("1.0", "test_namespace2", "action2");
command = vm.getCommand("1.1", "test_namespace2", "action2");
```
