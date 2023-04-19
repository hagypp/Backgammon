import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Backgammon
{
    private int[] board; // Positive numbers - white pieces, negative - black pieces.
    private int[] eaten; // 2 cells - 1st cell for whites eaten pieces, 2nd for blacks.
    private int[] cubesUsages; // How many mor	e usages do we have for each cube
    private boolean whitesTurn; // Is it whites turn?
    private Random rd = new Random(); // Random generator.
    private Scanner sc = new Scanner(System.in); // For getting the users position and cube to use.
    private final int BOARD_SIZE = 24; //represent the board size
    private final int MAX_CUBE = 6; //represent the max of the cubes
    private final int MIN_CUBE = 1; //represent the min of the cubes
    private final int EAT_WHITE = -1;  //represent the place of the eaten white
    private final int EAT_BLACK = 24;  //represent the place of the eaten black

    //default constructor that call init function
    Backgammon()
    {
        initBoard();
    }

    //constructor that get size of the board
    Backgammon(int board_size)
    {
        initBoard(board_size);
    }

    //function of constructor that call init board in size of 24
    @SuppressWarnings("unused")
    public void initBoard()
    { // useful when playing more than 1 game.
        initBoard(24);
    }

    //function that build all the elements in the class
    public void initBoard(int boardSize)
    {
        this.eaten = new int[]{0, 0};
        this.cubesUsages = new int[]{0, 0};
        this.whitesTurn = whiteStarts();
        if (boardSize == this.BOARD_SIZE)
            this.board = new int[]{2, 0, 0, 0, 0, -5, 0, -3, 0, 0, 0, 5, -5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0, -2};
    }

    //private function that return reverse array for to string function
    private int[] reverse(int[] array)
    {
        for (int i = 0; i < array.length / 2; i++)
        {
            int temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
        return array;
    }

    /**
     * to string function
     *
     * @return to string
     */
    public String toString()
    {
        return Arrays.toString(Arrays.copyOfRange(this.board, 0, this.BOARD_SIZE / 4))
                + Arrays.toString(Arrays.copyOfRange(this.board, this.BOARD_SIZE / 4, this.BOARD_SIZE / 2)) + "\n" +
                Arrays.toString(reverse(Arrays.copyOfRange(this.board, this.BOARD_SIZE / 2 + this.BOARD_SIZE / 4, this.BOARD_SIZE)))
                + Arrays.toString(reverse(Arrays.copyOfRange(this.board, this.BOARD_SIZE / 2, this.BOARD_SIZE / 2 + this.BOARD_SIZE / 4))) + "\n" +
                "Whites eaten - " + this.eaten[0] + ", blacks eaten - " + this.eaten[1];
        // Should look like:
        // [2, 0, 0, 0, 0, -5][0, -3, 0, 0, 0, 5]
        // [-5, 0, 0, 0, 3, 0][5, 0, 0, 0, 0, -2]
        // Whites eaten - 0, blacks eaten - 0

        // You can use Arrays functionalities here, will make your life much easier...
        // Hints: Arrays.copyOfRange, Arrays.toString

        // ...
    }

    //function that return array that represent the board
    public int[] getBoard()
    {
        return this.board;
    }

    //function that return the boolean argument of the turn
    public boolean getWhitesTurn()
    {
        return this.whitesTurn;
    }

    //function that get array of int and move that to the board
    public void setBoard(int[] board)
    {
        this.board=board;
        // Use with caution, should be used only for debugging purposes!
    }

    //function that randomly decide who will begin
    public boolean whiteStarts()
    {
        return this.rd.nextBoolean();
    }

    //function that randomly choose 2 numbers for the cubes
    // between 1 and 6 and return array of the cubes
    public int[] roll2Cubes()
    {
        int[] cubes = new int[2];
        for (int i = 0; i < cubes.length; i++)
            cubes[i] = this.rd.nextInt(this.MAX_CUBE - 0) + this.MIN_CUBE;
        return cubes;
    }

    //function that check if one of the players is won
    //the function run all over the board and sum the plus and the minus
    //if one of them is 0 he won
    public boolean gameOver()
    {
        int countMinus = 0;
        int countPlus = 0;
        for (int i = 0; i < this.board.length; i++)
        {
            if (this.board[i] > 0)
                countPlus++;
            else if (this.board[i] < 0)
                countMinus++;
        }
        //in case that all the players are eaten for one player its not sure victory
        if (this.eaten[0] > 0)
            countPlus++;
        if (this.eaten[1] > 0)
            countMinus++;
        return countMinus == 0 || countPlus == 0;
    }

    //function that check if the position is empty if it does return true
    private boolean empty(int position)
    {
        //if he has eaten and the position is good return false
        if (haveEaten(position))
            return false;
        else if (this.whitesTurn)
        {
            //if its white turn and the place is 0 or black return true
            if (this.board[position] <= 0)
                return true;
        } else if (!whitesTurn)
        {
            //if its black turn and position is empty or with white return true
            if (this.board[position] >= 0)
                return true;
        }
        //else if we get here the position is empty
        return false;
    }

    //function that check if the player have eaten, and he inserts the correct position
    //and return true if it does
    private boolean haveEaten(int position)
    {
        if (this.whitesTurn && position == this.EAT_WHITE && this.eaten[0] > 0)
            return true;
        else if (!this.whitesTurn && position == this.EAT_BLACK && this.eaten[1] > 0)
            return true;
        else
            return false;
    }

    //a function that make the changes in the game
    private boolean eat(int position, int move, int direction)
    {
        //if one of the players have eaten, and it's not the correct position for them return false
        if (this.eaten[0] > 0 && this.whitesTurn && position != this.EAT_WHITE)
            return false;
        if (this.eaten[1] > 0 && !this.whitesTurn && position != this.EAT_BLACK)
            return false;
        if (whitesTurn)
        {
            //if the player can out stone delete one
            if (position != this.EAT_WHITE && farthestStoneInLastQuadrant(farthestStone())
                    && (position + move >= this.BOARD_SIZE))
            {
                this.board[position]--;
                return true;
            }
            //if the player want to out one of the eaten move him
            if (haveEaten(position))
                this.eaten[0]--;
            else
                //else and its regular move delete the stone in the position
                this.board[position]--;
            //if the place that we land on its one black stone make it white
            //and insert the black to the eaten
            if (this.board[position + (move * direction)] == -1)
            {
                this.board[position + (move * direction)] = 1;
                this.eaten[1]++;
                return true;
            } else
            //else and its just move insert white in the position plus move
            {
                this.board[position + (move * direction)]++;
                return true;
            }
            //same thing on the black
        } else if (!whitesTurn)
        {
            if (position != this.EAT_BLACK && farthestStoneInLastQuadrant(farthestStone())
                    && (position - move <= this.EAT_WHITE))
            {
                this.board[position]++;
                return true;
            }
            if (haveEaten(position))
                this.eaten[1]--;
            else
                this.board[position]++;
            if (this.board[position + (move * direction)] == 1)
            {
                this.board[position + (move * direction)] = -1;
                this.eaten[0]++;
                return true;
            } else
            {
                this.board[position + (move * direction)]--;
                return true;
            }
        } else
            return false;
    }

    //function that make the moves
    public boolean move(int position, int move)
    {
        //if the position it's not correct return false
        if (outOfBoard(position))
            return false;
            //if the position is empty return false
        else if (empty(position))
            return false;
        else
        {
            int direction = this.whitesTurn ? 1 : -1;
            //if its legal move
            if (legalMove(position, move))
            {
                //and eat its correct return true else return false
                if (!eat(position, move, direction))
                    return false;
            } else if (!legalMove(position, move))
                return false;
        }
        return true;
    }

    //function that check if the player hae any moves given the cubes he rolls
    public boolean haveLegalMoves(int[] cubes)
    {
        if (this.whitesTurn)
        {
            //if he has eaten
            if (this.eaten[0] >= 1)
            {
                //one of the cubes should be enough
                if ((legalMove(this.EAT_WHITE, cubes[0]) && this.cubesUsages[0] > 0)
                        || (legalMove(this.EAT_WHITE, cubes[1]) && this.cubesUsages[1] > 0))
                    return true;
            } else
            {
                //else and he not has eaten move all over the board and check if any position
                //with one of the cubes is correct
                for (int i = 0; i < this.board.length; i++)
                {
                    if (this.board[i] > 0 && ((legalMove(i, cubes[0]) && this.cubesUsages[0] > 0)
                            || (legalMove(i, cubes[1]) && this.cubesUsages[1] > 0)))
                        return true;
                }
            }
        } else
        //same thing on the black
        {
            if (this.eaten[1] >= 1)
            {
                if ((legalMove(this.EAT_BLACK, cubes[0]) && cubesUsages[0] > 0) ||
                        (legalMove(this.EAT_BLACK, cubes[1]) && cubesUsages[1] > 0))
                    return true;
            } else
            {
                for (int i = this.board.length - 1; i > 0; i--)
                {
                    if (this.board[i] < 0 && ((legalMove(i, cubes[0]) && this.cubesUsages[0] > 0)
                            || (legalMove(i, cubes[1]) && this.cubesUsages[1] > 0)))
                        return true;
                }
            }
        }
        //if we came all over to here we not have legal move in all and return false
        return false;
    }

    //function that check if position and move its legal
    public boolean legalMove(int startPosition, int move)
    {
        if (this.whitesTurn)
        {
            //if the position is out of the board return false
            if (outOfBoard(startPosition))
                return false;
                //if the player want to out, and he can return true
            else if (move + startPosition >= this.BOARD_SIZE && farthestStoneInLastQuadrant(farthestStone()))
                return true;
            else if (move + startPosition >= this.BOARD_SIZE && !farthestStoneInLastQuadrant(farthestStone()))
                return false;
                //if the place that we land will be one black or 0 or white return true
            else if (this.board[startPosition + move] >= -1)
                return true;
        }
        //same thing for the black
        else if (!this.whitesTurn)
        {
            if (outOfBoard(startPosition))
                return false;
            else if (startPosition - move <= this.EAT_WHITE && farthestStoneInLastQuadrant(farthestStone()))
                return true;
            else if (startPosition - move <= this.EAT_WHITE && !farthestStoneInLastQuadrant(farthestStone()))
                return false;
            else if (this.board[startPosition - move] <= 1)
                return true;
        }
        //if we came all over to here the move is illegal and return false
        return false;
    }

    //function that run all over the board and return the index of the far stone from the home
    public int farthestStone()
    {
        if (this.whitesTurn)
        {
            if (this.eaten[0]>0)
                return this.EAT_WHITE;

            for (int i = 0; i < this.board.length; i++)
            {
                if (this.board[i] > 0)
                    return i;
            }
        }
        else
        {
            if (this.eaten[1]>0)
                return this.EAT_BLACK;
            for (int i = this.board.length - 1; i > 0; i--)
                if (this.board[i] < 0)
                    return i;
        }
        return -1;
    }

    //function that check if the stone is in the last quadrant the hone
    public boolean farthestStoneInLastQuadrant(int farthestStone)
    {
        if (farthestStone==-1||farthestStone==24)
            return false;
        //if its black <0 and the the board size minus the far stone is in the home and the far stone is the far
        if (this.board[farthestStone] < 0 && this.board.length - farthestStone >= this.BOARD_SIZE / 2 + this.BOARD_SIZE / 4
                && farthestStone() <= farthestStone)
            return true;
            //same for the white
        else if (this.board[farthestStone] > 0 && this.board.length - farthestStone <= this.BOARD_SIZE / 4
                && farthestStone() >= farthestStone)
            return true;
        else
            return false;
    }

    //function that check if the given position is legal if it does return false
    public boolean outOfBoard(int position)
    {
        if (this.whitesTurn)
        {
            if (this.eaten[0] >= 1)
            {
                if (position == this.EAT_WHITE)
                    return false;
            }
        } else
        {
            if (this.eaten[1] >= 1)
                if (position == this.EAT_BLACK)
                    return false;
        }
        return position > this.board.length - 1 || position < 0;
    }

    //function that move the turn to the other player
    public void nextTurn()
    {
        this.whitesTurn = !this.whitesTurn;
    }

    public void runGame()
    {
        while (!this.gameOver())
        {
            int[] cubes = this.roll2Cubes();
            this.cubesUsages[1] = this.cubesUsages[0] = (cubes[0] == cubes[1]) ? 2 : 1;

            // Move the board using legal moves rolled by the cubes:
            while (this.cubesUsages[0] > 0 || this.cubesUsages[1] > 0)
            {
                if (!this.haveLegalMoves(cubes))
                    break;

                System.out.print(this.whitesTurn ? "Whites turn (⇄)" : "Blacks turn (⇆)");
                System.out.println(", Rolled " + cubes[0] + " " + cubes[1]);
                System.out.print("Insert position number: ");
                int choosenPosition = this.sc.nextInt();
                System.out.print("Insert cube number (0 or 1): ");
                int cubeToUse = this.sc.nextInt();
                if (cubeToUse < 0 || cubeToUse >= cubes.length)
                {
                    System.out.println("Please select a cube from the range of 0 to " + (cubes.length - 1));
                    continue;
                }
                if (this.cubesUsages[cubeToUse] <= 0)
                {
                    System.out.println("Can\'t use this cube again!");
                    continue;
                }
                int choosenMove = cubes[cubeToUse];
                boolean moved = this.move(choosenPosition, choosenMove);
                if (moved)
                {
                    this.cubesUsages[cubeToUse] -= 1;
                    System.out.println(this);
                } else
                    System.out.println("Illegal move!");
            }
            this.nextTurn();
        }
        System.out.println(this.whitesTurn ? "Black won!" : "White won!");
    }

    public static void main(String[] args)
    {
        Backgammon bg = new Backgammon();
        System.out.println(bg.toString());
        bg.runGame();
    }
}
