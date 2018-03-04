package code;

public class ExceptionTest {

    public static void main(String[] args) {
        Integer a = 10;
        Integer b = 10;
        System.out.println(a == b);
        System.out.println(new Integer(10).equals(10));;

        System.out.println(new Integer(10).equals(new Integer(10)));;

        System.out.println(new Integer(10) == new Integer(10));

        System.out.println(new Boolean(true) == new Boolean(true));;

        System.out.println(new String("abc") == new String("abc"));;
    }

    ;
}
