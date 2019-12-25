package org.toy.acd.cfg;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.toy.acd.ir.BasicBlock;
import org.toy.acd.ir.CFG;
import org.toy.acd.ir.Phi;
import org.toy.acd.ir.Visitor;

import java.util.HashMap;
import java.util.Map;

public class Optimizer {
    // once implemented Souper IR, we can invoke Souper directly
    public void run(CFG cfg) {
        Map<String, Expression> toPropagate = new HashMap<>();
        // constant fold instructions, conditions and phis
        for (BasicBlock blk : cfg.allBlocks) {
            for (Statement instr : blk.instructions) {
                constantFolding.visit((ExpressionStmt) instr, null);
            }
            if (blk.condition != null) {
                blk.condition = (Expression) constantFolding.visit((ConditionalExpr) blk.condition, null);
            }
        }

        for (int i = 0; i < cfg.allBlocks.size(); i++) {
            for (Phi phi : cfg.allBlocks.get(i).phis.values()) {
                phi.checkIfConstant(toPropagate);
            }
        }

        propagateCopies(cfg.start, toPropagate);
    }

    void propagateCopies(BasicBlock bb, Map<String, Expression> toPropagate) {
        // propagate copies in phi nodes
        for (Phi phi : bb.phis.values()) {
            for (int i = 0; i < phi.rhs.size(); i++) {
                phi.rhs.set(i, propagateVisitor.visit(phi.rhs.get(i), toPropagate));
            }
        }
        // remove if phi is constant - no matter from which basic block you come, we same result will be phi-ed
        bb.phis.entrySet().removeIf(expressionPhiEntry -> (expressionPhiEntry).getValue().isConstant);

        for (Statement instr : bb.instructions) {
            if (instr.isExpressionStmt()) {
                // propagate variables and collect new variables to propagate
                propagateVisitor.visit((ExpressionStmt) instr, toPropagate);
                assignVisitor.visit((ExpressionStmt) instr, toPropagate);
            }
        }
        if (bb.condition != null) {
            if (bb.condition.isConditionalExpr()) {
                bb.condition = propagateVisitor.visit(bb.condition, toPropagate);
            }
        }

        for (BasicBlock next : bb.dominatorTreeChildren) {
            propagateCopies(next, toPropagate);
        }
    }

    private Visitor<Expression, Map<String, Expression>> propagateVisitor =
            new Visitor<Expression, Map<String, Expression>>() {
                @Override
                public Expression visit(AssignExpr expr, Map<String, Expression> toPropagate) {
                    expr.setValue(toPropagate.get(expr.getTarget().toString()));
                    return expr;
                }
            };

    private Visitor<Expression, Map<String, Expression>> assignVisitor =
            new Visitor<Expression, Map<String, Expression>>() {
                @Override
                public Expression visit(AssignExpr expr, Map<String, Expression> toPropagate) {
                    toPropagate.put(expr.getTarget().toString(), expr.getValue());
                    return super.visit(expr, null);
                }
            };

    private Visitor<Node, Expression> constantFolding = new Visitor<Node, Expression>() {
        @Override
        public Expression visit(BinaryExpr expr, Expression arg) {
            Expression left = expr.getLeft();
            Expression right = expr.getRight();
            Operator operator = expr.getOperator();

            if (operator == Operator.PLUS) {
                if (left.isIntegerLiteralExpr() && right.isIntegerLiteralExpr()) {
                    if (left.asIntegerLiteralExpr().asInt() == 0) {
                        return new IntegerLiteralExpr(right.asIntegerLiteralExpr().asInt());
                    } else if (right.asIntegerLiteralExpr().asInt() == 0) {
                        return new IntegerLiteralExpr(left.asIntegerLiteralExpr().asInt());
                    }
                } else if (left.isDoubleLiteralExpr() && right.isDoubleLiteralExpr()) {
                    if (left.asDoubleLiteralExpr().asDouble() == 0.0) {
                        return new DoubleLiteralExpr(right.asDoubleLiteralExpr().asDouble());
                    } else if (right.asDoubleLiteralExpr().asDouble() == 0.0) {
                        return new DoubleLiteralExpr(left.asDoubleLiteralExpr().asDouble());
                    }
                }
            } else if (operator == Operator.MINUS) {
                if (left.isIntegerLiteralExpr() && right.isIntegerLiteralExpr()) {
                    if (left.asIntegerLiteralExpr().asInt() == right.asIntegerLiteralExpr().asInt()) {
                        return new IntegerLiteralExpr(0);
                    }
                } else if (left.isDoubleLiteralExpr() && right.isDoubleLiteralExpr()) {
                    if (left.asDoubleLiteralExpr().asDouble() == right.asDoubleLiteralExpr().asDouble()) {
                        return new DoubleLiteralExpr(0.0);
                    }
                }
            } else if (operator == Operator.MULTIPLY) {
                if (left.isIntegerLiteralExpr() && right.isIntegerLiteralExpr()) {
                    if (left.asIntegerLiteralExpr().asInt() == 1) {
                        return new IntegerLiteralExpr(right.asIntegerLiteralExpr().asInt());
                    } else if (right.asIntegerLiteralExpr().asInt() == 1) {
                        return new IntegerLiteralExpr(left.asIntegerLiteralExpr().asInt());
                    }
                } else if (left.isDoubleLiteralExpr() && right.isDoubleLiteralExpr()) {
                    if (left.asDoubleLiteralExpr().asDouble() == 1.0) {
                        return new DoubleLiteralExpr(right.asDoubleLiteralExpr().asDouble());
                    } else if (right.asDoubleLiteralExpr().asDouble() == 1.0) {
                        return new DoubleLiteralExpr(left.asDoubleLiteralExpr().asDouble());
                    }
                }
            } else if (operator == Operator.DIVIDE) {
                if (left.isIntegerLiteralExpr() && right.isIntegerLiteralExpr()) {
                    if (left.asIntegerLiteralExpr().asInt() == right.asIntegerLiteralExpr().asInt()) {
                        return new IntegerLiteralExpr(1);
                    }
                } else if (left.isDoubleLiteralExpr() && right.isDoubleLiteralExpr()) {
                    if (left.asDoubleLiteralExpr().asDouble() == right.asDoubleLiteralExpr().asDouble()) {
                        return new DoubleLiteralExpr(1.0);
                    }
                }
            }
            return super.visit(expr, arg);
        }
    };
}
