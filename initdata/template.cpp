#ifndef __DATA_%(lang)s_%(lesson)s
#define __DATA_%(lang)s_%(lesson)s

class DATA_%(lang)s_%(lesson)s
{
public:
	static const char** texts;
	static const int num;
};

const char ** __DATA_%(lang)s_%(lesson)s::texts =
{
		%(arr)s
};

const int __DATA_%(lang)s_%(lesson)s::num = %(num)s;

#endif // __DATA_%(lang)s_%(lesson)s



