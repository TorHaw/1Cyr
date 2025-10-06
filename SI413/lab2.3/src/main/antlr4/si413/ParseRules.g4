parser grammar ParseRules;

tokens {TILDE, OP, BOOL, PRINT, INPUT, REV, LIT, ID}

prog
  : stmt prog #RegularProg
  | EOF #EmptyProg
  ;

stmt
  : PRINT expr #PrintStmt
  | ID TILDE expr #SaveStmt
  ;

expr
  : LIT #LiteralExpr
  | BOOL #BoolExpr
  | expr OP expr #EvalExpr
  | INPUT #InputExpr
  | REV TILDE expr TILDE #RevExpr
  | ID #VarExpr
  ;
