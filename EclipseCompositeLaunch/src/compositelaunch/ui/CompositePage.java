package compositelaunch.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * The CompositePage class for CompositeTab with buttons, table
 * and other useful methods 
 *
 * @author Sergey Iryupin
 * @version 0.0.1 dated Jan 5, 2017
 */
public class CompositePage extends Composite {

	private final String ADD_BUTTON = "Add...";
	private final String ADD_BUTTON_TIP = "Click to choice configuration for adding to list";
	private final String DEL_BUTTON = "Delete";
	private final String DEL_BUTTON_TIP = "Click to delete configuration from the list";
	private final int NUM_OF_COLUMNS = 2; // columns for GridLayout
	private final int WITH_BUTTON = 80;
	private final String TABLE_1_COLUMN = "Name";
	private final String TABLE_2_COLUMN = "Type";
	private final String NO_CONFIGURATION = "No configurations to choose/add";

	private Button addConfiguration;
	private Button deleteConfiguration;
	private Table table;
	private Menu menuChoiceConfiguration;

	private final String CONGIF_LIST = "ConfigurationList";
	private List<String> configs;

	private ITableChangedListener changedListener;

	/*
	 * Constructor: buttons + table
	 */
	public CompositePage(Composite parent) {
		super(parent, SWT.MULTI);

		this.setLayout(new GridLayout(NUM_OF_COLUMNS, false));
		GridData gridButton = new GridData();
		gridButton.widthHint = WITH_BUTTON;

		addConfiguration = new Button(this, SWT.NONE);
		addConfiguration.setText(ADD_BUTTON);
		addConfiguration.setToolTipText(ADD_BUTTON_TIP);
		addConfiguration.setLayoutData(gridButton);
		addConfiguration.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				choiceConfigurationToAdd();
				menuChoiceConfiguration.setVisible(true);
			}
		});

		deleteConfiguration = new Button(this, SWT.NONE);
		deleteConfiguration.setText(DEL_BUTTON);
		deleteConfiguration.setToolTipText(DEL_BUTTON_TIP);
		deleteConfiguration.setLayoutData(gridButton);
		deleteConfiguration.setEnabled(false);
		deleteConfiguration.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionIndex() > -1) {
					table.remove(table.getSelectionIndex());
					deleteConfiguration.setEnabled(false);
					changedConfig();
				}
			}
		});

		new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		GridData gridTable = new GridData(GridData.FILL_BOTH);
		gridTable.horizontalSpan = 2;

		table = new Table(this, SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(gridTable);
		TableColumn columnName = new TableColumn(table, SWT.NULL);
		columnName.setText(TABLE_1_COLUMN);
		columnName.pack();
		TableColumn columnType = new TableColumn(table, SWT.NULL);
		columnType.setText(TABLE_2_COLUMN);
		columnType.pack();
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteConfiguration.setEnabled(true);
			}
		});
	}

	/*
	 * Set my changedListener for table
	 */
	public void setChangedListener(ITableChangedListener changedListener) {
		this.changedListener = changedListener;
	}

	/*
	 * Send signal for my changedListener
	 */
	public void changedConfig() {
		if (changedListener != null) changedListener.handler();
	}

	/*
	 * Returns the name of attribute to save/load configuration list
	 */
	public String getConfigListName() {
		return CONGIF_LIST;
	}

	/*
	 * Expand the configuration list to the table
	 */
	public void setConfigList(ILaunchConfiguration configuration) {
		try {
			configs = configuration.getAttribute(CONGIF_LIST, (List<String>)null);
			table.removeAll();
			for(String item : configs) {
				TableItem tableItem = new TableItem(table, SWT.NULL);
				tableItem.setText(item);
				tableItem.setText(1, getTypeOfConfiguration(item));
				table.getColumn(0).pack();
				table.getColumn(1).pack();
			}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}
	}
	
	/*
	 * Get type of configuration by the name
	 */
	private String getTypeOfConfiguration(String name) {
		String type = "<unknown>";
		try {
			for (ILaunchConfiguration config : DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations()) {
				if (name.equals(config.getName())) {
					type = config.getType().getName();
					break;
				}
			}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}
		return type;
	}

	/*
	 * Prepare configuration list for save
	 */
	public List<String> getConfigList() {
		configs = new ArrayList<String>();
		for (TableItem item : table.getItems()) configs.add(item.getText());
		return configs;
	}

	/*
	 * Make menu for selection/addition configuration to table
	 */
	private void choiceConfigurationToAdd() {

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

				MenuItem menuItemConfig = new MenuItem(menuItem.getMenu(), SWT.NONE);
				menuItemConfig.setText(config.getName());
				menuItemConfig.setData(config);
				menuItemConfig.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						MenuItem item = MenuItem.class.cast(e.getSource());
						TableItem tableItem = new TableItem(table, SWT.NULL);
						tableItem.setText(item.getText());
						table.getColumn(0).pack();
						try {
							tableItem.setText(1, ((ILaunchConfiguration) item.getData()).getType().getName());
						} catch (CoreException ex) {
							ex.printStackTrace();
						}
						table.getColumn(1).pack();
						changedConfig();
					}
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});
			}
		} catch (CoreException ex) {
			ex.printStackTrace();
		}

		if (menuChoiceConfiguration.getItemCount() == 0) {
			MenuItem menuItem = new MenuItem(menuChoiceConfiguration, SWT.NONE);
			menuItem.setText(NO_CONFIGURATION);
		}
	}

}
