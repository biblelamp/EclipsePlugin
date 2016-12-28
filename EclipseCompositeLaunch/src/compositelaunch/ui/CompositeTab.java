package compositelaunch.ui;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import compositelaunch.activator.Activator;

/**
 * The CompositeTab class for tab with configuration' list for launch
 */
public class CompositeTab extends AbstractLaunchConfigurationTab {

	public static final String NAME = "Group";
	private static final String ICON = "icons/launch.png";

	/*
	 * Add the desired components in the tab
	 */
	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub
	}

	/*
	 * Initialize the components
	 */
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		// TODO Auto-generated method stub
	}

	/*
	 * Processing pressing the Apply button
	 */
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub
	}

	// Name of tab
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Image getImage() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, ICON).createImage();
	}
	
}