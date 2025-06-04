#include <iostream>
#include <vector>
#include <map>
#include <string>
#include <algorithm>
#include <cmath>
#include "calc.hpp"

std::ostream& Calc::operator<<(std::ostream& out, const Calc::Token& t) {
  out << "{'" << t.type << "', " << t.val << '}';
  return out;
}

bool Calc::operator==(const Calc::Token& a, const Calc::Token& b) {
  if (a.type != b.type) {
    return false;
  }
  if (a.type == 'n') {
    return a.val == b.val;
  }
  return true;
}

// This is the function for you to write
std::vector<Calc::Token> Calc::infixToPostfix(const std::vector<Token>& input) {
  // token output vector
  auto outputStack = std::vector<Token> ();
  // vector to hold the operators
  auto operatorStack = std::vector<Token> ();


  for(const Token& t : input){
    // if t a number pushing it to the output stack
    if(t.type == 'n'){
      outputStack.push_back(t);
    // when t has open parentheses push it operator stack
    }else if(t.type == '('){
      operatorStack.push_back(t);
    // when t is a closed parentheses, 
    // push them to the outpustack till it reaches the open parentheses
    //  and pop it from the operator stack
    }else if(t.type == ')'){
      while(!operatorStack.empty() && operatorStack.back().type != '(') {
        outputStack.push_back(operatorStack.back());
        operatorStack.pop_back();
      }
      // removing the open parentheses once the all the t pushed into the 
      // outputStack
      if(!operatorStack.empty() && operatorStack.back().type == '('){
        operatorStack.pop_back();
      }
    }else{
      // till the operator stack has not a (,
      // push to output stack the operator according to precedence 
      // + and - = 1 and * and / = 2, since * and / has a higher precedence
      // using right associative handled the exponentiation's precedence
      while(!operatorStack.empty() && operatorStack.back().type != '(' && 
            ((t.precedence() < operatorStack.back().precedence()) ||
            (t.precedence() == operatorStack.back().precedence() &&
            !t.isRightAssociative()))){
          outputStack.push_back(operatorStack.back());
          operatorStack.pop_back();
      }
       operatorStack.push_back(t);
    }
  }

  // reversing the operator stack using std::reverse
  std::reverse(operatorStack.begin(), operatorStack.end());
  // inserting the reversed operator stack to the end of the output stack
  outputStack.insert(outputStack.end(), operatorStack.begin(), operatorStack.end());

  return outputStack;
}

// evalPostfix evaluates a vector of tokens in postfix notation
// This function was done in tutorial Week 10
float Calc::evalPostfix(const std::vector<Token>& tokens) {
  if (tokens.empty()) {
    return 0;
  }
  std::vector<float> stack;
  for (Token t : tokens) {
    if (t.type == 'n') {
      stack.push_back(t.val);
    }else if(t.type == 'u'){
      // negates the top value of the stack using u
      if(!stack.empty()){
        float val = stack.back();
        stack.pop_back();
        stack.push_back(-val);
      }
    } else {
      float val = 0.0;
      if (t.type == '+') {
        val = stack.back() + *(stack.end()-2);
      } else if (t.type == '*') {
        val = stack.back() * *(stack.end()-2);
      } else if (t.type == '-') {
        val = *(stack.end()-2) - stack.back();
      } else if (t.type == '/') {
        if (stack.back() == 0) {
          throw std::runtime_error("divide by zero");
        }
        val = *(stack.end()-2) / stack.back();
      // handling ^ with std::pow
      // https://en.cppreference.com/w/cpp/numeric/math/pow
      }else if (t.type == '^'){
        val = std::powf(*(stack.end() - 2), stack.back());
      } else {
          std::cout << "invalid token\n";
      }
      stack.pop_back();
      stack.pop_back();
      stack.push_back(val);
    }
  }
  return stack.back();
}

// tokenise takes a string and parses it into a vector of tokens
std::vector<Calc::Token> Calc::tokenise(const std::string& expression) {
  const std::vector<char> symbols = {'+', '-', '*', '/', '(', ')', '^'};
  std::vector<Token> tokens {};
  for (std::size_t i =0; i < expression.size(); ++i) {
    const char c = expression[i];

    // check if c is one of '+', '-', '*', '/', '(', ')'
    if (std::find(symbols.begin(), symbols.end(), c) != symbols.end()) {
      // push u if there is a minus(-)
      if(c == '-'){
        if(tokens.empty() || (tokens.back().type != 'n' && tokens.back().type != ')')){
          tokens.push_back({'u'});
          continue;
        }
      }
      tokens.push_back({c});
    } else if (isdigit(c) || c == '.') {
      // process multiple digit integers
      std::string num {c};
      // decimal found
      bool decimalFound = (c == '.');

      while (i + 1 < expression.size() && (isdigit(expression[i + 1]) 
              || (!decimalFound && expression[i + 1] == '.'))) {
        ++i;
        // expression has decimal change to true
        if(expression[i] == '.'){
          decimalFound = true;
        }
        num.push_back(expression[i]);
      }
      tokens.push_back({'n', std::stof(num)}); //changed it to stod to handle double
    }
  }
  return tokens;
}

// eval puts the pieces together to take a string with an
// arithmetic expression and output its evaluation
float Calc::eval(const std::string& expression) {
  std::vector<Token> tokens = tokenise(expression);
  std::vector<Token> postfix = infixToPostfix(tokens);
  return evalPostfix(postfix);
}
