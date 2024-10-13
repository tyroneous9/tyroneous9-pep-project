package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public Message getMessageByID(int message_id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "select * from message where message_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, message_id);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            Message message = new Message(rs.getInt("message_id"),
                                         rs.getInt("posted_by"), 
                                         rs.getString("message_text"),
                                         rs.getLong("time_posted_epoch"));
            return message;
        }
        return null;
    }

    public List<Message> getAllMessages() throws SQLException {
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        String sql = "select * from message";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Message message = new Message(rs.getInt("message_id"),
                                         rs.getInt("posted_by"), 
                                         rs.getString("message_text"),
                                         rs.getLong("time_posted_epoch"));
            messages.add(message);
        }
        return messages;
    }

    public Message insertMessage(Message message) throws SQLException {
        Connection cn = ConnectionUtil.getConnection();
        String sql = "insert into Message (posted_by, message_text, time_posted_epoch) values (?, ?, ?)";
        PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, message.getPosted_by());
        ps.setString(2, message.getMessage_text());
        ps.setLong(3, message.getTime_posted_epoch());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()) {
            int pKey = rs.getInt(1);
            return new Message(pKey, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
        }
        return null;
    }

    public void deleteMessageByID(int message_id) throws SQLException {
        Connection cn = ConnectionUtil.getConnection();
        String sql = "delete from message where message_id = ?";
        PreparedStatement ps = cn.prepareStatement(sql);
        ps.setInt(1, message_id);
        ps.executeUpdate();
    }

    public void updateMessageByID(int message_id, String message_text) throws SQLException {
        Connection cn = ConnectionUtil.getConnection();
        String sql = "update message set message_text = ? where message_id = ?";
        PreparedStatement ps = cn.prepareStatement(sql);
        ps.setString(1, message_text);
        ps.setInt(2, message_id);
        ps.executeUpdate();
    }

    public List<Message> getUserMessages(int account_id) throws SQLException {
        List<Message> msgs = new ArrayList<>();
        Connection cn = ConnectionUtil.getConnection();
        String sql = "select * from message where posted_by = ?";
        PreparedStatement ps = cn.prepareStatement(sql);
        ps.setInt(1, account_id);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Message msg = new Message(  rs.getInt("message_id"),
                                        rs.getInt("posted_by"),
                                        rs.getString("message_text"),
                                        rs.getLong("time_posted_epoch"));
            msgs.add(msg);                                       
        }
        return msgs;
    }
}
