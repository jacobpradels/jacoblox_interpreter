package com.cecs497.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cecs497.lox.TokenType.*;

/**
 * Used to parse a file for tokens
 */
class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private static final Map<String, TokenType> keywords;
    
    /**
     * Default constructor
     * Argument: String source - Source code to read in
     */
    Scanner (String source) {
        this.source = source;
    }

    /**
     * scanTokens()
     * Scans through file tokenizing lexemes
     * Returns : List<Token> - The tokenized source code
     */
    List<Token> scanTokens() {
        
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF,"",null,line));
        return tokens;
    }


    /**
     * Scans an individual token based on the current value of
     * current.
     * Adds token based on character at current
     */
    private void scanToken() {
        char c = advance();
        switch (c) {
            // Single character lexemes
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            // Multi character lexemes
            // If next token is in match, advance and use two character Token,
            // Otherwise this is a single character lexeme
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : LESS);
                break;
            case '/':
                // A comment goes until the end of the line
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    // Just division
                    addToken(SLASH);
                }
                break;
            // Ignored lexemes
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            // More complex lexemes
            case '"': string(); break;
            default:
            if (isDigit(c)) {
                number();
            } else if (isAlpha(c)) {
                identifier();
            } else {
                Lox.error(line, "Unexpected character.");
            }
            break;
        }
    }

    /**
     * Continues scanning while the next character is alpha numeric.
     */
    private void identifier() {
        // Interesting choice, variables can include numbers
        // just can't start with numbers.
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }

    /**
     * Continuously peeks forward until reaching terminating ".
     * @implNote adds Token of enclosed string with " symbols stripped.
     */
    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            Lox.error(line,"Unterminated string.");
            return;
        }

        // The closing ".
        advance();
        
        // Trim the surrounding quotes
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    /**
     * Determines if the character at current is equal to expected. Only advances if
     * returns true
     * @param expected char to compare
     * @return true if current character is same as expected and not end of line
     */
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    /**
     * Used to peek forward and get the value of current without advancing
     * @return Char at location of current
     */
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    /**
     * Used to peek forward and get value ahead of current without advancing
     * @return Char at location of (current + 1)
     */
    private char peekNext() {
        // I'd rather add an overloaded method of isAtEnd(int offset)
        // But since this is the only case, i'll leave it.
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }
    
    /**
     * Checks if character is in alphabet.
     * @param c character to check
     * @return true if character is in alphabet.
     */
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    /**
     * Checks if character is alphanumeric.
     * @param c character to check.
     * @return true if character is alphanumeric.
     */
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    /**
     * Checks if a character is numeric
     * @param c the character to check
     * @return true if character is numeric
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Handles the processing of a number.
     * Consumes all numbers up to and including numbers after first decimal point
     */
    private void number() {
        while (isDigit(peek())) advance();

        //Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();
            while (isDigit(peek())) advance();
        }

        addToken(NUMBER,
            Double.parseDouble(source.substring(start,current)));
    }

    /**
     * Helper function for determining if we're at end of file
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }
    /**
     * Helper function, returns value of the character at current from source
     * then advances current
     */
    private char advance() {
        return source.charAt(current++);
    }
    
    /**
     * Helper function for adding token with no literal
     * @param type TokenType
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * Overloaded function for tokens with Object literals
     * @param type TokenType
     * @param literal Literal data representation
     */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }


    /**
     * Declare reserved keywords
     */
    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fun",    FUN);
        keywords.put("if",     IF);
        keywords.put("nil",    NIL);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",   THIS);
        keywords.put("true",   TRUE);
        keywords.put("var",    VAR);
        keywords.put("while",  WHILE);
    }
}
