"package com.voc4u.lang.lesson%s\n// %s""package com.voc4u.lang.lesson%s\n// %s""\" //"#!/usr/bin/python


import os
import sys

langs = [ "en" , "DE", "CZ", "FR", "ES"]
types = ["android", "bada"]
description = ["make xml files for android", "make xml files for bada"]
actualtype = "java"
temprow = {types[0] : "<string name=\"$(ID)\" formatted=\"false\">$(TEXT)</string>", types[1] : "<text id=\"$(ID)\">$(TEXT)</text>"}
template = { types[0] : "android.xml.template" , types[1] : "bada.xml.template"}
keyToUpper = {types[0] : False, types[1] : True}
keyPrefix = {types[0] : "", types[1] : "IDS_"}
textReplaces = {types[0] : [ ["\'" , "\\\'"], ["\"" , "\\\""] ], types[1] : [["\'" , "\\\'"], ["\"" , "\\\""]] }


def showHelp(sys):
    print "type:\n"
    n = 0
    for type in types:
        print "\t" + type + " : " + description[n]
        n += 1
    
    print "usage:"
    print sys.argv[0] + " [--help] type [src_dir] [desc_dir]"
    print "or"
    print sys.argv[0] + " --diff master_lang second_lang [src_dir] [desc_dir]"



def writeDesc(descpath, arr):
    f = open(descpath, 'w')
    
    
    temppath = "translates/" + template[actualtype]
    if not os.path.exists(temppath) :
        print "file : " + temppath + " IS NOT EXIST"
        return ;
    
    tempfile = open(temppath, "r");
    temp = tempfile.read();
    temp = temp % {"arr" : arr}
    
#    fi = initfile[actualtype] % {"lesson" : str(l), "name" : la}
#    fi += ";\n\npublic class Data" + la
#    fi += "\n{\npublic static final String[] text = new String[]{\n" + arr
#    fi += "\n};\n}"
    
    print >> f, temp
    f.close()
    tempfile.close()

def getFromRowIDTEXT(btoupper, line):
    lang = line.split(";")
    key = lang[0]
    if btoupper:
        key = key.upper()
    else:
        key = key.lower()
    text = ""
    if len(lang) > 1:
        text = lang[1]
        if text.endswith("\n"):
            text = text[:-1]
        
        text = text.strip()
        if len(text) < 1:
            return { }
    
    return { "ID" : key, "TEXT": text }

def addRowToArr(tempr, btoupper, line):
    arr = ""
    obj = getFromRowIDTEXT(btoupper, line)
    
    if not obj.has_key("ID"):
        return arr;
        
    text = obj["TEXT"]
    key = keyPrefix[actualtype] + obj["ID"] 
       
    replacement = textReplaces[actualtype]   
    if len(replacement) > 0:
            for repl in replacement:
                text = text.replace(repl[0],repl[1])
            
    tr = tempr.replace("$(ID)", key).replace("$(TEXT)", text)
    #print( "ID: " + key);
    arr += "\n\t" + tr
        
    return arr

def languageFile(lang):
    srcpath = "translates/" + lang + ".csv"
    if not os.path.exists(srcpath) :
        print "file : " + srcpath + " IS NOT EXIST"
        return "";
    
    return srcpath

def makeFile(srcdir, descdir, la):
    descpath = descdir + "/strings-" + actualtype + "-" + la + ".xml"
    
    print descpath
	
    srcpath = languageFile(la)
    if len(srcpath) < 1:
        return 

    arr = ""
    count = 0
    tempr = temprow[actualtype]
    btoupper = keyToUpper[actualtype]
    for line in open(srcpath, 'r').readlines():
        arr += addRowToArr(tempr, btoupper, line)
        
    # brutal giga hack :-)
    # must be make other way!
    if actualtype == "android" :
        arr += "<string-array name=\"lessons\"><item>Essential</item><item>Elementary</item><item>Pre-intermediate</item><item>Intermediate</item> </string-array>"
        
    writeDesc(descpath, arr)

def make(srcdir, descdir):
    if descdir.endswith("/"):
        descdir = descdir[0:-1]
    if srcdir.endswith("/"):
        srcdir = srcdir[0:-1]

    for lang in langs:
        makeFile(srcdir, descdir, lang)
    
def diffLang(master, slave):
    
    masterpath = languageFile(master)
    if len(masterpath) < 1:
        return 
    
    slavepath = languageFile(slave)
    if len(slavepath) < 1:
        return 

    missing = "";

    for master in open(masterpath, 'r').readlines():
        o = getFromRowIDTEXT(False, master)
        
        if o.has_key("ID"):
            have = False
            for slave in open(slavepath, 'r').readlines():
                 s = getFromRowIDTEXT(False, slave)
                 if s.has_key("ID") and s["ID"] == o["ID"] and s["TEXT"].strip() > 0:
                     have = True
                     break;
                 
        
            if not have:
                missing += o["ID"] + ";" + o["TEXT"] + "\n"
    
    if len(missing) < 1:
        print "OK\n"
    else:
        print "missing: \n"
        print missing

dir = "."
descdir = "."


if len(sys.argv) < 2 or sys.argv[1] == "--help" :
    showHelp(sys)
    sys.exit()

actualtype = sys.argv[1]


if len(sys.argv) > 2:
	dir = sys.argv[2]
if len(sys.argv) > 3:
	descdir = sys.argv[3]

if dir == "--help":
    showHelp(sys)
elif actualtype == "--diff":
    diffLang(sys.argv[2], sys.argv[3])
else:
	make(dir, descdir)
