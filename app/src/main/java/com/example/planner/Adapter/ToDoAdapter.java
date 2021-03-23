package com.example.planner.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.AddNewTask;
import com.example.planner.MainActivity;
import com.example.planner.Model.ToDoModel;
import com.example.planner.R;
import com.example.planner.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    //переменные
    //создаем todoList с характеристиками обычной таски класса <ToDoModel>
    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    //конструктор адаптера
    public ToDoAdapter(DatabaseHandler db, MainActivity activity) {
        //подключаем нужную Activity
        this.activity = activity;
        //подключаем нужную базу данных
        this.db = db;
    }
    //про ViewHolder - для каждого элемента списка создаётся объект,
    //хранящий ссылки на отдельные вьюхи внутри элемента.
    //
    //parent нужен, чтобы раздуть вид до параметров родительского контейнера.
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        //LayoutInflater берет XML-файл и создает по его подобию разные объекты
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_layout, parent, false);
            return new ViewHolder(itemView);
    }
    //привязка ViewHolder к объекту
    public void onBindViewHolder(ViewHolder holder, int position) {
        db.openDatabase();
        ToDoModel item = todoList.get(position);
        //помещаем текст в item - по сути, строчка с нашей задачей
        holder.task.setText(item.getTask());
        //статус - true/false - дает понять, мы поставили галочку на задачу или нет
        holder.task.setChecked(toBoolean(item.getStatus()));
        //нажали на статус таски - надо изменить статус и в базе данных
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }
    //объявляем внутренний статический класс, который мы используем ТОЛЬКО в данном классе,
    //т.е. в ToDoAdapter
    //возвращаем количество объектов в списке todoList
    public int getItemCount(){
        return todoList.size();
    }
    //переводим из инта в булиан, чтобы task.setChecked не ругался, что статус интовый
    private boolean toBoolean(int n) {
        return n!=0;
    }
    public void setTasks(List<ToDoModel> todoList){
        //мы подаем на setTasks список, который установится для класса ToDoAdapter
        this.todoList = todoList;
        //оповещает, что данные изменились
        notifyDataSetChanged();
    }

    public Context getContext(){
        return activity;
    }

    public void deleteItem(int position){
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task",item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        //конструктор,
        //здесь мы обращаемся к супер-классу класса View и в наш чекбокс заносим по айдишнику
        //todoCheckbox из XML файла
        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
