package com.laudandjolynn.avf;

import com.laudandjolynn.avf.cmd.Command;

import junit.framework.TestCase;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2014-2-19 下午3:37:46
 * @copyright: avf.laudandjolynn.com
 */
public class TestAction extends TestCase {
	private VersionManager vm = null;

	@Override
	protected void setUp() throws Exception {
		Application app = new Application("test", "1.0");
		String[] versions = new String[] { "1.0", "1.1" };
		String[] packages = new String[] { "com/laudandjolynn/avf/action1",
				"com/laudandjolynn/avf/action2" };
		this.vm = new VersionManager(app, versions, packages);
	}

	public void testAction() {
		Command command = vm.getCommand("TestCommand");
		assertNotNull(command);

		command = vm.getCommand("action1");
		assertNotNull(command);

		command = vm.getCommand("1.0", null, "action1");
		assertNotNull(command);

		command = vm.getCommand("1.1", null, "action1");
		assertNotNull(command);

		command = vm.getCommand("1.0", null, "action2");
		assertNull(command);

		command = vm.getCommand("1.1", null, "action2");
		assertNull(command);

		command = vm.getCommand("1.0", "test_namespace2", "action2");
		assertNotNull(command);

		command = vm.getCommand("1.1", "test_namespace2", "action2");
		assertNotNull(command);
	}
}
