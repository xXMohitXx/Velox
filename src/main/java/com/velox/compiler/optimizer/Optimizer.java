package com.velox.compiler.optimizer;

import com.velox.compiler.ast.*;
import com.velox.compiler.ast.expressions.*;
import com.velox.compiler.ast.statements.*;
import java.util.*;

public class Optimizer {
    private final List<OptimizationPass> passes;
    
    public Optimizer() {
        this.passes = new ArrayList<>();
        initializePasses();
    }
    
    private void initializePasses() {
        passes.add(new ConstantFoldingPass());
        passes.add(new DeadCodeEliminationPass());
        passes.add(new InliningPass());
        passes.add(new LoopOptimizationPass());
        passes.add(new StrengthReductionPass());
    }
    
    public void optimize(AST node) {
        for (OptimizationPass pass : passes) {
            pass.optimize(node);
        }
    }
    
    // Constant Folding Pass
    private static class ConstantFoldingPass implements OptimizationPass {
        @Override
        public void optimize(AST node) {
            if (node instanceof BinaryExpr) {
                BinaryExpr binExpr = (BinaryExpr) node;
                if (binExpr.getLeft() instanceof LiteralExpr && 
                    binExpr.getRight() instanceof LiteralExpr) {
                    Object result = evaluateBinaryExpression(
                        binExpr.getOperator().getLexeme(),
                        ((LiteralExpr) binExpr.getLeft()).getValue(),
                        ((LiteralExpr) binExpr.getRight()).getValue()
                    );
                    if (result != null) {
                        // AST mutation not implemented: would replace node with new LiteralExpr(result)
                        // node.replaceWith(new LiteralExpr(result));
                    }
                }
            }
        }
        
        private Object evaluateBinaryExpression(String operator, Object left, Object right) {
            if (left instanceof Number && right instanceof Number) {
                double l = ((Number) left).doubleValue();
                double r = ((Number) right).doubleValue();
                
                switch (operator) {
                    case "+": return l + r;
                    case "-": return l - r;
                    case "*": return l * r;
                    case "/": return r != 0 ? l / r : null;
                    case "%": return r != 0 ? l % r : null;
                }
            }
            return null;
        }
    }
    
    // Dead Code Elimination Pass
    private static class DeadCodeEliminationPass implements OptimizationPass {
        @Override
        public void optimize(AST node) {
            if (node instanceof IfStmt) {
                IfStmt ifNode = (IfStmt) node;
                if (ifNode.getCondition() instanceof LiteralExpr) {
                    LiteralExpr condition = (LiteralExpr) ifNode.getCondition();
                    if (condition.getValue() instanceof Boolean) {
                        // AST mutation not implemented: would replace node with then/else branch or remove
                        // if ((Boolean)condition.getValue()) node.replaceWith(ifNode.getThenBranch());
                        // else if (ifNode.getElseBranch() != null) node.replaceWith(ifNode.getElseBranch());
                        // else node.remove();
                    }
                }
            }
        }
    }
    
    // Inlining Pass
    private static class InliningPass implements OptimizationPass {
        @Override
        public void optimize(AST node) {
            if (node instanceof CallExpr) {
                // FunctionStmt func = findFunction(((CallExpr)node).getCallee());
                // if (func != null && isInlineable(func)) {
                //     inlineFunction((CallExpr)node, func);
                // }
            }
        }
        // private FunctionStmt findFunction(AST callee) { return null; }
        // private boolean isInlineable(FunctionStmt func) { return true; }
        // private void inlineFunction(CallExpr call, FunctionStmt func) {}
    }
    
    // Loop Optimization Pass
    private static class LoopOptimizationPass implements OptimizationPass {
        @Override
        public void optimize(AST node) {
            if (node instanceof WhileStmt) {
                WhileStmt whileNode = (WhileStmt) node;
                optimizeLoop(whileNode);
            }
        }
        
        private void optimizeLoop(WhileStmt node) {
            // Hoist invariant expressions (not implemented)
            // List<AST> invariants = findInvariants(node);
            // for (AST invariant : invariants) {
            //     hoistInvariant(node, invariant);
            // }
            // Unroll small loops (not implemented)
            // if (isSmallLoop(node)) {
            //     unrollLoop(node);
            // }
        }
        // private List<AST> findInvariants(WhileStmt node) { return new ArrayList<>(); }
        // private void hoistInvariant(WhileStmt node, AST invariant) {}
        // private boolean isSmallLoop(WhileStmt node) { return true; }
        // private void unrollLoop(WhileStmt node) {}
    }
    
    // Strength Reduction Pass
    private static class StrengthReductionPass implements OptimizationPass {
        @Override
        public void optimize(AST node) {
            if (node instanceof BinaryExpr) {
                BinaryExpr binExpr = (BinaryExpr) node;
                reduceStrength(binExpr);
            }
        }
        
        private void reduceStrength(BinaryExpr node) {
            // Replace x * 2 with x + x
            if (node.getOperator().getLexeme().equals("*")) {
                if (node.getRight() instanceof LiteralExpr) {
                    LiteralExpr literal = (LiteralExpr) node.getRight();
                    if (literal.getValue() instanceof Number) {
                        double value = ((Number) literal.getValue()).doubleValue();
                        if (value == 2.0) {
                            // AST mutation not implemented: would replace with x + x
                            // node.setOperator("+");
                            // node.setRight(node.getLeft().clone());
                        }
                    }
                }
            }
        }
    }
    
    // Optimization Pass Interface
    private interface OptimizationPass {
        void optimize(AST node);
    }
} 