/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chatsocket.redes.app.service;

import com.chatsocket.redes.app.bean.ChatMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author manoelaugusto
 */
public class ClienteService {


    private Socket socket;
    private ObjectOutputStream output;

    public Socket connect (){
        try {
            this.socket = new Socket("localhost",4000);
            this.output = new ObjectOutputStream(socket.getOutputStream()); 
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return socket;
    }
    public void send(ChatMessage message){
        try {
            output.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
