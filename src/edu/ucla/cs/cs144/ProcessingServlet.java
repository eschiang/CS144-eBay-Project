package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.util.Date;

public class ProcessingServlet extends HttpServlet implements Servlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	HttpSession session = request.getSession(true);
		if (session.isNew() == true || !request.isSecure()) 
		{
			request.setAttribute("valid", "false");
			session.invalidate();
			//Bad session
		} else {
			//Good session, complete purchase
			request.setAttribute("valid", "true");
			Item item = (Item) session.getAttribute("item");
			request.setAttribute("item", item);

			// Get the credit card number from the POSTed form
			String cardNumber = (String) request.getParameter("cardNumber");
			request.setAttribute("cardNumber", cardNumber);

			// Pass the time
			Date date = new Date();
			request.setAttribute("time", date.toString());
		}

    	// Redirect to confirmation page
    	request.getRequestDispatcher("./confirmation.jsp").forward(request, response);
        
    }
}