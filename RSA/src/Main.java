import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static int n=0, d=0, p=191, q=193;

    public static List<Integer> encryption(List<Integer> ascii){
        System.out.println(fermatPrimeTest(p));
        System.out.println(fermatPrimeTest(q));
        n = p * q;
        int phi = (p-1) * (q-1);

        List<Integer> Zphi = new ArrayList<>();
        Zphi = IntStream.range(0, phi-1)
                .boxed()
                .collect(Collectors.toList());

        int e = 0;
        Collections.shuffle(Zphi);
        for(int i : Zphi){
            if(gcd(i,phi) == 1){
                e=i;

                break;
            }
        }

        d = EEA(e, phi) % phi;
        if (d<0){
            d+=phi;
        }
        System.out.println("P: " + p);
        System.out.println("Q: " + q);
        System.out.println("E: " + e);
        System.out.println("D: " + d);

        List<Integer> y = new ArrayList<>();

        for(int i: ascii){
            y.add(squareAndMultiply(i,e,n));
        }

        return y;
    }

    public static List decryption(List<Integer> y) {
        List<Integer> x = new ArrayList<>();
        for(int i : y){
            x.add(CRT(i));
        }
        return x;
    }

    public static int CRT(int x) {
        int xp = x % p;
        int xq = x % q;

        int dp = d % (p-1);
        int dq = d % (q-1);

        int yp = squareAndMultiply(xp,dp, p);
        int yq = squareAndMultiply(xq,dq, q);

        int cp = EEA(q,p) % p;
        int cq = EEA(p,q) % q;

        int y = ((q*cp) * yp + (p*cq) * yq ) % n;
        return y;
    }

    public static int gcd(int e, int z)
    {
        if (e == 0)
        {
            return z;
        }
        else
            return gcd(z%e,e);
    }

    public static int EEA(int e, int phi)
    {
        int r1,r0;
        if (e < phi){
            r1 = e;
            r0 = phi;
        }
        else {
            r1 = phi;
            r0 = e;
        }
        List<Integer> r = new ArrayList<>();
        List<Integer> S = new ArrayList<>();
        List<Integer> T = new ArrayList<>();

        r.add(r0);
        r.add(r1);
        int S0 = 1,T1 = 1;
        int S1 = 0 ,T0 = 0;

        S.add(S0);
        S.add(S1);

        T.add(T0);
        T.add(T1);

        int i = 1;
        int temp=0;
        int Q = 0;
        do {
            i++;
            temp = r.get(i-2) % r.get(i-1);
            r.add(temp);
            Q = ( r.get(i-2) - r.get(i) ) / r.get(i-1);

            temp =  S.get(i-2) - (Q  * S.get(i-1));
            S.add(temp);

            temp =  T.get(i-2) - (Q  * T.get(i-1));
            T.add(temp);

        }while(r.get(i) != 0);

        return T.get(i-1);
    }
    public static String fermatPrimeTest(int primeNum) {
        for (int i =1; i<100; i++){
            int a = (int)Math.floor(Math.random()*(primeNum-2-(2+1))+2);
            if(squareAndMultiply(a,primeNum-1,primeNum) != 1){
                return "The number is composite";
            } else {
                return "The number is prime";
            }
        }
        return "0";
    }

    public static int squareAndMultiply(int m, int e, int n) {
        double y = m;
        String s = Integer.toBinaryString(e);
        for (int i =1; i<s.length();i++){
            y = Math.pow(y,2) % n;
            if (s.charAt(i) =='1'){
                y = (y*m) % n;
            }
        }
        return (int)y;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String msg;

        System.out.println("Enter The message ");
        msg = input.nextLine();
        List<Integer> ascii = new ArrayList<>();
        for (int i=0;i<msg.length();i++)
        {
            ascii.add((int)msg.charAt(i));
        }
        List<Integer> y = new ArrayList<>();
        y = encryption(ascii);
        System.out.println("Cipher Text: ");
        System.out.println(y);
        List<Integer> x = new ArrayList<>();
        x = decryption(y);

        for(int i : x){
            System.out.print(Character.toString((char) i));
        }
    }
}
