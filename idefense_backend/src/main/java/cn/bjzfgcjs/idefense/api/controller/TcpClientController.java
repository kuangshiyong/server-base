package cn.bjzfgcjs.idefense.api.controller;

import cn.bjzfgcjs.idefense.core.web.WebResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

@RestController
@RequestMapping("/idefense")
public class TcpClientController {
    @PostMapping(value = "/tcpclient", produces = "application/json; charset=UTF-8")
    public Object getData (@RequestParam(value = "host",defaultValue = "192.168.1.110") String host, @RequestParam(value="port", defaultValue="8899")Integer port, @RequestParam(value = "data",defaultValue = "789456") String data )  throws Exception{
        //建立socket服务。指定连接的主机和port
        Socket s=new Socket(host,port);
        //获取socket流中的输出流。将数据写入该流，通过网络传送给服务端
        DataOutputStream dOutputStream = new DataOutputStream(s.getOutputStream());
        dOutputStream.write(data.getBytes());
        //获取socket流中的输入流。将服务端反馈的数据获取到，并打印
        InputStream in = s.getInputStream();
        byte[] buf = new byte[1024];
        int len = in.read(buf);
        System.out.println(new String(buf,0,len));
        s.close();
        return WebResponse.write(new String(buf,0,len));
    }
}
/*public  class TcpClientController{

    public static void main(String[] args) {
        //建立socket服务。指定连接的主机和port
        Socket s = new Socket("192.168.1.10",10004);
        //获取socket流中的输出流。将数据写入该流，通过网络传送给服务端
        OutputStream out = (OutputStream) s.getOutputStream();
        out.write("hello Tcp".getBytes());
        //获取socket流中的输入流。将服务端反馈的数据获取到，并打印
        InputStream in = s.getInputStream();
        byte[] buf = new byte[1024];
        int len = in.read(buf);
        System.out.println(new String(buf,0,len));
        s.close();

    }
    }*/




