#include <gtest/gtest.h>
#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include "calc.hpp"

TEST(Calc, addTwoNumbers) {
  std::string input {"3 +5"};
  EXPECT_EQ(Calc::eval(input), 8);
}

// write your own test cases to demonstrate the 
// added functionality
// used EXPECT_NEAR since we are using foat
// float numbers
TEST(Calc, floatPrecisionAddition) {
  std::string input {"0.5 + 0.7"};
  EXPECT_NEAR(Calc::eval(input), 1.2, 1e-4);
}

TEST(Calc, floatPrecisionSubstraction) {
  std::string input {"5.5 - 2.2"};
  EXPECT_NEAR(Calc::eval(input), 3.3, 1e-6);
}

TEST(Calc, floatPrecisionMuliplication) {
  std::string input {"1.5 * 4"};
  EXPECT_NEAR(Calc::eval(input), 6, 1e-6);
}

TEST(Calc, floatPrecisionDivision) {
  std::string input {"12.5 / 2.5"};
  EXPECT_NEAR(Calc::eval(input), 5, 1e-6);
}

TEST(Calc, floatWithParanthesis) {
  std::string input {"13.65/ (5.6 + 2.654)"};
  EXPECT_NEAR(Calc::eval(input), 1.65, 1e-2);
}

// Uninary Minus operator
TEST(Calc, unaryMinusMultiplication) {
  std::string input {"-4.07 * 5"};
  EXPECT_NEAR(Calc::eval(input), -20.35, 1e-2);
}

TEST(Calc, unaryMinusDivision) {
  std::string input {"-24.65/ (5.246 + 4.569)"};
  EXPECT_NEAR(Calc::eval(input), -2.51, 1e-2);
}

TEST(Calc, unaryMinusSubstraction) {
  std::string input {"(-23.569 - 5326.3) * 5"};
  EXPECT_NEAR(Calc::eval(input), -26749.345, 1e-2);
}

// exponentiation 
TEST(Calc, expFirstTest) {
  std::string input {"5 ^ 4"};
  EXPECT_NEAR(Calc::eval(input), 625, 1e-2);
}

TEST(Calc, expRightAssociative1) {
  std::string input {"(2 ^ 3) * 8"};
  EXPECT_NEAR(Calc::eval(input), 64, 1e-2);
}

TEST(Calc, expRightAssociative2) {
  std::string input {"-2 ^ 3 ^ 2"};
  EXPECT_NEAR(Calc::eval(input), -512, 1e-2);
}

TEST(Calc, expRightAssociative3) {
  std::string input {"(10 ^ 5)/ (7 * 8)"};
  EXPECT_NEAR(Calc::eval(input), 1785.714286, 1e-2);
}

// the decimal value is slightly off
// I think beacuse of using std::powf
// adjusted the to near decimal value using 2e -2
TEST(Calc, expWithUnaryAndParanthesis) {
  std::string input {" (-9.032) ^ 5"};
  EXPECT_NEAR(Calc::eval(input), -60106.25155, 2e-2);
}

int main(int argc, char* argv[]) {
  ::testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
}
