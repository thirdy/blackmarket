// Copyright 2013 Data Design Group, Inc  All Rights Reserved
var CSV = {
    delimiter : ",",
    detectedDelimiter : ",",
    autodetect : true,
    quote : '"',
    outputQuote : '"',
    limit : '',
    isFirstRowHeader : true,
    headerToUpper : false,
    headerToLower : false,
    skipEmptyRows : true,
    skipEmptyRowCnt: 0,
    relaxedMode : true,
    ignoreQuote : false,
    relaxedInfo : {},
    excelMode : true,
    sortNeeded : false,
    addSequence : false,
    fieldImbalance : false,
    fieldImbalanceRows : [],
    headerImbalance : false,
    headerImbalanceRows : [],
    headerErrors : [],
    decodeBackslashLiterals : false,
    decimalChar : function (){return (0).toFixed(1).charAt(1)},

    maxColumnsFound : 0,
    headerColumns : 0,
    prevColumnsFound : 0,
    dataRowsFound : 0,
    arHeaderRow : [], // First line of csv if header present
    table : [],
    statsCnt : [],
    displayPoss : "", // display positions starting at 1,2,3,...n. Empty ==>default
    sortPoss : "",  // sort positions starting at 1,2,...n. Empty ==> no sort
    sortIgnoreCase : false,

    unescapeLiterals : function(s) {
        var j;
        var n=s.length-1;
        var a=[];
        var c;
        for(j=0;j<n;j++) {
            c=s.charAt(j);
            if(c=="\\") {
                switch(s.charAt(j+1)) {
                    case '\\' :
                       c = "\\"; j++;
                       break;
                    case 'b' :
                       c = "\b"; j++;
                       break;
                    case 'f' :
                       c = "\f"; j++;
                       break;
                    case 'n' :
                       c = "\n"; j++;
                       break;
                    case 'r' :
                       c = "\r"; j++;
                       break;
                    case 't' :
                       c = "\t"; j++;
                       break;
                    case 'v' :
                       c = "\v"; j++;
                       break;
                    case '"' :
                       c = '"'; j++;
                       break;
                    case "'" :
                       c = "'"; j++;
                       break;
                    case "," :
                       c = ","; j++;
                       break;
                    default:
                       break;
                }
            }
            a.push(c);
        } // for
        if(j==n){a.push(s.charAt(n));} // last char
        return a.join('');
    },
    parse : function (csv, reviver) {
        var j,k;
        var s = "";
        var ss = "";
        reviver = reviver || function (r, c, v) { return v; };
        this.table = [];
        this.statsCnt = [];
        this.arHeaderRow = [];
        this.maxColumnsFound = 0;
        this.dataRowsFound = 0;
        this.headerColumns = 0;
        this.relaxedInfo = {};
        var chars = csv.split('');
        var c = 0;
        var prevCh="";
        var cc = chars.length;
        var start, end, row;
        var cnt = 0;
        var equalUsed=false;
        var savestart;
        var linestart;
        var spacesFound=false;
        var firstRowColumnsFound=0;
        var brcnt=0;
        if (this.limit != '' && isNaN(this.limit)) this.limit = '';
        detect = { comma : 0, semi : 0, tab : 0, pipe : 0, colon : 0, space : 0};
        //alert("reset: detect.colon="+detect.colon+", comma="+detect.comma);
        for(j = 0; j < cc; j++)
        {
            if (j > 1 && (chars[j] == '\r' || chars[j] == '\n')) break;
            if(chars[j] == ",") detect.comma++;
            if(chars[j] == ";") detect.semi++;
            if(chars[j] == "\t") detect.tab++;
            if(chars[j] == "|") detect.pipe++;
            if(chars[j] == ":") detect.colon++;
            if(chars[j] == " ") detect.space++;
        }   
        this.detectedDelimiter = this.delimiter || ',';
        //alert("detect.colon="+detect.colon+", comma="+detect.comma);
        // Question - should the presense of one tab indicate tab separated?
        if     (detect.tab>0 && detect.tab >=detect.comma && detect.tab >=detect.pipe && detect.tab >=detect.semi && detect.tab>=detect.colon) this.detectedDelimiter = "\t";
        else if(detect.semi>0 && detect.semi >detect.comma && detect.semi >detect.pipe && detect.semi >detect.tab  && detect.semi>detect.colon) this.detectedDelimiter = ";";
        else if(detect.colon>0 && detect.colon>detect.comma && detect.colon>detect.pipe && detect.colon>detect.tab  && detect.colon>detect.semi)this.detectedDelimiter = ":";
        else if(detect.pipe>0 && detect.pipe >detect.comma && detect.pipe >detect.semi && detect.pipe >detect.tab  && detect.pipe>detect.colon) this.detectedDelimiter = "|";
        else if(detect.comma>detect.tab   && detect.comma>detect.pipe && detect.comma>detect.semi && detect.comma>detect.colon) this.detectedDelimiter = ",";
        else this.detectedDelimiter = ",";
        if (detect.tab == 0 && detect.comma == 0 && detect.pipe == 0 && detect.colon == 0 && detect.semi == 0 && detect.space > 0) this.detectedDelimiter = " ";
        if(this.autodetect)this.delimiter=this.detectedDelimiter;
        this.skipEmptyRowCnt=0;
        //alert('auto detected ' + this.detectedDelimiter+" ,because ,="+detect.comma+", ;="+detect.semi+", tab="+detect.tab+" ,pipe="+detect.pipe+" ,colon="+detect.colon);
        while (c < cc) { // for each char
            if (this.skipEmptyRows && (chars[c] == '\r' || chars[c] == '\n')) {
                c++;
                this.skipEmptyRowCnt++;
                continue;
            }
            this.table.push(row = []); // add row to table no fields
            if(this.addSequence && row.length===0 && this.table.length===1 && this.isFirstRowHeader)row.push("#");
            else if(this.addSequence && row.length===0) row.push(""+(cnt+1-(this.isFirstRowHeader?1:0)));
            spacesFound=false;
            linestart=c;
            while (c < cc && chars[c] !== '\r' && chars[c] !== '\n') { // look at one line
                savestart = start = end = c;
                if (this.relaxedMode) { // skip leading space to first double quote
                    while (chars[c] === ' ') {
                        ++c; // skip white stuff at front
                        spacesFound=true;
                    }
                    if (chars[c] === this.quote && !this.ignoreQuote){
                        if(spacesFound && !this.relaxedInfo[""+(this.table.length+brcnt)])this.relaxedInfo[""+(this.table.length+brcnt)]={"error":1,"column":c-linestart+1};
                        start = c;
                    }
                    else{c = savestart;}
                }
                if (this.excelMode) { // skip equal sign at front, indicates to treat field as text
                    if ((chars[c] === '=') && (c+1 < cc) && (chars[c+1] === this.quote)) {
                        start = ++c;
                        equalUsed = true; 
                    }
                }
                if (this.quote === chars[c] && !this.ignoreQuote) { // if doublequote, find matching doublequote
                    start = end = ++c;
                    while (c < cc) {
                        if (chars[c] === this.quote) { // found doublequote
                            if (this.quote !== chars[c + 1]) { break; }
                            else { chars[++c] = ''; } // unescape ""
                        }
                        //else if(chars[c]==='\n')brcnt++;
                        end = ++c;
                    }
                    if (chars[c] === this.quote) { // after match
                        ++c;
                        if(c < cc && chars[c] !== '\r' && chars[c] !== '\n' && this.delimiter !== chars[c]) {
                           if(!this.relaxedInfo[""+(this.table.length+brcnt)])this.relaxedInfo[""+(this.table.length+brcnt)]={"error":2,"column":c-linestart+1}; 
                        }
                    }
                    else {
                        if(!this.relaxedInfo[""+(this.table.length+brcnt)])this.relaxedInfo[""+(this.table.length+brcnt)]={"error":3,"column":c-linestart+1};
                    }
                    while (c < cc && chars[c] !== '\r' && chars[c] !== '\n' && this.delimiter !== chars[c]) { ++c; }
                } else { // not a doublequote
                    prevCh="";
                    while (c < cc && chars[c] !== '\r' && chars[c] !== '\n') { 
                        if(chars[c] === this.delimiter && !this.decodeBackslashLiterals)break; 
                        if(chars[c] === this.delimiter && this.decodeBackslashLiterals && prevCh!=="\\")break; 
                        prevCh=chars[c];
                        end = ++c; 
                    }
                }
                row.push(reviver(this.table.length - 1, row.length, chars.slice(start, end).join('')));
                if(this.decodeBackslashLiterals) { // \n \t \b \v \f \' \"
                    row[row.length-1]=this.unescapeLiterals(row[row.length-1]);
                    //alert(row[row.length-1]);
                }
                if (this.delimiter == ' ') { // crunch spaces if delimiter
                    while (c < cc && chars[c] == this.delimiter) { 
                        ++c;
                    } 
                }
                if (this.delimiter === chars[c]){++c;}
            } // while looking at one line
            if (chars[c - 1] == this.delimiter && this.delimiter != ' ') row.push(reviver(this.table.length - 1, row.length, ''));
            if (row.length > this.maxColumnsFound) this.maxColumnsFound = row.length;
            if (chars[c]==='\r') { ++c; }
            if (chars[c]==='\n') { ++c; }
            if (!this.isFirstRowHeader || cnt > 0) { // look at data type
                for (j = 0; j < row.length; j++) { // for each column, gather stats
                    if (j >= this.statsCnt.length || cnt == 0) {
                        this.statsCnt[j] = { dateCnt: 0, intCnt: 0, realCnt: 0, emptyCnt: 0, bitCnt: 0, equalUsed: false, fieldType: "", fieldDecs: 0, fieldPrec: 0, fldMinLen: Number.MAX_VALUE, fldMaxLen: 0 };
                    }
                    s =(j<row.length) ? row[j].replace(/^\s+|\s+$/g, '') : ""; // trim
                    // Handle Excel format of equal sign in first position
                    if (this.excelMode && s.length>2 && s.substr(0,2)==='="' && s.substr(s.length-1)==='"') {
                        this.statsCnt[j].equalUsed = true;
                        var e = new RegExp(this.quote+this.quote, "gmi");
                        s=row[j]=s.substr(2,s.length-3).replace(e,this.quote);
                        //alert(s);
                    }
                    if (s == "") {this.statsCnt[j].emptyCnt++;}
                    else {
                       if(s.length<this.statsCnt[j].fldMinLen)this.statsCnt[j].fldMinLen=s.length;
                       if(s.length>this.statsCnt[j].fldMaxLen)this.statsCnt[j].fldMaxLen=s.length;
                    }

                    ss=s;
                    // bug here .... need to verify commas are valid before removing, comment out for now
                    //if(ss.length>1)ss=ss.replace(/[\$,]/g,"").replace(/^\s+|\s+$/g, ''); // TODO: Euro support
                    if (ss != "" && ss.isNumeric()) {
                        this.statsCnt[j].realCnt++;
                        var dc=ss.split(this.decimalChar()); 
                        if(dc[0].length>this.statsCnt[j].fieldPrec)this.statsCnt[j].fieldPrec=dc[0].length;
                        if(dc.length>1){
                            if(dc[1].length>this.statsCnt[j].fieldDecs)this.statsCnt[j].fieldDecs=dc[1].length;
                        }
                        if (s.indexOf(this.decimalChar()) < 0) this.statsCnt[j].intCnt++; // TODO: Euro support
                        if (s === "0" || s === "1") this.statsCnt[j].bitCnt++;
                    }
                    
                    //var re=/^(19|20)\d\d[- \/.](0?[1-9]|1[012])[- \/.](0?[1-9]|[12][0-9]|3[01])$|^(0?[1-9]|1[012])[- \/.](0?[1-9]|[12][0-9]|3[01])[- \/.](19|20)\d\d$|^(0?[1-9]|[12][0-9]|3[01])[- \/.](0?[1-9]|1[012])[- \/.](19|20)?\d\d$/;
                    //alert("s="+s+",test="+re.test(s));
                    //if (re.test(s)) { this.statsCnt[j].dateCnt++; }
                    //alert(s+"/"+s.isDate())
                    if(s.isDateMaybe()){ this.statsCnt[j].dateCnt++; }
                } // each column in current row
            } // non-header with data
            cnt++; // # of rows
            //alert('Limit='+this.limit);
            if (this.limit != '' && cnt - (this.isFirstRowHeader ? 1 : 0) >= this.limit * 1) break;
        } // each char
        if(cnt<=0){this.dataRowsFound=0;}
        else{this.dataRowsFound = cnt - (this.isFirstRowHeader ? 1 : 0);}
        this.headerErrors = [];
        if (this.isFirstRowHeader && this.table.length > 0) {
            this.arHeaderRow = this.table.shift(); // remove header from data and assign to var
            this.headerColumns = this.arHeaderRow.length;
            for (j = 0; j < this.headerColumns; j++) {
                if (this.arHeaderRow[j].trim()=="") {
                     this.headerErrors.push({"error": 1, "field": (j+1)});
                } // record an warning here for empty header 
            }
            for (j = 0; j < this.maxColumnsFound; j++) {
                if (!this.arHeaderRow[j] || this.arHeaderRow[j]=="") this.arHeaderRow[j]="FIELD" + (j + 1);
                this.arHeaderRow[j]=this.arHeaderRow[j].trim();
            }
        }
        this.fieldImbalance = false;
        this.fieldImbalanceRows = [];
        if(this.dataRowsFound>0)this.fieldImbalanceRows.push(1);
        this.headerImbalanceRows = [];
        this.headerImbalance = false;
        firstRowColumnsFound = this.dataRowsFound > 0 ? this.table[0].length : 0;
        for(k=0;k<this.table.length;k++){
            if(this.table[k].length < this.maxColumnsFound) {
                for (j = this.table[k].length; j < this.maxColumnsFound; j++) {
                   if (j >= this.statsCnt.length) {
                       this.statsCnt[j] = { dateCnt: 0, intCnt: 0, realCnt: 0, emptyCnt: 0, bitCnt: 0, equalUsed: false, fieldType: "", fieldDecs: 0, fieldPrec: 0, fldMinLen: Number.MAX_VALUE, fldMaxLen: 0 };
                   }
                   this.statsCnt[j].emptyCnt++;
                } 
             }
             if(this.isFirstRowHeader && this.headerColumns != this.table[k].length) {
                this.headerImbalance = true;
                if(this.headerImbalanceRows.length<5){this.headerImbalanceRows.push(k+2);}
             } 
             if(!this.isFirstRowHeader && firstRowColumnsFound != this.table[k].length) {
                this.fieldImbalance = true;
                if(this.fieldImbalanceRows.length<5)this.fieldImbalanceRows.push(k+1);  
             }   
        }
        if (this.arHeaderRow.length > 0) { 
            for (j = 0; j < this.arHeaderRow.length; j++) this.determineCsvColType(j); 
        }
        else if (this.table.length > 0) {
            for (j = 0; j < this.maxColumnsFound; j++) {
                if (!this.arHeaderRow[j] || this.arHeaderRow[j] == "")this.arHeaderRow[j]="FIELD" + (j + 1);
                this.determineCsvColType(j);
            }
        }

        for (j = 0; j < this.arHeaderRow.length; j++) {
            if (this.headerToUpper) this.arHeaderRow[j] = this.arHeaderRow[j].toUpperCase();
            if (this.headerToLower) this.arHeaderRow[j] = this.arHeaderRow[j].toLowerCase();
        }
        if(this.sortPoss!=""){
            this.table.sort(this.sortCsv);
        }
        return 0;
    },

    setSortFlds: function(flds) {
        CSV.sortPoss = flds.trim(); 
    },

    sortCsv: function(a,b) {
        //alert('sortCSV, sortcols='+CSV.sortPoss+', a='+a+", b="+b+",dataRowsFound="+CSV.dataRowsFound);
        if(CSV.sortPoss=="")return 0;
        var p = [];
        var q = [];
        var t = [];
        p=CSV.sortPoss.split(",");
        for(j=0;j<p.length;j++) {
            q[j]=1;
            t[j] = "";
            if(p[j].right(1).toUpperCase()=='D') {
                q[j]=-1;
                p[j]=p[j].left(p[j].length-1);
            }
            switch(p[j].left(1).toUpperCase()) {
                case 'C' :
                   t[j]='C';
                   p[j]=p[j].right(p[j].length-1);
                   break;
                case 'N' :
                   t[j]='N';
                   p[j]=p[j].right(p[j].length-1);
                   break;
                default: 
                   break;
           }

        }
        for(j=0;j<p.length;j++) if(!isNaN(p[j])) p[j]=(p[j]*1)-1; else p[j]=-1;
        for(j=0;j<p.length;j++) if(p[j]>=a.length) p[j]=-1;
        for(j=0;j<p.length;j++) {
            if(p[j]<0)continue;
            //alert("j="+j+",p[j]="+p[j]+"type="+t[j]);
            if(!isNaN(a[p[j]].replace(/[\$,]/g,"")) && !isNaN(b[p[j]].replace(/[\$,]/g,""))&&CSV.dataRowsFound==CSV.statsCnt[p[j]].realCnt+CSV.statsCnt[p[j]].emptyCnt&&t[j]!='C') {
               if(a[p[j]].replace(/[\$,]/g,"")*1 < b[p[j]].replace(/[\$,]/g,"")*1) return -1 * q[j];
               if(a[p[j]].replace(/[\$,]/g,"")*1 > b[p[j]].replace(/[\$,]/g,"")*1) return 1 * q[j];
            }
            else {
               if(CSV.sortIgnoreCase) {
                   if(a[p[j]].toUpperCase() < b[p[j]].toUpperCase()) return -1 * q[j];
                   if(a[p[j]].toUpperCase() > b[p[j]].toUpperCase()) return 1 * q[j];
               } else {
                   if(a[p[j]] < b[p[j]]) return -1 * q[j];
                   if(a[p[j]] > b[p[j]]) return 1 * q[j];
               }
            }
        }
        return 0;
    },

    determineCsvColType: function (colPos) {
        var j = 0;
        var k = 0;
        if (this.table.length == 0) return "";
        //alert("this.table.length="+this.table.length+",colpos="+colPos);
        //alert("date="+this.statsCnt[colPos].dateCnt);

        if (colPos >= this.statsCnt.length) this.statsCnt[colPos] = { dateCnt: 0, intCnt: 0, realCnt: 0, emptyCnt: 0, bitCnt: 0, fieldType: "" };
        if (this.table.length == this.statsCnt[colPos].bitCnt) { this.statsCnt[colPos].fieldType = "B"; return "B"; }  //Bit
        if (this.table.length == this.statsCnt[colPos].dateCnt) { this.statsCnt[colPos].fieldType = "D"; return "D"; } //Date
        if (this.table.length == this.statsCnt[colPos].intCnt) { this.statsCnt[colPos].fieldType = "I"; return "I"; }  //Int
        if (this.table.length == this.statsCnt[colPos].realCnt) { this.statsCnt[colPos].fieldType = "N"; return "N"; } //Numeric

        if (this.statsCnt[colPos].bitCnt > 0 && this.table.length == this.statsCnt[colPos].bitCnt + this.statsCnt[colPos].emptyCnt) { this.statsCnt[colPos].fieldType = "B"; return "B"; }  //Bit
        if (this.statsCnt[colPos].dateCnt > 0 && this.table.length == this.statsCnt[colPos].dateCnt + this.statsCnt[colPos].emptyCnt) { this.statsCnt[colPos].fieldType = "D"; return "D"; } //Date
        if (this.statsCnt[colPos].intCnt > 0 && this.table.length == this.statsCnt[colPos].intCnt + this.statsCnt[colPos].emptyCnt) { this.statsCnt[colPos].fieldType = "I"; return "I"; }  //Int
        if (this.statsCnt[colPos].realCnt > 0 && this.table.length == this.statsCnt[colPos].realCnt + this.statsCnt[colPos].emptyCnt) { this.statsCnt[colPos].fieldType = "N"; return "N"; } //Numeric

        this.statsCnt[colPos].fieldType = "VC";
        return "VC"; // variable character string
    },

    // convert a table to csv
    stringify: function (eol,replacer) { // recreate csv from table
        replacer = replacer || function (r, c, v) { return v; };
        var csv = '';
        var c;
        var cc;
        var r;
        eol = eol || "\n";
        if(this.isFirstRowHeader)this.table.unshift(this.arHeaderRow);
        var rr = this.table.length;
        var re = new RegExp("[" + this.delimiter + "\r\n]","gmi");
        var cell;
        for (r = 0; r < rr; ++r) { // for each row
            if (r) { csv += eol; } // add newline for rows>0
            for (c = 0, cc = this.table[r].length; c < cc; ++c) {
                if (c) { csv += this.delimiter; }
                cell = replacer(r, c, this.table[r][c]);
                // If comma or newline then wrap quotes around text and double up on quotes 
                // Note that , needs to be changed to this.delimiter
                //if (/[,\r\n"]/.test(cell)) { cell = this.quote + cell.replace(/"/g, this.quote+this.quote) + this.quote; }
                if (re.test(cell)) { cell = this.outputQuote + cell.replace(/"/g, this.outputQuote+this.outputQuote) + this.outputQuote; }
                csv += (cell || 0 === cell) ? cell : '';
            }
        }
        return csv;
    }
};
