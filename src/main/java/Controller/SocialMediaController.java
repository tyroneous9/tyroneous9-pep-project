package Controller;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

     AccountService accountService;
     MessageService messageService;
     
    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("register", this::postUserHandler);
        app.post("login", this::postLoginHandler);
        app.post("messages", this::postMessageHandler);
        app.get("messages", this::getMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIDHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("messages/{message_id}", this::updateMessageByIDHandler);
        app.get("accounts/{account_id}/messages", this::getUserMessagesHandler);

        return app;
    }

    private void postUserHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.insertAccount(account);
        if(addedAccount != null) {
            ctx.json(addedAccount);
        } else {
            ctx.status(400);
        }
    }

    private void postLoginHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account validAccount = accountService.validateLogin(account.getUsername(), account.getPassword());
        if(validAccount != null) {
            ctx.json(validAccount);
        } else {
            ctx.status(401);
        }
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(ctx.body(), Message.class);
        Message addedMsg = messageService.insertMessage(msg);
        if(addedMsg != null) {
            ctx.json(addedMsg);
        } else {
            ctx.status(400);
        }
    }

    private void getMessagesHandler(Context ctx) throws SQLException {
        List<Message> msgs = messageService.getAllMessages();
        if(msgs != null) {
            ctx.json(msgs);
        }
    }

    private void getMessageByIDHandler(Context ctx) throws SQLException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = messageService.getMessageByID(message_id);
        if(msg != null) {
            ctx.json(msg);
        }
    }

    private void deleteMessageByIDHandler(Context ctx) throws SQLException {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = messageService.deleteMessageByID(message_id);
        if(msg != null) {
            ctx.json(msg);
        } 
    }
    
    private void updateMessageByIDHandler(Context ctx) throws SQLException, JsonMappingException, JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        String message_text = om.readValue(ctx.body(), Message.class).getMessage_text();
        Message msg = messageService.updateMessageByID(message_id, message_text);
        if(msg != null) {
            ctx.json(msg);
        } else {
            ctx.status(400);
        }
    }

    private void getUserMessagesHandler(Context ctx) throws SQLException {
        int account_id = Integer.parseInt(ctx.pathParam("account_id")); 
        List<Message> msgs = messageService.getUserMessages(account_id);
        if(msgs != null) {
            ctx.json(msgs);
        }
    }
}