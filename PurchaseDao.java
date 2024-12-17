/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Connections.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Laptop
 */
public class PurchaseDao {
     Connection con = MyConnection.getConnection();
    PreparedStatement ps;
    Statement st;
    ResultSet rs;
    
    
    
    public int getMaxRow(){
        int row=0;
        try {
            st=con.createStatement();
            rs=st.executeQuery("select max(id) from purchase"); 
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row + 1;
    }
    
    public String[] getUserValue(String email){
    String[] value = new String[5];
    String sql ="select uid,uname,uphone,uaddress1,uaddress2 from user where uemail = '"+email+"'";
         try {
             ps = con.prepareStatement(sql);
             rs =ps.executeQuery();
             if(rs.next()){
             value[0] = rs.getString(1);
             value[1] = rs.getString(2);
             value[2] = rs.getString(3);
             value[3] = rs.getString(4);
             value[4] = rs.getString(5);
             }
         } catch (SQLException ex) {
             Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
         }
    return value;
    }
    
   public void insert(int uid, String uName, String uPhone, int pid, String pName, int qty,
            double price, double total, String pDate, String address, String rDate, String supplier, String status) {

    String sql = "INSERT INTO purchase (uid, uname, uphone, pid, product_name, qty, price, total, p_date, uaddress, receive_date, supplier, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    // Default to "Unknown Supplier" if supplier is null or empty
    if (supplier == null || supplier.trim().isEmpty()) {
        supplier = "Unknown Supplier";  // Set a default value for the supplier
    }

    try {
        ps = con.prepareStatement(sql);
        
        // Setting values for all columns
        ps.setInt(1, uid);
        ps.setString(2, uName);
        ps.setString(3, uPhone);
        ps.setInt(4, pid);
        ps.setString(5, pName);
        ps.setInt(6, qty);
        ps.setDouble(7, price);
        ps.setDouble(8, total);
        ps.setString(9, pDate);
        ps.setString(10, address);
        
        // Check if rDate is null; if so, use the current date
        if (rDate == null || rDate.isEmpty()) {
            rDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());  // Current date
        }
        ps.setString(11, rDate);  // Set receive date
        
        ps.setString(12, supplier);
        ps.setString(13, status);
        
        // Execute the insertion
        ps.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    
    public int getQty(int pid){
    int qty =0;
    
         try {
             st = con.createStatement();
             rs = st.executeQuery("select pqty from product where pid = "+pid+"");
             if(rs.next()){
             qty = rs.getInt(1);
             }
         } catch (SQLException ex) {
             Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
         }
    return qty;
    }
    
    public void qtyUpdate(int pid,int qty){
    String sql ="update product set pqty = ? where pid =  ?";
         try {
             ps = con.prepareStatement(sql);
             ps.setInt(1, qty);
             ps.setInt(2, pid);
             ps.executeUpdate();
         } catch (SQLException ex) {
             Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
         }
    
    }
    public void setSuppStatus(int id, String supp, String status){
        String sql = "update purchase set supplier = ?, status = ? where id = ?";
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, supp);
            ps.setString(2, status);
            ps.setInt(3, id);
            
            if(ps.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "supplier successfully selected..");
            }
        }catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    //
    public void setDateStatus(int id, String rDate, String status){
        String sql = "update purchase set receive_date = ?, status = ? where id = ?";
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, rDate);
            ps.setString(2, status);
            ps.setInt(3, id);
            
