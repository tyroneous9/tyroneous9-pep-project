package Service;

import java.sql.SQLException;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;
    
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account insertAccount(Account account) throws SQLException {
        if(!account.getUsername().isEmpty() 
            && account.getPassword().length() > 4
            && accountDAO.getAccountByUsername(account.getUsername()) == null) 
        {
            return accountDAO.insertAccount(account);
        }
        return null;
    }

    public Account getAccountByID(int account_id) throws SQLException {
        return accountDAO.getAccountByID(account_id);
    }

    public Account getAccountByUsername(String username) throws SQLException {
        return accountDAO.getAccountByUsername(username);
    }

    public Account validateLogin(String username, String password) throws SQLException {
        Account foundAccount;
        if((foundAccount = accountDAO.getAccountByUsername(username)) != null) {
            if(foundAccount.getPassword().equals(password)) {
                return foundAccount;
            }
        }
        return null;
    }
}
// The registration will be successful if and only if the username is not blank, 
// the password is at least 4 characters long, 
// and an Account with that username does not already exist.
