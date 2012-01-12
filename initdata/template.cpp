#ifndef __DATA_%(lang)s_%(lesson)s
#define __DATA_%(lang)s_%(lesson)s

class DATA_%(lang)s_%(lesson)s
{
public:
	static const wchar_t* texts[];
	static const int num = %(num)s;
};

const wchar_t * DATA_%(lang)s_%(lesson)s::texts[] =
{
		%(arr)s
};

#endif // __DATA_%(lang)s_%(lesson)s



