package com.hqkang.DSMS.Client;
import java.io.IOException;
import java.net.*;

public class UDPClient {
	
	private byte[] buffer = new byte[1024];  
    private DatagramSocket socket = null;  
    
    private String clientData;
    private DatagramPacket sendPacket;
    private DatagramPacket receivePacket;

    
    public UDPClient() throws Exception {  
    	//random port number
        socket = new DatagramSocket();  
    }  
    public DatagramPacket Send(String ip,int port,String content) throws IOException  {  
        byte[] bytes = content.getBytes();
        sendPacket = new DatagramPacket(bytes, bytes.length);
        sendPacket.setAddress(InetAddress.getByName(ip));
        sendPacket.setPort(port);
        socket.send(sendPacket);  
        return sendPacket;  
    }  
    
    public String Receive()   throws Exception 
    {  
        receivePacket = new DatagramPacket(buffer, buffer.length);  
        socket.receive(receivePacket);  
      
        clientData = new String(receivePacket.getData(), 0, receivePacket.getLength());     
        return "["+clientData+"]"; 
    }  
    
    public void close() 
    {  
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
