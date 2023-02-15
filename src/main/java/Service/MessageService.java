package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService{
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message){
        if(message.getMessage_text().length() > 0 && message.getMessage_text().length() <= 255 && message.getPosted_by() > 0){
            return messageDAO.insertMessage(message);
        }
        else return null;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int id) throws SQLException{
            return messageDAO.getMessageByID(id);
        }

    public Message deleteMessage(int id) throws SQLException {
             return messageDAO.deleteMessage(id);
    }

    public Message updateMessage(int id, String text) throws SQLException {
        return messageDAO.updateMessage(id, text);
    }

    public List<Message> getByUser(int userId) {
        return messageDAO.getByUser(userId);
    }

        
}
