// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.mathlang.impl;

import java.io.IOException;

import com.google.codeu.mathlang.core.tokens.Token;
import com.google.codeu.mathlang.parsing.TokenReader;

// MY TOKEN READER
//
// This is YOUR implementation of the token reader interface. To know how
// it should work, read src/com/google/codeu/mathlang/parsing/TokenReader.java.
// You should not need to change any other files to get your token reader to
// work with the test of the system.
public final class MyTokenReader implements TokenReader {
  
  private final String source; 
  private int point = 0;

  private char read() throws IOException {
    try {
      if (point < source.length()) {
        return source.charAt(point++);
      }
      else {
        return '\0';
      }
    }
    catch(Exception exception) {
      exception.printStackTrace();
      throw new IOException();
    }
  }

  private void back() {
    point--;
  }

  private String readName(char a) {
    String result = "" + a;
    try {
      a = read();
      while (!Character.isWhitespace(a) && Character.isAlphabetic(a) && a != ';' && a != '\0') {
        result += a;
        a = read();
      }
      if(!Character.isAlphabetic(a)) {
        back();
      }
    }
    catch(IOException exception) {

    }
    return result; 
  }

  private String readString(char a) {
    String result = "";
    try {
      a = read();
      while (a != '\0' && a != '\"') {
        result += a;
        a = read();
      }
    }
    catch(IOException exception) {

    }
    return result;
  }

  private double readNumber(char a) {
    double result = a - '0';
    boolean decPoint = false;
    double division = 0.1;
    try {
      a = read();
      while (a != '\0' && ((a >= '0' && a <= '9') || a == '.')) {
        if(a == '.') {
          if (decPoint) {
            break;
          }
          decPoint = true;
        }
        else if(!foundDot) {
          result *= 10;
          result += a - '0';
        }
        else {
          result += (a - '0') * division;
          division /= 10;
        }
        a = read();
      }
    }
    catch (IOException exception) {

    }
    back();
    return result;
  }

  public MyTokenReader(String source) {
    // Your token reader will only be given a string for input. The string will
    // contain the whole source (0 or more lines).
    this.source = source;
  }

  @Override
  public Token next() throws IOException {
    // Most of your work will take place here. For every call to |next| you should
    // return a token until you reach the end. When there are no more tokens, you
    // should return |null| to signal the end of input.

    // If for any reason you detect an error in the input, you may throw an IOException
    // which will stop all execution.

    try {
      char current = read();
      while (current == ' ' || current == '\n') {
        current = read();
      }
      if (Character.isAlphabetic(current)) {
        return new NameToken(readName(current));
      }
      else if (current >= '0' && current <= '9') {
        return new NumberToken(readNumber(current));
      }
      switch(current) {
        case '\"':
          return new StringToken(readString(current));
        case ';':
          return new SymbolToken(';');
        case '=':
          return new SymbolToken('=');
        case '+':
          return new SymbolToken('+');
        case '-':
          return new SymbolToken('-');
        case '\0':
          return null;
      }
    }
    catch(IOException exception) {
      exception.printStackTrace();
      throw new IOException("Can not read next token.");
    }
    return null;
  }
}
