import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;


public class adaptive_algorithms {
    static int[] movie = new int[600];//Each element represents bits per second of the video,
    //assuming that the length of the video is 10 minutes.
    static int[] ntw_speed = new int[1800];//Each element represents bits per second of the network, total simulation time equals 30 minutes.


    static int bps1 = 96;//bit rate of each movie
    static int bps2 = 256;
    static int bps3 = 612;
    //int max_nspeed=1000;
    static int avg_nspeed = 800;//500,600

    static int[] construct_m(int bps) {
        for (int i = 0; i < movie.length; i++) {
            movie[i] = bps;
        }
        return movie;
    }

    static int[] ntw_environment(int avg) {
        Random rd = new Random();

        for (int i = 0; i < ntw_speed.length / 3; i++) {
            ntw_speed[i] = rd.nextInt(avg * 2 / 3);
        }
        for (int i = ntw_speed.length / 3; i < ntw_speed.length / 3 * 2; i++) {
            ntw_speed[i] = avg * 2 / 3 + rd.nextInt(avg * 2 / 3);
        }
        for (int i = ntw_speed.length / 3 * 2; i < ntw_speed.length; i++) {
            ntw_speed[i] = avg * 2 / 3 * 2 + rd.nextInt(avg * 2 / 3);
        }
        ArrayList temp = new ArrayList();
        for (int i = 0; i < ntw_speed.length; i++) {
            temp.add(ntw_speed[i]);
        }
        Collections.shuffle(temp);

        for (int i = 0; i < ntw_speed.length; i++) {
            ntw_speed[i] = (Integer) temp.get(i);
        }
        return ntw_speed;
    }


    static void s_trdtnl(int bps) {
        ArrayList buffer = new ArrayList();
        int count = 0;
        for (int i = 0; i < ntw_speed.length; i++) {
            if (bps > ntw_speed[i]) {
                if (buffer.size() == 0) {
                    System.out.println(0);
                } else {
                    System.out.println(1);
                    buffer.remove(0);
                    count++;
                    if (count == movie.length)
                        break;
                }

            } else {
                int buf = ntw_speed[i] / bps;
                for (int j = 0; j < buf; j++) {
                    buffer.add(bps);
                }

                System.out.println(1);
                buffer.remove(0);
                count++;
                if (count == movie.length)
                    break;

            }
        }

    }

    static double getBps(int indicator) {
        switch (indicator) {
            case 1:
                return bps1;
            case 2:
                return bps2;
            case 3:
                return bps3;
        }
        return bps1;
    }

