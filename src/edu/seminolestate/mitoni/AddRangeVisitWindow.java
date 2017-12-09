/*
 * Written by Christian Lundblad
 * December 8, 2017
 * This class contains the Add Range Visit Window, and related methods and event handlers
 */
package edu.seminolestate.mitoni;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;
import javax.swing.JComboBox;

public class AddRangeVisitWindow
{
	protected JInternalFrame frmAddRangeVisit;
	

	private JButton btnAddRangeVisitCheckOut;
	private JButton btnAddRangeVisitCancel;
	
	private JLabel lblCustomerNum;
	private JLabel lblRangeFeePrice;
	private JLabel lblRentalFeePrice;
	private JLabel lblAmmoTotal;
	private JLabel lblTargetTotal;
	private JLabel lblMembershipFee;
	private JLabel lblSalesTaxPrice;
	private JLabel lblTotalPrice;
	
	private JCheckBox ckbxAmmo;
	private JCheckBox ckbxRentalFee;
	private JCheckBox ckbxMembership;
	
	private double rangeFee = 0;
	private double rentalFee = 0;
	private double chosenAmmoCostPerRound = 0;
	private double targetTotal = 0;
	private double membershipFee = 0;
	
	private int chosenAmmoQty = 0;
	private int chosenAmmoStocked = 0;
	
	private NumberFormat c = NumberFormat.getCurrencyInstance();
	
	private final double TAX_RATE = 0.07;
	private final double MEMBER_RANGE_FEE = 0;
	private final double NOMEMBER_RANGE_FEE = 15.00;
	private final double MEMBERSHIP_FEE = 200.00;
	private final double RENTAL_FEE = 10.00;
	private final double TARGET_COST = 0.25;

	private String customerNumber = "";
	private String chosenRentalSerial = "";
	private String chosenAmmoModel = "";

	private JComboBox<Integer> cmbxTargetQty;
	
	private Date renewalDate;
	

