package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import edu.ucla.cs.cs144.*;

public class PaymentServlet extends HttpServlet implements Servlet {
	public PaymentServlet() 
	{}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		if (session.isNew()==true) 
		{
			request.setAttribute("valid", "false");
			session.invalidate();
			//Bad session
		}
		else {
			//Good session, pass item along to purchase
			request.setAttribute("valid", "true");
			Item item = (Item) session.getAttribute("item");
			request.setAttribute("item", item);
		}
		request.getRequestDispatcher("./purchase.jsp").forward(request, response);
	}
}
