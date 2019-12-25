package org.toy.acd.cfg;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.toy.acd.ir.BasicBlock;
import org.toy.acd.ir.CFG;
import org.toy.acd.ir.Phi;
import org.toy.acd.ir.Visitor;

import java.util.HashSet;
import java.util.Set;

public class SSA {
    public void run(final CFG cfg) {
        // Phase 1: introduce Phis
        for (final BasicBlock bb : cfg.allBlocks) {
            // Compute iterated dominance frontier for this block 'bb'.
            final Set<BasicBlock> idf = new HashSet<>();
            computeIteratedDominanceFrontier(bb, idf);

            // Introduce phi blocks.
            for (final Statement ast : bb.instructions)
                new PhiVisitor().visit((ExpressionStmt) ast, idf);
        }

        // todo
        // maybe we could skip these phases
        // because we are aiming for WebAssembly
        // Phase 2: rename variables
        // Phase 3: detect undefined variables
    }

    public void computeIteratedDominanceFrontier(final BasicBlock bb,
                                                 final Set<BasicBlock> idf) {
        for (final BasicBlock b : bb.dominanceFrontier) {
            if (idf.add(b))
                computeIteratedDominanceFrontier(b, idf);
        }
    }

    class PhiVisitor extends Visitor<Void, Set<BasicBlock>> {
        @Override
        public Void visit(AssignExpr expr, Set<BasicBlock> idf) {
            final Expression lhs = expr.getTarget();
            addPhis(lhs, idf);
            return super.visit(expr, idf);
        }

        private void addPhis(final Expression sym, final Set<BasicBlock> idf) {
            for (final BasicBlock bb : idf) {
                if (bb.phis.containsKey(sym))
                    continue; // already has a phi for this symbol
                final Phi phi = new Phi(sym, bb.predecessors.size());
                bb.phis.put(sym, phi);
            }
        }
    }
}
