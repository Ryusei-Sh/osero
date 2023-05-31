import java.awt.*;
 
import javax.swing.*;
 
import java.awt.event.*;
 
public class Reversi extends JPanel {
    static final int WIDTH = 500; // 画面サイズ（幅）
    static final int HEIGHT = 550; // 画面サイズ（高さ）
    int lm = 50;    // 左側余白
    int tm = 100;   // 上側余白
    int cs = 50;    // マスのサイズ
    int turn = 1; // 手番（1:黒，2:白)
 
    int[][] ban = {{0,0,0,0,0,0,0,0},
                   {0,0,0,0,0,0,0,0},
                   {0,0,0,0,0,0,0,0},
                   {0,0,0,1,2,0,0,0},
                   {0,0,0,2,1,0,0,0},
                   {0,0,0,0,0,0,0,0},
                   {0,0,0,0,0,0,0,0},
                   {0,0,0,0,0,0,0,0}};// 盤面
 
    // コンストラクタ（初期化処理）
    public Reversi() {
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        addMouseListener(new MouseProc());
    }
 
    // 画面描画
    public void paintComponent(Graphics g) {
        // 背景
        g.setColor(Color.gray);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        // 盤面描画
        int y = tm;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
            int x = lm + cs * j;
            g.setColor(new Color(0, 170, 0));
            g.fillRect(x, y, cs, cs);
            g.setColor(Color.black);
            g.drawRect(x, y, cs, cs);
                if (ban[i][j] != 0) {
                    if (ban[i][j] == 1) {
                        g.setColor(Color.black);
                    } else {
                        g.setColor(Color.white);
                    }
                    g.fillOval(x+cs/10, y+cs/10, cs*8/10, cs*8/10);
                }
            }
        }
        
    }
 
    // クリックされた時の処理用のクラス
    class MouseProc extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            // 盤の外側がクリックされたときは何もしないで終了
            if (x < lm) return;
            if (x >= lm+cs*8) return;
            if (y < tm) return;
            if (y >= tm+cs) return;
            // クリックされたマスを特定
            int col = (x - lm) / cs;
            int row = (x - lm) / cs;
            if (ban[row][col] == 0) {
                // 黒コマを置く
                ban[row][col] = turn;
                // 手番の変更
                if (turn == 1) {
                    turn = 2;
                } else {
                    turn = 1;
                }
            }
            // 再描画
            repaint();
        }
    }
 
    // 起動時
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.getContentPane().setLayout(new FlowLayout());
        f.getContentPane().add(new Reversi());
        f.pack();
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}