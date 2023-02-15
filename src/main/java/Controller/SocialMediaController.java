package Controller;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.*;


public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;


    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postAccountLogin);
        app.post("/messages", this::postNewMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageByID);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getByUser);
        return app;
    }

    private void postAccountHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            context.json(mapper.writeValueAsString(addedAccount));
        }
        else context.status(400);
    }

    private void postAccountLogin(Context context) throws JsonProcessingException, SQLException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account validAccount = accountService.validateAccount(account);

        if(validAccount != null){
            context.json(mapper.writeValueAsString(validAccount));
        }
        else context.status(401);
    }

    private void postNewMessage(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message newMessage = messageService.addMessage(message);

        if(newMessage != null){
            context.json(mapper.writeValueAsString(newMessage));
        }
        else context.status(400);
    }

    private void getAllMessages(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    private void getMessageByID(Context context) throws SQLException {
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageByID(id);
        if (message != null) {
            context.json(message);
        } else {
            context.status(200);
        }
    }
    private void deleteMessage(Context context) throws SQLException {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);
        if (deletedMessage.getMessage_id() > 0){
            context.status(200).json(deletedMessage);
        }
        else context.status(200);
        
    }

    private void updateMessage(Context context) throws SQLException, JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        int id = Integer.parseInt(context.pathParam("message_id"));
        String text = message.getMessage_text();
        Message update = messageService.updateMessage(id, text);
        if(update != null){
            context.status(200).json(update);
        }
        else
            context.status(400);
    }


    private void getByUser(Context context) throws SQLException, JsonMappingException, JsonProcessingException {
        int id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getByUser(id);
        context.json(messages);

    }

}
