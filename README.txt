Partner 1: Josh Dykstra
ID: 903888459
Email: dykstrajj@gmail.com

Partner 2: Meet Bhagdev
ID: 104079094
Email: meetbhagdev@ucla.edu

Part 2
---------
Relations in proposed schema:

Item(ItemID, Name, Description, Started, Ends, Currently, Buy_Price, First_Bid
     Number_of_bids, Seller)
Category(CategoryID, Category)
Item_Category(ItemID, CategoryID)
Bid(BidID, ItemID, UserID, Amount, Time)
User(UserID, Name, Rating, Location, Country)

The only functional dependencies in each relation are for keys.

This schema should be in BCNF.
