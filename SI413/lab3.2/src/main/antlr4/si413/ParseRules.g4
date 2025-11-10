parser grammar ParseRules;

tokens {TILDE, CONCAT, COMPARE, COND, SUBSTR, IF, ELSE, WHILE, BOOLLIT, PRINT, INPUT, REV, LB, RB, STRLIT, SID, BID}

prog
  : stmt prog               #RegularProg
  | EOF                     #EmptyProg
  ;

stmt
  : PRINT LB strExpr RB                                       #StrPrint
  | PRINT LB boolExpr RB                                      #BoolPrint
  | SID TILDE strExpr                                         #SaveStr
  | BID TILDE boolExpr                                        #SaveBool
  | IF TILDE boolExpr LB stmtlst RB ELSE TILDE LB stmtlst RB  #IfElseStmt
  | IF TILDE boolExpr LB stmtlst RB                           #IfStmt
  | WHILE TILDE boolExpr LB stmtlst RB                        #WhileStmt
  ;

stmtlst
  : stmt stmtlst            #MultStmt
  | stmt                    #OneStmt
  ;  

boolExpr
  : BOOLLIT                 #BoolLit
  | REV LB boolExpr RB      #RevBool
  | BID                     #BoolVar
  | boolExpr COND boolExpr  #CondExpr
  | strExpr SUBSTR strExpr  #SubStr
  | strExpr COMPARE strExpr #StrCompare
  ;

strExpr
  : INPUT                   #InputLit
  | STRLIT                  #StrLit
  | REV LB strExpr RB       #RevStr
  | SID                     #StrVar
  | strExpr CONCAT strExpr  #ConcatStr
  ;
