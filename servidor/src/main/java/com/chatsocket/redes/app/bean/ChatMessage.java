package com.chatsocket.redes.app.bean;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author manoelaugusto
 */
public class ChatMessage implements Serializable {
    private String name;
    private String text;
    private  String nameReseverd;
    private Set<String> setOnline = new HashSet<String>(); // Cria uma lista de usuarios online.
    private Action action;// Cria uma lista de ações para o usuarios.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNameReseverd() {
        return nameReseverd;
    }

    public void setNameReseverd(String nameReseverd) {
        this.nameReseverd = nameReseverd;
    }

    public Set<String> getSetOnline() {
        return setOnline;
    }

    public void setSetOnline(Set<String> setOnline) {
        this.setOnline = setOnline;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public enum Action{
        CONNECT, DISCONNECT, SEND_ONE,SEND_ALL, USER_ONLINE
    }
}