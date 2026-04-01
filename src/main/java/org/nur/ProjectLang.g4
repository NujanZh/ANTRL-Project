grammar ProjectLang;

program: statement* EOF;

statement
    : ';'                                                            # EmptyStmt
    | typeDecl ID (',' ID)* ';'                                      # DeclStmt
    | expr ';'                                                       # ExprStmt
    | 'read' ID (',' ID)* ';'                                        # ReadStmt
    | 'write' expr (',' expr)* ';'                                   # WriteStmt
    | '{' statement* '}'                                             # BlockStmt
    | 'if' '(' expr ')' statement ('else' statement)?                # IfStmt
    | 'while' '(' expr ')' statement                                 # WhileStmt
    ;

typeDecl
    : 'int' | 'float' | 'bool' | 'string'
    ;

expr
    : '-' expr                                                       # UnaryMinusExpr
    | '!' expr                                                       # NotExpr
    | expr op=('*' | '/' | '%') expr                                 # MulDivModExpr
    | expr op=('+' | '-' | '.') expr                                 # AddSubConcatExpr
    | expr op=('<' | '>') expr                                       # RelationalExpr
    | expr op=('==' | '!=') expr                                     # EqualityExpr
    | expr '&&' expr                                                 # AndExpr
    | expr '||' expr                                                 # OrExpr
    | ID '=' expr                                                    # AssignExpr
    | '(' expr ')'                                                   # ParensExpr
    | INT_VAL                                                        # IntExpr
    | FLOAT_VAL                                                      # FloatExpr
    | BOOL_VAL                                                       # BoolExpr
    | STRING_VAL                                                     # StringExpr
    | ID                                                             # IdExpr
    ;

BOOL_VAL: 'true' | 'false';
INT_VAL: [0-9]+;
FLOAT_VAL: [0-9]+ '.' [0-9]+;
STRING_VAL: '"' (~["\r\n\\] | '\\' .)* '"';
ID: [a-zA-Z] [a-zA-Z0-9]*;

WS: [ \t\r\n]+ -> skip;
COMMENT: '//' ~[\r\n]* -> skip;
