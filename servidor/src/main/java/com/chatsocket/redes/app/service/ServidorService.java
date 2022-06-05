/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chatsocket.redes.app.service;

import com.chatsocket.redes.app.bean.ChatMessage;
import com.chatsocket.redes.app.bean.ChatMessage.Action;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manoelaugusto
 */
public class ServidorService {

    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String, ObjectOutputStream> mapOnlines = new HashMap<String, ObjectOutputStream>();

    public ServidorService() {
        try {
            serverSocket = new ServerSocket(4000);

            System.out.println("Servidor ON!");

            while (true) {
                socket = serverSocket.accept();

                new Thread(new ListenerSocket(socket)).start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class ListenerSocket implements Runnable {

        private ObjectOutputStream output;
        private ObjectInputStream input;

        public ListenerSocket(Socket socket) {
            try {
                this.output = new ObjectOutputStream(socket.getOutputStream());
                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        public void run() {
            ChatMessage message = null;
            try {
                while ((message = (ChatMessage) input.readObject()) != null) {
                    ChatMessage.Action action = message.getAction();

                    if (action.equals(ChatMessage.Action.CONNECT)) {
                        boolean isConnect = connect(message, output);
                        if (isConnect) {
                            mapOnlines.put(message.getName(), output);
                            sendOnlines();
                        }

                    } else if (action.equals(ChatMessage.Action.DISCONNECT)) {
                        disconnect(message, output);
                        return;

                    } else if (action.equals(ChatMessage.Action.SEND_ONE)) {
                        sendOne(message, output);

                    } else if (action.equals(ChatMessage.Action.SEND_ALL)) {
                        sendAll(message);

                    } else if (action.equals(ChatMessage.Action.USER_ONLINE)) {

                    }

                }
            } catch (IOException e) {
                disconnect(message, output);
                System.out.println(message.getName() + " deixou a sala");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private boolean connect(ChatMessage message, ObjectOutputStream output) {
        if (mapOnlines.size() == 0) {
            message.setText("Yes");
            sendOne(message, output);
            return true;
        }
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            if (kv.getKey().equals(message.getName())) {
                message.setText("No");
                sendOne(message, output);

                return false;
            } else {
                message.setText("Yes");
                sendOne(message, output);
                return true;
            }
        }

        return false;
    }

    private void disconnect(ChatMessage message, ObjectOutputStream output) {
        mapOnlines.remove(message.getName());

        message.setText(" até logo!");

        message.setAction(Action.SEND_ONE);

        sendAll(message);

        System.out.println("Usuario: " + message.getName() + "  Adeus! Ate logo.");

    }

    private void sendOne(ChatMessage message, ObjectOutputStream output) {
        try {
            output.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendAll(ChatMessage message) {
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            if (!kv.getKey().equals(message.getName())) {
                message.setAction(Action.SEND_ONE);
                try {
                    kv.getValue().writeObject(message);
                } catch (IOException ex) {
                    Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

    }

    private void sendOnlines() {
        Set<String> setNames = new HashSet<String>();
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            setNames.add(kv.getKey());
        }

        ChatMessage message = new ChatMessage();
        message.setAction(Action.USER_ONLINE);
        message.setSetOnline(setNames);
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            message.setName(kv.getKey());
            try {
                kv.getValue().writeObject(message);
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
