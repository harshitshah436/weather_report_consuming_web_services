package com.rit.pa1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet class to handle http request and response from the server.
 *
 * @author Harshit
 */
public class WeatherFromZipServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String zipcode = request.getParameter("zipcode");
        WeatherFromZip wfz = new WeatherFromZip(zipcode);

        boolean googleApi_success;
        try {
            googleApi_success = wfz.callGoogleGeoCodingApi();

            System.out.println("com.rit.pa1.WeatherFromZipServlet.processRequest()");
            if (!googleApi_success) {
                request.setAttribute("error", "Please enter a valid 5-digit zip code. <br> Google Geocoding api couldn't find location associated with the zip.");
                getServletConfig().getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
            }

            String[] geoDetails = {wfz.city, wfz.country, Double.toString(wfz.latitude), Double.toString(wfz.longitude)};
            String xml_response = wfz.callGlobalWeatherWebService();

            if (xml_response.equalsIgnoreCase("Data Not Found") || xml_response.isEmpty() || xml_response == null) {
                System.err.println("Sorry, given location doesn't supported by the GlobalWeather api.");
                System.err.println("callGlobalWeatherWebService() - no data found");
                request.setAttribute("error", "Sorry, given location doesn't supported by the GlobalWeather api. <br> callGlobalWeatherWebService() - no data found");
                getServletConfig().getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
            }

            String json_response = wfz.callSunriseSunsetWebService();

            if (json_response.isEmpty() || json_response == null) {
                System.err.println("Sorry, given location doesn't supported by the Sunrise-Sunset api.");
                System.err.println("callSunriseSunsetWebService() - no data found");
                request.setAttribute("error", "Sorry, given location doesn't supported by the Sunrise-Sunset api. <br> callSunriseSunsetWebService() - no data found");
                getServletConfig().getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
            }

            ArrayList<String> weather_xml_data = wfz.parseXMLUsingDomParser(xml_response);
            ArrayList<String> sunriseset_json_data = wfz.parseJsonUsingJsonSimpleParser(json_response);

            System.out.println("com.rit.pa1.WeatherFromZipServlet.processRequest(): Length: " + sunriseset_json_data.size());

            request.setAttribute("geo", geoDetails);
            request.setAttribute("xmllist", weather_xml_data);
            request.setAttribute("jsonlist", sunriseset_json_data);
            getServletConfig().getServletContext().getRequestDispatcher("/response.jsp").forward(request, response);

        } catch (Exception ex) {
            System.err.println("Please enter a valid 5-digit zip code.");
            request.setAttribute("error", "Please enter a valid 5-digit zip code.");
            getServletConfig().getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
            Logger.getLogger(WeatherFromZipServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
