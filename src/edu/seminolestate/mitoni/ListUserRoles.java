/* 
 * Written by Christian Lundblad
 * November 25, 2017
 * This class contains the List User Role Window, and related methods and event handlers
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
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

public class ListUserRoles
{
	protected JInternalFrame frmListUserRoles;
	
	private DefaultTableModel model;	
	private JTable tblUserRoles;
	private JMenuItem mntmEditUserRole;

	public ListUserRoles()
	{
		//build window components
		frmListUserRoles = new JInternalFrame("User Role List");
		frmListUserRoles.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmListUserRoles.setClosable(true);
		frmListUserRoles.setBounds(50, 50, 300, 400);
		frmListUserRoles.getContentPane().setLayout(null);
			//context menu
		JPopupMenu ctxtUserRoleList = new JPopupMenu();
		mntmEditUserRole = new  JMenuItem("Edit Selected");
		ctxtUserRoleList.add(mntmEditUserRole);
		
		
		tblUserRoles = new JTable();
		tblUserRoles.setBounds(new Rectangle(0, 0, 200, 400));
		addPopup(tblUserRoles, ctxtUserRoleList);
		
		JScrollPane scrlPnManf = new JScrollPane();
		scrlPnManf.setLocation(0, 0);
		scrlPnManf.setSize(284, 371);
		scrlPnManf.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frmListUserRoles.getContentPane().add(scrlPnManf);
		scrlPnManf.setViewportView(tblUserRoles);

		
		//event handlers
		frmListUserRoles.addComponentListener(new ComponentListener()
		{
			
			public void componentShown(ComponentEvent e)
			{
				QueryRole newQuery = new QueryRole(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
				
				try
				{
					model = buildTableModel(newQuery.listRoles());
					tblUserRoles.setModel(model);
				}
				catch (SQLException ex)
				{
					JOptionPane.showMessageDialog(null, "Unable to load User Role list. Please see your system admin.");
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
		
		mntmEditUserRole.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				String user = (String) tblUserRoles.getModel().getValueAt(tblUserRoles.getSelectedRow(), 0);
				
				QueryRole newQuery = new QueryRole(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);					
				ResultSet loadRS = newQuery.loadEditUserRole(user);
				//parse results to edit window
				MainApplication.editUserRole.parseLoadUserRole(loadRS);
				//load edit window
				MainApplication.editUserRole.frmEditUserRole.setVisible(true);
				newQuery.closeSQL();
				frmListUserRoles.setVisible(false);
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
				if (tblUserRoles.getSelectedRow() != -1)
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
