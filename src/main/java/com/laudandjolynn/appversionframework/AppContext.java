package com.laudandjolynn.appversionframework;

/**
 * @author: huangtiande
 * @email:htd0324@gmail.com
 * @date: 2013年10月9日
 */

public class AppContext {
	private String appName;
	private String version;

	public AppContext(String appName, String version) {
		this.appName = appName;
		this.version = version;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
