import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.*;


public class MineSweeper extends JPanel implements ActionListener, MouseListener {
    private int boardWidth;
    private int boardHeight;
    private int tileSize = 75;
    private int mineSize = 8;
    private Tile[][] tiles;
    private Random random;
    public static Tile[] arrMines;


    //Game Logic
    Timer gameLoop;
    boolean gameOver = false;
    Tile selectedTile;
    

    private class Tile 
    {
        int x;
        int y;
        boolean isMine;
        boolean isRevealed = false;
        boolean isFlagged = false;
        int mineCount = 0;

        Tile(int x, int y, boolean isMine) 
        {
            this.x = x;
            this.y = y;
            this.isMine = isMine;
        }
    }

    public MineSweeper() 
    {
        this(600, 600); // Default size
    }

    public MineSweeper(int boardWidth, int boardHeight) 
    {
        setBoardWidth(boardWidth);
        setBoardHeight(boardHeight);
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.GRAY);
        arrMines = new Tile[mineSize];
        gameLoop = new Timer(1000 / 60, this); // 60 FPS update rate
        gameLoop.start();

        tiles = new Tile[boardHeight / tileSize][boardWidth / tileSize];

        for (int k = 0; k < boardWidth / tileSize; k++) 
        {
            for (int l = 0; l < boardHeight / tileSize; l++) 
            {
                tiles[k][l] = new Tile(k, l, false);
            }
        }

