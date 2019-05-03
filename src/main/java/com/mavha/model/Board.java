package com.mavha.model;

import java.util.List;

public class Board {

    private String title;
    private State state;
    private List<Todo> todos;


    public Board(String title, State state, List<Todo> todos) {
        this.title = title;
        this.state = state;
        this.todos = todos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }
}
