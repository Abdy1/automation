package com.abdydidit.automation.service;


import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Scanner;

@Service
public class Initiator {
    public static void executeBatch(String host, String command){
        try{
            JSch jsch = new JSch();

            // Prompt the user for SSH username and password
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter SSH username: ");
            String username = scanner.nextLine();
            System.out.print("Enter SSH password: ");
            String password = scanner.nextLine();

            Session session = jsch.getSession(username,host,22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking","no");
            session.connect();

            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);
            channelExec.setInputStream(null);
            channelExec.setErrStream(System.err);

            java.io.InputStream in = channelExec.getInputStream();
            channelExec.connect();


            java.io.InputStream err = channelExec.getErrStream();

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    System.out.print(new String(tmp, 0, i));
                }
                while (err.available() > 0) {
                    int i = err.read(tmp, 0, 1024);
                    if (i < 0) break;
                    System.err.print(new String(tmp, 0, i));
                }
                if (channelExec.isClosed()) {
                    if (in.available() > 0 || err.available() > 0) continue;
                    System.out.println("exit-status: " + channelExec.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ignored) {
                }
            }

            // Disconnect
            channelExec.disconnect();
            session.disconnect();


        } catch ( JSchException | IOException e){
          e.printStackTrace();
        }
    }
}
