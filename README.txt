Partner 1: Meet Bhagdev
ID: 104079094
Email: meetbhagdev@ucla.edu

Partner 2: Joshua Dykstra
ID: 903888459
Email: dykstrajj@gmail.com

Both the partners equally contributed on the whole project.
Helped each other with debugging and design. 

Answers : 
Q1: For which communication(s) do you use the SSL encryption? If you are 
encrypting the communication from (1) to (2) in Figure 2, for example, write (1)?(2) 
in your answer.

We use SSL encryption for (4)->(5)(or 5->6), since this is when 
the user's credit card information is in being sent to the server over the 
internet.

Q2: How do you ensure that the item was purchased exactly at the Buy_Price of that particular item?
We have a HTTPSession variable in the item information page. We use that to validate and 
invalidate the requests made.
For example if a new request is made to the payment page without going through the item infomration 
page, the user gets an invalid response.

Q3: How do you guarantee that the user cannot scroll horizontally?

// Josh's part. I would recommend using

<meta name="viewport" charset="utf-8" content="width=device-width, initial-scale=1.0,maximum-scale=1.0, user-scalable=no"/>
<meta name="viewport" content="width=device-width; user-scalable=no" />

Q4: How do you guarantee that the width of your textbox component(s) can fit the screen width of a mobile device? 
Note: you have to explain "how", and you can't simply state that 
"we use a XXX downloaded from YYY, and it magically solve the problem."

//Josh's part




