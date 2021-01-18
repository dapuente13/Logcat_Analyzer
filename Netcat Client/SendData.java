package com.app.netcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.lang.Integer;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by tlehman at 2014-Feb-13
 *
 * This AsyncTask wraps a socket that connects to (ip,port) and writes
 * the contents of body as a stream of bytes.
 */
public class SendData extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... args) {
        try {
            String ip = args[0], filtro = args[2];
            int port = Integer.parseInt(args[1]);

            InetSocketAddress addr = new InetSocketAddress(ip, port);
            Socket socket = new Socket();
            socket.connect(addr);
            OutputStream stream = socket.getOutputStream();

            filtro = filtro + '\n';
            stream.write(filtro.getBytes());
            socket.close();
            Thread.sleep(8000);
            socket = new Socket();
            socket.connect(addr);
            stream = socket.getOutputStream();
            while(!isCancelled()){
                Process process = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});
                Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                String line = bufferedReader.readLine();
                String tmp = line;
                while ((line = bufferedReader.readLine()) != null) {
                    line += '\n';
                    if (line != tmp){
                        tmp = line;
                        stream.write(line.getBytes());
                    }
                }
            }
            socket.close();

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            Log.e("SocketExceptionTag", e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("IOExceptionTag", e.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}