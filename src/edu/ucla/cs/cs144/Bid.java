package edu.ucla.cs.cs144;

/**
 * Bid
 * Helper class to represent a single Bid
 */
public class Bid
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