package org.toy.acd.cfg;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.toy.acd.ir.BasicBlock;
import org.toy.acd.ir.CFG;
import org.toy.acd.ir.Phi;
import org.toy.acd.ir.Visitor;

import java.util.*;


public class DeSSA {
    /**
     * Build up a dependency graph.  The nodes are variables defined
     * by phi statements.  The edges represent a copy of the nodes
     * incoming value, so if a -> b then the value of a is stored into b.
     * Note that every node in the graph has in-degree at most 1, because
     * it can only get a value from one location.  Some nodes have in-degree 0
     * if the value comes from a constant or a variable not created by a phi
     * statement in this block.
     * <p>
     * To serialize the phi nodes, we walk this graph, forming a spanning
     * tree.  The value is assigned from the parent in the spanning tree.
     * If while walking the graph we detect a cycle, we introduce a temporary
     * to save the original value of one of the nodes in the cycle.  These
     * temporaries are given a guaranteed unique name like "(de-ssa-0)".
     */

    class DepNode {

        /**
         * Outgoing edges
         */
        final List<DepNode> copiedToNodes = new ArrayList<>();

        public DepNode(Expression sym) {
        }
    }

    /**
     * Goes over the control flow graph and removes
     * any phi nodes, converting SSA variables into normal
     * variables and phi nodes into standard assignments.
     */
    public void run(final CFG cfg) {
        for (BasicBlock blk : new DFS(cfg)) {
            if (blk.phis.isEmpty()) continue; // no phis, no work to do
            for (int i = 0; i < blk.predecessors.size(); i++) {
                // Construct nodes in the dependency graph:
                final Map<Expression, DepNode> depNodes = new HashMap<>();
                for (Phi phi : blk.phis.values())
                    depNodes.put(phi.lhs, new DepNode(phi.lhs));

                // Add edges:
                //   For any assignments to a symbol X whose RHS is not from a phi node
                //   defined in this block, add the assignment to the assigns
                //   array.  These assignments are independent in order from
                //   one another and we will place them at the end.
                //   Note that a variable X may appear as the RHS of other symbols,
                //   and therefore we add the assignment at the end so as to
                //   preserve X's value.
                final List<Statement> assigns = new ArrayList<>();
                for (Phi phi : blk.phis.values()) {
                    assert blk.predecessors.size() == phi.rhs.size();
                    DepNode lhsNode = depNodes.get(phi.lhs);
                    Expression rhsExpr = phi.rhs.get(i);
                    DepNode rhsNode = new Visitor<DepNode, Void>() {
                        public DepNode var(Expression ast, Void arg) {
                            return depNodes.get(ast);
                        }
                    }.visit((ConditionalExpr) rhsExpr, null);

                    if (rhsNode == null) { // RHS is not a phi variable defined in this block.  No dependencies,
                        AssignExpr assign = new AssignExpr(phi.lhs, rhsExpr, AssignExpr.Operator.ASSIGN);
                        ExpressionStmt expressionStmt = new ExpressionStmt(assign);
                        assigns.add(expressionStmt);
                    } else {
                        rhsNode.copiedToNodes.add(lhsNode);
                    }
                }

                final BasicBlock predblk = blk.predecessors.get(i);
                assert predblk.successors.size() == 1;
                assert predblk.successors.get(0) == blk;

                // Add other assignments:
                predblk.instructions.addAll(assigns);
            }

            blk.phis.clear();
        }
    }

    /**
     * A potentially handy iterator which yields the blocks in a control-flow
     * graph.  The order is pre-order, depth-first.  Pre-order means that a
     * node is visited before its successors.
     */
    private class DFS implements Iterable<BasicBlock> {

        public final CFG cfg;

        public DFS(CFG cfg) {
            this.cfg = cfg;
        }

        public Iterator<BasicBlock> iterator() {
            return new Iterator<BasicBlock>() {

                /** Blocks we still need to visit */
                private final Stack<BasicBlock> stack = new Stack<>();

                /** Blocks we pushed thus far */
                private final Set<BasicBlock> pushed = new HashSet<>();

                {
                    stack.add(cfg.start);
                    pushed.add(cfg.start);
                }

                public boolean hasNext() {
                    return !stack.isEmpty();
                }

                public BasicBlock next() {
                    if (stack.isEmpty())
                        throw new NoSuchElementException();

                    BasicBlock res = stack.pop();
                    for (BasicBlock s : res.successors)
                        if (!pushed.contains(s)) {
                            pushed.add(s);
                            stack.add(s);
                        }

                    return res;
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }

            };
        }

    }

}
