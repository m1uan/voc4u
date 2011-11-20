"package com.voc4u.lang.lesson%s\n// %s""package com.voc4u.lang.lesson%s\n// %s""\" //"#!/usr/bin/python


import os
import sys




dirs = [ 1, 2, 3, 4, 5 ]
langs = [ "CS" , "DE", "EN" , "ES", "FR", "PL", "PT"]
hidden = [ "es", "cs", "de", "lv", "sq", "lt", "tl", "fi", "no", "sl", "bs", "eo", "de " ] 
dirname = "lesson"
filename = "Data"
sourcend = ".txt"
javafile = "package com.voc4u.lang.lesson%s;\npublic class Data%s\n{\npublic static final String[] text = new String[]{\n%s\n};\n}"
types = ["plain", "java", "cpp", "make_target_handler_cpp"]
description = ["make plain text", "make java files from plain text", "make cpp files from plain text", "make navigator for cpp"]
actualtype = "java"




template = { types[0] : "template.cvs" , types[1] : "template.java", types[2] : "template.cpp"}
prefix = { types[0] : "" , types[1] : "\t\t, \"", types[2] : "\t\t, L\""}
surfix = { types[0] : " ; " , types[1] : "\" // ", types[2] : "\" // "}
descend = { types[0] : ".cvs" , types[1] : ".java", types[2] : ".h"}
initfile = { types[0] : "%(lesson)s - %(lang)s" , types[1] : "package com.voc4u.lang.lesson%(lesson)s\n// %(lang)s", types[2] : "#ifndef _DATA_%(lesson)s_%(name)s\n#define _DATA_%(lesson)s_%(name)s"}

def showHelp(sys):
    print "type:\n"
    n = 0
    for type in types:
        print "\t" + type + " : " + description[n]
        n += 1
    
    print "usage:"
    print sys.argv[0] + " [--help] type [src_dir] [desc_dir]"

def hiddenWords(word):
  for hid in hidden:
     if hid == word:
       return False
  return True

def lastCheck(words):
	arr = words.split("|")
	if len(arr) > 0:
		words = ""
		num = 0
		for a in arr:
			if hiddenWords(a) :
				num += 1
				words += ", " + a
			if num > 1: break
            
		return words[1:]
	return words


def writeDesc(descpath, arr, l, la, count):
    f = open(descpath, 'w')
    
    tempfile = open(template[actualtype], "r");
    temp = tempfile.read();
    temp = temp % {"lesson" : str(l), "lang" : la, "arr" : arr , "num" : str(count)}
    
#    fi = initfile[actualtype] % {"lesson" : str(l), "name" : la}
#    fi += ";\n\npublic class Data" + la
#    fi += "\n{\npublic static final String[] text = new String[]{\n" + arr
#    fi += "\n};\n}"
    
    print >> f, temp
    f.close()
    tempfile.close()

def makeFile(srcpath, descpath, l, la):
    print descpath
	
    if not os.path.exists(srcpath) :
        print "file : " + srcpath + " IS NOT EXIST"
        return ;
    
    arr = ""
    count = 0
    for line in open(srcpath, 'r').readlines():
	    lang = line.split(";");
	    if len(lang) > 1:
	         arr += prefix[actualtype] + lastCheck(lang[0]) + surfix[actualtype] + lang[1]
	         count+=1
             
    writeDesc(descpath, arr[3:], l , la, count)

def make(srcdir, descdir):
    if descdir.endswith("/"):
        descdir = descdir[0:-1]
    if srcdir.endswith("/"):
        srcdir = srcdir[0:-1]

    for l in dirs:
        for lang in langs:
            body = "/" + dirname + str(l) + "/" + filename + lang;
            srcpath = srcdir + body + sourcend
            descpath = descdir + body + descend[actualtype]
            makeFile(srcpath, descpath, l, lang)
    
    
#
# make navigation file for cpp
#
def makeNavigateCPPFILE():
    tinc = "#include \"lesson%(lesson)s/Data%(lang)s.h\"\n";
    tifs = "\t\tif( lesson == %(lesson)s && lang == \"%(lang)s\"){ count = DATA_%(lang)s_%(lesson)s::num;return (wchar_t**)DATA_%(lang)s_%(lesson)s::texts;}\n"
    
    inc = ""
    ifs = ""
    for lesson in dirs:
        for lang in langs:
            param = {"lesson" : lesson, "lang" :lang}
            inc += tinc % param
            ifs += tifs % param
            
    tempfile = open("target_template.cpp", "r");
    temp = tempfile.read();
    temp = temp % {"include" : inc, "ifcondition" : ifs}
    tempfile.close()
    
    f = open("InitData.cpp", "w")
    print >> f, temp
    f.close()
    
dir = "."
descdir = "."


if len(sys.argv) < 2 or sys.argv[1] == "--help" or (sys.argv[1] != types[0] and sys.argv[1] != types[1] and sys.argv[1] != types[2] and sys.argv[1] != types[3]):
    showHelp(sys)
    sys.exit()

actualtype = sys.argv[1]



if len(sys.argv) > 2:
	dir = sys.argv[2]
if len(sys.argv) > 3:
	descdir = sys.argv[3]

if dir == "--help":
    showHelp(sys)
elif actualtype == types[3]:
    makeNavigateCPPFILE()
else:
	make(dir, descdir)