        random = new Random();
        placeMines();  
        placeNumbers(); 
        addMouseListener(this);
        repaint();     
    }

    public void setBoardWidth(int boardWidth) 
    {
        this.boardWidth = boardWidth;
    }

    public void setBoardHeight(int boardHeight) 
    {
        this.boardHeight = boardHeight;
    }

    public int getBoardWidth() 
    {
        return boardWidth;
    }

    public int getBoardHeight() 
    {
        return boardHeight;
    }

    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        int countCorrectFlagged = 0;
        for (int k = 0; k < boardWidth / tileSize; k++) 
        {
            for (int l = 0; l < boardHeight / tileSize; l++) 
            {
                g.setFont(new Font("Arial", Font.BOLD, 20));
                Tile currentTile = tiles[k][l];
                int x = currentTile.x * tileSize;
                int y = currentTile.y * tileSize;

                if (currentTile.isRevealed && !currentTile.isMine) 
                {
                    g.setColor(Color.WHITE); // Highlight selected tile
                } 
                else if (currentTile.isRevealed && currentTile.isMine)
                {
                    g.setColor(Color.RED); // Mine color
                    gameOver = true;
                }
                else 
                {
                    g.setColor(Color.GRAY); // Non-mine tile color
                } 

                if (currentTile.isFlagged)
                {
                    g.setColor(Color.BLUE); 
                    g.setFont(new Font("Arial", Font.BOLD, 20));
                    g.drawString("F", x + tileSize / 2 - 5, y + tileSize / 2 + 5);
                }

                if (currentTile.isFlagged && currentTile.isMine)
                {
                    countCorrectFlagged = countCorrectFlagged + 1;
                }

                g.fill3DRect(x, y, tileSize, tileSize, true);

                if (currentTile.mineCount == 1 && currentTile.isRevealed) 
                {
                    g.setColor(Color.BLUE);  
                    g.drawString(String.valueOf(currentTile.mineCount), k * tileSize + tileSize / 2 - 10, l * tileSize + tileSize / 2 + 5);
                } 
                else if (currentTile.mineCount == 2 && currentTile.isRevealed) 
                {
                    g.setColor(Color.GREEN);  
                    g.drawString(String.valueOf(currentTile.mineCount), k * tileSize + tileSize / 2 - 10, l * tileSize + tileSize / 2 + 5);
                }
                else if (currentTile.mineCount == 3 && currentTile.isRevealed) 
                {
                    g.setColor(Color.ORANGE);  
                    g.drawString(String.valueOf(currentTile.mineCount), k * tileSize + tileSize / 2 - 10, l * tileSize + tileSize / 2 + 5);
                }
                else if (currentTile.mineCount == 5 && currentTile.isRevealed) 
                {
                    g.setColor(Color.RED);  
                    g.drawString(String.valueOf(currentTile.mineCount), k * tileSize + tileSize / 2 - 10, l * tileSize + tileSize / 2 + 5);
                }
            }
        }

        if (gameOver) 
        {
            g.setColor(Color.BLACK);
            g.drawString("Game Over!", boardWidth / 2 - 40, boardHeight / 2); // Display "Game Over"
            //gameLoop.stop();
        }
    }
    

    public void placeMines() 
    {
        int minesPlaced = 0;
        while (minesPlaced < mineSize) 
        {
            int x = random.nextInt(boardWidth / tileSize);
            int y = random.nextInt(boardHeight / tileSize);

            if (!tiles[x][y].isMine) 
            {
                tiles[x][y].isMine = true;
                arrMines[minesPlaced] = tiles[x][y];
                minesPlaced++;
            }
        }
    }
    

    public void placeNumbers()
    {
        for (Tile mine : arrMines)
        {
            if (mine == null) continue; 
        
            int mineX = mine.x;
            int mineY = mine.y;
    
            // Loop through the 8 surrounding tiles
            for (int dx = -1; dx <= 1; dx++)
            {
                for (int dy = -1; dy <= 1; dy++)
                {
                    int newX = mineX + dx;
                    int newY = mineY + dy;
    
                    if (newX >= 0 && newX < tiles.length && newY >= 0 && newY < tiles[0].length) 
                    {
                        Tile neighbor = tiles[newX][newY];
    
                        if (!neighbor.isMine) 
                        {
                            neighbor.mineCount++; 
                        }
                    }
                }
            }
        }

    }

    //Not my code
    public void floodFill(int x, int y) {
        // Base conditions
        if (x < 0 || x >= boardWidth / tileSize || y < 0 || y >= boardHeight / tileSize) return; // Out of bounds
        Tile tile = tiles[x][y];
        if (tile.isRevealed || tile.isMine || tile.isFlagged) return; // Already revealed or a mine
    
        // Reveal the tile
        tile.isRevealed = true;
    
        // If the tile has no adjacent mines, recursively reveal neighbors
        if (tile.mineCount == 0) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx != 0 || dy != 0) { // Avoid checking the current tile
                        floodFill(x + dx, y + dy);
                    }
                }
            }
        }
    }

    //Not my code
    public void checkWinCondition() {
        int totalNonMineTiles = 0;
        int revealedNonMineTiles = 0;
    
        // Loop through all the tiles to check the win condition
        for (int i = 0; i < boardWidth / tileSize; i++) {
            for (int j = 0; j < boardHeight / tileSize; j++) {
                Tile tile = tiles[i][j];
    
                if (!tile.isMine) {  // If the tile is not a mine
                    totalNonMineTiles++;  // Increment total non-mine tiles
                    if (tile.isRevealed) {
                        revealedNonMineTiles++;  // Increment revealed non-mine tiles
                    }
                }
            }
        }
    
        // If all non-mine tiles have been revealed, the player wins
        if (totalNonMineTiles == revealedNonMineTiles) {
            gameOver = true;  // End the game
            repaint();  // Trigger redraw to show the win message
            JOptionPane.showMessageDialog(this, "You Win!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        //repaint();
        //if (gameOver)
        //{
        //    gameLoop.stop();
        //}
    }

    @Override
    public void mousePressed(MouseEvent e) 
    {
        if (gameOver) return; // Ignore input if game is over

        int x = e.getX() / tileSize;
        int y = e.getY() / tileSize;

        if (x >= 0 && x < boardWidth / tileSize && y >= 0 && y < boardHeight / tileSize) 
        {
            Tile clickedTile = tiles[x][y];

            if (e.getButton() == MouseEvent.BUTTON3) 
            { // Right-click (flag)
                if (!clickedTile.isRevealed) 
                { 
                    clickedTile.isFlagged = !clickedTile.isFlagged;
                }
            } 
            else if (e.getButton() == MouseEvent.BUTTON1) 
            { // Left-click (reveal)
                if (!clickedTile.isFlagged) 
                { 
                    if (clickedTile.isMine) 
                    {
                        clickedTile.isRevealed = true;
                    } 
                    else 
                    {
                        floodFill(x, y); 
                    }
                    
                    checkWinCondition();
                }
            }

            repaint(); // Redraw the board
        }
    }



    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
