#include "ui.h"
#include "tests.h"

int main()
{
	runTests();
	run();
	_CrtDumpMemoryLeaks();
	return 0;
}
