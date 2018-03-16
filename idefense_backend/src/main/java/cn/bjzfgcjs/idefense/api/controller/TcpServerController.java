package cn.bjzfgcjs.idefense.api.controller;

import cn.bjzfgcjs.idefense.core.web.WebResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

@RestController
@RequestMapping("/idefense")
public class TcpServerController{
    @PostMapping(value = "/tcpserver", produces = "application/json; charset=UTF-8")
    public Object getData(@RequestParam(value="port", defaultValue="8899")Integer port)  throws Exception{
        //建立服务端socket服务，并监听一个port
        ServerSocket ss = new ServerSocket(port);
        //通过accept方法获取连接过来的client对象
        Socket s = ss.accept();
        String ip = s.getInetAddress().getHostAddress();
        System.out.println(ip);
        //获取client发送过来的数据。使用client对象的读取流来读取数据
        DataInputStream  in= new DataInputStream(s.getInputStream());
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        byte[] buf = new byte[1024];
        int len = in.read(buf);
        System.out.println(new String(buf,0,len));
        //发送数据给客户端
        DataOutputStream ds=new DataOutputStream(s.getOutputStream());
        ds.write("have receive\r\n".getBytes());
        //关闭client
        s.close();
        ss.close();
        return WebResponse.write(new String(buf,0,len));
    }
}
