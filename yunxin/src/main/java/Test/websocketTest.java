//package Test;
//
//
//import java.net.URI;
//import java.net.URISyntaxException;
//
//import javax.websocket.Session;
//
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
//
//public class websocketTest extends WebSocketClient {
//
//	public websocketTest(URI serverURI) {
//		super(serverURI);
//	}
//
//	@Override
//	public void onClose(int arg0, String arg1, boolean arg2) {
//		System.out.println("close");
//	}
//
//	@Override
//	public void onError(Exception arg0) {
//		System.out.println("err");
//	}
//
//	@Override
//	public void onMessage(String arg0) {
//		System.out.println(arg0);
//
//	}
//
//	@Override
//	public void onOpen(ServerHandshake arg0) {
//		System.out.println("open");
//		String message = "{\"cmd\":\"enter\",\"params\":{\"accId\":\"54ea53e0e5634af7bd3dfb5e49eb1d41\",\"cId\":\"f64999b4bb4242eab32b2abd3e9dc684\"}}";
//		send(message);
//	}
//
//	public Session session;
//
////	protected void start() {
////		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
////		String uri = "ws://127.0.0.1:8080/websocket";
////		System.out.println("Connecting to" + uri);
////		try {
////			session = container.connectToServer(websocketTest.class, URI.create(uri));
////		} catch (DeploymentException e) {
////			e.printStackTrace();
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////	}
//
//	public static void main(String[] args) throws URISyntaxException {
//		websocketTest client = new websocketTest(new URI("ws://127.0.0.1:8080/websocket"));
//		client.connect();
//		System.out.println("1111111111111111111");
//		
////		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
////		String input ="";
////		try { 
////			do{ 
////				input = br.readLine();  
////				if(!input.equals("exit")) 
////					client.session.getBasicRemote().sendText(input);
////				}while(!input.equals("exit")); 
////			} catch (IOException e) {} }
////		}
//	}
//
//}
