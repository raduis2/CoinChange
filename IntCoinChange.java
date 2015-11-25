import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntCoinChange {

    private static int numCoins(Integer[] l) {
        return l==null ? Integer.MAX_VALUE - 1 : // -1 to make sure 1+numCoins does not overflow
                Arrays.asList(l).stream().mapToInt(x->x).sum();
    }

    /* Label each element in a list of Integers with the corresponding tag from the second list */
    /* Similar to "zipping" two streams */
    static List<String> label (List<Integer> count, List<Integer> values) {
      return IntStream.range(0, count.size())
               .mapToObj(i-> count.get(i).intValue()==0 ? "     " :
                  String.format("%2dx%2d", count.get(i), values.get(i)))
               .collect(Collectors.toList());
    }

    private static Integer[] computeSolution(List<Integer> coinValues, int S, Map<Integer, Integer[]> cache) {
        // base case 1: invalid target sum
        if (S < 0) return null;
        // base case 2: already computed
        Integer[] cached = cache.get(S);
        if (cached != null) { return cached; }
        // base case 3: S=0
        int n = coinValues.size();
        Integer[] solution = new Integer[n];
        Arrays.fill(solution, 0);
        if (S == 0) { return solution; }

        // Dynamic Programming technique:
        // solution(S) = 1 + min {i=0..n-1 | solution(S - v_i)}
        solution = null;
        int min = Integer.MAX_VALUE - 1;
        for (int i=0; i<n; i++) {
            int v = coinValues.get(i);
            Integer[] partialSolution = computeSolution(coinValues, S - v, cache);
            int thisNumCoins = numCoins(partialSolution);
            if (1 + thisNumCoins < min) {
                solution = Arrays.copyOf(partialSolution, n);
                solution[i]++;
                min = 1 + thisNumCoins;
            }
        }
        cache.put(S, solution);
        return solution;
    }

    public static void main(String[] args) {
        int numargs = args.length;
        Integer sum = Integer.parseInt(args[numargs-1]);
        List<Integer> coinValues = new ArrayList<Integer>();
        for (int i=0; i<numargs-1; i++) { coinValues.add(Integer.parseInt(args[i])); }

        Map<Integer, Integer[]> cache = new HashMap<Integer, Integer[]>();
        Integer[] solution = computeSolution(coinValues, sum.intValue(), cache);
        System.out.println(String.format("Solution: %s = %d coins.",
            label(Arrays.asList(solution), coinValues), numCoins(solution)));
    }
}
