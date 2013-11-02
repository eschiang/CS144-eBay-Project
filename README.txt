Partner 1: Meet Bhagdev
ID: 104079094
Email: meetbhagdev@ucla.edu

Partner 2: Joshua Dykstra
ID: 903888459
Email: dykstrajj@gmail.com

We elected to created MySQL indexes on Buy_Price, Ends, and Seller
in the Item table, and on UserID in the Bid table. We created 
these indexes in MySQL because these fields don't hold text.

We used Lucene to index the ItemID, Name, Description, Categories, 
and the UNION(Name, Description, Categories) fields. To allow for more 
efficient text searching on these fields of the Item table.