	public AddRangeVisitWindow()
	{
		//build window components
		frmAddRangeVisit = new JInternalFrame("New Range Visit");
		frmAddRangeVisit.setBounds(100, 100, 300, 350);
		frmAddRangeVisit.getContentPane().setLayout(null);
		
		cmbxTargetQty = new JComboBox<Integer>();
		cmbxTargetQty.setBounds(10, 152, 102, 23);
		frmAddRangeVisit.getContentPane().add(cmbxTargetQty);
		loadTargetBox();
		
		lblCustomerNum = new JLabel("Customer Number: ");
		lblCustomerNum.setFont(new Font("Tahoma", Font.ITALIC, 16));
		lblCustomerNum.setHorizontalAlignment(SwingConstants.LEFT);
		lblCustomerNum.setBounds(10,11,201,31);
		frmAddRangeVisit.getContentPane().add(lblCustomerNum);
		
		JLabel lblRangeFee = new JLabel("Range Fee:");
		lblRangeFee.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRangeFee.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRangeFee.setBounds(89,64,100,23);
		frmAddRangeVisit.getContentPane().add(lblRangeFee);
		
		ckbxRentalFee = new JCheckBox("Rental Fee:");
		ckbxRentalFee.setHorizontalAlignment(SwingConstants.RIGHT);
		ckbxRentalFee.setFont(new Font("Tahoma", Font.BOLD, 14));
		ckbxRentalFee.setBounds(42, 94, 150, 23);
		frmAddRangeVisit.getContentPane().add(ckbxRentalFee);
		
		lblRangeFeePrice = new JLabel(c.format(rangeFee));
		lblRangeFeePrice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRangeFeePrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRangeFeePrice.setBounds(199, 64, 75, 23);
		frmAddRangeVisit.getContentPane().add(lblRangeFeePrice);
		
		lblRentalFeePrice = new JLabel(c.format(rentalFee));
		lblRentalFeePrice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRentalFeePrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRentalFeePrice.setBounds(199, 94, 75, 23);
		frmAddRangeVisit.getContentPane().add(lblRentalFeePrice);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBackground(Color.BLACK);
		separator.setBounds(10, 273, 264, 2);
		frmAddRangeVisit.getContentPane().add(separator);
		
		ckbxAmmo = new JCheckBox("Ammo:");
		ckbxAmmo.setHorizontalAlignment(SwingConstants.RIGHT);
		ckbxAmmo.setFont(new Font("Tahoma", Font.BOLD, 14));
		ckbxAmmo.setBounds(42, 120, 150, 23);
		frmAddRangeVisit.getContentPane().add(ckbxAmmo);
		
		lblAmmoTotal = new JLabel(c.format(getAmmoTotal()));
		lblAmmoTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAmmoTotal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAmmoTotal.setBounds(199, 120, 75, 23);
		frmAddRangeVisit.getContentPane().add(lblAmmoTotal);
		
		JLabel lblTargets = new JLabel("Targets:");
		lblTargets.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTargets.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTargets.setBounds(89, 150, 100, 23);
		frmAddRangeVisit.getContentPane().add(lblTargets);
		
		lblTargetTotal = new JLabel(c.format(targetTotal));
		lblTargetTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTargetTotal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTargetTotal.setBounds(199, 150, 75, 23);
		frmAddRangeVisit.getContentPane().add(lblTargetTotal);
		
		btnAddRangeVisitCheckOut = new JButton("Complete Purchase");
		frmAddRangeVisit.getRootPane().setDefaultButton(btnAddRangeVisitCheckOut);
		btnAddRangeVisitCheckOut.setBounds(149,280,125,23);
		frmAddRangeVisit.getContentPane().add(btnAddRangeVisitCheckOut);
		
		btnAddRangeVisitCancel = new JButton("Cancel");
		btnAddRangeVisitCancel.setBounds(49,280,90,23);
		frmAddRangeVisit.getContentPane().add(btnAddRangeVisitCancel);
		
		ckbxMembership = new JCheckBox("Membership:");
		ckbxMembership.setHorizontalAlignment(SwingConstants.RIGHT);
		ckbxMembership.setFont(new Font("Tahoma", Font.BOLD, 14));
		ckbxMembership.setBounds(13, 180, 179, 23);
		frmAddRangeVisit.getContentPane().add(ckbxMembership);
		
		lblMembershipFee = new JLabel(c.format(membershipFee));
		lblMembershipFee.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMembershipFee.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMembershipFee.setBounds(199, 180, 75, 23);
		frmAddRangeVisit.getContentPane().add(lblMembershipFee);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.BLACK);
		separator_1.setBackground(Color.BLACK);
		separator_1.setBounds(10, 210, 264, 2);
		frmAddRangeVisit.getContentPane().add(separator_1);
		
		JLabel lblSalesTax = new JLabel("Sales Tax: ");
		lblSalesTax.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSalesTax.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		lblSalesTax.setBounds(89, 210, 100, 23);
		frmAddRangeVisit.getContentPane().add(lblSalesTax);
		
		lblSalesTaxPrice = new JLabel(c.format(getTaxes()));
		lblSalesTaxPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSalesTaxPrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSalesTaxPrice.setBounds(199, 210, 75, 23);
		frmAddRangeVisit.getContentPane().add(lblSalesTaxPrice);
		
		JLabel lblOrderTotal = new JLabel("Order Total:");
		lblOrderTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOrderTotal.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblOrderTotal.setBounds(89, 239, 100, 23);
		frmAddRangeVisit.getContentPane().add(lblOrderTotal);
		
		lblTotalPrice = new JLabel(c.format(getOrderTotal()));
		lblTotalPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalPrice.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTotalPrice.setBounds(199, 239, 75, 23);
		frmAddRangeVisit.getContentPane().add(lblTotalPrice);
		
