package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    public Account getAccountByID(int account_id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "select * from account where account_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, account_id);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Account account = new Account(account_id, 
                                        rs.getString("username"), 
                                        rs.getString("password"));
            return account;
        }
        return null;
    }

    public Account getAccountByUsername(String username) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "select * from account where username = ?";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            Account account = new Account(rs.getInt("account_id"), 
                                        username, 
                                        rs.getString("password"));
            return account;
        }
        return null;
    }

    public Account insertAccount(Account account) throws SQLException {
        Connection cn = ConnectionUtil.getConnection();
        String sql = "insert into account (username, password) values (?, ?)";
        PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, account.getUsername());
        ps.setString(2, account.getPassword());
        ps.executeUpdate();
        ResultSet pKeys = ps.getGeneratedKeys();
        if(pKeys.next()) {
            int pKey = pKeys.getInt(1);
            return new Account(pKey, account.getUsername(), account.getPassword());
        }
        return null;
    }
}
