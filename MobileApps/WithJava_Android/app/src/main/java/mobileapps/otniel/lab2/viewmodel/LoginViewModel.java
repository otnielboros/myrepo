package mobileapps.otniel.lab2.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import mobileapps.otniel.lab2.persistance.user.UserAccountDatabase;
import mobileapps.otniel.lab2.persistance.user.UserRepository;

public class LoginViewModel extends ViewModel {
    private UserRepository userRepository;

    public LoginViewModel(Context ctxt) {
        userRepository = UserRepository.getInstance(UserAccountDatabase.getAppDatabase(ctxt).userAccountDao());

    }

    public void deleteUser(String username){
        userRepository.deleteUser(username);
    }

    public void createUser(String username, String password)
    {
        userRepository.insertUser(username, password);
    }


    public static class Factory implements ViewModelProvider.Factory {
        private final Context ctxt;

        public Factory(Context ctxt) {
            this.ctxt=ctxt.getApplicationContext();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return((T)new LoginViewModel(ctxt));
        }
    }
}
