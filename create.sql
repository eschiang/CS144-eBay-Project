-- create.sql -- 

-- Item Table --
CREATE TABLE Item (
ItemID INTEGER NOT NULL PRIMARY KEY,
Name VARCHAR(200) NOT NULL,
Description VARCHAR(4000),
Started TIMESTAMP,
Ends TIMESTAMP,
Currently DECIMAL(8,2),
Buy_Price DECIMAL(8,2),
First_Bid DECIMAL(8,2),
Number_of_Bids INTEGER,
Seller INTEGER
);

-- Category Table --
CREATE TABLE Category (
CategoryID INTEGER NOT NULL PRIMARY KEY,
Category VARCHAR(50)
);

-- Item_Category Table --
CREATE TABLE Item_Category (
ItemID INTEGER NOT NULL,
CategoryID INTEGER,
PRIMARY KEY(ItemID, CategoryID),
 FOREIGN KEY (ItemID) REFERENCES Item(ItemID),
        FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID)
);

-- Bid Table --
CREATE TABLE Bid (
BidID INTEGER NOT NULL PRIMARY KEY,
ItemID INTEGER,
UserID INTEGER,
Amount DECIMAL(8,2),
Time TIMESTAMP,
        FOREIGN KEY (ItemID) REFERENCES Item(ItemID),
        FOREIGN KEY (UserID) REFERENCES User(UserID));



-- User Table --
CREATE TABLE User (
UserID INTEGER NOT NULL PRIMARY KEY,
Name VARCHAR(50),
Rating INTEGER,
Location VARCHAR(50),
Country VARCHAR(30)




);
