import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpTest {

	public static void main(String[] args) {

//		doPost
		
	}
	
	
	
	
	private static String getParameters(Map<String, Object> map){
		StringBuffer buffer=new StringBuffer();
		if(map==null || map.isEmpty()){
			return "";
		}else{
			for (String key : map.keySet()) {
				String value=String.valueOf(map.get(key));
				if(buffer.length()<1){
					buffer.append(key).append("=").append(value);
				}else{
					buffer.append("&").append(key).append("=").append(value);
				}
			}
		}
		return buffer.toString();
	} 
	
	public static String doGet(String url,Map<String, Object> map) {
		String result="";
		try {
			URL urlGet=new URL(url+"?"+getParameters(map)); 
			HttpURLConnection  conn=(HttpURLConnection) urlGet.openConnection();
			conn.setRequestMethod("GET");
			result=getResult(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String doPost(String url,Map<String, Object> map) {
		String result="";
		try {
			//http://192.168.0.151:8080/mymoney/index.do?m=user&a=unique
			URL urlPost=new URL(url);
			HttpURLConnection conn=(HttpURLConnection) urlPost.openConnection();
			conn.setRequestMethod("POST");//设置请求方式
			conn.setDoInput(true);//设置可以使用输入流读取数据
			conn.setDoOutput(true);//设置可以使用输出流写数据
			conn.setUseCaches(false);//不使用缓存
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Charset", "utf-8");
			String data=getParameters(map);
			conn.setRequestProperty("Connection", "Keep-Alive");
			//写
			OutputStream out=conn.getOutputStream();
			out.write(data.getBytes());
			out.flush();//刷新
			out.close();
			result=getResult(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static String getResult(HttpURLConnection conn){
		StringBuffer buffer=null;
		try {
			BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			buffer=new StringBuffer();
			String line=null;
			while(null!=(line=reader.readLine())){
				buffer.append(line);
			}
			reader.close();
			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer==null?"":buffer.toString();
	}
	

}
