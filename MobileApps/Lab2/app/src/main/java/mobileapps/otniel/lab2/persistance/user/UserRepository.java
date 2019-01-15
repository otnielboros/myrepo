package mobileapps.otniel.lab2.persistance.user;

import android.arch.lifecycle.LiveData;

public class UserRepository {

    private final UserAccountDao userAccountDao;
    private static UserRepository instance;
    //private LiveData<UserAccount> userAccountLiveData;

    private UserRepository(UserAccountDao userAccountDao)
    {
        this.userAccountDao = userAccountDao;
    }

    public static UserRepository getInstance(UserAccountDao userAccountDao)
    {
        if(instance == null)
        {
            instance = new UserRepository(userAccountDao);
        }
        return instance;
    }

    public void deleteUser(String username){
        UserAccount userAccount=new UserAccount();
        userAccount.setUserId(username);
        userAccountDao.delete(userAccount);
    }

    public void insertUser(String username, String password)
    {
        UserAccount account = new UserAccount(username, password);
        userAccountDao.insert(account);
    }

    public UserAccount getAccount(String username, final String password)
    {

        UserAccount userAccount = userAccountDao.getAccount(username);
        return userAccount;
    }

}
