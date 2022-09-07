package com.cecs497.lox;

public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;

    /**
     * Bundles a lexeme with other useful data to make it more readable for parser 
     * @param type
     * @param lexeme
     * @param literal
     * @param line
     */
    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    /**
     * Returns string representation of a token
     */
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
