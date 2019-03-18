import java.util.*;
import java.io.*;
import java.lang.*;

public class EpicFight{	
	/*
												Input Specification
						The first line of input will contain 1 integer, t(1 ≤t≤120), representing the time the fight took in seconds.
		
		The next line contains 2 integers, mx and sx(1 ≤mx≤10, 10 ≤sx≤100), representing the number of moves Xorvier knows and his maximum stamina.
				The next mx lines describes Xorvier's moves. The i-th line contains 2 integers, ai and bi,(-100 ≤ai≤100,-100 ≤bi≤100)
						representing Xorvier's initial stamina cost and Ruffus's ending stamina reduction, respectively.
		
		The next line contains 2 integers, mr and sr(1 ≤mr≤10, 10 ≤sr≤100), representing the number of moves Ruffusknows and his maximum stamina. 
				The next mr lines describes Ruffus's moves. The i-th line contains 2 integers, ai and bi, (-100 ≤ai≤100,-100 ≤bi≤100)
					representingRuffus's initial stamina cost and Xorvier's ending stamina reduction, respectively.
					
												Output Specification
		The first and only line of output should containthe remainder of the number of distinct possible fight sequences when divided by 10,007. (mod10007)
	*/
	/*
		Modular arithmetic:  if b is the real answer, our program should output  a such that : 
			b mod(10007)=  a
		but if b is product of 2 numbers, then b can be factored in the following way:
			b = c * d
		then we can use our modular properties to get
			b mod(10007)=  a  =  c mod(10007) *   d mod(10007)
		similarly we can add a constant  in the following way:
			(b + k) mod(10007)=  (a + k) mod(10007)
	*/

	
    public static int time; //time
    public static int numXorMoves;
    public static int numRufMoves;
    public static int xorStamina;
    public static int rufStamina;
    public static int xorvier; //To keep track of Xorviers index
    public static int ruffus; //To keep track of Ruffus index
	public static int[] xorMovesCost;
	public static int[] xorMovesDamage;
	public static int[] rufMovesCost;
	public static int[] rufMovesDamage;
	public static int MOD = 10007;
	
	//public static int [2][][] state; //[2][100][100]  =  [time][xorStamina][rufStamina]
	
	public static void main(String[] args){
		
		Scanner sc = new Scanner(System.in);
		time = sc.nextInt(); //Total time of the fight
		//int breakTime = time - 1; // if time is odd, we need to stop half loop, and since time increases by 2, it will never stop when time is even
		numXorMoves = sc.nextInt(); //Number of moves Xorvier have
		xorStamina = sc.nextInt(); //Xorvier Stamina
		xorMovesCost = new int[numXorMoves]; 
		xorMovesDamage = new int[numXorMoves]; 
		for(int i=0; i<numXorMoves;i++){
			xorMovesCost[i] = sc.nextInt(); // Row 0 have Xorvier's initial stamina cost.
			xorMovesDamage[i] = sc.nextInt(); //Row 1 have Ruffus's ending stamina reduction
		}
		
		numRufMoves = sc.nextInt(); //Number of moves Ruffus have
		rufStamina = sc.nextInt(); //Ruffus Stamina
		rufMovesCost = new int[numRufMoves]; 
		rufMovesDamage = new int[numRufMoves]; 
		for(int i=0; i<numRufMoves;i++){
			rufMovesCost[i] = sc.nextInt(); // Row 0 have Xorvier's initial stamina cost.
			rufMovesDamage[i] = sc.nextInt(); //Row 1 have Ruffus's ending stamina reduction
		}
		
		int [][] curStates = new int[xorStamina+1][rufStamina+1]; //[100][100]  =  [xorStamina][rufStamina]
		int [][] nextStates = new int[xorStamina+1][rufStamina+1]; //[100][100]  =  [xorStamina][rufStamina]
		curStates[0][0] = 1;
        for (int t = 0; t <=time; t+=2){ // Go by seonccds
			for(int[] row: nextStates)
				Arrays.fill(row, 0);//Reset all Values to 0 before loop
			
			for (int i = 0; i < xorStamina; i++){ //Navigate the Matrix
				for (int j = 0; j < rufStamina; j++){ //dont grab from previous states where they are dead (Max stamina)
					if(curStates[i][j]== 0) //Skip iteration if curXor[ i ] wont add anything to the answer
							continue;
					for(int x = 0; x<numXorMoves; x++){ //for each move of Xor try
						if(i + xorMovesCost[x] >= xorStamina)// if Xor dies 
							continue; //skip move
						for(int r = 0; r<numRufMoves; r++){ //each move of Ruffus
							xorvier = 0; //To keep track of Xorviers index
							ruffus = 0; //To keep track of Ruffus index
							if(t == time){ // if we are in theLAST LOOP and we have to break mid loop
								if(j + rufMovesCost[r] >=rufStamina){//Overkill to Ruffus, count as kill
									nextStates[i][rufStamina] += curStates[i][j]; //just add it somewhere in the answer
									nextStates[i][rufStamina] %= MOD;
								}
								//Else Rufus don't die with this final move... don't bother adding it to the matrix
								continue;
							}
							
							if(j + rufMovesCost[r]>= rufStamina)// if Ruffus dies, before the time
								continue; //skip since he fainted before time
							
							// Getting index for XORVIER
							if(i + xorMovesCost[x]<= 0){//if Xor used regen and has top health, then keep as top health
								if(rufMovesDamage[r] > 0) //Ruffus did not used Regen in Xorvier
									xorvier = rufMovesDamage[r];
							}
							//Xor not at top Health
							else if(i + xorMovesCost[x] + rufMovesDamage[r] >= xorStamina)// if Xor is dead after Ruffus attack
								continue; // skip this move
							else if(i + xorMovesCost[x] + rufMovesDamage[r] > 0) // if Xor is in some positive hp and Ruffus did not regenerate him back to 0
								xorvier = i + xorMovesCost[x] + rufMovesDamage[r];
							
							// Getting index for RUFFUS
							if(j + rufMovesCost[r] <= 0){ // if Ruffus used regeneration and has top health, keep as 0
								if(xorMovesDamage[x] > 0) //Xorvier did nor used Regen on Ruffus
									ruffus = xorMovesDamage[x];
							}
									
							//Ruffus have some positive hp before Xorviers attack
							else if(j + rufMovesCost[r] + xorMovesDamage[x] >= rufStamina)//Overkill to Ruffus, count as kill
								ruffus = rufStamina;
							else //Ruffus is still alive, so save state for Next iteration
								ruffus = j + rufMovesCost[r] + xorMovesDamage[x];
							
							nextStates[xorvier][ruffus] += curStates[i][j];
							nextStates[xorvier][ruffus] %= MOD;
						}
					}
				}
			}
            int[][] tmp = curStates; //Swap Xor's cur and next 
            curStates = nextStates;
            nextStates = tmp;
		}
		int sum=0;
        for(int k=0; k<xorStamina;k++)
			sum+=curStates[k][rufStamina]; //sum all outcomes where Ruffus dies and xor is still alive
		sum %= MOD;
        System.out.println(sum);
	}
	//for (int[] row : curStates)
				//System.out.println(Arrays.toString(row));
}
