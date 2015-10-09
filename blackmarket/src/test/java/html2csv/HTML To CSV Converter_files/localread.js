// Copyright 2013 Data Design Group, Inc  All Rights Reserved
function ieReadLocalFile(that,callback) {
        //alert(that.value);
        if(!that.value)return;
        if(that.value.length<=0)return;
        var request;
        if (window.XMLHttpRequest && false) { // code for IE7+, Firefox, Chrome, Opera, Safari
           request=new XMLHttpRequest();
        }
        else {// code for IE6, IE5
          request=new ActiveXObject("Msxml2.XMLHTTP"); // Microsoft.XMLHTTP
        }
        var fn=that.value;
        //fn="file:///"+that.value.replace("\\","/");
        request.open('get', fn, true);
        request.onreadystatechange = function() 
        {
          //alert(request.readystate+":"+request.status);
          if (request.readyState == 4 && (request.status == 200 || request.status==0)) {
              callback(request.responseText);
          }
        }
        request.send();
}
    
function readLocalFile(that,callback)
{   
    var reader = new FileReader();

    if(that.files && that.files[0]){
	     var reader = new FileReader();
	     reader.onload = function (e) {  
           //document.getElementById(txtTargetName).value=e.target.result;
           callback(e.target.result);
	     };//end onload()
       reader.readAsText(that.files[0]);
    }//
} // readLocaFile

function loadTextFile(f,callback)
{
    (navigator.appName.search('Microsoft') > -1) ? ieReadLocalFile(f, callback) : readLocalFile(f, callback);
    //(FileReader===undefined) ? ieReadLocalFile(f, callback) : readLocalFile(f, callback) ;
}