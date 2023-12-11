package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnackGame extends JFrame implements ActionListener, KeyListener {

    private static final int TILE_SIZE = 20;
    private static final int GRID_SIZE = 20;

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private List<Point> snake;
    private Point food;
    private Direction direction;
    private Timer timer;

    public SnackGame() {
        setTitle("Snack Game");
        setSize(TILE_SIZE * GRID_SIZE, TILE_SIZE * GRID_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeGame();

        addKeyListener(this);
        setFocusable(true);

        timer = new Timer(150, this);
        timer.start();

        setVisible(true);
    }

    private void initializeGame() {
        System.out.println("Patan gaththaa");
        snake = new ArrayList<>();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        direction = Direction.RIGHT;

        spawnFood();
    }

    private void spawnFood() {
        Random rand = new Random();
        System.out.println(rand);
        int x = rand.nextInt(GRID_SIZE);
        int y = rand.nextInt(GRID_SIZE);

        food = new Point(x, y);

        // Ensure food does not spawn on the snake
        while (snake.contains(food)) {
            x = rand.nextInt(GRID_SIZE);
            y = rand.nextInt(GRID_SIZE);
            food.setLocation(x, y);
        }
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = (Point) head.clone();

        switch (direction) {
            case UP:
                newHead.translate(0, -1);
                break;
            case DOWN:
                newHead.translate(0, 1);
                break;
            case LEFT:
                newHead.translate(-1, 0);
                break;
            case RIGHT:
                newHead.translate(1, 0);
                break;
        }

        // Check if the new head collides with the snake or hits the wall
        if (newHead.x < 0 || newHead.x >= GRID_SIZE || newHead.y < 0 || newHead.y >= GRID_SIZE
                || snake.contains(newHead)) {
            gameOver();
            return;
        }

        snake.add(0, newHead);

        // Check if the snake eats the food
        if (newHead.equals(food)) {
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + (snake.size() - 1));
        initializeGame();
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw the snake
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw the food
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw grid lines
        g.setColor(Color.GRAY);
        for (int i = 0; i < GRID_SIZE; i++) {
            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, getHeight());
            g.drawLine(0, i * TILE_SIZE, getWidth(), i * TILE_SIZE);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                if (direction != Direction.DOWN) {
                    direction = Direction.UP;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != Direction.UP) {
                    direction = Direction.DOWN;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (direction != Direction.RIGHT) {
                    direction = Direction.LEFT;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != Direction.LEFT) {
                    direction = Direction.RIGHT;
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SnackGame());
    }
}
