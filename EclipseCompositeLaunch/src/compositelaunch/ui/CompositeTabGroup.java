package compositelaunch.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

/**
 * The CompositeTabGroup class for working with tabs
 */
public class CompositeTabGroup extends AbstractLaunchConfigurationTabGroup {

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {

		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new CompositeTab(),
				new CommonTab()
		};
		setTabs(tabs);

	}

}