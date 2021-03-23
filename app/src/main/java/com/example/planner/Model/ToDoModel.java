package com.example.planner.Model;
//по сути класс представляет собой экземпляр Task, в который мы всовываем его ID
//для его идентификации в БД, всовываем статус(сделано/несделано) и вводим текст задачи
public class ToDoModel {
    private int id, status;
    //текст задачи, вводимый пользователем
    private String task;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }
}
