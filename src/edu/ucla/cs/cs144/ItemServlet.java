package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
/
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import java.lang.Object;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    String bidstring;
                     SortedSet bidset = new TreeSet(); 



///////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////Our Code from Mypareser.java///////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
      public static class Category {
        int c_categoryID;
        String c_name;

        public Category(int categoryID, String name) {
            c_categoryID = categoryID;
            c_name = name;
        }
    }

    /**
* Item Category
* Helper class to represent a single entry in Item_Category.csv
*/
    public static class ItemCategory {
        String ic_itemID;
        int ic_categoryID;

        public ItemCategory(String itemID, int categoryID) {
            ic_itemID = itemID;
            ic_categoryID = categoryID;
        }
    }

    /**
* User
* Helper class to represent a single entry in User.csv
*/
    public static class User {
        String u_name;
        String u_location;
        String u_rating;
        String u_country;

        User(String name, String rating, String location, String country) {
            u_name = name;
            u_rating = rating;
            u_location = location;
            u_country = country;
        }
    }
 
    /**
* Bid
* Helper class to represent a single entry in Bid.csv
*/
    public static class Bid
    {
        String b_userid;
        String b_time;
        String b_item;
        String b_amount;


        Bid(String userID, String time, String itemID, String amount)
        {
          
            b_userid = userID;
            b_time = time;
            b_item = itemID;
            b_amount = amount;
        }
    }

    /**
* Item
* Helper class to represent a single entry in Item.csv
*/
    public static class Item {
        String i_id;
        String i_name;
        String i_description;
        String i_started;
        String i_ends;
        String i_currently;
        String i_firstbid;
        String i_buyprice;
        String i_numberofbids;
        String i_seller;

        Item(String id, String name, String description, String started, String ends, String currently,
                String firstbid, String buyprice, String numberofbids, String seller) {

            i_id = id;
            i_name = name;
            i_description = description;
            i_started = started;
            i_ends = ends;
            i_currently = currently;
            i_firstbid = firstbid;
            i_buyprice = buyprice;
            i_numberofbids = numberofbids;
            i_seller = seller;
        }
    }
    
    // Maps which will hold entries for .csv files while parsing
    static Map<String, User> userMap = new HashMap<String, User>();
    static Map<String, Bid> bidMap = new HashMap<String, Bid>();
    static Map<String, Item> itemMap = new HashMap<String, Item>();
    static Map<String, Category> categoryMap = new HashMap<String, Category>();
    static Map<String, ItemCategory> itemcategoriesMap = new HashMap<String, ItemCategory>();
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;

    static final String[] typeName = {
    "none",
    "Element",
    "Attr",
    "Text",
    "CDATA",
    "EntityRef",
    "Entity",
    "ProcInstr",
    "Comment",
    "Document",
    "DocType",
    "DocFragment",
    "Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
*/
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
* null if one does not exist. NR means Non-Recursive.
*/
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
* type #PCDATA) as child, or "" if it contains no text.
*/
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
* of e with the given tagName. If no such X exists or X contains no
* text, "" is returned. NR means Non-Recursive.
*/
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
* like $3,453.23. Returns the input if the input is an empty string.
*/
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }

    /* Returns the date from the xml files' format (Dec-04-01 04:03:12)
* converted to MySQL timestamp readable format ().
*/
    static String convertDate(String dateString) {
        String old_format = "MMM-dd-yy HH:mm:ss";
        String new_format = "yyyy-MM-dd HH:mm:ss";
        
        SimpleDateFormat sdf = new SimpleDateFormat(old_format);
        String out = "";
        try {
            Date d = sdf.parse(dateString);
            sdf.applyPattern(new_format);
            out = sdf.format(d);
        } catch (ParseException pe) {
            System.out.println("Could not format date");
        }
        
        return out;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////CODE FOR PROJECT 4/////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////JOSH SHOULD CHECK OUT///////////////////////////////////////////////////////////////////////////
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
            try
            {
                 String id = request.getParameter("id");
                 String xml = AuctionSearchClient.getXMLDataForItemId(id);
                 
                 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                 factory.setValidating(false);
                 factory.setIgnoringElementContentWhitespace(true);
                 DocumentBuilder builder = factory.newDocumentBuilder();

                StringReader reader = new StringReader(xml);
                 Document doc = builder.parse(new InputSource(reader));
                 Element itemElement = doc.getDocumentElement();
                //     long itemID = Long.valueOf(itemElement.getAttribute("ItemID"));
                   
                // Get information about the item that belongs in itemMap
                String itemID = itemElement.getAttribute("ItemID");
                String name = getElementTextByTagNameNR(itemElement, "Name");
                String start_time = convertDate(getElementTextByTagNameNR(itemElement, "Started"));
                String end_time = convertDate(getElementTextByTagNameNR(itemElement, "Ends"));
                String description = getElementTextByTagNameNR(itemElement, "Description");
                // Trim the description if it is over 4000 chars.
                if (description.length() > 4000)
                        description = description.substring(0, 4000);

                // Get current bidding information for the item
                String currently = strip(getElementTextByTagNameNR(itemElement, "Currently"));
                String buy_price = strip(getElementTextByTagNameNR(itemElement, "Buy_Price"));
                // Give a buy now price of NULL if not supplied.
                if (buy_price.isEmpty())
                    buy_price = "";

                String first_bid = strip(getElementTextByTagNameNR(itemElement, "First_Bid"));
                String number_of_bids = getElementTextByTagNameNR(itemElement, "Number_of_Bids");
                
                // Get information about Categories that will go in categoryMap
                Element[] categories = getElementsByTagNameNR(itemElement, "Category");
                
            // Iterate through categories and add to categoryMap if necessary
                int categoryCount = categories.length;
                for (int ind = 0; ind < categoryCount; ind++) 
                {

                    String categoryName = getElementText(categories[ind]);
                    int categoryID;

                    // Create Category object and add to map if it doesn't exist
                    if (categoryMap.containsKey(categoryName)) 
                    {
                        // Category is already registered - get the categoryID from the map.
                        Category category_object = categoryMap.get(categoryName);
                        categoryID = category_object.c_categoryID;
                     } 
                     else 
                     {
                    // Create our own categoryID
                    categoryID = categoryMap.size() + 1;
                    Category category_object = new Category(categoryID, categoryName);
                    categoryMap.put(categoryName, category_object);
                     }

                    // Add the relation to the itemscategoriesMap
                    ItemCategory item_cat = new ItemCategory(itemID, categoryID);
                    itemcategoriesMap.put(itemID + categoryID, item_cat);
                }

            // Get the list of bids
                    Element BidsParent = getElementByTagNameNR(itemElement, "Bids");
                    Element[] Bids = getElementsByTagNameNR(BidsParent, "Bid");

            // Iterate through the bids and add bidder and bid to respective maps if necessary
            int bidCount = Bids.length;
            for (int j=0; j<bidCount; j++) {
                // Get info about the bid for bidMap
                Element bid = Bids[j];
                String b_time = convertDate(getElementTextByTagNameNR(bid, "Time"));
                String b_amount = strip(getElementTextByTagNameNR(bid, "Amount"));

                // Get info about the bidder to add to userMap
                Element bidder = getElementByTagNameNR(bid, "Bidder");
                String b_userid = bidder.getAttribute("UserID");
                String b_rating = bidder.getAttribute("Rating");
                String b_location = getElementTextByTagNameNR(bidder, "Location");
                if (b_location.isEmpty())
                    b_location = "";

                String b_country = getElementTextByTagNameNR(bidder, "Country");
                if (b_country.isEmpty())
                    b_country = "";

                // Add the bid to the bidMap
                Bid bid_object = new Bid(b_userid, b_time, itemID, b_amount);
                bidMap.put(b_userid + b_time, bid_object);
                bidstring = b_userid + " " + b_time + " " + b_amount;
                bidset.add(bidstring);
                // Add the bidder to the userMap if not present
                if (!userMap.containsKey(b_userid)) {
                    User bidder_object = new User(b_userid, b_rating, b_location, b_country);
                    userMap.put(b_userid, bidder_object);
                }
            }

            // Get the seller information that will go in userMap
            // This block parses UserID, Rating, Location, and Country for the seller.
            Element seller = getElementByTagNameNR(itemElement, "Seller");
            String s_userid = seller.getAttribute("UserID");
            String s_rating = seller.getAttribute("Rating");
            String s_location = getElementTextByTagNameNR(itemElement, "Location");
            String s_country = getElementTextByTagNameNR(itemElement, "Country");
          
            // Add the user to the userMap if not presentely identifies a user, who can be a bidder or a seller at different times. I think that means a user has one single ID, as opposed to having a pair. I may be wrong but both the seller and the bidder have location and country as subelements. I think t
            if (!userMap.containsKey(s_userid)) {
                User seller_object = new User(s_userid, s_rating, s_location, s_country);
                userMap.put(s_userid, seller_object);
            }

            // FINALLY Add the item to the itemMap
            Item item_object = new Item(itemID, name, description, start_time, end_time, currently, first_bid, buy_price, number_of_bids, s_userid);
            itemMap.put(itemID, item_object);
        

                /////////////////////////////////////////////////////////////////////////
                ////////////////////Passing name for testing purposes///////////////////
                ////////////////////Rest is TODO////////////////////////////////////////
                /////////////////////Will be passing in objects/////////////////////////
                 
                 request.setAttribute("name", name);
                 request.setAttribute("userid", s_userid);
                 request.setAttribute("location", s_location);
                 request.setAttribute("first_bid", first_bid);
                 request.setAttribute("description", description);
                 request.setAttribute("rating", s_rating);
                 request.setAttribute("start_time", start_time);
                 request.setAttribute("end_time", end_time);
                 request.setAttribute("bids",bidset);
                 SortedSet set = new TreeSet(); 
                 for (String key : categoryMap.keySet()) {
                        set.add(key);
                    }   
                 request.setAttribute("categories", set);


                     request.getRequestDispatcher("/itemResults.jsp").forward(request, response);
            } catch (ParserConfigurationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (SAXException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } finally {}
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
*/
 
}



