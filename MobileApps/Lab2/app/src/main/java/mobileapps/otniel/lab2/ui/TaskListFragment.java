package mobileapps.otniel.lab2.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import mobileapps.otniel.lab2.MainActivity;
import mobileapps.otniel.lab2.R;
import mobileapps.otniel.lab2.TaskActivity;
import mobileapps.otniel.lab2.persistance.task.TaskRepository;
import mobileapps.otniel.lab2.persistance.token.TokenDatabase;
import mobileapps.otniel.lab2.persistance.token.TokenRepository;
import mobileapps.otniel.lab2.viewmodel.LoginViewModel;
import mobileapps.otniel.lab2.viewmodel.TaskViewModel;
import mobileapps.otniel.lab2.viewobjects.Task;

public class TaskListFragment extends Fragment {

    private TaskViewModel mTaskViewModel;
    private RecyclerView mTaskList;
    private Button logoutButton;

    public static TaskListFragment newInstance() {
        return new TaskListFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTaskList = view.findViewById(R.id.task_list);
        logoutButton=view.findViewById(R.id.logoutBtn);
        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                getActivity().finish();
                TokenRepository tokenRepository=TokenRepository.getInstance(TokenDatabase.getAppDatabase(getActivity().getApplicationContext()).tokenDao());
                tokenRepository.update("");
                Intent in=new Intent(getActivity().getApplicationContext(),MainActivity.class);
                startActivity(in);

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTaskList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskViewModel=ViewModelProviders.of(this, new TaskViewModel.Factory(getActivity())).get(TaskViewModel.class);
        mTaskViewModel.getTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                TaskListAdapter taskListAdapter =new TaskListAdapter(getActivity(),tasks);
                mTaskList.setAdapter(taskListAdapter);
            }
        });
    }

}
