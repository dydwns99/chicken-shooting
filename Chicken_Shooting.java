import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Chicken_Shooting extends JFrame {

    public Chicken_Shooting() {
        setTitle("사격 게임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(null);
        //치킨
        Chi chi = new Chi();
        JLabel jchi = chi.getchi();
        c.add(jchi);
        //총
        Cho cho = new Cho();
        JPanel jcho1 = cho.getjPanel1();
        JPanel jcho2 = cho.getjPanel2();
        c.add(jcho1);//총알
        c.add(jcho2);//받침대

        //이벤트 설정
        c.setFocusable(true);
        c.requestFocus();
        //치킨과 총 생성
        ChiCho chiCho = new ChiCho(jchi, jcho1);

        Mythread mythread = new Mythread(chiCho);
        mythread.start();


        c.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    Shooting shooting = new Shooting(chiCho);
                    shooting.start();
                }
                //@@@ 수정사항
                if (event.getKeyCode() == KeyEvent.VK_R) {
                    Mythread mythread = new Mythread(chiCho);
                    Shooting shooting1 = new Shooting(chiCho);
                    mythread.start();
                    shooting1.start();
                }
            }
        });
        setSize(400, 400);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Chicken_Shooting();
    }
}

//치킨 스레드 생성
class Mythread extends Thread {
    private ChiCho chiCho;
    public Mythread(ChiCho chiCho) {
        this.chiCho = chiCho;
    }
    public void run() {
        while (true) {
            try {
                sleep(20);
                chiCho.chimove();
                if (chiCho.cho.getY()<=30 && chiCho.chi.getX() >= chiCho.cho.getX()-20 && chiCho.chi.getX()<= chiCho.cho.getX()+20) {
                    chiCho.chirevolve();
                    this.interrupt();
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}

// 총알 스레드 생성
class Shooting extends Thread {
    private ChiCho chiCho;
    public Shooting(ChiCho chiCho) {
        this.chiCho=chiCho;
    }
    public void run() {
        while (true) {
            try {
                sleep(20);
                chiCho.chomove();
                if (chiCho.cho.getY() == 0) {
                    chiCho.revolve();
                    this.interrupt();
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
//치킨 이미지
class Chi extends JLabel {
    public JLabel label;
    public Chi() {
        ImageIcon imageIcon = new ImageIcon("images/chicken.png");
        Image img = imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon imgicon = new ImageIcon(img);
        JLabel label = new JLabel(imgicon);
        label.setLocation(15, 15);
        label.setSize(30,30);
        this.label = label;
    }
    public JLabel getchi() {
        return this.label;
    }
}
//총 이미지
class Cho extends JPanel {
    public JPanel jPanel1;
    public JPanel jPanel2;
    public Cho() {
        JPanel jPanel1 = new JPanel();
        jPanel1.setBackground(Color.red);
        jPanel1.setLocation(193, 340);
        jPanel1.setSize(5, 5);
        this.jPanel1 = jPanel1;

        JPanel jPanel2 = new JPanel();
        jPanel2.setBackground(Color.BLACK);
        jPanel2.setLocation(180,345);
        jPanel2.setSize(30, 30);
        this.jPanel2 = jPanel2;
    }
    public JPanel getjPanel1() {
        return this.jPanel1;
    }
    public JPanel getjPanel2() {
        return this.jPanel2;
    }
}

//치킨과 총의 메서드
class ChiCho extends JLabel {
    public JLabel chi;
    public JPanel cho;
    private int chint=1;
    private int choint = 1;
    public ChiCho(JLabel chi, JPanel cho) {
        this.chi = chi;
        this.cho = cho;
    }

    public synchronized void chimove() {
        //치킨이 총알에 맞으면 다시 시작
        if (cho.getY()<=30 && chi.getX() >= cho.getX()-20 && chi.getX()<= cho.getX()+20) {
            chistop();
        }
        if (chi.getX() == 400) {
            chint = 1;
        }
        chi.setLocation(chint * 5, 10);
        chint++;

        notify();
    }
    public synchronized void chirevolve() {
        try {
            chi.setLocation(15, 15);
            chint = 1;
            wait();
        } catch (InterruptedException e) {
        }
        notify();
    }
    public synchronized void chistop() {
        try {
            System.out.print("명중했습니다!");
            chi.setLocation(chi.getX(), chi.getY());
            wait();
        } catch (InterruptedException e) {
        }
        notify();
    }
    public synchronized void chomove() {
        if (cho.getY()<=30 && chi.getX() >= cho.getX()-20 && chi.getX() <= cho.getX()+20) {
            chostop();
        }
        cho.setLocation(193, 340 - choint * 5);
        choint++;
    }
    public synchronized void revolve(){
        try {
            System.out.print("장전 중...");
            cho.setLocation(193, 340);
            choint = 1;
            wait();
        } catch (InterruptedException e) {
        }
        notify();
    }
    public synchronized void chostop() {
        try {
            cho.setLocation(cho.getX(),cho.getY());
            wait();
        } catch (InterruptedException e) {
        }
        notify();
    }
}

