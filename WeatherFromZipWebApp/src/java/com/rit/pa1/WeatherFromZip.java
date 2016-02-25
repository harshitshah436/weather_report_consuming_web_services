package com.rit.pa1;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * WeatherFromZip class is a controller class for invoking all the web services.
 *
 * @author Harshit
 */
public class WeatherFromZip {

    private static final String GOOGLE_MAPS_API_KEY
            = "AIzaSyBn_NKRZ4l0TI_Dvx772u8uEfjhIUPipnI";

    public static int zipcode;

    public double latitude;
    public double longitude;
    public String city;
    public String country;

    /**
     * Construct an object with zip code entered by user.
     *
     * @param zipcode
     */
    public WeatherFromZip(String zipcode) {
        WeatherFromZip.zipcode = Integer.parseInt(zipcode);
    }

    /**
     * Invoke Google Geocoding Api using Restful web services.
     *
     * @return true if Google Geocoding Api is successfully invoked.
     * @throws Exception
     */
    public boolean callGoogleGeoCodingApi() throws Exception {
        // Call Web Service Operation
        GeoApiContext context = new GeoApiContext().setApiKey(GOOGLE_MAPS_API_KEY);
        GeocodingResult[] results = GeocodingApi.geocode(context,
                Integer.toString(zipcode)).await();
        if (results.length > 0) {
            city = results[0].addressComponents[1].longName;
            country = results[0].addressComponents[results[0].addressComponents.length - 1].longName;
            latitude = results[0].geometry.location.lat;
            longitude = results[0].geometry.location.lng;
        } else {
            System.err.println("Sorry no information found for this zip code.");
            System.err.println("callGoogleGeoCodingApi() - null returned");
            return false;
        }
        return true;
    }

    /**
     * Invoking Global Weather API using SOAP web services.
     *
     * @return a string containing XML response from the API.
     */
    public String callGlobalWeatherWebService() {

        net.webservicex.GlobalWeather service = new net.webservicex.GlobalWeather();
        net.webservicex.GlobalWeatherSoap port = service.getGlobalWeatherSoap();

        return port.getWeather(city, country);
    }

    /**
     * Invoking sunrise sunset api using Restful web service.
     *
     * @return a string containing Json response from the API.
     *
     * @throws Exception
     */
    public String callSunriseSunsetWebService() throws Exception {

        String url = "http://api.sunrise-sunset.org/json?lat=" + latitude + "&lng=" + longitude;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        System.out.println("\nSending 'GET' request to URL : " + url);

        StringBuilder response;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        
        return response.toString();
    }

    /**
     * Parse an xml response using Java DOM parser.
     * 
     * @param xml_response
     * @return
     * @throws Exception 
     */
    public ArrayList<String> parseXMLUsingDomParser(String xml_response) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml_response));
        Document doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("CurrentWeather");
        Node nNode = nList.item(0);
        ArrayList<String> weatherXMLResponse = new ArrayList<>();

        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) nNode;
            weatherXMLResponse.add(eElement.getElementsByTagName("Location").item(0).getTextContent());
            weatherXMLResponse.add(eElement.getElementsByTagName("Time").item(0).getTextContent());
            weatherXMLResponse.add(eElement.getElementsByTagName("SkyConditions").item(0).getTextContent());
            weatherXMLResponse.add(eElement.getElementsByTagName("Temperature").item(0).getTextContent());
            weatherXMLResponse.add(eElement.getElementsByTagName("RelativeHumidity").item(0).getTextContent());
        }
        return weatherXMLResponse;
    }

    /**
     * Parse json response using Json library.
     * 
     * @param json_response
     * @return 
     */
    public ArrayList<String> parseJsonUsingJsonSimpleParser(String json_response) {
        ArrayList<String> sunriseset_json_data = new ArrayList<>();

        JSONObject jObj = new JSONObject(json_response);
        JSONObject newjObj = jObj.getJSONObject("results");

        sunriseset_json_data.add(newjObj.getString("sunrise"));
        sunriseset_json_data.add(newjObj.getString("sunset"));
        sunriseset_json_data.add(newjObj.getString("day_length"));
        sunriseset_json_data.add(newjObj.getString("civil_twilight_begin"));
        sunriseset_json_data.add(newjObj.getString("nautical_twilight_begin"));
        sunriseset_json_data.add(newjObj.getString("astronomical_twilight_begin"));

        return sunriseset_json_data;
    }
}
