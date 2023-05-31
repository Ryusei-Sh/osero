import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Reversi extends JPanel {
    static final int WIDTH = 1000; // 画面サイズ（幅）
    static final int HEIGHT = 1100; // 画面サイズ（高さ）
    int lm = 100; // 左側余白
    int tm = 200; // 上側余白
    int cs = 100; // マスのサイズ
    int turn = 1; // 手番（1:黒，2:白)

    int ban[][] = {
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 2, 0, 0, 0},
        {0, 0, 0, 2, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0}
    }; // 盤面

    // コンストラクタ（初期化処理）
    public Reversi() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addMouseListener(new MouseProc());
    }

    // 画面描画
    public void paintComponent(Graphics g) {
        // 背景
        g.setColor(Color.gray);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // ターン情報の表示
        String turnInfo;
        if (turn == 1) {
            turnInfo = "黒石のターン";
            g.setColor(Color.black);
        } else {
            turnInfo = "白石のターン";
            g.setColor(Color.white);
        }
        g.setFont(new Font("SansSerif", Font.BOLD, 50));
        g.drawString(turnInfo, 50, 150);

        // 盤面描画
        for (int i = 0; i < 8; i++) {
            int x = lm + cs * i;
            for (int j = 0; j < 8; j++) {
                int y = tm + cs * j;

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
                    g.fillOval(x + cs / 10, y + cs / 10, cs * 8 / 10, cs * 8 / 10);
                }
                // 自分のターンの時に相手の石をひっくり返せるマスに「○」マークを表示
                if (canPlace(i, j) && ban[i][j] == 0) {
                    if (turn == 1) {
                        g.setColor(Color.black);
                    } else if (turn == 2) {
                        g.setColor(Color.white);
                    }
                    g.setFont(new Font("SansSerif", Font.BOLD, 30));
                    g.drawString("○", x + cs / 2 - 10, y + cs / 2 + 15);
                }
            }
        }
    }


    // 指定されたマスに石を置けるかどうかを判定するメソッド
    private boolean canPlace(int col, int row) {
        // 石を挟む方向の定義
        Direction[] directions = {
            new Direction(0, -1),   // 上
            new Direction(1, -1),   // 右上
            new Direction(1, 0),    // 右
            new Direction(1, 1),    // 右下
            new Direction(0, 1),    // 下
            new Direction(-1, 1),   // 左下
            new Direction(-1, 0),   // 左
            new Direction(-1, -1)   // 左上
        };

        // 相手の石を挟む処理
        for (Direction dir : directions) {
            int dx = dir.dx;
            int dy = dir.dy;
            int nx = col + dx;
            int ny = row + dy;

            if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && ban[nx][ny] == 3 - turn) {
                // 相手の石が隣接している場合のみ挟める可能性がある
                nx += dx;
                ny += dy;

                while (nx >= 0 && nx < 8 && ny >= 0 && ny < 8) {
                    if (ban[nx][ny] == turn) {
                        // 挟める場合はtrueを返す
                        return true;
                    } else if (ban[nx][ny] == 0) {
                        break;
                    }

                    nx += dx;
                    ny += dy;
                }
            }
        }

        // 挟める場所がない場合はfalseを返す
        return false;
    }

    // 石を挟む方向に対する座標の変化量を表すクラス
    class Direction {
        int dx;
        int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }


    // クリックされた時の処理用のクラス
    class MouseProc extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            // 盤の外側がクリックされたときは何もしないで終了
            if (x < lm || x >= lm + cs * 8 || y < tm || y >= tm + cs * 8) {
                return;
            }

            // クリックされたマスを特定
            int col = (x - lm) / cs;
            int row = (y - tm) / cs;

            if (ban[col][row] == 0) {
                // 石を挟む方向の定義
                Direction[] directions = {
                    new Direction(0, -1),   // 上
                    new Direction(1, -1),   // 右上
                    new Direction(1, 0),    // 右
                    new Direction(1, 1),    // 右下
                    new Direction(0, 1),    // 下
                    new Direction(-1, 1),   // 左下
                    new Direction(-1, 0),   // 左
                    new Direction(-1, -1)   // 左上
                };

                boolean canPlace = false;

                // 相手の石を挟む処理
                for (Direction dir : directions) {
                    int dx = dir.dx;
                    int dy = dir.dy;
                    int nx = col + dx;
                    int ny = row + dy;

                    if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && ban[nx][ny] == 3 - turn) {
                        // 相手の石が隣接している場合のみ挟める可能性がある
                        nx += dx;
                        ny += dy;

                        while (nx >= 0 && nx < 8 && ny >= 0 && ny < 8) {
                            if (ban[nx][ny] == turn) {
                                canPlace = true;
                                // 挟んだ石をひっくり返す
                                int mx = col + dx;
                                int my = row + dy;

                                while (mx != nx || my != ny) {
                                    ban[mx][my] = turn;
                                    mx += dx;
                                    my += dy;
                                }
                                break;
                            } else if (ban[nx][ny] == 0) {
                                break;
                            }

                            nx += dx;
                            ny += dy;
                        }
                    }
                }

                if (canPlace) {
                    // 石を置く
                    ban[col][row] = turn;
                    // 手番の変更
                    turn = 3 - turn;
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
