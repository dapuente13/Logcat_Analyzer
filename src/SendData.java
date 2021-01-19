package com.app.netcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.lang.Integer;
import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by tlehman at 2014-Feb-13
 *
 * This AsyncTask wraps a socket that connects to (ip,port) and writes
 * the contents of body as a stream of bytes.
 */
public class SendData extends AsyncTask<String, Void, String> {
    private Context context;
    private boolean stop = false;
    private boolean receive;

    public SendData(Context mContext, boolean receive) { this.context = mContext; this.receive = receive; }

    @Override
    protected String doInBackground(String... args) {
        String msg = "";
        try {
            String ip = args[0], filtro = args[2];
            String[] valores_filtro;
            int port = Integer.parseInt(args[1]);
            final int device_id;
            boolean isFound;
            String line;

            Thread.sleep(8000);

            InetSocketAddress addr = new InetSocketAddress(ip, port);
            Socket socket = new Socket();
            socket.connect(addr);

            if(!receive) {
                stop = false;
                device_id = new Random().nextInt(10000);
                valores_filtro = filtro.split(",");

                OutputStream ostream = socket.getOutputStream();

                while(!stop) {
                    Process process = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});
                    Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                    String tmp = "";
                    while ((line = bufferedReader.readLine()) != null) {

                        if (line != tmp && line.charAt(0) != '-') {
                            tmp = line;
                            line = device_id + " " + line + '\n';
                            isFound = (valores_filtro[0] == "");

                            for (int i = 0; i < valores_filtro.length && !isFound; ++i)
                                isFound = isFound || line.toLowerCase().contains(valores_filtro[i]);

                            if(isFound)
                                ostream.write(line.getBytes());
                        }
                    }
                }
            }

            else {
                InputStream istream = socket.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(istream));

                while(!(line = in.readLine()).equals("end")) {
                    msg += line + '\n';
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

        return msg;
    }

    protected void setStop(boolean st) { stop = st; }

    protected boolean getStop() { return stop; }

    @Override
    protected void onPostExecute(String msg) {
        if(receive) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();

            alertDialog.setTitle("Results");
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.setMessage(msg);
            alertDialog.show();
        }

        stop = true;
    }

}