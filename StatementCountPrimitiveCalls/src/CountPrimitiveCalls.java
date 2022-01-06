import components.statement.Statement;
import components.statement.StatementKernel.Condition;

/**
 * Utility class with method to count the number of calls to primitive
 * instructions (move, turnleft, turnright, infect, skip) in a given
 * {@code Statement}.
 *
 * @author Put your name here
 *
 */
public final class CountPrimitiveCalls {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private CountPrimitiveCalls() {
    }

    /**
     * Reports the number of calls to primitive instructions (move, turnleft,
     * turnright, infect, skip) in a given {@code Statement}.
     *
     * @param s
     *            the {@code Statement}
     * @return the number of calls to primitive instructions in {@code s}
     * @ensures <pre>
     * countOfPrimitiveCalls =
     *  [number of calls to primitive instructions in s]
     * </pre>
     */
    public static int countOfPrimitiveCalls(Statement s) {
        int count = 0;
        switch (s.kind()) {
            case BLOCK: {
                /*
                 * Add up the number of calls to primitive instructions in each
                 * nested statement in the BLOCK.
                 */

                for (int i = 0; i < s.lengthOfBlock(); i++) {
                    Statement temp = s.removeFromBlock(i);
                    count += countOfPrimitiveCalls(temp);
                    s.addToBlock(i, temp);
                }

                break;
            }
            case IF: {
                /*
                 * Find the number of calls to primitive instructions in the
                 * body of the IF.
                 */

                Statement temp = s.newInstance();
                Condition cndl = s.disassembleIf(temp);
                count = countOfPrimitiveCalls(temp);
                s.assembleIf(cndl, temp);

                break;
            }
            case IF_ELSE: {
                /*
                 * Add up the number of calls to primitive instructions in the
                 * "then" and "else" bodies of the IF_ELSE.
                 */

                Statement tempIf = s.newInstance();
                Statement tempElse = s.newInstance();
                Condition cndl = s.disassembleIfElse(tempIf, tempElse);
                count = countOfPrimitiveCalls(tempIf)
                        + countOfPrimitiveCalls(tempElse);
                s.assembleIfElse(cndl, tempIf, tempElse);

                break;
            }
            case WHILE: {
                /*
                 * Find the number of calls to primitive instructions in the
                 * body of the WHILE.
                 */

                Statement temp = s.newInstance();
                Condition cndl = s.disassembleWhile(temp);
                count = countOfPrimitiveCalls(temp);
                s.assembleWhile(cndl, temp);

                break;
            }
            case CALL: {
                /*
                 * This is a leaf: the count can only be 1 or 0. Determine
                 * whether this is a call to a primitive instruction or not.
                 */

                String call = s.disassembleCall();
                if (call.equals("turnleft") || call.equals("turnright")
                        || call.equals("move") || call.equals("infect")
                        || call.equals("skip")) {
                    count++;
                }
                s.assembleCall(call);
            }
            default: {
                // this will never happen...can you explain why?
                break;
            }
        }
        return count;
    }

}
