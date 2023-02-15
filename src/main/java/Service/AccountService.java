package Service;
import java.sql.SQLException;

import DAO.AccountDAO;
import Model.Account;

public class AccountService{
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public Account addAccount(Account account){
        if(account.getUsername().length() > 0 && account.getPassword().length() >= 4){
            return accountDAO.insertAccount(account);
        }
        else return null;
    }

    public Account validateAccount(Account account) throws SQLException {
        return accountDAO.getAccount(account);

    }
}