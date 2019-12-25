package org.toy.acd.ir;

import com.github.javaparser.ast.expr.Expression;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the control flow graph of a single method.
 */
public class CFG {
    public BasicBlock start, end;
    public final List<BasicBlock> allBlocks = new ArrayList<>();

    public int count() {
        return allBlocks.size();
    }

    public BasicBlock newBlock() {
        BasicBlock blk = new BasicBlock(count());
        allBlocks.add(blk);
        return blk;
    }

    /**
     * Given a list of basic blocks that do not yet have successors,
     * merges their control flows into a single successor and returns
     * the new successor.
     */
    public BasicBlock join(BasicBlock... blocks) {
        BasicBlock result = newBlock();
        for (BasicBlock block : blocks) {
            assert block.condition == null;
            assert block.successors.size() == 0;
            block.successors.add(result);
            result.predecessors.add(block);
        }
        return result;
    }

    /**
     * Terminates {@code blk} so that it evaluates {@code code},
     * and creates two new basic blocks, one for the case where
     * the result is true, and one for the case where the result is
     * false.
     */
    public void terminateInCondition(BasicBlock blk, Expression cond) {
        assert blk.condition == null;
        assert blk.successors.size() == 0;
        blk.condition = cond;
        blk.successors.add(newBlock());
        blk.successors.add(newBlock());
        blk.trueSuccessor().predecessors.add(blk);
        blk.falseSuccessor().predecessors.add(blk);
    }

    public void connect(BasicBlock from, BasicBlock to) {
        to.predecessors.add(from);
        from.successors.add(to);
    }
}
