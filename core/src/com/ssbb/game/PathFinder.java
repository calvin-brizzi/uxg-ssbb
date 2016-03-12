package com.ssbb.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.utils.Array;

import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * A pathfinding class, some optimizations done
 * Created by calvin on 2014/10/06.
 */
public class PathFinder {

    // Useful numbers and things
    public int mapHeight;
    public int mapWidth;
    SquishyBlock game;
    Array<RectangleMapObject> obstacles;
    Node[][] nodes;
    PriorityQueue<Node> open;
    HashSet<Node> closed;

    // Quick constructor
    public PathFinder(SquishyBlock game) {
        mapHeight = game.mapHeight / 64;
        mapWidth = game.mapWidth / 64;
        this.game = game;
        obstacles = game.obstacles;
        createGrid();
        open = new PriorityQueue<Node>();
        closed = new HashSet<Node>();
    }

    public int[] nextMove(Sprite hunter, Sprite prey) {
        // Looks at the hunter's position and sees if there is already a path to follow
        int hunterX = (int) hunter.getX() / 64;
        int hunterY = (int) hunter.getY() / 64;
        Node child = nodes[hunterX][hunterY];

        while (!child.hasPath()) {
            // If there is none, find one!
            findPath(hunter, prey);
        }

        // Ugly, but no tuples in Java
        int[] toReturn = {child.child.x, child.child.y};

        return toReturn;
    }

    public void resetPaths() {
        // Clear the paths every half a second to avoid aiming at the old location
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                nodes[i][j].clear();
            }
        }

    }

    public void findPath(Sprite hunter, Sprite prey) {

        // Get out positions in terms of tiles
        int hunterX = Math.min((int) hunter.getBoundingRectangle().x / 64, mapWidth - 1);
        int hunterY = Math.min((int) hunter.getBoundingRectangle().y / 64, mapHeight - 1);
        int preyX = Math.min((int) (prey.getBoundingRectangle().x + 10) / 64, mapWidth - 1);
        int preyY = Math.min((int) (prey.getBoundingRectangle().y + 10) / 64, mapHeight - 1);

        // Start the things that need to be started
        open.clear();
        closed.clear();
        int currentX = hunterX;
        int currentY = hunterY;
        int g = 0;

        // Setup our nodes
        Node next = nodes[currentX][currentY];
        next.x = currentX;
        next.y = currentY;
        next.parent = null;
        Node objective = nodes[preyX][preyY];
        Node current = null;

        // So we can break
        boolean done = false;
        closed.add(next);

        do {
            // increment g
            g++;

            // Loop through the 4 closest options
            for (int i = -1; i < 2; i++) {
                if (done) break;
                for (int j = -1; j < 2; j++) {
                    int x = currentX + i;
                    int y = currentY + j;
                    // Make sure it's on the map
                    if (x >= 0 && x < mapWidth && y >= 0 && y < mapHeight && (x != currentX || y != currentY) && (j == 0 || i == 0)) {
                        current = nodes[x][y];
                        if (!closed.contains(current) && !open.contains(current)) {
                            // If it's not in closed and not already in open
                            if (current.pass) {
                                int h = Math.max(Math.abs(currentX - preyX), Math.abs(currentY - preyY));
                                int f = h + g;
                                current.set(g, h, f, x, y, next);
                                if (current == objective) {
                                    // We've got him boys
                                    done = true;
                                    break;
                                }
                                // Add the node
                                open.add(current);
                            }
                        }
                    }
                }
            }
            if (done) break;

            // Close the current node and move on
            closed.add(next);
            next = open.remove();
            while (closed.contains(next)) {
                next = open.remove();
            }
            g = next.g;
            currentX = next.x;
            currentY = next.y;

        } while (open.size() > 0);

        retrace(current);

    }

    public void retrace(Node current) {
        // Step back until the beginning
        Node temp;
        current.child = null;
        while (current.parent != null) {

            temp = current;
            current = current.parent;
            current.child = temp;
        }
    }

    public void createGrid() {
        // For each wall, create an unpassable node
        nodes = new Node[mapWidth][mapHeight];
        for (RectangleMapObject o : obstacles) {

            fill(o);
        }
    }

    public void fill(RectangleMapObject o) {
        // Find the positions of the vertexes and fill the rectangle

        int blx = (int) o.getRectangle().x;
        int bly = (int) o.getRectangle().y;
        int width = (int) o.getRectangle().width;
        int height = (int) o.getRectangle().height;


        for (int j = bly / 64; j <= (bly + height) / 64 && j < mapHeight; j++) {
            for (int i = blx / 64; i <= (blx + width) / 64 && i < mapWidth; i++) {
                nodes[i][j] = new Node(false);
            }
        }

        // Fill the rest
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (null == nodes[i][j]) {
                    nodes[i][j] = new Node(true);
                }
            }
        }
    }


    public class Node implements Comparable<Node> {
        // A tile Node
        public Node parent;
        public Node child;
        public int g;
        public int h;
        public int f;
        public int x;
        public int y;
        boolean pass;


        public Node(boolean pass) {
            // To see if it's passable
            this.pass = pass;
        }


        public void clear() {
            child = null;
            parent = null;
        }

        public boolean hasPath() {
            // To see if it's been used on a path
            return child != null;
        }

        // Practically a constuctor
        public void set(int g, int h, int f, int x, int y, Node parent) {
            this.parent = parent;
            this.g = g;
            this.h = h;
            this.f = f;
            this.x = x;
            this.y = y;
        }

        @Override
        // So we can put it into a priorityQueue
        public int compareTo(Node node) {
            return this.f - node.f;
        }
    }
}
