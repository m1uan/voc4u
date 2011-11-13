#include "InitData.h"

%(include)s

char ** getInitData(String lang, const int lesson, int &count)
{
	%(ifcondition)s

	count = 0;
	return null;
}




