package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ChatThread extends Server implements Runnable {

    Socket s = null;
    private BufferedReader br = null;
    public PrintStream ps = null;
    public boolean canRun = true;
    String nickName = null;

    public ChatThread(Socket s) throws Exception {
        this.s = s;
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        ps = new PrintStream(s.getOutputStream());
    }

    public void run() {
        while (canRun) {
            try {
                String msg = br.readLine();//接收客户端发来的消息
                String[] strs = msg.split("#");
                switch (strs[0]) {
                    case "LOGIN": //收到来自客户端的上线消息
                        nickName = strs[1];
                        dl.addElement(nickName);
                        userList.repaint();
                        sendMessage(msg);
                        break;
                    case "MSG":
                    case "SMSG":
                    case "FSMSG":
                        sendMessage(msg);
                        break;
                    case "OFFLINE": //收到来自客户端的下线消息
                        sendMessage(msg);
                        //System.out.println(msg);
                        dl.removeElement(strs[1]);
                        // 更新List列表
                        userList.repaint();
                        break;
                }
            } catch (Exception ignored) {

            }
        }
    }


}
