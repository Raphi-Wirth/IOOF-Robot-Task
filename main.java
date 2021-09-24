import java.util.Scanner;
import unittest;

class Board{
    public static final int maxX = 5;
    public static final int maxY = 5;
}

class Input{
    enum CardinalDirections {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }
    enum InputType {
        PLACE,
        MOVE,
        LEFT,
        RIGHT,
        REPORT,
        ERROR
    }

    InputType inputType;
    String[] placement;
    public Input(String userInput){
        String[] command = userInput.split(" ");
        try{
            inputType = InputType.valueOf(command[0]);
        }
        catch(IllegalArgumentException e){
            inputType = InputType.ERROR;
        }
        if(inputType == InputType.PLACE){
            placement = command[1].split(",");
        }
    }
}

class Robot{
    int x;
    int y;
    Input.CardinalDirections direction;

    public Robot(Input input){
        x = Integer.parseInt(input.placement[0]);
        y = Integer.parseInt(input.placement[1]);
        direction = Input.CardinalDirections.valueOf(input.placement[2]);
    }

    public void Report(){
        String currentX = Integer.toString(x);
        String currentY = Integer.toString(y);
        String currentDir = direction.toString();
        System.out.format("\nOutput: %s,%s,%s\n", currentX, currentY, currentDir);
    }
    
    public void Place(String[] placement){
        x = Integer.parseInt(placement[0]);
        y = Integer.parseInt(placement[1]);
        direction = Input.CardinalDirections.valueOf(placement[2]);
    }
    public static boolean CheckInBounds(int x, int y){
        if(x>Board.maxX || x<0 || y<0 || y>Board.maxY){
            return false;
        }
        return true;
    }
    public void Move(){
        switch(direction){
            case EAST:
                if(CheckInBounds(x+1,y)){
                    x+=1;
                }
                break;
            case NORTH:
                if(CheckInBounds(x,y+1)){
                    y+=1;
                }
                break;
            case WEST:
                if(CheckInBounds(x-1,y)){
                    x-=1;
                }
                break;
            case SOUTH:
                if(CheckInBounds(x,y-1)){
                    y-=1;
                }
                break;
        }
    }

    public void Left(){
        switch(direction){
            case EAST:
                direction = Input.CardinalDirections.NORTH;
                break;
            case NORTH:
                direction = Input.CardinalDirections.WEST;
                break;
            case WEST:
                direction = Input.CardinalDirections.SOUTH;
                break;
            case SOUTH:
                direction = Input.CardinalDirections.EAST;
                break;
        }
    }

    public void Right(){
        switch(direction){
            case EAST:
                direction = Input.CardinalDirections.SOUTH;
                break;
            case NORTH:
                direction = Input.CardinalDirections.EAST;
                break;
            case WEST:
                direction = Input.CardinalDirections.NORTH;
                break;
            case SOUTH:
                direction = Input.CardinalDirections.WEST;
                break;
        }
    }

    public void executeCommand(Input input){
        switch(input.inputType){
            case MOVE:
                Move();
                break;
            case LEFT:
                Left();
                break;
            case RIGHT:
                Right();
                break;
            case PLACE:
                Place(input.placement);
                break;
            case REPORT:
                Report();
                break;
        }
    }
}


class Main{
    static Robot robot;
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        while(true){
            String input = scanner.nextLine();
            Input currentInput = new Input(input);
            if(currentInput.inputType == Input.InputType.ERROR){
                continue;
            }
            if(robot == null && currentInput.inputType == Input.InputType.PLACE){
                if(Robot.CheckInBounds(Integer.parseInt(currentInput.placement[0]),Integer.parseInt(currentInput.placement[1]))){
                    robot = new Robot(currentInput);
                }
            }
            else if(robot != null){
                robot.executeCommand(currentInput);
            }
        }
    }
}