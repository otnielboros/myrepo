package mobileapps.otniel.lab2.persistance.token;

public class TokenRepository {
    private final TokenDao tokenDao;
    private static TokenRepository instance;
    //private LiveData<UserAccount> userAccountLiveData;

    private TokenRepository(TokenDao tokenDao)
    {
        this.tokenDao = tokenDao;
    }

    public static TokenRepository getInstance(TokenDao tokenDao)
    {
        if(instance == null)
        {
            instance = new TokenRepository(tokenDao);
        }
        return instance;
    }


    public void update(String token)
    {
        tokenDao.update(token,1);
    }

    public Token getToken()
    {
        Token token=tokenDao.getToken(1);
        return token;
    }

    public void insert(Token token){
        tokenDao.insert(token);
    }
}
