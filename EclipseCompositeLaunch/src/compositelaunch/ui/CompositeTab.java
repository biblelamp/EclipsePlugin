package compositelaunch.ui;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import compositelaunch.activator.Activator;
import compositelaunch.core.CompositeTools;

/**
 * The CompositeTab class for tab with list of configurations for launch,
 * overrides the general functionality of the tab
 *
 * @author Sergey Iryupin
 * @version 0.0.10 dated Jan 8, 2017
 */
public class CompositeTab extends AbstractLaunchConfigurationTab {

	private final String NAME_OF_TAB = "Configurations List";
	private final String ICON_TAB = "icons/launch.png";

	private CompositePage compositePage;

	@Override
	public void createControl(Composite parent) {
		compositePage = new CompositePage(parent);
		compositePage.setChangedListener(new ITableChangedListener() {
			@Override
			public void handler() {
				setDirty(true);
				updateLaunchConfigurationDialog();
			}
		});
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		compositePage.setConfigList(configuration);
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(CompositeTools.getConfigListName(), compositePage.getConfigList());
	}

	@Override
	public boolean isValid(ILaunchConfiguration configuration) {
		return compositePage.isValid();
	}

	@Override
	public String getName() {
		return NAME_OF_TAB; // name of tab
	}

	@Override
	public Image getImage() {
		return Activator.getImageDescriptor(ICON_TAB).createImage(); // image for tab
	}

	@Override
	public Control getControl() {
		return compositePage; // get control elements
	}

}