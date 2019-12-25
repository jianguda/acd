package org.toy.acd.ir;

import com.github.javaparser.ast.expr.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Phi {
    public Expression lhs;
    public boolean isConstant;
    public List<Expression> rhs = new ArrayList<>(); // Ast.Var or an Ast.Const

    public Phi(Expression sym, int predCount) {
        this.lhs = sym;
        for (int i = 0; i < predCount; i++)
            rhs.add(sym);
    }

    public void checkIfConstant(Map<String, Expression> toPropagate) {
        if (rhs.size() == 1) {
            // we can propagate in this case
            toPropagate.put(lhs.toString(), rhs.get(0));
            isConstant = true;
            return;
        }

        // all expressions must be the same constant
        if (rhs.get(0).isBooleanLiteralExpr()) {
            boolean v = rhs.get(0).asBooleanLiteralExpr().getValue();
            for (Expression expr : rhs) {
                if (expr.isBooleanLiteralExpr()) {
                    if (v != expr.asBooleanLiteralExpr().getValue()) {
                        return;
                    }
                } else {
                    return;
                }
            }
        } else if (rhs.get(0).isDoubleLiteralExpr()) {
            double v = rhs.get(0).asDoubleLiteralExpr().asDouble();
            for (Expression expr : rhs) {
                if (expr.isDoubleLiteralExpr()) {
                    if (v != expr.asDoubleLiteralExpr().asDouble()) {
                        return;
                    }
                } else {
                    return;
                }
            }
        } else if (rhs.get(0).isIntegerLiteralExpr()) {
            double v = rhs.get(0).asIntegerLiteralExpr().asInt();
            for (Expression expr : rhs) {
                if (expr.isIntegerLiteralExpr()) {
                    if (v != expr.asIntegerLiteralExpr().asInt()) {
                        return;
                    }
                } else {
                    return;
                }
            }
        } else {
            return;
        }

        isConstant = true;
        // we can further propagate this variable
        toPropagate.put(lhs.toString(), rhs.get(0));
    }
}
