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
encrypting the communication from (1) to (2) in Figure 2, for example, write 
(1)?(2) in your answer.

We use SSL encryption for (4)->(5) and (5)->(6), since this is when 
the user's credit card information is in being sent to the server over the 
internet. By submitting the payment information form  which contains the 
user's credit card information over HTTPS, the confirmation page containing 
the user's card information is also sent over HTTPS when the server sends its
response. This way the user's credit card details are protected throughout
the transaction.

Q2: How do you ensure that the item was purchased exactly at the Buy_Price of 
that particular item?

We have an HTTPSession variable in the item information page. We use that to 
validate and invalidate the requests made. For example if a new request is made 
to the payment page without going through the item infomration page, the user 
gets an invalid response.

Q3: How do you guarantee that the user cannot scroll horizontally?

There are two tools we used to restrict users from scrolling horizontally.  
First, we used meta tags that smart phone browsers will register to set the 
content's width to be equal to that of the phone's viewport.  These tags can 
be seen below:

<meta name="viewport" charset="utf-8" content="width=device-width, 
	initial-scale=1.0,maximum-scale=1.0, user-scalable=no"/>
<meta name="viewport" content="width=device-width; user-scalable=no" />

Also, We limit any content that may be outside of the width of the viewport 
using several simple css rules for the body element to hide any overflow:

overflow-x: hidden;
width: auto;

Q4: How do you guarantee that the width of your textbox component(s) can fit 
the screen width of a mobile device? Note: you have to explain "how", and you 
can't simply state that "we use a XXX downloaded from YYY, and it magically 
solve the problem."

By placing a div element around the input field, with a small margin on the
left and right sides, we can then set the input element's width to be 100% and
it will be contained within the screen's width.


