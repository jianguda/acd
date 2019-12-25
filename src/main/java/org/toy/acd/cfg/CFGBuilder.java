package org.toy.acd.cfg;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import org.toy.acd.ir.BasicBlock;
import org.toy.acd.ir.CFG;
import org.toy.acd.ir.Visitor;

public class CFGBuilder {

    CFG cfg;

    public CFG run(MethodDeclaration md) {
        cfg = new CFG();
        cfg.start = cfg.newBlock(); // Note: Use newBlock() to create new basic blocks
        cfg.end = cfg.newBlock(); // unique exit block to which all blocks that end with a return stmt. lead
        md.accept(new TmpVisitor(), cfg.start);
        {
            BasicBlock lastInBody = new TmpVisitor().visit(md.getBody().orElse(null), cfg.start);
            if (lastInBody != null) cfg.connect(lastInBody, cfg.end);
        }
        return cfg;
    }

    protected class TmpVisitor extends Visitor<BasicBlock, BasicBlock> {
        @Override
        public BasicBlock visit(ExpressionStmt stmt, BasicBlock block) {
            block.instructions.add(stmt);
            return null;
        }

        @Override
        public BasicBlock visit(WhileStmt stmt, BasicBlock block) {
            if (block == null) return null; // dead code, no need to generate anything
            BasicBlock cond = cfg.join(block);
            cfg.terminateInCondition(cond, stmt.getCondition());
            BasicBlock body = visit((BlockStmt) stmt.getBody(), cond.trueSuccessor());
            if (body != null) cfg.connect(body, cond);
            return cond.falseSuccessor();
        }

        @Override
        public BasicBlock visit(IfStmt stmt, BasicBlock block) {
            if (block == null) return null; // dead code, no need to generate anything
            cfg.terminateInCondition(block, stmt.getCondition());
            BasicBlock then = visit((BlockStmt) stmt.getThenStmt(), block.trueSuccessor());
            BasicBlock otherwise = visit((BlockStmt) stmt.getElseStmt().orElse(null), block.falseSuccessor());
            if (then != null && otherwise != null) {
                return cfg.join(then, otherwise);
            } else if (then != null) {
                BasicBlock newBlock = cfg.newBlock();
                cfg.connect(then, newBlock);
                return newBlock;
            } else if (otherwise != null) {
                BasicBlock newBlock = cfg.newBlock();
                cfg.connect(otherwise, newBlock);
                return newBlock;
            } else {
                return null;
            }
        }

        @Override
        public BasicBlock visit(ReturnStmt stmt, BasicBlock block) {
            if (block == null) return null; // dead code, no need to generate anything
            block.instructions.add(stmt);
            cfg.connect(block, cfg.end);
            return null; // null means that this block leads nowhere else
        }
    }
}
