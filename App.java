import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception 
    {
        int boardWidth = 600;
        int boardHeight = boardWidth;

        JFrame frame = new JFrame("MineSweeper");
        frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MineSweeper mineSweeper = new MineSweeper(boardWidth, boardHeight);
        frame.add(mineSweeper);
        frame.pack();
        mineSweeper.requestFocus();
    }
}
