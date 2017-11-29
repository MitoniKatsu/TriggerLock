/* 
 * Written by Christian Lundblad
 * November 25, 2017
 * This class contains the List Manufacturer Window, and menu related event handlers
 */
package edu.seminolestate.mitoni;

import java.awt.Component;
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

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.Rectangle;
import javax.swing.JFrame;

public class ListManufacturersWindow
{
	protected JInternalFrame frmListManufacturers;
	
	private DefaultTableModel model;	
	private JTable tblManufacturers;
	private JMenuItem mntmEditManufacturer;
	private JMenuItem mntmDeleteManufacturer;	

	public ListManufacturersWindow()
	{
		//build window components
		frmListManufacturers = new JInternalFrame("Manufacturer List");
		frmListManufacturers.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmListManufacturers.setClosable(true);
		frmListManufacturers.setBounds(50, 50, 200, 400);
		frmListManufacturers.getContentPane().setLayout(null);
			//context menu
		JPopupMenu ctxtManufacturerList = new JPopupMenu();
		mntmEditManufacturer = new  JMenuItem("Edit Selected");
		ctxtManufacturerList.add(mntmEditManufacturer);
		JSeparator ctxtSeparator = new JSeparator();
		ctxtManufacturerList.add(ctxtSeparator);
		mntmDeleteManufacturer = new JMenuItem("Delete Selected");
		ctxtManufacturerList.add(mntmDeleteManufacturer);
		
		tblManufacturers = new JTable();
		tblManufacturers.setBounds(new Rectangle(0, 0, 200, 400));
		addPopup(tblManufacturers, ctxtManufacturerList);
		
		JScrollPane scrlPnManf = new JScrollPane();
		scrlPnManf.setLocation(0, 0);
		scrlPnManf.setSize(184, 371);
		scrlPnManf.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frmListManufacturers.getContentPane().add(scrlPnManf);
		scrlPnManf.setViewportView(tblManufacturers);		
		
		//event handlers
		frmListManufacturers.addComponentListener(new ComponentListener()
		{
			
			public void componentShown(ComponentEvent e)
			{
				QueryManufacturer newQuery = new QueryManufacturer(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
				
				try
				{
					model = buildTableModel(newQuery.listManufacturers());
					tblManufacturers.setModel(model);
				}
				catch (SQLException ex)
				{
					JOptionPane.showMessageDialog(null, "Unable to load Manufacturer List. Please see your system admin.");
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
		
		mntmEditManufacturer.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String companyName = (String) tblManufacturers.getModel().getValueAt(tblManufacturers.getSelectedRow(), 0);
				
				QueryManufacturer newQuery = new QueryManufacturer(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);					
				ResultSet loadRS = newQuery.loadEditManufacturer(companyName);
				//parse results to edit window
				MainApplication.editManufacturerWindow.parseLoadManufacturer(loadRS);
				//load edit window
				MainApplication.editManufacturerWindow.frmEditManufacturer.setVisible(true);
				newQuery.closeSQL();
				frmListManufacturers.setVisible(false);
			}
		});
		
		mntmDeleteManufacturer.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				String companyName = (String) tblManufacturers.getModel().getValueAt(tblManufacturers.getSelectedRow(), 0);
				
				int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete "+ companyName
						+"? This operation cannot be reversed, and will fail if any gun and ammo records use this manufacturer!", "Confirm Delete", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.YES_OPTION)
				{
					QueryManufacturer newQuery = new QueryManufacturer(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);					
					newQuery.deleteManufacturer(companyName);
					frmListManufacturers.setVisible(false);
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
				if (tblManufacturers.getSelectedRow() != -1)
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
