package si413;

import java.util.List;

/** AST nodes for statements.
 * Statements can be executed but do not return a value.
 */
public interface Stmt {
    /** Executes this AST node in an interpreter. */
    void exec(Interpreter interp);

    /** Compiles this AST node.
     * The Compiler instance comp is used to store shared state
     * of the running compiler, such as the destination output stream.
     */
    void compile(Compiler comp);

    // ******** AST Node types for statements ******** //

    record Block(List<Stmt> children) implements Stmt {
        @Override
        public void exec(Interpreter interp) {
            for (Stmt child : children) {
                child.exec(interp);
            }
        }

        @Override
        public void compile(Compiler comp) {
            for (Stmt child: children) {
                child.compile(comp);
            }
        }
    }

    record ExprStmt(Expr<?> child) implements Stmt {
        @Override
        public void exec(Interpreter interp) {
            child.eval(interp);
        }

        @Override
        public void compile(Compiler comp) {
            child.compile(comp);
        }
    }

    record AssignString(String name, Expr<String> child) implements Stmt {
        @Override
        public void exec(Interpreter interp) {
            String val = child.eval(interp);
            interp.getStringVars().put(name, val);
        }

        @Override
        public void compile(Compiler comp) {
            String val = child.compile(comp);
            String addr = comp.nextRegister();
            
            if (comp.getStrVar(name) == null) {
                comp.newStrVar(name, addr);
                String ptr = comp.nextRegister();
                comp.dest().format("  %s = alloca ptr\n", addr);
                comp.dest().format("  %s = getelementptr inbounds [%d x i8], ptr %s, i64 0, i64 0\n", ptr, val.length()+1, val);
            }
            else {
                addr = comp.getStrVar(name);
            }
            comp.dest().format("  store ptr %s, ptr %s\n", val, addr);
        }
    }

    record AssignBool(String name, Expr<Boolean> child) implements Stmt {
        @Override
        public void exec(Interpreter interp) {
            boolean val = child.eval(interp);
            interp.getBoolVars().put(name, val);
        }

        @Override
        public void compile(Compiler comp) {
            String val = child.compile(comp);
            String res;
            if (comp.getBoolVar(name) == null) {
                res = comp.nextRegister();
                comp.newBoolVar(name, res);
                comp.dest().format("  %s = alloca i1\n", res);
            } 
            else 
                res = comp.getBoolVar(name);

            comp.dest().format("  store i1 %s, ptr %s\n", val, res);
        }
    }

    record PrintString(Expr<String> child) implements Stmt {
        @Override
        public void exec(Interpreter interp) {
            System.out.println(child.eval(interp));
        }

        @Override
        public void compile(Compiler comp) {
            String chreg = child.compile(comp);
            comp.dest().format("  call i32 @puts(ptr %s)\n", chreg);
        }
    }

    record PrintBool(Expr<Boolean> child) implements Stmt {
        @Override
        public void exec(Interpreter interp) {
            if (child.eval(interp)) System.out.println("True");
            else System.out.println("False");
        }

        @Override
        public void compile(Compiler comp) {
            String chreg = child.compile(comp);
            comp.dest().format("  call void @print_bool(i1 %s)\n", chreg);
        }
    }

    record IfElse(Expr<Boolean> condition, Stmt ifBody, Stmt elseBody) implements Stmt {
        @Override
        public void exec(Interpreter interp) {
            if (condition.eval(interp)) {
                ifBody.exec(interp);
            }
            else {
                elseBody.exec(interp);
            }
        }

        @Override
        public void compile(Compiler comp) {
            String condBlock = comp.nextBlock();
            String thenBlock = comp.nextBlock();
            String elseBlock = comp.nextBlock();
            String mergeBlock = comp.nextBlock();

            comp.dest().format("  br label %%%s\n", condBlock);

            comp.dest().format("%s:\n", condBlock);
            String condReg = condition.compile(comp);
            comp.dest().format("  br i1 %s, label %%%s, label %%%s\n", condReg, thenBlock, elseBlock);

            comp.dest().format("%s:\n", thenBlock);
            ifBody.compile(comp);
            comp.dest().format("  br label %%%s\n", mergeBlock);

            comp.dest().format("%s:\n", elseBlock);
            elseBody.compile(comp);
            comp.dest().format("  br label %%%s\n", mergeBlock);

            comp.dest().format("%s:\n", mergeBlock);
        }
    }

    public record EmptyStmt() implements Stmt {
        @Override
        public void exec(Interpreter interp) {}  

        @Override
        public void compile(Compiler comp) {}
    }

    record While(Expr<Boolean> condition, Stmt body) implements Stmt {
        @Override
        public void exec(Interpreter interp) {
            while (condition.eval(interp)) {
                body.exec(interp);
            }
        }

        @Override
        public void compile(Compiler comp) {
            String condBlock = comp.nextBlock();
            String bodyBlock = comp.nextBlock();
            String exitBlock = comp.nextBlock();

            comp.dest().format("  br label %%%s\n", condBlock);

            comp.dest().format("%s:\n", condBlock);
            String condReg = condition.compile(comp);
            comp.dest().format("  br i1 %s, label %%%s, label %%%s\n", condReg, bodyBlock, exitBlock);

            comp.dest().format("%s:\n", bodyBlock);
            body.compile(comp);
            comp.dest().format("  br label %%%s\n", condBlock);

            comp.dest().format("%s:\n", exitBlock);
        }
    }
}
