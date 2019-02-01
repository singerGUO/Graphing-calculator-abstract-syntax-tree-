package calculator.ast;

import calculator.interpreter.Environment;
import calculator.errors.EvaluationError;
import calculator.gui.ImageDrawer;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
//import misc.exceptions.NotYetImplementedException;

/**
 * All of the public static methods in this class are given the exact same parameters for
 * consistency. You can often ignore some of these parameters when implementing your
 * methods.
 *
 * Some of these methods should be recursive. You may want to consider using public-private
 * pairs in some cases.
 */
public class ExpressionManipulators {
    private static IDictionary<String, AstNode> dictionary;

    /**
     * Checks to make sure that the given node is an operation AstNode with the expected
     * name and number of children. Throws an EvaluationError otherwise.
     */
    private static void assertNodeMatches(AstNode node, String expectedName, int expectedNumChildren) {
        if (!node.isOperation()
                && !node.getName().equals(expectedName)
                && node.getChildren().size() != expectedNumChildren) {
            throw new EvaluationError("Node is not valid " + expectedName + " node.");
        }
    }

    /**
     * Accepts an 'toDouble(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'toDouble'.
     * - The 'node' parameter has exactly one child: the AstNode to convert into a double.
     *
     * Postconditions:
     *
     * - Returns a number AstNode containing the computed double.
     *
     * For example, if this method receives the AstNode corresponding to
     * 'toDouble(3 + 4)', this method should return the AstNode corresponding
     * to '7'.
     *
     * This method is required to handle the following binary operations
     *      +, -, *, /, ^
     *  (addition, subtraction, multiplication, division, and exponentiation, respectively)
     * and the following unary operations
     *      negate, sin, cos
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if any of the expressions uses an unknown operation.
     */
    public static AstNode handleToDouble(Environment env, AstNode node) {
        // To help you get started, we've implemented this method for you.
        // You should fill in the locations specified by "your code here"
        // in the 'toDoubleHelper' method.
        //
        // If you're not sure why we have a public method calling a private
        // recursive helper method, review your notes from CSE 143 (or the
        // equivalent class you took) about the 'public-private pair' pattern.
        dictionary = env.getVariables();
        assertNodeMatches(node, "toDouble", 1);
        AstNode toDouble = node.getChildren().get(0);
        return new AstNode(toDoubleHelper(toDouble));
    }

    private static double toDoubleHelper(AstNode node) {

        if (node.isNumber()) {
            return node.getNumericValue();
        } else if (node.isVariable()) {

            String nodeName = node.getName();
            //remember that if we call toDouble(3+a) it doesn't work;
            if (!dictionary.containsKey(nodeName)) {
                throw new EvaluationError("Variable " + nodeName + " is undefined");
            }
            //we have to use recursion to avoid the situation that a=b,b=3
            AstNode findRealValue = dictionary.get(nodeName);
            return toDoubleHelper(findRealValue);
        } else {
            // You may assume the expression node has the correct number of children.
            // If you wish to make your code more robust, you can also use the provided
            // "assertNodeMatches" method to verify the input is valid.
            String name = node.getName();
            AstNode child1 = node.getChildren().get(0);
            AstNode child2 = null;
            if (node.getChildren().size() == 2) {
                child2 = node.getChildren().get(1);
            }


            switch (name) {
                case "sin":
                    return Math.sin(toDoubleHelper(child1));
                case "cos":
                    return Math.cos(toDoubleHelper(child1));
                case "negate":
                    return -toDoubleHelper(child1);
                case "+":
                    return toDoubleHelper(child1) + toDoubleHelper(child2);
                case "-":
                    return toDoubleHelper(child1) - toDoubleHelper(child2);
                case "*":
                    return toDoubleHelper(child1) * toDoubleHelper(child2);
                case "/":
                    return toDoubleHelper(child1) / toDoubleHelper(child2);
                case "^":
                    return Math.pow(toDoubleHelper(child1), toDoubleHelper(child2));
                default:
                    throw new EvaluationError("Operator " + name + " is undefined");
            }
        }
    }

    /**
     * Accepts a 'simplify(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'simplify'.
     * - The 'node' parameter has exactly one child: the AstNode to simplify
     *
     * Postconditions:
     *
     * - Returns an AstNode containing the simplified inner parameter.
     *
     * For example, if we received the AstNode corresponding to the expression
     * "simplify(3 + 4)", you would return the AstNode corresponding to the
     * number "7".
     *
     * Note: there are many possible simplifications we could implement here,
     * but you are only required to implement a single one: constant folding.
     *
     * That is, whenever you see expressions of the form "NUM + NUM", or
     * "NUM - NUM", or "NUM * NUM", simplify them.
     */
    public static AstNode handleSimplify(Environment env, AstNode node) {
        dictionary = env.getVariables();
        // Try writing this one on your own!

        // Hint 1: Your code will likely be structured roughly similarly
        //         to your "handleToDouble" method
        // Hint 2: When you're implementing constant folding, you may want
        //         to call your "handleToDouble" method in some way
        // Hint 3: When implementing your private pair, think carefully about
        //         when you should recurse. Do you recurse after simplifying
        //         the current level? Or before?


        assertNodeMatches(node, "simplify", 1);
        AstNode toSimplify = node.getChildren().get(0);
        return handleSimplifyHelper(toSimplify);
    }

