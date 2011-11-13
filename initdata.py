#!/usr/bin/python


import os
from PIL import Image
import sys

dirs = [ 1, 2, 3, 4, 5 ]
langs = [ "CS" , "DE", "EN" , "ES", "FR","PL", "PT"]
hidden = [ "es", "cs", "de", "lv", "sq", "lt", "tl", "fi", "no", "sl", "bs", "eo", "de " ] 
dirname = "lesson"
filename = "Data"
sourcend = ".txt"
descend = ".java"
javafile = "package com.voc4u.lang.lesson%s;\npublic class Data%s\n{\npublic static final String[] text = new String[]{\n%s\n};\n}"

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
				words += "|" + a
			if num > 2:
			  break
		return words[1:]
	return words


def writeDesc(descpath, arr, l, la):
  f = open(descpath,'w')
  
  fi = "package com.voc4u.lang.lesson" + str(l) +";\n"
  fi += "\npublic class Data" + la
  fi += "\n{\npublic static final String[] text = new String[]{\n" + arr
  fi += "\n};\n}"
  
  print >>f, fi
  f.close()

def makeFile(srcpath, descpath, l, la):
	print descpath
	
	if not os.path.exists(srcpath) :
		print "file : " + srcpath + " IS NOT EXIST"
		return ;
	
	arr = ""
	for line in open(srcpath,'r').readlines():
	    lang = line.split(";");
	    if len(lang) > 1:
	      arr += "\t\t, \"" + lastCheck(lang[0]) + "\" // " + lang[1]
	    
	writeDesc(descpath, arr[3:], l , la)

def make(srcdir, descdir):
	if descdir.endswith("/"):
		descdir = descdir[0:-1]
	if srcdir.endswith("/"):
		srcdir = srcdir[0:-1]
		
	for l in dirs:
		for lang in langs:
			body = "/" + dirname + str(l) + "/" +filename + lang;
			srcpath = srcdir + body + sourcend
			descpath = descdir + body + descend 
			makeFile(srcpath, descpath, l, lang)


dir = "."
descdir = "."



if len(sys.argv) > 1:
	dir = sys.argv[1]
if len(sys.argv) > 2:
	descdir = sys.argv[2]

if dir == "--help":
	print "usage:"
	print sys.argv[0] + " [src_dir|--help] [desc_dir]"
else:
	make(dir, descdir)
