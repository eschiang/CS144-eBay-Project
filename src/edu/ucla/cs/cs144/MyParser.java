/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

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


class MyParser {
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static class Category {
        int categoryID;
        String name;
      
        public Category(int categoryID, String name) {
                categoryID = categoryID;
                name = name;
        }

   
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
    public static class ItemCategory {
        String itemID;
        int categoryID;

        /**
         * @param itemID
         * @param categoryID
         */
        public ItemCategory(String itemID, int categoryID) {
                super();
                itemID = itemID;
                categoryID = categoryID;
        }}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     public static class User 
     { 
    String u_name;
    String u_location;
    String u_rating;
    String u_country;

    User(String uname, String ulocation, String urating, String ucountry)
     { 
      u_name = uname; 
      u_location=ulocation;
      u_rating=urating;
      u_country=ucountry;
    } 
  }
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public static class Bid { 
    String b_item;
    String b_userid;
    String b_amount;
    String b_time; // handle time conversion

    Bid(String bitem, String buserid, String bamount, String btime)
    { 
      b_item = bitem; 
      b_userid=buserid;
      b_amount=bamount;
      b_time=btime; // handle time conversion
    } 
  }
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   public static class Item { 
    String i_name;
    String i_description;
    String i_started;
    String i_ends;
    String i_currently;
    String i_buyprice;
    String i_numberofbids;
    String i_seller;
    String i_id;


    Item(String iid,String iname, String idescription, String istarted, String iends, String icurrently, String ibuyprice, String inumberofbids, String iseller)
    { 
    i_id=iid;  
     i_name = iname; 
     i_description=idescription;
     i_started=istarted;
     i_ends=iends;
     i_currently = icurrently; 
     i_buyprice=ibuyprice;
     i_numberofbids=inumberofbids;
      i_seller=iseller;
    } 
  }
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static Map<String, User> users = new HashMap<String, User>();
    static Map<String, Bid> bids = new HashMap<String, Bid>();
    static Map<String, Item> itemsmap = new HashMap<String, Item>();
    static Map<String, Category> categoryMap =new HashMap<String, Category>(); 
    static Map<String, ItemCategory> itemcategories =new HashMap<String, ItemCategory>(); 

    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    static Set<String> categories = new HashSet<String>();


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
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
    
        // Begin by getting all the root element of the file and an array of 
        // the items it contains.  
        Element root = doc.getDocumentElement();
        Element[] items = getElementsByTagNameNR(root, "Item");

        // Iterate through the items 
        int itemCount = items.length;
        for(int i=0; i<itemCount; i++)
        {
            // Get the element's ItemID
            String ItemID = items[i].getAttribute("ItemID");
            // Get the element's name
            String name = getElementTextByTagNameNR(items[i], "Name");
            // Get the categories for the item
            Element[] categoriesElements = getElementsByTagNameNR(items[i], "Category");
            // Get the location for the item
            String s_location = getElementTextByTagNameNR(items[i], "Location");
            // Get the country for the item
            String s_country = getElementTextByTagNameNR(items[i], "Country");
            // Get the start bid time for the item
            String s_time = getElementTextByTagNameNR(items[i], "Started");
            // Get the end bit time for the item
            String e_time = getElementTextByTagNameNR(items[i], "Ends");
            // Get the description for the item
            String description = getElementTextByTagNameNR(items[i], "Description");
            if (description.length() > 4000)
                    description = description.substring(0, 4000);
            // Get the first bid for the item
            String minimumBid = strip(getElementTextByTagNameNR(items[0], "First_Bid"));
            // Get the buy now for the item
            String buy_now = strip(getElementTextByTagNameNR(items[0], "Buy_Price"));
            if (buy_now.isEmpty())
                    buy_now = "0.00";
            
            // Get the seller
            Element seller = getElementByTagNameNR(items[0], "Seller");
            // Get the seller's uid
            String u_id = seller.getAttribute("UserID");
            // Get the seller's rating
            String s_rating = seller.getAttribute("Rating");
            
            
    
          
            ////Making the User object and putting it into the usermap with seller id being key//////
            User sellerObject = new User(u_id, s_location, s_country, s_rating);//User object made
            users.put(u_id, sellerObject);//User Object added to map with user id being the key

            //Getting the length of the categories
            int categoryCount = categoriesElements.length;
            // Iterate through the categories adding them to the categories set if necessary
            /////Set not needed any more. Cehck out how i do the categories with a map//////

           // for(int j=0; j<categoryCount; j++)
            //{
             //   categories.add(getElementText(categoriesElements[j]));
           // }
            //Iterator iter = categories.iterator();
        /*    while(iter.hasNext())
            {
                System.out.println(iter.next());                
            }*/
            ////////////////////////////////////////////////////////////////////////
            
   //////THIS CODE PUTS THE CATEGORIES INTO A MAP WITH A KEY(NAME) AND ASSIGNS IT A VALUE(INDEX)///////

            for (int ind = 0; ind < categoryCount; ind++)
            {
                    
                    String categoryName =getElementText(categoriesElements[ind]);
                    int categoryID;
                    
                    // Create Category object and add to map
                    if (categoryMap.containsKey(categoryName))
                    {
                            Category categoryObject = categoryMap.get(categoryName);
                            categoryID = categoryObject.categoryID;
                    }
                    else
                    {
                            categoryID = categoryMap.size();
                            Category categoryObject = new Category(categoryID, categoryName);
                            categoryMap.put(categoryName, categoryObject);
                    }
                    //////////Making the itemcategory obejct and putting the itemcategories map with item id being the key///////////
                    ItemCategory item_cat = new ItemCategory(ItemID, categoryID);
                    itemcategories.put(ItemID,item_cat);

            }

            // Get current Bid
            String currently = getElementTextByTagNameNR(items[i], "Currently");
 
            // Get the number of bids
            String numberOfBids = getElementTextByTagNameNR(items[i], "Number_of_Bids");
            ////Making the Item object and putting it into the item map with itemId being key//////
            Item i_object = new Item(ItemID, name, description, s_time, e_time,currently, buy_now, numberOfBids,u_id ); 
            itemsmap.put(ItemID,i_object);
            Element BidsParent = getElementByTagNameNR(items[i], "Bids");

            // Get the list of bids
            Element[] Bids = getElementsByTagNameNR(items[i], "Bid");

            // Iterate through the bids 
          
            int bidCount = Bids.length;
            for(int j=0; j<bidCount; j++)
            {
                
                  //Parsing the bid
                  Element bid = Bids[j];
                  //GEtting the bid time
                  String b_time = getElementTextByTagNameNR(bid, "Time");
                  //Getting the bid amount
                  String b_amount = strip(getElementTextByTagNameNR(bid, "Amount"));
                  
                  //Inner parse
                  Element bidder = getElementByTagNameNR(bid, "Bidder");
                  //Get the uid of the bidder
                  String b_ID =  bidder.getAttribute("UserID");
                  //Get the rating of the bidder
                  String b_Rating = bidder.getAttribute("Rating");
                  //Get the bidder's location
                  String b_Location = getElementTextByTagNameNR(bidder, "Location");
                  //Get the bidders country
                  String b_country = getElementTextByTagNameNR(bidder, "Country");
                  ///////////////////DATA BEING ADDED TO THE BIDS MAP///////////
                  Bid b_object = new Bid(ItemID, b_ID, b_amount, b_time);
                  bids.put(ItemID,b_object);
   
            }


        }
        
        
        
        /**************************************************************/
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
 //       "Test : Josh you can play around with the other maps to make sure we go everything
        System.out.println("Test : Josh you can play around with the other maps to make sure we got everything");
        for (Map.Entry<String,Item> entry : itemsmap.entrySet()) {
            String key = entry.getKey();
            Item thing = entry.getValue();
            System.out.print(key + thing.i_description);
            System.out.println();

        }
    }
}
