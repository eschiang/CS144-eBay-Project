-- queries.sql --

-- Find the number of users in the database --
SELECT COUNT(*)
FROM User;

-- Find the number of sellers from "New York" --
SELECT COUNT(*)
FROM User
WHERE Binary Location = 'New York';

-- Find the number of auctions belonging to exactly four categories --
SELECT COUNT(*)
FROM (SELECT ItemID
		FROM Item_Category
		GROUP BY ItemID
		HAVING COUNT(CategoryID) = 4) T;

-- Find the ID(s) of current (unsold) auctions with the highest bid -- 
-- SELECT Item.ItemID 
-- FROM Item
-- INNER JOIN Bid
-- ON Item.ItemID = Bid.ItemID
-- WHERE Item.Ends > "2001-12-20 00:00:01"
-- AND Item.Started < "2001-12-20 00:00:00"
-- WHERE MAX(Bid.Amount);

-- Find the number of sellers whose rating is higher than 1000 -- 
SELECT COUNT(DISTINCT UserID)
FROM Item
INNER JOIN User
ON Item.Seller = User.UserID 
WHERE User.Rating > 1000;

-- Find the number of users who are both sellers and bidders --
SELECT COUNT(*)
FROM Item
INNER JOIN Bid
ON Item.Seller = Bid.UserID

-- Find the number of categories that include at least one item with a bid of more than $100 --
--SELECT COUNT(DISTINCT CategoryID)
--FROM Item_Category
--INNER JOIN Bid
--ON Item_Category.ItemID = Bid.ItemID
--WHERE Bid.Amount > 100.00
--GROUP BY Item_Category.ItemID
