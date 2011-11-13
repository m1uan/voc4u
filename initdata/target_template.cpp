#include "InitData.h"

%(include)s

wchar_t ** getInitData(String lang, const int lesson, int &count)
{
	%(ifcondition)s

	count = 0;
	return null;
}




