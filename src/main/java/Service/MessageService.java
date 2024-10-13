package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;
    AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public Message insertMessage(Message msg) throws SQLException {
        if(!msg.getMessage_text().isEmpty() &&
            msg.getMessage_text().length() < 255 &&
            accountDAO.getAccountByID(msg.getPosted_by()) != null) 
        {
            return messageDAO.insertMessage(msg);
        }
        return null;
    }

    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int message_id) throws SQLException {
        return messageDAO.getMessageByID(message_id);
    }

    public Message deleteMessageByID(int message_id) throws SQLException {
        Message msg = messageDAO.getMessageByID(message_id);
        messageDAO.deleteMessageByID(message_id);
        return msg;
    }

    public Message updateMessageByID(int message_id, String message_text) throws SQLException { 
        if(!message_text.isEmpty() &&
            message_text.length() < 255)
        {
            messageDAO.updateMessageByID(message_id, message_text);
            return messageDAO.getMessageByID(message_id);
        }
        return null;
    }

    public List<Message> getUserMessages(int account_id) throws SQLException {
        return messageDAO.getUserMessages(account_id);
    }
}