    private static AstNode handleSimplifyHelper(AstNode node) {
        // base case

        if (node.isVariable() && dictionary.containsKey(node.getName())) {


            return handleSimplifyHelper(dictionary.get(node.getName()));
        }
        if (node.isNumber() || (node.isVariable() && !dictionary.containsKey(node.getName()))) {
            return node;
        }
        // recrusive case
        AstNode left = handleSimplifyHelper(node.getChildren().get(0));

        // code
        //DFS                                name           children
        AstNode smallTree = new AstNode(node.getName(), new DoubleLinkedList<>());
        //linkedlist only store right childeren and left childeren
        smallTree.getChildren().add(left);
        if ("+-*/^".contains(node.getName())) {
            AstNode right = handleSimplifyHelper(node.getChildren().get(1));
            smallTree.getChildren().add(right);
            if (left.isNumber() && right.isNumber() && "+-*".contains(node.getName())) {
                return new AstNode(calculate(smallTree));
            }
        }
        return smallTree;
    }

    private static double calculate(AstNode node) {
        double left = node.getChildren().get(0).getNumericValue();
        double right = node.getChildren().get(1).getNumericValue();
        String name = node.getName();
        if (name.equals("+")) {
            return left + right;
        } else if (name.equals("-")) {
            return left - right;
        } else {
            return left * right;
        }
    }


    /**
     * Accepts an Environment variable and a 'plot(exprToPlot, var, varMin, varMax, step)'
     * AstNode and generates the corresponding plot on the ImageDrawer attached to the
     * environment. Returns some arbitrary AstNode.
     *
     * Example 1:
     *
     * >>> plot(3 * x, x, 2, 5, 0.5)
     *
     * This method will receive the AstNode corresponding to 'plot(3 * x, x, 2, 5, 0.5)'.
     * Your 'handlePlot' method is then responsible for plotting the equation
     * "3 * x", varying "x" from 2 to 5 in increments of 0.5.
     *
     * In this case, this means you'll be plotting the following points:
     *
     * [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
     *
     * ---
     *
     * Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4"
     * from -10 to 10 in 0.01 increments. In this case, "a" is our "x" variable.
     *
     * >>> c := 4
     * 4
     * >>> step := 0.01
     * 0.01
     * >>> plot(a^2 + c*a + a, a, -10, 10, step)
     *
     * ---
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if varMin > varMax
     * @throws EvaluationError  if 'var' was already defined
     * @throws EvaluationError  if 'step' is zero or negative
     *
     *
     */


    public static AstNode plot(Environment env, AstNode node) {
        dictionary = env.getVariables();
        assertNodeMatches(node, "plot", 5);
        return plotHelper(env, node);
    }

    public static AstNode plotHelper(Environment env, AstNode node) {
        ImageDrawer drawer = env.getImageDrawer();
        IList<AstNode> children = node.getChildren();
        double min = toDoubleHelper(children.get(2));
        double max = toDoubleHelper(children.get(3));
        double step = toDoubleHelper(children.get(4));

        AstNode exprToPlot = children.get(0);
        AstNode var = children.get(1);
        String name = var.getName();

        if (dictionary.containsKey(name)) {
            throw new EvaluationError("Variable " + name + "defined");
        }
        if (min > max) {
            throw new EvaluationError("min has to be greater than max");
        }
        if (step <= 0) {
            throw new EvaluationError("Step " + step + " is negative");
        }


        IList<Double> x = new DoubleLinkedList<>();
        IList<Double> y = new DoubleLinkedList<>();

        for (double i = min; i <= max; i += step) {
            // we are drawing every a series of points with different x and y values.
            dictionary.put(name, new AstNode(i));
            //different values are stored in the same variables because they only need to use it once for calculations;
            // we give the small subtree to toDoublehelper to function it
            double yValue = toDoubleHelper(exprToPlot);
            x.add(i);
            y.add(yValue);
        }


        drawer.drawScatterPlot("plot", name, "y", x, y);
        //
        dictionary.remove(name);

        // Note: every single function we add MUST return an
        // AST node that your "simplify" function is capable of handling.
        // However, your "simplify" function doesn't really know what to do
        // with "plot" functions (and what is the "plot" function supposed to
        // evaluate to anyways?) so we'll settle for just returning an
        // arbitrary number.
        //
        // When working on this method, you should uncomment the following line:
        //
        return new AstNode(0);

    }


}