		//event handlers
		frmAddRangeVisit.addComponentListener(new ComponentListener()
		{
			
			@Override
			public void componentShown(ComponentEvent e)
			{
				//show member number
				lblCustomerNum.setText("Customer Number: " + customerNumber);
				
				//check member status and update range fee
				if (checkMembership())
				{
					ckbxMembership.setText("Renew " + ckbxMembership.getText());
					rangeFee = MEMBER_RANGE_FEE;
					updateLabels();
				}
				else if(!checkMembership())
				{
					ckbxMembership.setText("Start " + ckbxMembership.getText());
					rangeFee = NOMEMBER_RANGE_FEE;
					updateLabels();
				}
			}
			
			@Override
			public void componentResized(ComponentEvent e)
			{
				
			}
			
			@Override
			public void componentMoved(ComponentEvent e)
			{
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e)
			{
				clearAddRangeVisit();
			}
		});
		
		ckbxRentalFee.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (ckbxRentalFee.isSelected())
				{
					rentalFee = RENTAL_FEE;
					updateLabels();
					MainApplication.rentalGunChooser.frmRentalGunChooser.setVisible(true);
				}
				else if(!ckbxRentalFee.isSelected())
				{
					rentalFee = 0;
					chosenRentalSerial = "";
					updateLabels();
				}
			}
		});
		
		ckbxAmmo.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (ckbxAmmo.isSelected())
				{
					MainApplication.ammoChooser.frmAmmoChooser.setVisible(true);
				}
				else if(!ckbxAmmo.isSelected())
				{
					setChosenAmmo("", 0, 0, 0);
					updateLabels();
				}
			}
		});
		
		cmbxTargetQty.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				targetTotal = getTargetTotal();
				updateLabels();
			}
		});
		
		ckbxMembership.addItemListener(new ItemListener()
		{
			
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				if (ckbxMembership.isSelected())
				{
					if (checkMembership())
					{
						//20% off on renewal of current memberships
						membershipFee = MEMBERSHIP_FEE * .8;
						updateLabels();
					}
					else
					{
						membershipFee = MEMBERSHIP_FEE;
						//void range fee since starting membership
						rangeFee = MEMBER_RANGE_FEE;
						updateLabels();
					}
				}
				else if(!ckbxMembership.isSelected())
				{
					if(checkMembership())
					{
						rangeFee = MEMBER_RANGE_FEE;
						membershipFee = 0;
						updateLabels();
					}
					else if(!checkMembership())
					{
						membershipFee = 0;
						rangeFee = NOMEMBER_RANGE_FEE;
						updateLabels();
					}
				}
			}
		});
		
		btnAddRangeVisitCheckOut.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				processRangeTransaction();
			}
		});
		
		btnAddRangeVisitCancel.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				frmAddRangeVisit.setVisible(false);			
			}
		});
	}
	
	//getters and setters
	public void setCustomerNumber(String tocustNum)
	{
		customerNumber = tocustNum;
	}
	
	public void loadTargetBox()
	{
		for (int i = 0; i < 21; i++)
		{
			cmbxTargetQty.addItem(i);
		}
		cmbxTargetQty.setSelectedIndex(0);
	}
	
	private int getTargetQty()
	{
		if (cmbxTargetQty.getItemCount() != 0)
		{
			return cmbxTargetQty.getSelectedIndex();
		}
		
		return 0;
	}
	
	public double getTargetTotal()
	{
		targetTotal = TARGET_COST * getTargetQty();
		
		return targetTotal;
	}
	
	public double getSubtotal()
	{
		return rangeFee + rentalFee + getAmmoTotal() + getTargetTotal() + membershipFee;
	}
	
	public void setChosenAmmo(String model, double cost, int qty, int oldQty)
	{
		chosenAmmoModel = model;
		chosenAmmoCostPerRound = cost;
		chosenAmmoQty = qty;
		chosenAmmoStocked = oldQty;
		updateLabels();
	}
	
	public double getAmmoTotal()
	{
		return (chosenAmmoQty * chosenAmmoCostPerRound) * 1.2;
	}
	
	public double getTaxes()
	{
		return getSubtotal() * TAX_RATE;
	}
	
	public double getOrderTotal()
	{
		return getSubtotal() + getTaxes();
	}
	
	public void setRentalSerial(String serial)
	{
		chosenRentalSerial = serial;
	}
	
	public void cancelGunRental()
	{
		ckbxRentalFee.setSelected(false);
	}
	
	public void cancelAmmoSale()
	{
		ckbxAmmo.setSelected(false);
	}
	
	public void clearAddRangeVisit()
	{
		//default cust number label
		lblCustomerNum.setText("Customer Number: ");
		//default range fee
		rangeFee = 0;
		//default rental fee
		rentalFee = 0;
		chosenRentalSerial = "";
		ckbxRentalFee.setSelected(false);
		//default ammo
		chosenAmmoCostPerRound = 0;
		ckbxAmmo.setSelected(false);
		chosenAmmoQty = 0;
		chosenAmmoModel = "";
		//default targets
		cmbxTargetQty.setSelectedIndex(0);
		
		ckbxMembership.setSelected(false);
		ckbxMembership.setText("Membership: ");
		membershipFee = 0;
		updateLabels();
	}
	
	//methods	
	private boolean checkMembership()
	{
		QueryCustomer newQuery = new QueryCustomer(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
		
		ResultSet memberCheck = newQuery.listMembers(customerNumber);
		boolean member = false;
		
		try
		{
			java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());
			
			memberCheck.next();
			
			if (memberCheck.getInt(2) == 1)
			{
				if (memberCheck.getDate(3).after(today))
				{
					member = true;
					renewalDate = memberCheck.getDate(3);
				}
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Unable to check customer member status. Please see your system admin.");
		}
		
		return member;
	}
	
	private void processRangeTransaction()
	{
		QueryRangeVisit visitQuery = new QueryRangeVisit(MainApplication.getCurrentUser(), MainApplication.getCurrentPassword(), null);
		String rentalSerial = "";
		String ammoPurchased = "";
		int newQty = 0;
		
		//update query parameters
		if (ckbxRentalFee.isSelected())
		{
			rentalSerial = chosenRentalSerial;
		}
		
		if (ckbxAmmo.isSelected())
		{
			ammoPurchased = chosenAmmoModel;
			newQty = chosenAmmoStocked - chosenAmmoQty;
		}
		
		if (ckbxMembership.isSelected())
		{
			if (checkMembership())
			{
				visitQuery.addRangeVisit(customerNumber, getOrderTotal(), rentalSerial, ammoPurchased, newQty, true, renewalDate, false);
			}
			else
			{
				visitQuery.addRangeVisit(customerNumber, getOrderTotal(), rentalSerial, ammoPurchased, newQty, false, null, true);
			}
		}
		else
		{
			visitQuery.addRangeVisit(customerNumber, getOrderTotal(), rentalSerial, ammoPurchased, newQty, false, null, false);
		}
		
		JOptionPane.showMessageDialog(null, "Transaction completed. Total due is " + c.format(getOrderTotal()));
		frmAddRangeVisit.setVisible(false);
	}

	
	public void updateLabels()
	{
		lblRangeFeePrice.setText(c.format(rangeFee));
		lblRentalFeePrice.setText(c.format(rentalFee));
		lblAmmoTotal.setText(c.format(getAmmoTotal()));
		lblTargetTotal.setText(c.format(getTargetTotal()));
		lblMembershipFee.setText(c.format(membershipFee));
		lblSalesTaxPrice.setText(c.format(getTaxes()));
		lblTotalPrice.setText(c.format(getOrderTotal()));		
	}
	
}
