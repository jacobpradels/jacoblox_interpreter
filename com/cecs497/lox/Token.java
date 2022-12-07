//> Scanning token-class
package com.cecs497.lox;

class Token {
  final TokenType type;
  final String lexeme;
  final Object literal;
  final int line; // [location]
  final String filename;

  Token(TokenType type, String lexeme, Object literal, int line, String filename) {
    this.type = type;
    this.lexeme = lexeme;
    this.literal = literal;
    this.line = line;
    this.filename = filename;
  }

  public String toString() {
    return type + " " + lexeme + " " + literal;
  }
}
