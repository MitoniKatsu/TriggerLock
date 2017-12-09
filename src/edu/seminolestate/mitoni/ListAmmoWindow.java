/*
 * Written by Christian Lundblad
 * December 1, 2017
 * This class contains the List Ammo Window, and related methods and event handlers
 */
package edu.seminolestate.mitoni;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

public class ListAmmoWindow
{
	
	protected JInternalFrame frmListAmmo;
	
	private DefaultTableModel model;
	private JTable tblAmmo;
	private JMenuItem mntmEditAmmo;
	private JMenuItem mntmDeleteAmmo;

	public ListAmmoWindow()
	{
		//build window components
		frmListAmmo = new JInternalFrame("Ammo List");
		frmListAmmo.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmListAmmo.setClosable(true);
		frmListAmmo.setBounds(50, 50, 600, 400);
		frmListAmmo.getContentPane().setLayout(null);
			//context menu
		JPopupMenu ctxtManufacturerList = new JPopupMenu();
		mntmEditAmmo = new  JMenuItem("Edit Selected");
		ctxtManufacturerList.add(mntmEditAmmo);
		JSeparator ctxtSeparator = new JSeparator();
		ctxtManufacturerList.add(ctxtSeparator);
		mntmDeleteAmmo = new JMenuItem("Delete Selected");
		ctxtManufacturerList.add(mntmDeleteAmmo);
		
		tblAmmo = new JTable();
		tblAmmo.setBounds(new Rectangle(0, 0, 200, 400));
		addPopup(tblAmmo, ctxtManufacturerList);
		
		JScrollPane scrlPnManf = new JScrollPane();
		scrlPnManf.setLocation(0, 0);
		scrlPnManf.setSize(584, 371);
		scrlPnManf.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frmListAmmo.getContentPane().add(scrlPnManf);
		scrlPnManf.setViewportView(tblAmmo);		
		
		//event handlers
		frmListAmmo.addComponentListener(new ComponentListener()
		{
			
			public void componentShown(ComponentEvent e)
			{
				QueryAmmo newQuery = new QueryAmmo(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
				
				try
				{
					model = buildTableModel(newQuery.listAmmo());
					tblAmmo.setModel(model);
				}
				catch (SQLException ex)
				{
					JOptionPane.showMessageDialog(null, "Unable to load Ammo List. Please see your system admin.");
				}
				finally 
				{
					newQuery.closeSQL();
				}
			}

			@Override
			public void componentHidden(ComponentEvent e)
			{
				model.setRowCount(0);
			}

			@Override
			public void componentMoved(ComponentEvent e)
			{

			}

			@Override
			public void componentResized(ComponentEvent e)
			{

			}
			
			
		});
		
		mntmEditAmmo.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String model = (String) tblAmmo.getModel().getValueAt(tblAmmo.getSelectedRow(), 1);
				
				QueryAmmo newQuery = new QueryAmmo(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);					
				ResultSet loadRS = newQuery.loadEditAmmo(model);
				//parse results to edit window
				MainApplication.editAmmoWindow.parseLoadAmmo(loadRS);
				//load edit window
				MainApplication.editAmmoWindow.frmEditAmmo.setVisible(true);
				newQuery.closeSQL();
				frmListAmmo.setVisible(false);
			}

		});
		
		mntmDeleteAmmo.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				String model = (String) tblAmmo.getModel().getValueAt(tblAmmo.getSelectedRow(), 1);
				
				int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete "+ model
						+"? This operation cannot be reversed!", "Confirm Delete", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.YES_OPTION)
				{
					QueryAmmo newQuery = new QueryAmmo(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);					
					newQuery.deleteAmmo(model);
					frmListAmmo.setVisible(false);
				}
				else
				{
					// Do nothing
				}
			}
		});

	}
	
	//public methods
	
	private void addPopup(Component component, final JPopupMenu popup)
	{
		component.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e)
			{
				if (tblAmmo.getSelectedRow() != -1)
				{
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}
	
	public DefaultTableModel buildTableModel(ResultSet rs) throws SQLException
	{
		// get metadata of results
		ResultSetMetaData rsmd = rs.getMetaData();

		// populate column names
		Vector<String> columns = new Vector<String>();
		int countColumns = rsmd.getColumnCount();
		for (int col = 1; col <= countColumns; col++)
		{
			columns.add(rsmd.getColumnName(col));
		}

		// populate rows
		Vector<Vector<Object>> tableData = new Vector<Vector<Object>>();
		while (rs.next())
		{
			Vector<Object> vector = new Vector<Object>();
			for (int colIndex = 1; colIndex <= countColumns; colIndex++)
			{
				vector.add(rs.getObject(colIndex));
			}
			tableData.add(vector);
		}
		
		return new DefaultTableModel(tableData, columns)
		{

			private static final long serialVersionUID = 1L;

			// lock table contents from user editing
			@Override
			public boolean isCellEditable(int rowIndex, int mColIndex)
			{
				return false;
			}
		};
	}

}