            if(ps.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "Product successfully delievered..");
            }
        }catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    public void getProductValue(JTable table,String search, int uid){
     String sql = "select * from purchase where concat(id, pid,uname, product_name) like ?and uid = ? order by id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%"+ search+"%");
            ps.setInt(2, uid);
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object [] row;
            while(rs.next()){
            row = new Object[10];
            row[0] = rs.getInt(1);
            row[1] = rs.getInt(5);
            row[2] = rs.getString(6);
            row[3] = rs.getInt(7);
            row[4] = rs.getDouble(8);
            row[5] = rs.getDouble(9);
            row[6] = rs.getString(10);
            row[7] = rs.getString(12);
            row[8] = rs.getString(13);
            row[9] = rs.getString(14);
            model.addRow(row);
            
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    public void getProductValue(JTable table,String search){
     String sql = "select * from purchase where concat(id, pid, product_name) like ?and status = 'pending' order by id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%"+ search+"%");
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object [] row;
            while(rs.next()){
            row = new Object[14];
            row[0] = rs.getInt(1);
            row[1] = rs.getInt(2);
            row[2] = rs.getString(3);
            row[3] = rs.getString(4);
            row[4] = rs.getInt(5);
            row[5] = rs.getString(6);
            row[6] = rs.getInt(7);
            row[7] = rs.getDouble(8);
            row[8] = rs.getDouble(9);
            row[9] = rs.getString(10);
            row[10] = rs.getString(11);
            row[11] = rs.getString(12);
            row[12] = rs.getString(13);
            row[13] = rs.getString(14);
            model.addRow(row);
            
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
    //get all on the way purchased product
    public void getOnTheWayProduct(JTable table,String search, String suppliers){
     String sql = "select * from purchase where concat(id, pid, uname, product_name) like ?and supplier = ? and status = 'On the way' order by id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%"+ search+"%");
            ps.setString(2, suppliers);
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object [] row;
            while(rs.next()){
            row = new Object[14];
            row[0] = rs.getInt(1);
            row[1] = rs.getInt(2);
            row[2] = rs.getString(3);
            row[3] = rs.getString(4);
            row[4] = rs.getInt(5);
            row[5] = rs.getString(6);
            row[6] = rs.getInt(7);
            row[7] = rs.getDouble(8);
            row[8] = rs.getDouble(9);
            row[9] = rs.getString(10);
            row[10] = rs.getString(11);
            row[11] = rs.getString(12);
            row[12] = rs.getString(13);
            row[13] = rs.getString(14);
            model.addRow(row);
            
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
    //get supplier delivered products
    public void getSuppDeliProduct(JTable table,String search, String suppliers){
     String sql = "select * from purchase where concat(id, pid,uname, product_name) like ?and supplier = ? and status = 'Received' order by id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%"+ search+"%");
            ps.setString(2, suppliers);
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object [] row;
            while(rs.next()){
            row = new Object[14];
            row[0] = rs.getInt(1);
            row[1] = rs.getInt(2);
            row[2] = rs.getString(3);
            row[3] = rs.getString(4);
            row[4] = rs.getInt(5);
            row[5] = rs.getString(6);
            row[6] = rs.getInt(7);
            row[7] = rs.getDouble(8);
            row[8] = rs.getDouble(9);
            row[9] = rs.getString(10);
            row[10] = rs.getString(11);
            row[11] = rs.getString(12);
            row[12] = rs.getString(13);
            row[13] = rs.getString(14);
            model.addRow(row);
            
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
    //transaction
     public void transaction(JTable table,String search){
     String sql = "select * from purchase where concat(id, uid, pid) like ? and status = 'Received' order by id desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%"+ search+"%");
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object [] row;
            while(rs.next()){
            row = new Object[8];
            row[0] = rs.getInt(1);
            row[1] = rs.getInt(2);
            row[2] = rs.getInt(5);
            row[3] = rs.getInt(7);
            row[4] = rs.getDouble(8);
            row[5] = rs.getDouble(9);
            row[6] = rs.getString(12);
            row[7] = rs.getString(13);
            model.addRow(row);
            
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
    public void refund(int id) {
    int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to refund this product?", "Refund Account", JOptionPane.OK_CANCEL_OPTION);
    if (x == JOptionPane.OK_OPTION) {
        try {
            Connection con = MyConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("delete from purchase where id = ?");
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Product refund success");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

}
