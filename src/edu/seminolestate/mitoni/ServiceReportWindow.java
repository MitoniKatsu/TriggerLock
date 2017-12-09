/*
 * Written by Christian Lundblad
 * December 9, 2017
 * This class contains the Service Report Window, and related methods and event handlers
 */
package edu.seminolestate.mitoni;

import javax.swing.JInternalFrame;
import javax.swing.JFrame;

import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ServiceReportWindow
{
	protected JInternalFrame frmReport;
	
	private TextArea txtReportArea;
	String reportName;
	
	public ServiceReportWindow()
	{
		//build window components
		frmReport = new JInternalFrame("Report");
		frmReport.setClosable(true);
		frmReport.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmReport.setBounds(1, 1, 1010, 700);
		frmReport.getContentPane().setLayout(null);
		
		txtReportArea = new TextArea();
		txtReportArea.setEditable(false);
		txtReportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
		txtReportArea.setBounds(0, 0, 994, 669);
		frmReport.getContentPane().add(txtReportArea);
		
		//event handlers
		frmReport.addComponentListener(new ComponentListener()
		{
			
			@Override
			public void componentShown(ComponentEvent e)
			{
				displayServiceReport();
			}
			
			@Override
			public void componentResized(ComponentEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentMoved(ComponentEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e)
			{
				txtReportArea.setText("");
				
			}
		});
	}
	
	//methods
	
	private String addDashedLine()
	{
		String dashLine = "";
		
		for (int i = 0; i <= 155; i++)
		{
			dashLine += "-";
		}
		dashLine += "\n";
		
		return dashLine;
	}
	
	private String addStarredLine()
	{
		String starLine = "";
		
		for (int i = 0; i <= 155; i++)
		{
			starLine += "*";
		}
		starLine += "\n";
		
		return starLine;
	}
	
	private String addBlankLine()
	{
		return "\n";
	}
	
	private void displayServiceReport()
	{
		//query result
		QueryReports queryReports = new QueryReports(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
		ResultSet reportRs = null;
		ResultSetMetaData reportMeta = null;
		//run query
		reportRs = queryReports.serviceContactQuery();

		try
		{
			//parse meta data
			reportMeta = reportRs.getMetaData();
			//build report header
			txtReportArea.append("    " + reportMeta.getColumnName(1) + "                       " 
				+ reportMeta.getColumnName(2) + "                                              " 
				+ reportMeta.getColumnName(3) + "                            " 
				+ reportMeta.getColumnName(4) + "    \n");
			txtReportArea.append(addStarredLine());
			txtReportArea.append(addBlankLine());
			
			while(reportRs.next())
			{
				txtReportArea.append("    " + reportRs.getString(1) + "    " + reportRs.getString(2) + "    " + reportRs.getString(3) + "    " + String.valueOf(reportRs.getLong(4)).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)-$2-$3") + "    \n");
				txtReportArea.append(addDashedLine());
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}
}
