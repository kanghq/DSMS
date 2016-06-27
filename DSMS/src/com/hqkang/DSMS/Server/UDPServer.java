package com.hqkang.DSMS.Server;

import java.io.IOException;
import java.net.*;

public class UDPServer {
	private byte[] buffer = new byte[1024];    
    private DatagramSocket socket = null;    
    private DatagramPacket packet = null;    
    private InetSocketAddress socketAddress = null;    
    private InetAddress clientAddress;
    private String clientIP;
    private int clientPort;
    private String clientData;
    
    public UDPServer(String ip, int port) throws SocketException {
    	Bind(ip, port);
    	System.out.println(ip+" "+ port +" "+"UDP Srv Started");
    }
    public void Bind(String ip, int port) throws SocketException {    
       //create socket
        socketAddress = new InetSocketAddress(ip, port);  
        // bind to local address
        socket = new DatagramSocket(socketAddress);
        packet = new DatagramPacket(buffer, buffer.length);
    }  
    
    public final String getOrgIp() {    
        return clientIP;    
    } 
    
    public String Receive() throws IOException {
        
        socket.receive(packet);
        // get client address
        clientAddress = packet.getAddress();
        clientIP = clientAddress.getHostAddress();
        // get clien port
        clientPort = packet.getPort();
        // get data
        clientData = new String(packet.getData(), 0, packet.getLength());     
        return clientData;    
    }    
    
    public void Send(String content) throws IOException {    
        packet.setAddress(clientAddress);
        packet.setPort(clientPort);
        packet.setData(content.getBytes()); 
        
     
        socket.send(packet);    
    } 
    public void close() {    
        try 
        {    
            socket.close();    
        } 
        catch (Exception ex) 
        {    
            ex.printStackTrace();    
        }    
    }    
    
    

}
