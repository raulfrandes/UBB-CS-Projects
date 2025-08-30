#include "Var0.h"
#include "Utils.h"

Var0::Var0(){}

void Var0::run(std::string fileName)
{
	std::vector<int> number1;
	std::vector<int> number2;

	readNumberFromFile("Numbers/" + fileName + "/Number1.txt", number1);
	readNumberFromFile("Numbers/" + fileName + "/Number2.txt", number2);

	std::vector<int> result;
	int carry = 0;
	for (int i = 0; i < number1.size() || i < number2.size(); i++) {
		int digit1 = (i < number1.size()) ? number1[i] : 0;
		int digit2 = (i < number2.size()) ? number2[i] : 0;
		int sum = digit1 + digit2 + carry;
		carry = sum / 10;
		result.push_back(sum % 10);
	}
	if (carry > 0) {
		result.push_back(carry);
	}

	std::string resultFileName = "Numbers/" + fileName + "/Number3Var0.txt";
	Utils::writeNumberToFile(resultFileName, result);
}

void Var0::readNumberFromFile(std::string fileName, std::vector<int>& number)
{
	std::ifstream file(fileName);
	if (!file) {
		std::cerr << "Error opening file " << fileName << std::endl;
		return;
	}

	int size;
	file >> size;

	number.resize(size);

	char digit;
	for (int i = 0; i < size; i++) {
		file >> digit;
		if (isdigit(digit)) {
			number[i] = digit - '0';
		}
		else {
			std::cerr << "Error reading digit " << digit << std::endl;
			return;
		}
	}
}