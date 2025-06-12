#include <iostream>
#include <sstream>
#include <fstream>
#include <optional>
#include <vector>

//enum class
enum class TokenType
{
   _return,
    init_lit,
    semi,
};

//constructor
struct Token
{
    TokenType type;
    std::optional<std::string> value; //holds the value if integer present
};

//returns vector of tokens
std::vector<Token> tokenize(const std::string& str)
{
    std::vector<Token> tokens;

    std::string buf;
    for (int i = 0; i < str.length(); ++i)
    {
        char c = str.at(i);
        if (std::isalpha(c))
        {
            buf.push_back(c);
            ++i;
            while (std::isalnum(str.at(i)))
            {
                buf.push_back(str.at(i));
                ++i;
            }
            --i;
            if (buf == "return")
            {
                tokens.push_back({.type = TokenType::_return});
                buf.clear();
                continue;
            }
            else
            {
                std::cout << "yOu FuCkEd Up!" << std::endl;
                exit(EXIT_FAILURE);
            }
        }
        else if (std::isdigit(c))
        {
            buf.push_back(c);
            ++i;
            while (std::isdigit(str.at(i)))
            {
                buf.push_back(str.at(i));
                ++i;
            }
            --i;
            tokens.push_back({.type = TokenType::init_lit,  .value = buf});
            buf.clear();
        }
        else if (c == ';')
        {
            tokens.push_back({.type = TokenType::semi});

        }
        else if (std::isspace(c))
        {
            continue;
        }
        else
        {
            std::cout << "yOu FuCkEd Up!" << std::endl;
            exit(EXIT_FAILURE);
        }
    }

    return tokens;
}

//return token as asm
std::string tokenToAsm(const std::vector<Token>& tokens)
{
    std::stringstream output;
    output << "global _start\n_start:\n";
    for (int i = 0; i < tokens.size(); ++i)
    {
        const Token& token = tokens.at(i);
        if (token.type == TokenType::_return)
        {
            if (i + 1 < tokens.size() && tokens.at(i + 1).type == TokenType::init_lit)
            {
                if (i + 2 < tokens.size() && tokens.at(i +2).type == TokenType::semi)
                {
                    output << "   mov rax, 60\n";
                    output << "   mov rdi, " << tokens.at(i + 1).value.value() << std::endl;
                    output << "   syscall";
                }
            }
        }
    }
    return output.str();
}

int main(int argc, char* argv[]) {
    //Expect one arg after the program name
    if (argc != 2)
    {
        std::cerr << "Invalid Usage. Valid Usage is..." << std::endl;
        std::cerr << "nitro <input.ni>" << std::endl;
        return EXIT_FAILURE;
    }


    //Read the entire string file - test.ni
    std::string contents;
    {
        std::stringstream contents_stream;
        std::fstream input(argv[1], std::ios::in);
        if (!input) {
            std::cerr << "Error: Could not open file " << argv[1] << std::endl;
            return EXIT_FAILURE;
        }
        contents_stream << input.rdbuf();
        contents = contents_stream.str();
    }


    {
         std::vector<Token> tokens = tokenize(contents);
         std::fstream file("output.asm", std::ios::out);
         file << tokenToAsm(tokens);
    }

    //system commands in WIN
    system("wsl nasm -felf64 output.asm -o output.o");
    system("wsl ld output.o -o output");

    return EXIT_SUCCESS;
}