package compositelaunch.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import compositelaunch.activator.Activator;
import compositelaunch.data.LaunchConfiguration;

/**
 * The CompositeTab class for tab with list of configurations for launch,
 * overrides the general functionality of the tab
 *
 * @author Sergey Iryupin
 * @version 0.0.5 dated Jan 2, 2017
 */
public class CompositeTab extends AbstractLaunchConfigurationTab {

	private final String NAME_OF_TAB = "Group";
	private final String ICON_TAB = "icons/launch.png";
	private final String ADD_BUTTON = "Add...";
	private final String ADD_BUTTON_TIP = "Click to choice configuration for adding to list";
	private final String DEL_BUTTON = "Delete";
	private final String DEL_BUTTON_TIP = "Click to delete configuration from the list";
	private final int NUM_OF_COLUMNS = 2;
	private final int WITH_OF_BUTTON = 80;

	private LaunchConfiguration launchConfiguration = new LaunchConfiguration();

	private Button addConfiguration;
	private Button deleteConfiguration;
	private Table table;

	Menu menuChoiceConfiguration;

	/*
	 * Add the desired components on the tab
	 */
	@Override
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);

		composite.setLayout(new GridLayout(NUM_OF_COLUMNS, false));
		GridData gridButton = new GridData();
		gridButton.widthHint = WITH_OF_BUTTON;

		addConfiguration = createPushButton(composite, ADD_BUTTON, null);
		addConfiguration.setToolTipText(ADD_BUTTON_TIP);
		addConfiguration.setLayoutData(gridButton);
		addConfiguration.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				TableItem item = new TableItem(table, SWT.NULL); // for debugging
				item.setText("Item " + table.getItemCount());

				//choiceConfigurationToAdd();
				//menuChoiceConfiguration.setVisible(true);
				updateLaunchConfigurationDialog();
			}
		});

		deleteConfiguration = createPushButton(composite, DEL_BUTTON, null);
		deleteConfiguration.setToolTipText(DEL_BUTTON_TIP);
		deleteConfiguration.setLayoutData(gridButton);
		deleteConfiguration.setEnabled(false);
		deleteConfiguration.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (table.getSelectionIndex() > -1) {
					table.remove(table.getSelectionIndex());
					deleteConfiguration.setEnabled(false);
					updateLaunchConfigurationDialog();
				}
			}
		});

		createSeparator(composite, NUM_OF_COLUMNS);

		GridData gridTable = new GridData(GridData.FILL_BOTH);
		gridTable.horizontalSpan = NUM_OF_COLUMNS;

		table = new Table(composite, SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLayoutData(gridTable);
		TableColumn columnName = new TableColumn(table, SWT.NULL);
		columnName.setText("Configuration's Name");
		columnName.pack();
		TableColumn columnType = new TableColumn(table, SWT.NULL);
		columnType.setText("Configuration's Type");
		columnType.pack();
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				deleteConfiguration.setEnabled(true);
			}
		});
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	/*
	 * Initialize the components
	 */
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		launchConfiguration.setConfiguration(configuration);
		System.out.println("initializeFrom");
	}

	/*
	 * Processing pressing the Apply button
	 */
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		System.out.println("performApply");
	}

	// Name of tab
	@Override
	public String getName() {
		return NAME_OF_TAB;
	}

	// Image for tab
	@Override
	public Image getImage() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, ICON_TAB).createImage();
	}

	// Choice configuration for adding in table
	void choiceConfigurationToAdd() {

		if (menuChoiceConfiguration != null)
			menuChoiceConfiguration.dispose();
		menuChoiceConfiguration = new Menu(addConfiguration);
		try {
			for (ILaunchConfiguration config : DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations()) {
				String configType = config.getType().getName();

				MenuItem menuItem = null;
				for (MenuItem item : menuChoiceConfiguration.getItems()) {
					if (configType.equals(item.getText())) {
						menuItem = item;
						break;
					}
				}

				if (menuItem == null) {
					menuItem = new MenuItem(menuChoiceConfiguration, SWT.CASCADE);
					menuItem.setText(configType);

					Menu subMenuItem = new Menu(menuItem);
					menuItem.setMenu(subMenuItem);
				}

				MenuItem menuItem2 = new MenuItem(menuItem.getMenu(), SWT.NONE);
				menuItem2.setText(config.getName());
				menuItem2.setData(config);
			}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}

		if (menuChoiceConfiguration.getItemCount() == 0) {
			MenuItem menuItem = new MenuItem(menuChoiceConfiguration, SWT.NONE);
			menuItem.setText("No configurations that can be added");
		}
	}

}