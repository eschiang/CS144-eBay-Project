// suggest.js

function SuggestCtrl(searchBar, searchContainer) {
    this.cur = -1;
    this.layer = null;
    this.searchBar = searchBar;
    this.searchContainer = searchContainer;
    this.init();
}

/**
 * Initialzes the object and attaches events
 *
 */
SuggestCtrl.prototype.init = function () {
    oThis = this;

    this.searchBar.onkeyup = function(e) {
        switch(e.keyCode) {
            case 38: //up arrow
                oThis.previousSuggestion();
                break;
            case 40: //down arrow 
                oThis.nextSuggestion();
                break;
            case 13: //enter
                oThis.hideSuggestions();
                break;
            default:
                oThis.fetchSuggestions();
        }
        
    }

    // If we leave the text box, we'll hide the suggestions
    this.searchBar.onblur = function () {
        oThis.hideSuggestions();
    }

    this.buildSuggestBox();
}

/**
 * Displays google suggest results below text field based 
 * on the contents of the text field.
 *
 * Triggered by keypresses in text field.
 *
 * @param DOMnode element
 */
SuggestCtrl.prototype.fetchSuggestions = function () { 
    // Get the text currently in the text field and format
    var text = escape(this.searchBar.value);
    console.log(text);
    if (text == "") {
        this.hideSuggestions();
        return;
    }

    // Fetch the results via the proxy server
    var request = new XMLHttpRequest();
    var oThis = this;

    request.onreadystatechange = function() {
        if (request.readyState == 4 && request.status == 200) {
            // Parse the response XML
            var suggestions = oThis.parseSuggestions(request.responseXML);

            if(suggestions.length > 0)
                oThis.showSuggestions(suggestions);
            else
                oThis.hideSuggestions();
        }
    }
    var url = "/eBay/suggest?q=" + text;
    request.open("GET", url, true);
    request.send();
};

/**
 * Parses the suggestions returned from google
 * 
 * @param XML response
 * @return array suggestions
 */
SuggestCtrl.prototype.parseSuggestions = function (response) {
    var suggestions = Array();

    if (typeof response === "undefined") 
        return [];

    var completeSuggestions = response.documentElement.childNodes;

    // Iterate over the suggestons and build an array to return
    for(var i = 0; i < completeSuggestions.length; i++) {
        suggestions[i] = completeSuggestions[i].childNodes[0].getAttribute("data"); 
    }


    return suggestions;

};


/**
 * Adds Suggestions to the dropdown
 */
SuggestCtrl.prototype.showSuggestions = function (suggestions) {

    var div = null;
    this.layer.innerHTML = "";

    for (var i=0; i < suggestions.length; i++) {
        div = document.createElement("div");
        div.appendChild(document.createTextNode(suggestions[i]));
        this.layer.appendChild(div);
    }

    this.layer.style.left = this.getLeft() + "px";
    this.layer.style.top = (this.getTop() + this.searchBar.offsetHeight) + "px";
    this.layer.style.visibility = "visible";
};

/**
 * Builds the suggestion box that appears below the input
 *
 * @param array suggestions
 */
SuggestCtrl.prototype.buildSuggestBox = function (suggestions) {
    this.layer = document.createElement("div");
    this.layer.className = "suggestions";
    this.layer.style.visibility = "hidden";
    this.layer.style.width = this.searchBar.offsetWidth + "px";
    this.searchContainer.appendChild(this.layer);

    var oThis = this;

    this.layer.onmousedown = this.layer.onmouseup = 
    this.layer.onmouseover = function (e) {
        e = e || window.event;
        target = e.target || e.srcElement;

        if (e.type == "mousedown") {
            oThis.searchBar.value = target.firstChild.nodeValue;
            oThis.hideSuggestions();
        } else if (e.type == "mouseover") {
            oThis.highlightSuggestion(target);
        } else {
            oThis.searchBar.focus();
        }

    };
};

/**
 * Removes highlighting from node that has it and gives to 
 * node supplied
 * @param node suggestNode
 */
SuggestCtrl.prototype.highlightSuggestion = function (suggestionNode) {

    for (var i=0; i < this.layer.childNodes.length; i++) {
        var current = this.layer.childNodes[i];
        if(current == suggestionNode)
            current.className = "current";
        else if (current.className == "current")
            current.className = "";
    
    }
};

/**
 * Moves down suggestion list
 */
SuggestCtrl.prototype.nextSuggestion = function () {
    var nodes = this.layer.childNodes;

    if (nodes.length > 0 && this.cur < nodes.length) {
        var node = nodes[++this.cur];
        this.highlightSuggestion(node);
        this.searchBar.value = node.firstChild.nodeValue;
    }
}

/**
 * Moves up suggestions list
 */
SuggestCtrl.prototype.previousSuggestion = function () {
    var nodes = this.layer.childNodes;

    if (nodes.length > 0 && this.cur > 0) {
        var node = nodes[--this.cur];
        this.highlightSuggestion(node);
        this.searchBar.value = node.firstChild.nodeValue;
    }
}

/**
 * Hides the suggestions div
 */
SuggestCtrl.prototype.hideSuggestions = function () {
    this.layer.style.visibility = "hidden";
};


/**
 * Determines left offset
 */
SuggestCtrl.prototype.getLeft = function () {

    var node = this.searchBar;
    var left = 0;

    while(node.tagName != "BODY") {
        left += node.offsetLeft;
        node = node.offsetParent;
    }

    return left;
}

/**
 * Determines the top offset
 */
SuggestCtrl.prototype.getTop = function () {

    var node = this.searchBar;
    var top = 0;

    while(node.tagName != "BODY") {
        top += node.offsetTop;
        node = node.offsetParent;
    }

    return top;
}


