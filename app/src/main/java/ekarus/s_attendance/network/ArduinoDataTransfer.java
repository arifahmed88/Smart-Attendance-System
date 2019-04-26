/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ekarus.s_attendance.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bigdaddy
 */
public class ArduinoDataTransfer implements Observer {

    public final Vector<String> studentsId = new Vector<>();
    public boolean finished;
    private Socket s;
    private BufferedReader input;
    private DataOutputStream outToServer;
    int timeout = 20;
    int currentStudentIdVersion = 1;

    public ArduinoDataTransfer() {

    }

    public void startAttendance() throws IOException, InterruptedException {
        s = new Socket("192.168.4.1", 23);
        input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        outToServer = new DataOutputStream(s.getOutputStream());
        outToServer.writeBytes("\n");
        String answer = input.readLine();
        System.out.println(">>>>>>>>>>>>>>>>>>>" + answer);
        finished = false;

        outToServer.writeBytes("sendFingerDataVer.\n");
        answer = input.readLine();
        System.out.println(answer);
        int serverFingerDataVer = Integer.valueOf(answer);

        if (serverFingerDataVer != currentStudentIdVersion) {
            outToServer.writeBytes("sendFingerData.\n");
            while (true) {
                answer = input.readLine();
                if(answer.contains("endSync")) break;
                System.out.println("syncID:" + answer);
            }
        }
        currentStudentIdVersion = serverFingerDataVer;

        outToServer.writeBytes("startAttendance.\n");
        getAttendance();
    }

    private void getAttendance() {
        Thread ReceiverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (finished) {
                        System.err.println("timeouted");
                        break;
                    }
                    String answer;
                    try {
                        if ((s.getInputStream()).available() != 0) {
                            answer = input.readLine();
                            if (answer.contains("okstopAt.")) {
                                s.close();
                                System.out.println("finished Attendence");
                                finished = true;
                                break;

                            }
                            System.out.println(answer);
                            studentsId.add(answer);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ArduinoDataTransfer.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });

        ReceiverThread.start();

    }

    private void stopAttendance() throws IOException, InterruptedException {
        outToServer.writeBytes("stopAt.\n");
        TimeUnit.MILLISECONDS.sleep(50);
    }

    @Override
    public void update(Observable o, Object o1) {
        long startTimeOfStopButton = System.currentTimeMillis();
        while (!finished) {
            if (System.currentTimeMillis() - startTimeOfStopButton > timeout * 1000) {
                finished = true;
                break;
            }
            try {
                stopAttendance();
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(ArduinoDataTransfer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
