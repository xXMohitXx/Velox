package com.velox.compiler.optimizer;

import com.velox.compiler.ast.*;
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
    
    public void optimize(ASTNode node) {
        for (OptimizationPass pass : passes) {
            pass.optimize(node);
        }
    }
    
    // Constant Folding Pass
    private static class ConstantFoldingPass implements OptimizationPass {
        @Override
        public void optimize(ASTNode node) {
            if (node instanceof BinaryExpressionNode) {
                BinaryExpressionNode binExpr = (BinaryExpressionNode) node;
                if (binExpr.getLeft() instanceof LiteralNode && 
                    binExpr.getRight() instanceof LiteralNode) {
                    Object result = evaluateBinaryExpression(
                        binExpr.getOperator(),
                        ((LiteralNode) binExpr.getLeft()).getValue(),
                        ((LiteralNode) binExpr.getRight()).getValue()
                    );
                    if (result != null) {
                        node.replaceWith(new LiteralNode(result));
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
        public void optimize(ASTNode node) {
            if (node instanceof IfNode) {
                IfNode ifNode = (IfNode) node;
                if (ifNode.getCondition() instanceof LiteralNode) {
                    LiteralNode condition = (LiteralNode) ifNode.getCondition();
                    if (condition.getValue() instanceof Boolean) {
                        boolean value = (Boolean) condition.getValue();
                        if (value) {
                            node.replaceWith(ifNode.getThenBranch());
                        } else if (ifNode.getElseBranch() != null) {
                            node.replaceWith(ifNode.getElseBranch());
                        } else {
                            node.remove();
                        }
                    }
                }
            }
        }
    }
    
    // Inlining Pass
    private static class InliningPass implements OptimizationPass {
        @Override
        public void optimize(ASTNode node) {
            if (node instanceof CallNode) {
                CallNode call = (CallNode) node;
                FunctionNode func = findFunction(call.getFunctionName());
                if (func != null && isInlineable(func)) {
                    inlineFunction(call, func);
                }
            }
        }
        
        private FunctionNode findFunction(String name) {
            // Implementation to find function definition
            return null;
        }
        
        private boolean isInlineable(FunctionNode func) {
            // Check if function is small enough and doesn't have side effects
            return true;
        }
        
        private void inlineFunction(CallNode call, FunctionNode func) {
            // Implementation of function inlining
        }
    }
    
    // Loop Optimization Pass
    private static class LoopOptimizationPass implements OptimizationPass {
        @Override
        public void optimize(ASTNode node) {
            if (node instanceof WhileNode) {
                WhileNode whileNode = (WhileNode) node;
                optimizeLoop(whileNode);
            }
        }
        
        private void optimizeLoop(WhileNode node) {
            // Hoist invariant expressions
            List<ExpressionNode> invariants = findInvariants(node);
            for (ExpressionNode invariant : invariants) {
                hoistInvariant(node, invariant);
            }
            
            // Unroll small loops
            if (isSmallLoop(node)) {
                unrollLoop(node);
            }
        }
        
        private List<ExpressionNode> findInvariants(WhileNode node) {
            // Implementation to find loop-invariant expressions
            return new ArrayList<>();
        }
        
        private void hoistInvariant(WhileNode node, ExpressionNode invariant) {
            // Implementation to hoist invariant expressions
        }
        
        private boolean isSmallLoop(WhileNode node) {
            // Check if loop is small enough for unrolling
            return true;
        }
        
        private void unrollLoop(WhileNode node) {
            // Implementation of loop unrolling
        }
    }
    
    // Strength Reduction Pass
    private static class StrengthReductionPass implements OptimizationPass {
        @Override
        public void optimize(ASTNode node) {
            if (node instanceof BinaryExpressionNode) {
                BinaryExpressionNode binExpr = (BinaryExpressionNode) node;
                reduceStrength(binExpr);
            }
        }
        
        private void reduceStrength(BinaryExpressionNode node) {
            // Replace expensive operations with cheaper ones
            if (node.getOperator().equals("*")) {
                if (node.getRight() instanceof LiteralNode) {
                    LiteralNode literal = (LiteralNode) node.getRight();
                    if (literal.getValue() instanceof Number) {
                        double value = ((Number) literal.getValue()).doubleValue();
                        if (value == 2.0) {
                            // Replace x * 2 with x + x
                            node.setOperator("+");
                            node.setRight(node.getLeft().clone());
                        }
                    }
                }
            }
        }
    }
    
    // Optimization Pass Interface
    private interface OptimizationPass {
        void optimize(ASTNode node);
    }
} 