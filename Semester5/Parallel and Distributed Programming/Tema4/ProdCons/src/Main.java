import methodA.MethodA;
import methodB.MethodB;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        try {
//            Utils.generateFiles();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        List<String> fileNames = new ArrayList<>();

        for (int i = 1; i <= 5; i ++) {
            for (int j = 1; j <= 10; j++) {
                fileNames.add("Scores/ResultC" + i + "_P" + j + ".txt");
            }
        }

        String option = args[0];
        int p = Integer.parseInt(args[1]);
        int p_r = Integer.parseInt(args[2]);
        int p_w = p - p_r;

        long startTime, endTime;

        if (option.equals("MethodA")) {
            MethodA methodA = new MethodA(fileNames);
            startTime = System.nanoTime();
            methodA.run();
            endTime = System.nanoTime();
            System.out.println((endTime - startTime) / 1000000f);
            System.out.println();
        } else if (option.equals("MethodB")) {
            MethodB methodB = new MethodB(fileNames, p_r, p_w);
            startTime = System.nanoTime();
            methodB.run();
            endTime = System.nanoTime();
            System.out.println((endTime - startTime) / 1000000f);

            try {
                boolean identical = Utils.areResultFilesIdentical("Clasament_MethodB.txt");
                if (identical) {
                    System.out.println("Identical");
                } else {
                    System.out.println("Not identical");
                }
            } catch (IOException e) {
                System.out.println("IO Exception");
            }
        }
    }
}