package util;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;

public class FTPScanner {
	/**
	 * 服务器域名
	 */
	private String hostname;
	/**
	 * FTP服务器地址
	 */
	private String host;
	/**
	 * FTP服务器端口号
	 */
	private int port = 20;
	/**
	 * 扫描器开始IP地址
	 */
	private String begHost;
	/**
	 * 扫描器结束IP地址
	 */
	private String endHost;
	
	private FTPClient ftpClient = new FTPClient();
	/**
	 * 扫描到的FTP服务器IP地址
	 */
	public List<String> ftpServerList = new ArrayList<>();
	
	//--------------------------------------------------------------
	public FTPScanner(){
		
	}
	
	public FTPScanner(String begHost, String endHost){
		this();
		this.begHost = begHost;
		this.endHost = endHost;
	}
	
	//--------------------------------------------------------------
	
	/**
	 * 判断一个IP地址是否为FTP服务器
	 * @param host
	 * @return
	 */
	public boolean isFTPServer(String host){
		boolean isConnected = false;
		try {
			System.out.print("Scanning:" + host);
			this.ftpClient.connect(host);
			isConnected = this.ftpClient.isConnected();
			//System.out.println("isConnected()=" + isConnected);
			if(isConnected){
				FTPScanner.this.ftpClient.disconnect();
			}
			
		}catch(ConnectException e){ // 若不是FTP服务器则抛出java.net.ConnectException: Connection timed out: connect
			//System.out.println("ConnectException");
			//e.printStackTrace();
		}catch (SocketException e) {
			System.out.println("SocketException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}
		return isConnected;
	}
	
	public int scanFTPServer(){
		return this.scanFTPServer(this.begHost, this.endHost);
	}
	
	/**
	 * 开始扫描
	 * @param begIP 开始扫描的IP地址
	 * @param endIP 结束扫描的IP地址
	 * @return 本次该方法扫描到的FTP服务器数量
	 */
	private int scanFTPServer(String begIP, String endIP){
		int n = 0;
		while(!begIP.equals(endIP)){
			if(this.isFTPServer(begIP)){
				System.out.println(" is FTP Server");
				this.ftpServerList.add(begIP);
				n++;
			}else{
				System.out.println(" is not");
			}
			begIP = this.addIPAddress(begIP);
		}
		return n;
	}
	
	/**
	 * 
	 * @param ip
	 * @return 自增1的IP
	 */
	private String addIPAddress(String ip){
		String[] buf = ip.split("[.]");
		Integer[] work = new Integer[4];
		for(int i = 0; i < 4; i++){
			work[i] = Integer.valueOf(buf[i]);
		}
		work[3]++;
		for(int i = 3; i >= 0; i--){
			if(work[i] >= 256){
				work[i] = 0;
				if(i > 0){
					work[i-1]++;
				}else{
					
				}
			}
		}
		return work[0] + "." + work[1] + "."+ work[2] + "." + work[3];
	}
	//--------------------------------------------------------------
	public static void main(String[] args) {
		System.out.println("main");
		FTPScanner ftp = new FTPScanner("172.26.14.34", "172.26.14.38");
		System.out.println(ftp.isFTPServer("192.168.1.105"));
		//int n = ftp.scanFTPServer();
		//System.out.println("n=" + n);
	}
}
