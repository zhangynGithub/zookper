package com.itmayiedu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.I0Itec.zkclient.ZkClient;

//##ServerScoekt服务端
public class ZkServerScoekt implements Runnable {
	private int port = 18081;

	public static void main(String[] args) throws IOException {
		int port = 18081;
		ZkServerScoekt server = new ZkServerScoekt(port);
		Thread thread = new Thread(server);
		thread.start();
	}

	public ZkServerScoekt(int port) {
		this.port = port;
	}

	// 将服务信息注册到注册中心上去
	public void regServer() {
		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 6000, 1000);
		String path = "/member/server-" + port;
		if (zkClient.exists(path)) {
			zkClient.delete(path);
		}
		String value="127.0.0.1:" + port;
		zkClient.createEphemeral(path, "127.0.0.1:" + port);
		System.out.println("##服务注册成功###"+value);
	}

	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			regServer();
			System.out.println("Server start port:" + port);
			Socket socket = null;
			while (true) {
				socket = serverSocket.accept();
				new Thread(new ServerHandler(socket)).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null) {
					serverSocket.close();
				}
			} catch (Exception e2) {

			}
		}
	}

}
