#pragma once
#include <vector>
#include <fstream>
#include <iostream>
#include <string>

class Var0
{
private:
	void readNumberFromFile(std::string fileName, std::vector<int>& number);

public:
	Var0();

	void run(std::string fileName);
};

