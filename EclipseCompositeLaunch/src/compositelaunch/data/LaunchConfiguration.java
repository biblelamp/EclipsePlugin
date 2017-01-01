package compositelaunch.data;

import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;

public class LaunchConfiguration {

	private ILaunchConfiguration configuration;
	private List<ILaunchConfiguration> attachedConfiguration;

	public ILaunchConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(ILaunchConfiguration configuration) {
		this.configuration = configuration;
	}

	public void addAttachedConfiguration(ILaunchConfiguration configuration) {
		attachedConfiguration.add(configuration);
	}

	public void delAttachedConfiguration(ILaunchConfiguration configuration) {
		attachedConfiguration.remove(configuration);
	}

}