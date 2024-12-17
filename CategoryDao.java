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
public class CategoryDao {
     Connection con = MyConnection.getConnection();
    PreparedStatement ps;
    Statement st;
    ResultSet rs;
    
    
    public int getMaxRow(){
        int row=0;
        try {
            st=con.createStatement();
            rs=st.executeQuery("select max(cid) from category"); 
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row + 1;
    }
    
      public boolean isIdExist(int ID){
    try{
    ps = con.prepareStatement("select * from category where cid = ?");
    ps.setInt(1,ID);
    rs = ps.executeQuery();
    if(rs.next()){
    return  true;
    }  
    }catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    return false;
    }
    
    public boolean isCategoryNameExist(String cname){
    try{
    ps = con.prepareStatement("select * from category where cname = ?");
    ps.setString(1,cname);
    rs = ps.executeQuery();
    if(rs.next()){
    return  true;
    }  
    }catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    return false;
    }
    
     
     public void insert(int ID,String cname,String desc )
     {
     String sql = "insert into Category values(?,?,?)";
       try{
    ps = con.prepareStatement(sql);
    ps.setInt(1,ID);
    ps.setString(2,cname);
    ps.setString(3,desc);
   if(ps.executeUpdate() > 0){
   JOptionPane.showMessageDialog(null,"Category Added Successfully");
   }
   
       }catch (SQLException ex) {
            Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
}
       
     public void getCategoryValue(JTable table,String search){
     String sql = "select * from category where concat(cid,cname) like ? order by cid desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%"+ search+"%");
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object [] row;
            while(rs.next()){
            row = new Object[3];
            row[0] = rs.getInt(1);
            row[1] = rs.getString(2);
            row[2] = rs.getString(3);
            model.addRow(row);
            
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
      public void update(int ID,String cname,String desc ) {
    String sql = "UPDATE category SET cname = ?, cdesc = ? where cid = ?";
    try {
        ps = con.prepareStatement(sql);
        
        // Set all parameters for the query
        ps.setString(1, cname);
        ps.setString(2, desc);
        ps.setInt(3, ID);  // Set the 9th parameter (uid)
        
        // Execute the update query
        if (ps.executeUpdate() > 0) {
            JOptionPane.showMessageDialog(null, "Category successfully updated");
        }
    } catch (SQLException ex) {
        Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, ex);
    }
}
      
       public void delete(int ID) {
    int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this category?", "Delete account", JOptionPane.OK_CANCEL_OPTION);
    if (x == JOptionPane.OK_OPTION) {
        try {
            Connection con = MyConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("delete from category where cid = ?");
            ps.setInt(1, ID);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Category deleted successfully");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
       
       
}
