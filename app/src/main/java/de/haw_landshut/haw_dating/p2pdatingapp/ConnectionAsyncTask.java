package de.haw_landshut.haw_dating.p2pdatingapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Tobias on 06.06.2016.
 */
public class ConnectionAsyncTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "ConnectionAsyncTask";
    private P2pInterface p2pInterface;
    public static final int PORT = 8888;
    public static final String veryImporantMessage= "Huhu, it's working";

    public ConnectionAsyncTask(P2pInterface p2pInterface){
        this.p2pInterface = p2pInterface;
    }


    @Override
    protected String doInBackground(String[] params) {
        if (p2pInterface.getIsGroupOwner()){
            try {
                Log.d(TAG, "Point 1");
                ServerSocket serverSocket = new ServerSocket(PORT);
                Socket client = serverSocket.accept();


                Log.d(TAG, "Point 2");
                DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
                dataOutputStream.writeUTF(p2pInterface.getProfile());

                Log.d(TAG, "Point 3");
                DataInputStream inputStream = new DataInputStream(client.getInputStream());
                String msgReceived = inputStream.readUTF();
                //activity.setTxtStatus("Incoming Message: " + msgReceived);
                Log.d(TAG, "Point 4");
                p2pInterface.setShouldDisconnect(true);
                dataOutputStream.close();
                inputStream.close();
                client.close();
                serverSocket.close();
                p2pInterface.setIsGroupOwner(false);
                //p2pInterface.disconnect();
                return msgReceived;
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }

        else{
            try {
                if (p2pInterface.getGroupOwnerAdress() == null){
                    return null;
                }
                Log.d(TAG, "Point 5");
                Socket socket = new Socket();
                socket.bind(null);

                Log.d(TAG, "Point 6");
                socket.connect(new InetSocketAddress(p2pInterface.getGroupOwnerAdress(), PORT), 500);
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String message = dataInputStream.readUTF();

                Log.d(TAG, "Point 7");
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(p2pInterface.getProfile());

                Log.d(TAG, "Point 8");
                p2pInterface.setShouldDisconnect(true);
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();
                return message;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return null;
    }


}