    static void s_QAA(int bps1, int bps2, int bps3) {
        ArrayList buffer = new ArrayList();

        int l_cur;
        if (ntw_speed[0] >= bps3)
            l_cur = 3;
        else if (ntw_speed[0] >= bps2 && ntw_speed[0] < bps3)
            l_cur = 2;
        else
            l_cur = 1;
        //System.out.println(l_cur);

        int t_firstfrag = 0;
        for (int i = 0; i < ntw_speed.length; i++) {
            if (ntw_speed[i] < bps1)
                t_firstfrag++;
            else {
                t_firstfrag++;
                break;
            }
        }
        //System.out.println(t_firstfrag);
        if (ntw_speed[0] >= bps3) {
            int temp = ntw_speed[0] / bps3;
            t_firstfrag = 1 / temp;
            //System.out.println(bps3);
//            for(int i=1;i<temp;i++){
//                buffer.add(bps3);
//            }
        }
        if (ntw_speed[0] >= bps2 && ntw_speed[0] < bps3) {
            t_firstfrag = 1;
            //System.out.println(bps2);
        }
        if (ntw_speed[0] >= bps1 && ntw_speed[0] < bps2) {
            t_firstfrag = 1;
            //System.out.println(bps1);
        }

        double r_download, t_lastfrag;
        int l_nxt = 1;
        t_lastfrag = t_firstfrag;
        int count = 0;
        for (int i = t_firstfrag; i < ntw_speed.length; ) {

            r_download = 1 / t_lastfrag;
            if (r_download < 1) {
                if (l_cur > 1) {
                    if (r_download < getBps(l_cur - 1) / getBps(l_cur)) {
                        l_nxt = 1;
                    } else l_nxt = l_cur - 1;
                }
            } else {
                if (l_cur < 3) {
                    if (r_download >= getBps(l_cur - 1) / getBps(l_cur)) {
                        while (l_nxt != 3 && r_download >= getBps(l_nxt + 1) / getBps(l_cur)) {
                            l_nxt = l_nxt + 1;
                        }
                    }
                }
            }
            if (getBps(l_cur) > ntw_speed[i]) {
                if (buffer.size() == 0) {
                    System.out.println(0);
                } else {
                    System.out.println(buffer.get(0));
                    buffer.remove(0);
                    count++;
                    if (count == movie.length)
                        break;
                }

            } else {
                int buf = ntw_speed[i] / (int) getBps(l_cur);
                for (int j = 0; j < buf; j++) {
                    buffer.add(l_cur);
                }

                System.out.println(buffer.get(0));
                buffer.remove(0);
                count++;
                if (count == movie.length)
                    break;

            }

//            System.out.println(l_nxt);
//            count++;
//            if(count==movie.length)
//                break;


            l_cur = l_nxt;

            t_firstfrag = 0;
            int j = i + 1;
            for (i = i + 1; i < ntw_speed.length; i++) {
                if (ntw_speed[i] < bps1)
                    t_firstfrag++;
                else {
                    t_firstfrag++;
                    break;
                }
            }

            if (ntw_speed[j] >= bps3) {
                int temp = ntw_speed[j] / bps3;
                t_firstfrag = 1 / temp;
                //System.out.println(bps3);
//            for(int i=1;i<temp;i++){
//                buffer.add(bps3);
//            }
            }
            if (ntw_speed[j] >= bps2 && ntw_speed[j] < bps3) {
                t_firstfrag = 1;
                //System.out.println(bps2);
            }
            if (ntw_speed[j] >= bps1 && ntw_speed[j] < bps2) {
                t_firstfrag = 1;
                //System.out.println(bps1);
            }
            t_lastfrag = t_firstfrag;

        }

    }


    static public void s_typicalDSAH(int bps1, int bps2, int bps3) {
        ArrayList buffer = new ArrayList();
        int bps, count = 0;
        for (int i = 0; i < ntw_speed.length; i++) {
            if (ntw_speed[i] >= bps3)
                bps = 3;
            else if (ntw_speed[i] >= bps2 && ntw_speed[i] < bps3)
                bps = 2;
            else
                bps = 1;

            if (getBps(bps) > ntw_speed[i]) {
                if (buffer.size() == 0) {
                    System.out.println(0);
                } else {
                    System.out.println(buffer.get(0));
                    buffer.remove(0);
                    count++;
                    if (count == movie.length)
                        break;
                }

            } else {
                int buf = ntw_speed[i] / (int) getBps(bps);
                for (int j = 0; j < buf; j++) {
                    buffer.add(bps);
                }

                System.out.println(buffer.get(0));
                buffer.remove(0);
                count++;
                if (count == movie.length)
                    break;

            }
        }
    }

    public static void main(String args[]) {
        ntw_environment(avg_nspeed);
        //s_trdtnl(bps1);

        s_QAA(bps1, bps2, bps3);
        System.out.println("---------------");
        s_typicalDSAH(bps1,bps2,bps3);
        System.out.println("---------------");
        s_trdtnl(bps3);
//        ArrayList l = new ArrayList();
//        l.add("a");
//        l.add("b");
//        System.out.println("he");
//        System.out.println(l.size());
//        System.out.println(l.get(1));
//        System.out.println(l.get(0));
//        l.remove(0);
//        System.out.println(l.get(0));


    }

}
