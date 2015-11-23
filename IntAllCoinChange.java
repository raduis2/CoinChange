import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class IntAllCoinChange {

  static List<String> label (List<Integer> count, List<Integer> values) {
    return IntStream.range(0, count.size())
             .mapToObj(i-> count.get(i).intValue()==0 ? "     " :
                String.format("%2dx%2d", count.get(i), values.get(i)))
             .collect(Collectors.toList());
  }

  private static Collection<List<Integer>> getSolutions(List<Integer> coinValues, int S) {
    Collection<List<Integer>> solutions = new LinkedHashSet<List<Integer>>();
    int numCoinValues = coinValues.size();
    if (numCoinValues <= 0) return solutions;
    int headValue = coinValues.get(0).intValue();
    int headCount = S/headValue;  // coin values should be strictly positive
    if (numCoinValues==1) {
      if (S == headValue*headCount) {
         solutions.add(Arrays.asList(headCount));
      }
    } else {
      for (int i=0; i<=headCount; i++) {
         Collection<List<Integer>> partialSolutions =
           getSolutions(coinValues.subList(1, numCoinValues), S - i*headValue);
         for (List<Integer> partialSolution : partialSolutions) {
            List<Integer> solution = new ArrayList<Integer>(Arrays.asList(i));
            solution.addAll(partialSolution);  // concatenate partial solution
            solutions.add(solution);
         }
      }
    }
    return solutions;
  }

  public static void main(String[] args) {
    int numargs = args.length;
    Integer sum = Integer.parseInt(args[numargs-1]);
    List<Integer> coinValues = new ArrayList<Integer>();
    for (int i=0; i<numargs-1; i++) { coinValues.add(Integer.parseInt(args[i])); }

    Collection<List<Integer>> solutions = getSolutions(coinValues, sum.intValue());
    solutions.stream().forEach(s->System.out.println(label(s, coinValues)));
    System.out.println("#Solutions: " + solutions.size());
  }
}
