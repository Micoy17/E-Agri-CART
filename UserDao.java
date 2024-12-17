
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

public class UserDao {
    Connection con = MyConnection.getConnection();
    PreparedStatement ps;
    Statement st;
    ResultSet rs;
    
    
    public int getMaxRow(){
        int row=0;
        try {
            st=con.createStatement();
            rs=st.executeQuery("select max(uid) from user"); 
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row + 1;
    }
    
    public boolean isEmailExist(String email){
    try{
    ps = con.prepareStatement("select * from user where uemail = ?");
    ps.setString(1,email);
    rs = ps.executeQuery();
    if(rs.next()){
    return  true;
    }  
    }catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    return false;
    }
    
     public boolean isPhoneExist(String phone){
    try{
    ps = con.prepareStatement("select * from user where uemail = ?");
    ps.setString(1,phone);
    rs = ps.executeQuery();
    if(rs.next()){
    return  true;
    }  
    }catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    return false;
    }
     
     public void insert(int ID,String Username,String Email,String Password,String Phone,String Seq,String Answer,String adds1,String adds2)
     {
     String sql = "insert into user values(?,?,?,?,?,?,?,?,?)";
       try{
    ps = con.prepareStatement(sql);
    ps.setInt(1,ID);
    ps.setString(2,Username);
    ps.setString(3,Email);
    ps.setString(4,Password);
    ps.setString(5,Phone);
    ps.setString(6,Seq);
    ps.setString(7,Answer);
    ps.setString(8,adds1);
    ps.setString(9,adds2);
   if(ps.executeUpdate() > 0){
   JOptionPane.showMessageDialog(null,"User Added Successfully");
   }
   
       }catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
}
     
      
   public void update(int ID, String Username, String Email, String Password, String Phone, String Seq, String Answer, String adds1, String adds2) {
    String sql = "UPDATE user SET uname = ?, uemail = ?, upassword = ?, uphone = ?, usecqus = ?, uans = ?, uaddress1 = ?, uaddress2 = ? WHERE uid = ?";
    try {
        ps = con.prepareStatement(sql);
        
        // Set all parameters for the query
        ps.setString(1, Username);
        ps.setString(2, Email);
        ps.setString(3, Password);
        ps.setString(4, Phone);
        ps.setString(5, Seq);
        ps.setString(6, Answer);
        ps.setString(7, adds1);
        ps.setString(8, adds2);  // Set the 8th parameter (uaddress2)
        ps.setInt(9, ID);  // Set the 9th parameter (uid)
        
        // Execute the update query
        if (ps.executeUpdate() > 0) {
            JOptionPane.showMessageDialog(null, "User data successfully updated");
        }
    } catch (SQLException ex) {
        Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    
  public void delete(int id) {
    int x = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this account?", "Delete account", JOptionPane.OK_CANCEL_OPTION);
    if (x == JOptionPane.OK_OPTION) {
        try {
            Connection con = MyConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("delete from user where uid = ?");
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Account deleted successfully");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

      
    public String[] getUserValue(int id){
        String[] value = new String[9];
    try{
    ps = con.prepareStatement("select * from user where uid = ?");
    ps.setInt(1,id);
    rs = ps.executeQuery();
    if(rs.next()){
    value[0] = rs.getString(1);
    value[1] = rs.getString(2);
    value[2] = rs.getString(3);
    value[3] = rs.getString(4);
    value[4] = rs.getString(5);
    value[5] = rs.getString(6);
    value[6] = rs.getString(7);
    value[7] = rs.getString(8);
    value[8] = rs.getString(9);
    
    }  
    }catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    return value;
    }
    
    
   
     public int getUserId(String email) {
    int id = 0;
    try {
        // Change 'email' to 'uemail' to match the column name in the table
        ps = con.prepareStatement("select uid from user where uemail = ?");
        ps.setString(1, email);
        rs = ps.executeQuery();
        if (rs.next()) {
            id = rs.getInt(1);
        }  
    } catch (SQLException ex) {
        Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
    }
    return id;
}
     
     public void getUserValue(JTable table,String search){
     String sql = "select * from user where concat(uid,uname,uphone) like ? order by uid desc";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%"+ search+"%");
            rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object [] row;
            while(rs.next()){
            row = new Object[9];
            row[0] = rs.getInt(1);
            row[1] = rs.getString(2);
            row[2] = rs.getString(3);
            row[3] = rs.getString(4);
            row[4] = rs.getString(5);
            row[5] = rs.getString(6);
            row[6] = rs.getString(7);
            row[7] = rs.getString(8);
            row[8] = rs.getString(9);
            model.addRow(row);
            
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
}


