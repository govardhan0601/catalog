import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;

public class ShamirSecretSharing {

    static class Point {
        int x;
        BigInteger y;

        Point(int x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }

    // Function to decode base N string to BigInteger
    private static BigInteger decodeBase(String value, int base) {
        return new BigInteger(value, base);
    }

    // Lagrange interpolation to find the constant term 'c'
    private static BigInteger lagrangeInterpolation(ArrayList<Point> points, int k) {
        BigInteger constant = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger term = points.get(i).y;
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term = term.multiply(BigInteger.valueOf(-points.get(j).x));
                    term = term.divide(BigInteger.valueOf(points.get(i).x - points.get(j).x));
                }
            }
            constant = constant.add(term);
        }
        return constant;
    }

    public static void main(String[] args) {
        try {
            // Read the input JSON file
            FileInputStream inputStream = new FileInputStream("input.json");
            JSONTokener tokener = new JSONTokener(inputStream);
            JSONObject input = new JSONObject(tokener);

            int n = input.getJSONObject("keys").getInt("n");
            int k = input.getJSONObject("keys").getInt("k");

            ArrayList<Point> points = new ArrayList<>();

            // Parse and decode Y values
            for (int i = 1; i <= n; i++) {
                int base = Integer.parseInt(input.getJSONObject(String.valueOf(i)).getString("base"));
                String value = input.getJSONObject(String.valueOf(i)).getString("value");
                BigInteger decodedY = decodeBase(value, base);
                points.add(new Point(i, decodedY));
            }

            // Find constant term using Lagrange interpolation
            BigInteger constant = lagrangeInterpolation(points, k);
            System.out.println("Constant term (Secret): " + constant);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
