import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class AllCoinChange {
  private static double EPSILON = 0.001;
  private static boolean areEqual(double x, double y) {
    return Math.abs(x-y) < EPSILON;
  }

  private static Collection<List<Integer>> getSolutions(List<Double> coinValues, double S) {
    Collection<List<Integer>> solutions = new LinkedHashSet<List<Integer>>();
    int numCoinValues = coinValues.size();
    if (numCoinValues <= 0) return solutions;
    double headValue = coinValues.get(0).doubleValue();
    int headCount = (int) ((S+EPSILON)/headValue);  // coin values should be strictly positive
    if (numCoinValues==1) {
      if (areEqual(S, headValue*headCount)) {
         solutions.add(Arrays.asList(headCount));
      }
    } else {
      for (int i=0; i<=headCount; i++) {
         Collection<List<Integer>> partialSolutions =
           getSolutions(coinValues.subList(1, numCoinValues), S - Double.valueOf(i)*headValue);
         for (List<Integer> partialSolution : partialSolutions) {
            List<Integer> solution = new ArrayList<Integer>(Arrays.asList(i));
            solution.addAll(partialSolution);  // concatenate partial solution
            solutions.add(solution);
         }
      }
    }
    return solutions;
  }

  static List<String> label (List<Integer> count, List<Double> values) {
    return IntStream.range(0, count.size())
             .mapToObj(i-> count.get(i).intValue()==0 ? "       " :
                String.format("%2dx%4.2f", count.get(i), values.get(i)))
             .collect(Collectors.toList());
  }

  public static void main(String[] args) {
    int numargs = args.length;
    Double sum = Double.parseDouble(args[numargs-1]);
    List<Double> coinValues = new ArrayList<Double>();
    for (int i=0; i<numargs-1; i++) { coinValues.add(Double.parseDouble(args[i])); }

    Collection<List<Integer>> solutions = getSolutions(coinValues, sum.doubleValue());
    System.out.println("#Solutions: " + solutions.size());
    solutions.stream().forEach(s->System.out.println(label(s, coinValues)));
  }
}
