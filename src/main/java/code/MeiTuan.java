package code;

import java.util.Scanner;

public class MeiTuan {
    public static void main(String[] args) {
//        int[] ans = new int[7];
//        ans[0] = 1;
//
//        Scanner scanner = new Scanner(System.in);
//        int  n = scanner.nextInt();
//        for(int i =1; i<6;i++){
//            for(int j = i-1; j >= 0; j--){
//                ans[i] += ans[j];
//            }
//            ans[i] +=1;
//        }
//        System.out.println(ans[n-1]);

        Scanner scanner = new Scanner(System.in);
        int [] data = new int[]{1,5,10,20,50,100};
        int number = scanner.nextInt();
        int [] ans = new int[number+1];
        for(int i=0; i<data.length;i++){
            if((data[i]-1) < number)
                 ans[data[i]-1] = 1;
        }
        for(int i=1; i<number;i++){
            for(int j = 0; j < data.length;j++){
                ans[i] += ( i - data[j] > 0 ? ans[i-data[j]] : 0 );
            }
        }
        System.out.println(ans[number-1]);
    }

}
