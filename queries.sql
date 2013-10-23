-- queries.sql --

-- Find the number of users in the database --
SELECT COUNT(*)
FROM User;

-- Find the number of sellers from "New York" --
SELECT COUNT(*)
FROM User
WHERE BINARY location = "New York";

-- Find the number of auctions belonging to exactly four categories --
SELECT COUNT(*)
FROM Item_Category
GROUP BY ItemID
HAVING COUNT(CategoryID) = 4;

-- Find the ID(s) of current (unsold) auctions with the highest bid -- 
SELECT ItemID
FROM Item
INNER JOIN bid
ON Item.ItemID = Bid.ItemID
WHERE Item.end > "2001-12-20 00:00:01"
AND Item.start < "2001-12-20 00:00:00"
HAVING MAX(Bid.Amount)

-- Find the number of sellers whose rating is higher than 1000 -- 
SELECT COUNT(*)
FROM Item
INNER JOIN User
ON Item.seller = User.UserID 
WHERE User.rating > 1000;

-- Find the number of users who are both sellers and bidders --
SELECT COUNT(Item.UserID)
FROM Item
INNER JOIN Bid
ON Item.seller = Bid.UserID
GROUP BY Item.UserID;

-- Find the number of categories that include at least one item with a bid of more than $100 --
SELECT COUNT(DISTINCT Category)
FROM Category 
INNER JOIN Item_Category
ON Category.CategoryID = Item_Category.CategoryID
INNER JOIN Bid
ON Item_Category.ItemID = Bid.ItemID
WHERE Bid.Amount > 100.00
