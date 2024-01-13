package Mypackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;




@WebServlet("/MyServlet")

public class MyServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
	String inputdata = req.getParameter("searchInput");
//	System.out.println(inputdata);
	String apiKey = "cf543bd80d927e4800db3ec4571b076c";
	String city = req.getParameter("city");
	
	String apiurl = "https://api.openweathermap.org/data/2.5/weather?q=new%20delhi&appid=cf543bd80d927e4800db3ec4571b076c";
	
	
	URL url = new URL(apiurl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    
    // READING DATA FROM NETWORK 
    InputStream inputStream = connection.getInputStream();
    InputStreamReader reader = new InputStreamReader(inputStream);
    
    // STORING IN THE STRING 
 
    StringBuilder responseContent = new StringBuilder();
  
    //for taking input 
    Scanner scanner = new Scanner(reader);
    
 while (scanner.hasNext()) {
	responseContent.append(scanner.nextLine());
}
    scanner.close();
  
 // typcasting into json 
    Gson gson = new Gson();
    JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
    
    //Date & Time
    long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
    String date = new Date(dateTimestamp).toString();
    
    //Temperature
    double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
    int temperatureCelsius = (int) (temperatureKelvin - 273.15);
   
    //Humidity
    int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
    
    //Wind Speed
    double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
    
    //Weather Condition
    String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
    req.setAttribute("date", date);
    req.setAttribute("city", city);
    req.setAttribute("temperature", temperatureCelsius);
    req.setAttribute("weatherCondition", weatherCondition); 
    req.setAttribute("humidity", humidity);    
    req.setAttribute("windSpeed", windSpeed);
    req.setAttribute("weatherData", responseContent.toString());
    
    connection.disconnect();
	
    

    // Forward the request to the weather.jsp page for rendering
    req.getRequestDispatcher("index.jsp").forward(req, resp);
	}

}
