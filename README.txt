Partner 1: Josh Dykstra
ID: 903888459
Email: dykstrajj@gmail.com

Partner 2: Meet Bhagdev
ID: 104079094
Email: meetbhagdev@ucla.edu

Part 2
---------
1) Relations in proposed schema:

	Item(ItemID, Name, Description, Started, Ends, Currently, First_Bid, Buy_Price, Number_of_bids, Seller) Keys: ItemID

	Category(CategoryID, Category) Keys: CategoryID

	Item_Category(ItemID, CategoryID) Keys: (ItemID, CategoryID)

	Bid(UserID, Time, ItemID, Amount) Keys: (UserID, Time)

	User(UserID, Rating, Location, Country) Keys: UserID

2) The only functional dependencies in each relation are for keys.

3) This schema should be in BCNF because all the functional dependencies for each table contains a key in the left hand side.
